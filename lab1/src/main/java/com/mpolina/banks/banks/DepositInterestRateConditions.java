package com.mpolina.banks.banks;

import com.mpolina.banks.exceptions.BanksException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс условий депозитного счёта. Хранит валидированные список денежных лимитов и список процентный ставок,
 * соответствующих промежуткам между этими денежными приделами соответственно. (последниц процент -
 * от последнего числа в списке денежных педелов и до бесконечности.)
 */
public class DepositInterestRateConditions {
    private final double MIN_MONEY_LIMIT = 0;
    private final double MIN_INTEREST_RATE = 0;
    private final double NEEDED_DIFFERENCE_BETWEEN_ELEMENTES_IN_LISTS = 1;

    private ArrayList<Double> moneyLimitsList;
    private ArrayList<Double> interestRatesList;

    /**
     * Конструктор класса.
     * В лист с денежными пределами будет добавлен только 0, то есть переданная ставка
     * будет действительна для любой денежной суммы (разумеется положительной)
     *
     * @param newInterestRate депозитнная ставка, которая добавится в список ставок,
     *                        станет единственной на всём промежутке.
     */
    public DepositInterestRateConditions(double newInterestRate) {
        if (!interestRateIsCorrect(newInterestRate)) {
            throw new BanksException("Interest rate is incorrect while creating DepositInterestRateConditions.");
        }

        moneyLimitsList = new ArrayList<Double>();
        moneyLimitsList.add(MIN_MONEY_LIMIT);
        interestRatesList = new ArrayList<Double>();
        interestRatesList.add(newInterestRate);
    }

    /**
     * Конструктор класса.
     *
     * @param limits список денежных пределов, которые будут в классе.
     * @param interestRates список процентных ставок, которые будут в классе.
     */
    public DepositInterestRateConditions(ArrayList<Double> limits, ArrayList<Double> interestRates) {
        if (!moneyLimitsAreCorrect(limits)) {
            throw new BanksException("limits are incorrect while creating DepositInterestRateConditions.");
        }

        if (!interestRatesAreCorrect(interestRates)) {
            throw new BanksException("interestRates are incorrect while creating DepositInterestRateConditions.");
        }

        if ((interestRates.size() - limits.size()) != NEEDED_DIFFERENCE_BETWEEN_ELEMENTES_IN_LISTS) {
            throw new BanksException("interestRates and limits are incorrect while creating DepositInterestRateConditions.");
        }

        moneyLimitsList = new ArrayList<>();
        moneyLimitsList.add(MIN_MONEY_LIMIT);
        moneyLimitsList.addAll(limits);
        interestRatesList = interestRates;
    }

    public List<Double> getMoneyLimitsList() {
        return Collections.unmodifiableList(moneyLimitsList);
    }

    public List<Double> getInterestRatesList() {
        return Collections.unmodifiableList(interestRatesList);
    }

    /**
     * Проверяет валидность процентной ставки.
     *
     * @param interestRate процентная ставка на проверку.
     * @return true - если верно, false - иначе.
     */
    private boolean interestRateIsCorrect(double interestRate) {
        if (interestRate <= MIN_INTEREST_RATE) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет валижность денежного предела.
     *
     * @param moneyLimit денежный предел на проверкую
     * @return true - если верно, false - иначе.
     */
    private boolean moneyLimitIsCorrect(double moneyLimit) {
        if (moneyLimit <= MIN_MONEY_LIMIT) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет валидность списка процентных ставок.
     *
     * @param newInterestRatesList список процентных ставок на проверку.
     * @return true - если верно, false - иначе.
     */
    private boolean interestRatesAreCorrect(ArrayList<Double> newInterestRatesList) {
        if (newInterestRatesList == null) {
            return false;
        }

        var allCorrect = newInterestRatesList.stream().allMatch(interest -> interestRateIsCorrect(interest));

        return allCorrect;
    }

    /**
     * Проверяет валижность денежных пределов.
     *
     * @param givenMoneyLimitsList список денежных пределов на проверкую
     * @return true - если верно, false - иначе.
     */
    private boolean moneyLimitsAreCorrect(ArrayList<Double> givenMoneyLimitsList) {
        if (givenMoneyLimitsList == null)
            return false;

        var allCorrect = givenMoneyLimitsList.stream().allMatch(limit -> moneyLimitIsCorrect(limit));

        return allCorrect;
    }
}
