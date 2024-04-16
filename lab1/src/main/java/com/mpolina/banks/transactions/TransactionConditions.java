package com.mpolina.banks.transactions;

import com.mpolina.banks.exceptions.BanksException;
import lombok.Getter;

/**
 * Условия транзакции: счета, с которыми она связана, комиссия при совершении, количество денег
 */
@Getter
public class TransactionConditions {
    public final double MIN_MONEY_LIMIT = 0;

    /**
     * количество денег в транзакция
     */
    public double amountOfMoney;
    /**
     * комиссия на транзакцию
     */
    public double commission;

    /**
     * Конструктор условий странзакции
     *
     * @param withrawMoney количество денег на списание
     * @param newCommission комиссия на списание
     */
    public TransactionConditions(double withrawMoney, double newCommission)
    {
        if (withrawMoney < MIN_MONEY_LIMIT) {
            throw new BanksException("withrawMoney is incorrect while creating TransactionConditions.");
        }
        if (newCommission < MIN_MONEY_LIMIT) {
            throw new BanksException("Commission is incorrect while creating TransactionConditions.");
        }

        amountOfMoney = withrawMoney;
        commission = newCommission;
    }
}
