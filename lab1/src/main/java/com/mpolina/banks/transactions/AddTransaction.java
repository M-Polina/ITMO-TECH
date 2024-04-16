package com.mpolina.banks.transactions;

import com.mpolina.banks.accounts.Account;
import com.mpolina.banks.exceptions.BanksException;
import lombok.Getter;

import java.util.UUID;

/**
 * Класс транзакции пополнения счёта. Простая транзакция.
 */
@Getter
public class AddTransaction implements SimpleTransaction {
    private final double INITIAL_COMMISSION = 0;
    private final int MIN_ID = 0;

    private boolean transactionIsDone = false;
    /**
     * Аккаунт для пополненя
     */
    private Account account;
    private UUID id;
    /**
     * Условия транзакции
     */
    private TransactionConditions transactionConditions;

    /**
     * Конструктор транзакции пополнения счёта.
     *
     * @param amount количество денег на пополнение
     * @param newAccount счёт начисления.
     */
    public AddTransaction(double amount, Account newAccount) {
        if (newAccount == null) {
            throw new BanksException("Null account while creating transaction.");
        }

        id = UUID.randomUUID();
        account = newAccount;
        transactionConditions = new TransactionConditions(amount, INITIAL_COMMISSION);
    }

    /**
     * Помечает транзакцию совершённой.
     */
    public void markTransactionMade() {
        transactionIsDone = true;
    }

    /**
     *Выполнение транзакции пополнения счёта.
     */
    public void createTransaction() {
        transactionConditions = account.addMoney(transactionConditions.getAmountOfMoney());
        transactionIsDone = true;
    }
}
