package com.mpolina.banks.accounts;

import com.mpolina.banks.banks.DepositInterestRateConditions;
import com.mpolina.banks.client.ClientAccount;
import com.mpolina.banks.exceptions.BanksException;
import com.mpolina.banks.time.OwnTime;
import com.mpolina.banks.transactions.RollBackType;
import com.mpolina.banks.transactions.TransactionConditions;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Депозитный аккаунт. Открывается на месяц, при этом нельзя списывать деньги, но начисляются проценты.
 */
public class DepositAccount implements Account {
    private final double MIN_COMMISSION = 0;
    private final int COEF_TO_COUNT_COMMISSION = 1;
    private final int COEF_TO_NOT_COUNT_COMMISSION = 0;
    private final int MIN_ID = 0;
    private final int MIN_MONTHS = 0;
    private final double MIN_MONEY_LIMIT = 0;

    private double interestRateStorage = MIN_MONEY_LIMIT;
    private DepositInterestRate depositInterestRate;

    /**
     * Первоначальное количество денег, с которым открыли счёт
     */
    @Getter
    private double initialAmountOfMoney = MIN_MONEY_LIMIT;
    @Getter
    public int id;
    @Getter
    public ClientAccount clientAccount;
    @Getter
    public LocalDateTime creationTime;
    /**
     * Период, на сколько месяцев открыли депозитный счёт.
     */
    @Getter
    public int period;
    /**
     * Текущее количество денег на счету.
     */
    @Getter
    public double money;
    @Getter
    public LocalDateTime expirationDate;

    /**
     * Конструтор депозитного аккаунта.
     *
     * @param newId Id который будет присвоен счёту
     * @param newClient клиент, которому принадлежит счёт
     * @param amountOfMoney количество денег, с которым открывают счёт.
     * @param newMonths количество месяцев, на сколько откроют счёт
     */
    public DepositAccount(int newId, ClientAccount newClient, double amountOfMoney, int newMonths) {
        if (newId < MIN_ID) {
            throw new BanksException("Id is <0 while creating CreditAccount");
        }
        if (newClient == null) {
            throw new BanksException("Client is null while creating DepositAccount.");
        }
        if (amountOfMoney < MIN_MONEY_LIMIT) {
            throw new BanksException("amountOfMoney to deposit is incorrect.");
        }
        if (newMonths <= MIN_MONTHS) {
            throw new BanksException("Amount of months to create deposit is incorrect.");
        }

        id = newId;
        initialAmountOfMoney = amountOfMoney;
        money = amountOfMoney;
        period = newMonths;
        clientAccount = newClient;
        creationTime = OwnTime.getInstance().getTimeNow();
        expirationDate = creationTime.plusMonths(newMonths);
        depositInterestRate = new DepositInterestRate(newClient.getBank().getConditions().getDepositInterestRateConditions(), amountOfMoney);
    }

    /**
     * Получение процентной ставки.
     *
     * @return процентная ставка.
     */
    public double getInterestRate() {
        return depositInterestRate.getValue();
    }


    /**
     * Считает месячную процентную ставку
     *
     * @return месячная процентная ставка.
     */
    public double getInterestRatePerMonth() {
        return depositInterestRate.getValuePerMonth();
    }

    public DepositInterestRateConditions InterestRateConditions() {
        return depositInterestRate.getConditions();
    }

    /**
     * Возвращает можно ли списать определённое количество денег
     *
     * @param amount количество денег, на которое проверяем
     * @return true - если можно, false - если нельзя
     */
    public boolean canWithdrawMoney(double amount) {
        var time = OwnTime.getInstance().getTimeNow();

        if (expirationDate.isAfter(time)) {
            return false;
        }
        if (amount < MIN_MONEY_LIMIT || (money - amount) < MIN_MONEY_LIMIT) {
            return false;
        }
        if (amount > clientAccount.getBank().getConditions().getSuspiciousClientMoneyLimit() && !clientAccount.isNotSuspicious()) {
            return false;
        }

        return true;
    }

