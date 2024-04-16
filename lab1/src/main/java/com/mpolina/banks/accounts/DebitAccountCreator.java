package com.mpolina.banks.accounts;

import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.client.ClientAccount;

/**
 * Класс для создания дебетового счёта в банке.
 */
public class DebitAccountCreator extends AccountCreator {
    /**
     * Создание дебетового счёта в банке.
     *
     * @param id id которое будет присвоено счёту
     * @param client клиент, которому принадлежит счёт.
     * @param bank банк, в котором открывается счёт.
     * @return созданный счёт.
     */
    public Account CreateAccount(int id, ClientAccount client, Bank bank) {
        return new DebitAccount(id, client);
    }
}
