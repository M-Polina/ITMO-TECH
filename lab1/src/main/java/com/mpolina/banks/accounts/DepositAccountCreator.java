package com.mpolina.banks.accounts;

import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.client.ClientAccount;

/**
 * Класс для создания депозитного счёта в банке.
 */
public class DepositAccountCreator extends AccountCreator {
    /**
     * Начальное количество денег, с которым хотят открыть счёт
     */
    private double startAmountOfMoney;
    /**
     * Количество месяцев, на которое хотят открыть счёт.
     */
    private int monthDuration;

    /**
     * Конструктор депозитного счёта.
     * Нужен чтобы хранить дополнительные данные о будущем счёте, чтобы потом была возмжность его создать.
     *
     * @param amountOfMoney начальное количество денег, с которым хотят открыть счёт
     * @param newMonths количество месяцев, на которое хотят открыть счёт.
     */
    public DepositAccountCreator(double amountOfMoney, int newMonths) {
        monthDuration = newMonths;
        startAmountOfMoney = amountOfMoney;
    }

    /**
     * Создание дкпозитного счёта в банке.
     *
     * @param id id которое будет присвоено счёту
     * @param client клиент, которому принадлежит счёт.
     * @param bank банк, в котором открывается счёт.
     * @return созданный счёт.
     */
    public Account CreateAccount(int id, ClientAccount client, Bank bank) {
        return new DepositAccount(id, client, startAmountOfMoney, monthDuration);
    }
}