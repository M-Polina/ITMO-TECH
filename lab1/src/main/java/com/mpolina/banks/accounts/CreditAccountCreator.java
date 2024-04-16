package com.mpolina.banks.accounts;

import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.client.ClientAccount;

/**
 * Класс для создания кредитного счёта в банке.
 */
public class CreditAccountCreator extends AccountCreator {
    /**
     * Создание кредитного счёта в банке.
     *
     * @param id id которое будет присвоено счёту
     * @param client клиент, которому принадлежит счёт.
     * @param bank банк, в котором открывается счёт.
     * @return созданный счёт.
     */
    public Account CreateAccount(int id, ClientAccount client, Bank bank) {
        return new CreditAccount(id, client);
    }
}
