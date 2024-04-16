package com.mpolina.banks.transactions;

import com.mpolina.banks.accounts.Account;
import com.mpolina.banks.exceptions.BanksException;
import lombok.Getter;

import java.util.UUID;

/**
 * Класс транзакции снятия денег со счёта. Простая транзакция.
 */
@Getter
public class WithdrawTransaction implements SimpleTransaction {
    private final int MIN_ID = 0;
    private final int COMISSION = 0;

    /**
     * Флаг совершённости транзакции true-совершена
     */
    protected boolean transactionIsDone = false;
    /**
     * Счёт с которого снимают деньги
     */
    private Account account;
    private UUID id;
    /**
     * условия транзакции.
     */
    private TransactionConditions transactionConditions;

    /**
     * Конструктор транзакции снятия денег.
     *
     * @param amount количество денег на снятие
     * @param newAccount счёт, с которого потом надо снять деньги.
     */
    public WithdrawTransaction(double amount, Account newAccount) {
        if (newAccount == null) {
            throw new BanksException("Null account while creating transaction.");
        }

        id = UUID.randomUUID();
        account = newAccount;
        transactionConditions = new TransactionConditions(amount, COMISSION);
    }

    /**
     * Помечает транзакцию совершённой.
     */
    public void markTransactionMade() {
        transactionIsDone = true;
    }

    /**
     *Выполнение транзакции снятия денег со счёта.
     */
    public void createTransaction() {
        if (!account.canWithdrawMoney(transactionConditions.getAmountOfMoney())) {
            throw new BanksException("Wrong ammount while creating withdrawTransaction.");
        }

        transactionConditions = account.withdrawMoney(transactionConditions.getAmountOfMoney());
        transactionIsDone = true;
    }
}