    /**
     * Начисляет деньги в аккаунт.
     *
     * @param amount количество денег, которое кладём на счёт.
     * @return условия транзакции {@see TransactionConditions}
     */
    public TransactionConditions addMoney(double amount) {
        if (amount <= MIN_MONEY_LIMIT) {
            throw new BanksException("Amount of money to add can't be <0 in DepositAccount.");
        }

        money += amount;

        return new TransactionConditions(amount, MIN_COMMISSION);
    }

    /**
     * Списывает деньги из аккаунта. В минус уходить нельзя.
     *
     * @param amount количество денег, которое списываем со счёта.
     * @return условия транзакции {@see TransactionConditions}
     * @throws BanksException если деньги списать нельзя. При этом деньги не списываются.
     */
    public TransactionConditions withdrawMoney(double amount) {
        if (!canWithdrawMoney(amount)) {
            throw new IllegalArgumentException("Money can't be withdrawed from DepositAccount.");
        }

        money -= amount;

        return new TransactionConditions(amount, MIN_COMMISSION);
    }

    /**
     * Откатываем операцию, согласно переданным условиям транзакции
     *
     * @param newConditions   условия транзакции, которую хотим откатить
     * @param rollBackType тип транзакции, будет учтён как коэффициент,
     *                        учитывая который произойдёт либо списание,
     *                        либо пополнение при откате транзакции.
     * @return условия новой транзакции, являющейся откатом
     */
    public TransactionConditions rollBack(TransactionConditions newConditions, int rollBackType) {
        if (newConditions == null) {
            throw new IllegalArgumentException("Transaction can't be rollbacked in Deposit Account, null conditions.");
        }

        int coefficient = rollBackType == RollBackType.WITHDRAW_ROLLBACK ? COEF_TO_COUNT_COMMISSION : COEF_TO_NOT_COUNT_COMMISSION;

        money = money + (((int) rollBackType) * newConditions.getAmountOfMoney()) + (newConditions.getCommission() * coefficient);
        return newConditions;
    }

    /**
     * Изменение процентной ставки счёта.
     *
     * @param newConditionsList новая процентная ставка.
     */
    public void changeInterestRate(DepositInterestRateConditions newConditionsList) {
        if (newConditionsList == null) {
            throw new IllegalArgumentException("InterestRateConditions is null so interestRate in Deposit can't be changed.");
        }

        depositInterestRate.changeInterestRate(newConditionsList, initialAmountOfMoney);
    }

    /**
     * Запасает процент {@code interestRateStorage}, суммируя месячную процентную ставку
     * помноженную на текущее количество денег на счету, но только если срок счёта не истёк.
     * При наступлении следующего месяца вызывает метод по начислению запасённых процентов
     * на счёт {@see getInterestRatePerMonth}
     */
    public void accumulateInterestRate() {
        var time = OwnTime.getInstance().getTimeNow();
        if (expirationDate.isAfter(time) || expirationDate.isEqual(time))
            interestRateStorage += money * getInterestRatePerMonth();

        if (OwnTime.getInstance().countMonthPassed(creationTime)) {
            payInterestRate();
        }
    }

    /**
     * Изменяет условия счета - процентную ставку. {@see changeInterestRate}
     */
    public void changeConditions() {
        if (clientAccount.getBank().getConditions() == null) {
            throw new IllegalArgumentException("Null conditions while chnging them in CreditAccount.");
        }

        changeInterestRate(clientAccount.getBank().getConditions().getDepositInterestRateConditions());
    }

    /**
     * Записывает запасённые деньги на счёт (начисляет проценты за месяц).
     */
    private void payInterestRate() {
        var time = OwnTime.getInstance().getTimeNow();
        System.out.println(money);
        System.out.println(interestRateStorage);
        System.out.println(getInterestRatePerMonth());

        if (expirationDate.isAfter(time) || expirationDate.isEqual(time)) {
            money += interestRateStorage;
            interestRateStorage = MIN_MONEY_LIMIT;
        }
    }
}