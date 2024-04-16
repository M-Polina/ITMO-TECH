package com.mpolina.banks.transactions;

import com.mpolina.banks.accounts.Account;

/**
 * Интерфейс простой транзакции, то есть связанной с единственной простой операцией с одним счётом.
 * Простая операция - снятие или пополнение.
 */
public interface SimpleTransaction extends MoneyTransaction{
    /**
     * @return получение счёта, над которым была совершена транзакция.
     */
    Account getAccount();
}
