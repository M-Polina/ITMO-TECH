package com.mpolina.banks.accounts;

import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.client.ClientAccount;

/**
 * Абстрактный класс паттерна "фабричный метод" для создания счёта в банке.
 */
public abstract class AccountCreator
{
    /**
     * Создание счёта в банке.
     *
     * @param id id которое будет присвоено счёту
     * @param client клиент, которому принадлежит счёт.
     * @param bank банк, в котором открывается счёт.
     * @return созданный счёт.
     */
    public abstract Account CreateAccount(int id, ClientAccount client, Bank bank);
}