package com.mpolina.banks.transactions;

import java.util.UUID;

/**
 * Интерфейс класса транзакции, хранящий условия транзакции, её статус завершённости,
 * а также совершающий саму транзакцию.
 */
public interface MoneyTransaction {
    /**
     * @return уникальный id транзакции
     */
    UUID getId();

    /**
     * @return условия транзакции {@see TransactionConditions}
     */
    public TransactionConditions getTransactionConditions();

    /**
     * Совершает транзакциию и помечает, что транзакция выполнена.
     */
    void createTransaction();

    /**
     * Помечает, что транзакция уже была выполнена.
     */
    void markTransactionMade();
}
