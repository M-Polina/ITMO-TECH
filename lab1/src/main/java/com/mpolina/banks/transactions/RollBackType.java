package com.mpolina.banks.transactions;

/**
 * Тип транзакции отката - отмены транзакции
 */
public class RollBackType {
    /**
     * Откат транзакци снятия денег.
     */
    public static final int WITHDRAW_ROLLBACK = 1;
    /**
     * Откат транзакци добавления денег.
     */
    public static final int ADD_ROLLBACK = -1;
}
