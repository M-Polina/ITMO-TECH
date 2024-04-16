package com.mpolina.banks.banks;

import com.mpolina.banks.observer.*;
import com.mpolina.banks.observer.*;


/**
 * Список условий для счетов для конкретного банка - кредитный предел, депозитная и
 * дебетовая процентные ставки, предел снятия денег для людей без паспорта или адреса
 * и депозитные условия.
 */
public class BankConditions {
    private DebitInterestRate debitInterestRate;
    private CreditLimit creditLimit;
    private CreditCommission commission;
    /**
     * Предел снятия денег для людей без паспорта или адреса.
     */
    private SuspiciousClientMoneyLimit suspiciousClientMoneyLimit;
    private DepositConditions depositConditions;


    public BankConditions(double newDebitInterestRate,
                          double newCommission,
                          double newCreditLimit,
                          DepositInterestRateConditions newDepositCoditions,
                          double newSuspiciousClientMoneyLimit)
    {
        debitInterestRate = new DebitInterestRate(newDebitInterestRate);
        commission = new CreditCommission(newCommission);
        creditLimit = new CreditLimit(newCreditLimit);
        depositConditions = new DepositConditions(newDepositCoditions);
        suspiciousClientMoneyLimit = new SuspiciousClientMoneyLimit(newSuspiciousClientMoneyLimit);
    }

    public double getDebitInterestRate(){
        return debitInterestRate.getInterestRate();
    }
    public double getComission(){
        return commission.getCommission();
    }
    public CreditCommission getComissionClass() {
        return commission;
    }
    public double getCreditLimit() {
        return creditLimit.getLimit();
    }
    public CreditLimit getCreditLimitClass() {
        return creditLimit;
    }
    public DebitInterestRate getDebitInterestRateClass(){return debitInterestRate;}
    public double getSuspiciousClientMoneyLimit() {
        return suspiciousClientMoneyLimit.getMoneyLimit();
    }
    public SuspiciousClientMoneyLimit getSuspiciousClientMoneyLimitClass() {
        return suspiciousClientMoneyLimit;
    }

    public DepositInterestRateConditions getDepositInterestRateConditions() {
        return depositConditions.getDepositInterestRateConditions();
    }
    public DepositConditions getDepositConditionsClass() {
        return depositConditions;
    }

    /**
     * Изменение дебетовой ставки.
     *
     * @param newRate новая дебетовая ставка.
     */
    public void changeDebitInterestRate(double newRate)
    {
        debitInterestRate.changeDebitInterestRate(newRate);
    }

    /**
     * Изменение кридитной комиссии.
     *
     * @param newCommission новая кредитная комиссия
     */
    public void changeCreditComission(double newCommission)
    {
        commission.changeCreditComission(newCommission);
    }

    public void changeCreditLimit(double newCreditLimit)
    {
        creditLimit.changeCreditLimit(newCreditLimit);
    }

    /**
     * Изменение денежного предела на списание со счетов подозрительных клиентов
     * (без адреса или паспорта)
     *
     * @param newMoneyLimit новый денежный предел.
     */
    public void changeSuspiciousClientMoneyLimit(double newMoneyLimit)
    {
        suspiciousClientMoneyLimit.changeSuspiciousClientMoneyLimit(newMoneyLimit);
    }

    /**
     * Изменение депозитных условий.
     *
     * @param newCoditions новые депозитные условия
     */
    public void changeDepositConditions(DepositInterestRateConditions newCoditions)
    {
        depositConditions.changeDepositConditions(newCoditions);
    }
}
