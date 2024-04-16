package com.mpolina.banks.transactions;

import com.mpolina.banks.exceptions.BanksException;
import lombok.Getter;

import java.util.UUID;

/**
 * Класс транзакции отката другой транзакции. Позволяет совершить откат транзакции
 * и хранит данные об этом.
 */
@Getter
public class RollBackTransaction implements MoneyTransaction{
    private final int MIN_ID = 0;

    private boolean transactionIsDone = false;
    /**
     * Условия транзакции отката
     */
    private TransactionConditions transactionConditions;
    /**
     * Транзакция, откат которой делается данной транзакцией
     */
    private MoneyTransaction transaction;
    private UUID id;

    /**
     * Конструктор транзакции отката.
     *
     * @param newTransaction транзакция, откат которой нужно сделать.
     */
    public RollBackTransaction(MoneyTransaction newTransaction)
    {
        if (newTransaction == null) {
            throw new BanksException("Null transaction while creating RollbackTransaction");
        }

        id = UUID.randomUUID();
        transaction = newTransaction;
        transactionConditions = newTransaction.getTransactionConditions();
    }

    /**
     * Помечает транзакцию совершённой.
     */
    public void markTransactionMade()
    {
        transactionIsDone = true;
    }

    /**
     * Выполнение простой транзакции отката: либо откат снятия, либо - пополнения.
     */
    public void createSimpleTransaction()
    {
        if (transaction instanceof WithdrawTransaction) {
            ((WithdrawTransaction) transaction).getAccount().rollBack(transaction.getTransactionConditions(), RollBackType.WITHDRAW_ROLLBACK);
        } else if (transaction instanceof AddTransaction) {
            ((AddTransaction) transaction).getAccount().rollBack(transaction.getTransactionConditions(), RollBackType.ADD_ROLLBACK);
        }else {
            throw new BanksException("Wrong transaction type in CreateSimpleTransaction Rollback.");
        }
        transactionIsDone = true;
    }

    /**
     * Выполнение транзакции отката путём совершения одной простой транзакции,
     * если откатывается простая транзакция, иначе - двух простых транзакций.
     */
    public void createTransaction()
    {
        if (transaction instanceof SimpleTransaction)
        {
            createSimpleTransaction();
        }
        else
        {
            ((TransferTransaction)transaction).getAccountFrom().rollBack(transaction.getTransactionConditions(), RollBackType.WITHDRAW_ROLLBACK);
            ((TransferTransaction)transaction).getAccountTo().rollBack(transaction.getTransactionConditions(), RollBackType.ADD_ROLLBACK);
        }
    }
}