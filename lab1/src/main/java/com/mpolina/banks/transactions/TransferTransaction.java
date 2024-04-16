package com.mpolina.banks.transactions;

import com.mpolina.banks.accounts.Account;
import com.mpolina.banks.exceptions.BanksException;
import lombok.Getter;

import java.util.UUID;

/**
 * Класс транзакции перевода денег между счетами. Хранит связанные счета, условия транзакции.
 * Позволяет совершить перевод между счетами.
 */
@Getter
public class TransferTransaction implements MoneyTransaction {
    private final int MIN_ID = 0;
    private final int MIN_COMMISSION = 0;

    protected boolean transactionIsDone = false;
    /**
     * Аккаунт, с которого переводят деньги.
     */
    private Account accountFrom;
    /**
     * Аккаунт, на который переводят деньги.
     */
    private Account accountTo;
    /**
     * id транзакции.
     */
    private UUID id;
    /**
     * Условия транзакции.
     */
    private TransactionConditions transactionConditions;

    /**
     * @param amount количество денег для перевода
     * @param newAccountFrom Аккаунт, с которого переводят деньги.
     * @param newAccountTo Аккаунт, на который переводят деньги.
     */
    public TransferTransaction(double amount, Account newAccountFrom, Account newAccountTo)
    {
        if (newAccountFrom == null)
        {
            throw new BanksException("Null accountFrom while creating transaction.");
        }
        if (newAccountTo == null)
        {
            throw new BanksException("Null accountTo while creating transaction.");
        }

        id = UUID.randomUUID();
        accountFrom = newAccountFrom;
        accountTo = newAccountTo;
        transactionConditions = new TransactionConditions(amount, MIN_COMMISSION);
    }

    /**
     * Помечает транзакцию совершённой.
     */
    public void markTransactionMade()
    {
        transactionIsDone = true;
    }

    /**
     * Выполнение транзакции перевода - снятие денег с одного счёта и пополнение другого.
     */
    public void createTransaction()
    {
        if (!accountFrom.canWithdrawMoney(transactionConditions.getAmountOfMoney())) {
            throw new BanksException("Can't withdraw money from account (Creating transaction).");
        }

        transactionConditions = accountFrom.withdrawMoney(transactionConditions.getAmountOfMoney());
        accountTo.addMoney(transactionConditions.getAmountOfMoney());
        transactionIsDone = true;
    }
}