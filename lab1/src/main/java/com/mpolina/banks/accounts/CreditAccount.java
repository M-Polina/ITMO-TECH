package com.mpolina.banks.accounts;

import com.mpolina.banks.client.ClientAccount;
import com.mpolina.banks.exceptions.BanksException;
import com.mpolina.banks.transactions.RollBackType;
import com.mpolina.banks.transactions.TransactionConditions;
import lombok.Getter;
import com.mpolina.banks.time.OwnTime;

import java.time.LocalDateTime;

/**
 * Кредитный аккаунт, позволяет класть на него деньги, снимать с него деньги
 * и менять кредитный лимит, комиссию.
 */
@Getter
public class CreditAccount implements Account {
    private final int COEF_TO_COUNT_COMMISSION = 1;
    private final int COEF_TO_NOT_COUNT_COMMISSION = 0;
    private final double MAX_CREDIT_LIMIT = 0;
    private final double MIN_MONEY_LIMIT = 0;
    private final int MIN_ID = 0;
    private final double MIN_AMOUNT_OF_MONEY = 0;
    private final double MIN_COMMISSION = 0;

    private LocalDateTime creationTime;
    private int id;
    private ClientAccount clientAccount;
    /**
     * Кредитный лимит - отрицательное число, на которое средства на аккаунте могут уйти в минус.
     */
    private double creditLimit;
    /**
     * Коммиссия, снимающаяся со счёта при уходе в минус при снятии денег.
     * Положительное число.
     */
    private double commission;
    /**
     * Количество денег на счету, может быть как положительным, так и отрицательным.
     */
    private double money;

    /**
     * Конструктор кредитного аккаунта
     *
     * @param newId     Id счёта, которое будет ему присвоено
     * @param newClient клиент, которому принадлежит счёт
     */
    public CreditAccount(int newId, ClientAccount newClient) {
        if (newClient == null) {
            throw new BanksException("Client is null while creating CreditAccount.");
        }
        if (newId < MIN_ID) {
            money = MIN_AMOUNT_OF_MONEY;
        }

        id = newId;
        clientAccount = newClient;
        commission = newClient.getBank().getConditions().getComission();
        creditLimit = newClient.getBank().getConditions().getCreditLimit();
        creationTime = OwnTime.getInstance().getTimeNow();
    }

    /**
     * Возвращает можно ли списать определённое количество денег
     *
     * @param amount количество денег, на которое проверяем
     * @return true - если можно, false - если нельзя
     */
    public boolean canWithdrawMoney(double amount) {
        if (amount < creditLimit || (money - amount) < creditLimit) {
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

        return new TransactionConditions(amount, MIN_MONEY_LIMIT);
    }

    /**
     * Списывает деньги из аккаунта. При уходе в минус также списывается комиссия.
     *
     * @param amount количество денег, которое списываем со счёта.
     * @return условия транзакции {@see TransactionConditions}
     */
    public TransactionConditions withdrawMoney(double amount) {
        double withrawMoney = MIN_AMOUNT_OF_MONEY;
        double newCommission = MIN_AMOUNT_OF_MONEY;

        if (!canWithdrawMoney(amount)) {
            throw new BanksException("Money can't be withdrawed from Credit Account, wrong ammount.");
        }
        if ((money - amount) >= MIN_AMOUNT_OF_MONEY) {
            withrawMoney = amount;
            money -= amount;
        }
        else if ((money - amount) >= creditLimit) {
            withrawMoney = amount;
            newCommission = commission;
            money = money - amount - commission;
        }
        else {
            throw new BanksException("Amount of money to withdraw is too big in Credit Account.");
        }

        return new TransactionConditions(withrawMoney, newCommission);
    }

    /**
     * Откатываем операцию, согласно переданным условиям транзакции
     *
     * @param newConditions   условия транзакции, которую хотим откатить
     * @param newRollBackType тип транзакции, будет учтён как коэффициент,
     *                        учитывая который произойдёт либо списание,
     *                        либо пополнение при откате транзакции.
     * @return условия новой транзакции, являющейся откатом
     */
    public TransactionConditions rollBack(TransactionConditions newConditions, int newRollBackType) {
        if (newConditions == null) {
            throw new BanksException("Transaction can't be rollbacked in Credit Account, null conditions.");
        }

        int coefficient = newRollBackType == RollBackType.WITHDRAW_ROLLBACK
                ? COEF_TO_COUNT_COMMISSION
                : COEF_TO_NOT_COUNT_COMMISSION;

        money = money + ((newRollBackType) * newConditions.getAmountOfMoney()) +
                (newConditions.getCommission() * coefficient);

        return newConditions;
    }

    /**
     * Изменение комиссии счёта.
     *
     * @param newCommission новая комиссия, которая будет на счету
     */
    public void changeCommission(double newCommission) {
        if (newCommission <= MIN_COMMISSION) {
            throw new BanksException("newCommission is incorrect so it can't be changed.");
        }

        commission = newCommission;
    }

    /**
     * Процентной ставки на кредитном счету нет, поэтому и проценты не начисляются.
     */
    public void accumulateInterestRate() {
    }

    /**
     * Изменение кредитного лимита счёта.
     *
     * @param newCreditLimit новый кредитный лимит
     */
    public void changeCreditLimit(double newCreditLimit) {
        if (newCreditLimit >= MAX_CREDIT_LIMIT) {
            throw new BanksException("newCreditLimit is incorrect so it can't be changed.");
        }

        creditLimit = newCreditLimit;
    }

    /**
     * Изменение условий счёта - кредитного лимита или кредитной комиссии,
     * вызывает соответствующие методы изменения {@see changeCreditLimit}
     * {@see changeCommission}
     */
    public void changeConditions() {
        if (clientAccount.getBank().getConditions() == null) {
            throw new BanksException("Null conditions while chnging them in CreditAccount.");
        }

        changeCreditLimit(clientAccount.getBank().getConditions().getCreditLimit());
        changeCommission(clientAccount.getBank().getConditions().getComission());
    }
}
