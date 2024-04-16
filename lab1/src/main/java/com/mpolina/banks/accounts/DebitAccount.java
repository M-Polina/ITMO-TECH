package com.mpolina.banks.accounts;

import com.mpolina.banks.client.ClientAccount;
import com.mpolina.banks.exceptions.BanksException;
import com.mpolina.banks.transactions.RollBackType;
import com.mpolina.banks.transactions.TransactionConditions;
import lombok.Getter;
import com.mpolina.banks.time.OwnTime;

import java.time.LocalDateTime;

/**
 * Дебетовый счёт: начисляются проценты каждый месяц.
 */
@Getter
public class DebitAccount implements Account {
    private final int COEF_TO_COUNT_COMMISSION = 1;
    private final int COEF_TO_NOT_COUNT_COMMISSION = 0;
    private final int MIN_ID = 0;
    private final int MONTHS_IN_YEAR = 12;
    private final double MIN_MONEY_LIMIT = 0;
    private final double MIN_INTREST_RATE = 0;
    private final double MIN_COMMISSION = 0;

    private double interestRateStorage = MIN_MONEY_LIMIT;

    public int id;
    public ClientAccount clientAccount;
    public double money;
    private LocalDateTime creationTime;
    /**
     * Годовая процентная ставка счёта.
     */
    private double interestRate;

    /**
     * Конструктор дебетового счёта.
     *
     * @param newId Id который будет присвоен аккаунту
     * @param newClient клиент, которому принадлежит счёт
     */
    public DebitAccount(int newId, ClientAccount newClient) {
        if (newClient == null) {
            throw new BanksException("Client is null while creating DebitAccount.");
        }
        if (newId < MIN_ID) {
            throw new BanksException("Id is <0 while creating CreditAccount");
        }

        id = newId;
        money = MIN_MONEY_LIMIT;
        interestRate = newClient.getBank().getConditions().getDebitInterestRate();
        clientAccount = newClient;
        creationTime = OwnTime.getInstance().getTimeNow();
    }

    /**
     * Считает месячную процентную ставку
     *
     * @return месячная процентная ставка.
     */
    public double getInterestRatePerMonth() {
        return interestRate / MONTHS_IN_YEAR;
    }

    /**
     * Возвращает можно ли списать определённое количество денег
     *
     * @param amount количество денег, на которое проверяем
     * @return true - если можно, false - если нельзя
     */
    public boolean canWithdrawMoney(double amount) {
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
            throw new BanksException("Amount of money to add can't be <0 in Debit Account.");
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
            throw new BanksException("Money can't be withdrawed from Debit Account.");
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
            throw new BanksException("Transaction can't be rollbacked in Debit Account, null conditions.");
        }

        int coefficient = rollBackType == RollBackType.WITHDRAW_ROLLBACK
                ? COEF_TO_COUNT_COMMISSION
                : COEF_TO_NOT_COUNT_COMMISSION;
        money = money + ((rollBackType) * newConditions.getAmountOfMoney()) +
                (newConditions.getCommission() * coefficient);
        return newConditions;
    }

    /**
     * Изменяет прцентную ставку счёта.
     *
     * @param newInterestRate новая процентная ставка.
     */
    public void changeInterestRate(double newInterestRate) {
        if (newInterestRate <= MIN_INTREST_RATE) {
            throw new BanksException("newInterestRate is incorrect so interest rate in Debit can't be changed.");
        }

        interestRate = newInterestRate;
    }

    /**
     * Запасает процент {@code interestRateStorage}, суммируя месячную процентную ставку
     * помноженную на текущее количество денег на счету.
     * При наступлении следующего месяца вызывает метод по начислению запасённых процентов
     * на счёт {@see getInterestRatePerMonth}
     */
    public void accumulateInterestRate() {
        interestRateStorage += money * getInterestRatePerMonth();

        if (OwnTime.getInstance().countMonthPassed(creationTime)) {
            payInterestRate();
        }
    }

    /**
     * Записывает запасённые деньги на счёт (начисляет проценты за месяц).
     */
    public void payInterestRate() {
        money += interestRateStorage;
        interestRateStorage = MIN_INTREST_RATE;
    }

    /**
     * Изменяет условия счета - процентную ставку. {@see changeInterestRate}
     */
    public void changeConditions() {
        if (clientAccount.getBank().getConditions() == null) {
            throw new BanksException("Null conditions while chnging them in CreditAccount.");
        }

        changeInterestRate(clientAccount.getBank().getConditions().getDebitInterestRate());
    }
}
