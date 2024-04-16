package com.mpolina.banks.messagehandlers;

import com.mpolina.banks.exceptions.BanksException;
import com.mpolina.banks.observer.*;
import lombok.Getter;

/**
 * Класс сообщения, хранящий обработчик сообщения (устройство отправки сообщения)
 * и позволяющий получить сообщение об изменении конкретного условия счёта в банке в нужной форме.
 */
public class ObservableMessage implements Message {
    /**
     * Конструктор класса.
     *
     * @param newObservable устройство, в соответствии с изменением которого
     *                      надо будет получить соответствующее сообщение.
     */
    public ObservableMessage(Observable newObservable) {
        if (newObservable == null) {
            throw new BanksException("Null IObservable while creating IObservableMessage");
        }

        observable = newObservable;
    }

    /**
     * устройство, в соответствии с изменением которого
     * надо будет получить соответствующее сообщение.
     */
    @Getter
    private Observable observable;

    /**
     * Получение сообщения в строковом формате об изменении определённого условия счетов в банке.
     *
     * @return строковое сообщение об изменении.
     */
    public String getStringMessage() {
        String stringMessage = "";
        if (observable instanceof CreditCommission) {
            stringMessage = stringComissionMessage();
        }
        if (observable instanceof CreditLimit) {
            stringMessage = stringCreditLimitMessage();
        }
        if (observable instanceof DebitInterestRate) {
            stringMessage = stringDebitInterestRateMessage();
        }
        if (observable instanceof SuspiciousClientMoneyLimit) {
            stringMessage = stringSuspiciousClientMessage();
        }
        if (observable instanceof DepositConditions) {
            stringMessage = stringDepositConditionsMessage();
        }
        return stringMessage;
    }

    private String stringComissionMessage() {
        return "Credit comission was changed to " + ((CreditCommission) observable).getCommission() + "!";
    }

    private String stringCreditLimitMessage() {
        return "Credit limit was changed to " + ((CreditLimit) observable).getLimit() + "!";
    }

    private String stringDebitInterestRateMessage() {
        return "Debit interest rate was changed to " + ((DebitInterestRate) observable).getInterestRate() + "!";
    }

    private String stringDepositConditionsMessage() {
        return "Deposit Conditions were changed!";
    }

    private String stringSuspiciousClientMessage() {
        return "Suspicious Client Money Limit was changed!";
    }
}
