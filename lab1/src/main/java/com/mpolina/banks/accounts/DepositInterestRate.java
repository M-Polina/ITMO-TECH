package com.mpolina.banks.accounts;

import com.mpolina.banks.exceptions.BanksException;
import lombok.Getter;
import com.mpolina.banks.banks.DepositInterestRateConditions;

/**
 * Класс, хранящий и определяющий депозитную процентную ставку.
 * Процентная ставка определяется из количества денег, с которым был открыт счёт
 * и из условий, на процентную ставку в зависимости от этого начального количества денег.
 */
public class DepositInterestRate {
    private final int MONTH_IN_YEAR = 12;
    private final double MIN_MONEY_LIMIT = 0;
    private final double MIN_INTEREST_RATE = 0;
    private final int MIN_ELEMENTS_IN_LIST_NUMBER = 0;

    private DepositInterestRateConditions conditions;
    @Getter
    private double value;


    /**
     * Конструктор класса
     *
     * @param newConditions условия, по которым будет определяться процентная ставка.
     * @param amountOfMoney начальное количество денег, с которым откроют счёт.
     *                      По нему определяется процентная ставка.
     */
    public DepositInterestRate(DepositInterestRateConditions newConditions, double amountOfMoney)
    {
        if (newConditions == null) {
            throw new BanksException("ConditionsList is null while creating DepositAccount.");
        }
        if (amountOfMoney < MIN_MONEY_LIMIT) {
            throw new BanksException("amountOfMoney to deposit is incorrect.");
        }

        conditions = newConditions;
        value = countInterestRate(newConditions, amountOfMoney);
    }

    public DepositInterestRateConditions getConditions() {
        return conditions;
    }

    /**
     * Считает месячную процентную ставку
     *
     * @return месячная процентная ставка.
     */
    public double getValuePerMonth(){
      return  value / MONTH_IN_YEAR;
    }

    /**
     * Вычисляет процентную ставку по условиям и сумме и меняет старую на новую.
     *
     * @param newConditions новые условия, по которым определится процентная ставка
     * @param amountOfMoney количество денег, по которому определять ставку.
     * @return новая процентная ставка
     */
    public double changeInterestRate(DepositInterestRateConditions newConditions, double amountOfMoney)
    {
        if (newConditions == null) {
            throw new BanksException("ConditionsList is null while creating DepositAccount.");
        }
        if (amountOfMoney < MIN_MONEY_LIMIT) {
            throw new BanksException("amountOfMoney to deposit is incorrect.");
        }

        conditions = newConditions;
        value = countInterestRate(newConditions, amountOfMoney);

        return value;
    }

    /**
     * Вычисляет процентную ставку по условиям и сумме.
     *
     * @param conditionsList условия для определения ставки.
     * @param amountOfMoney количество денег, по которому определится ставка
     * @return новая процентная ставка
     */
    private double countInterestRate(DepositInterestRateConditions conditionsList, double amountOfMoney)
    {
        for (int i = conditionsList.getInterestRatesList().size() - 1; i >= MIN_ELEMENTS_IN_LIST_NUMBER; i--)
        {
            if (amountOfMoney >= conditionsList.getMoneyLimitsList().get(i))
            {
                return conditionsList.getInterestRatesList().get(i);
            }
        }

        throw new BanksException("Bad conditions while counting Deposit Interest Rate.");
    }
}
