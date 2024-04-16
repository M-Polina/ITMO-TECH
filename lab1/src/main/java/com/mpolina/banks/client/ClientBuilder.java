package com.mpolina.banks.client;

import com.mpolina.banks.exceptions.BanksException;
import lombok.Getter;
import com.mpolina.banks.banks.Bank;

/**
 * Класс строителя для клиентов, позволяющий добавить все желаемые данные клиенту (или не добавить),
 * а потом создать клиета с этими данными.
 */
@Getter
public class ClientBuilder implements Builderable {
    /**
     * Полное имя клиента
     */
    private FullName name;
    /**
     * Банк, которому принадлежит клиент
     */
    private Bank bank;
    /**
     * адрес клиента, который можно дабавить или не добавить
     */
    private Address address;
    /**
     * паспорт клиента, который можно дабавить или не добавить.
     */
    private Passport passport;

    /**
     * Конструктор строителя клиента.
     *
     * @param newBank банк, которому принадлежит клиент.
     * @param newName имя клиента.
     * @param newSurname фамилия клиента.
     */
    public ClientBuilder(Bank newBank, String newName, String newSurname) {
        if (newBank == null) {
            throw new BanksException("Bank is null while creating ClientBuilder.");
        }

        bank = newBank;
        name = new FullName(newName, newSurname);
    }

    /**
     * Добавление адреса клиенту.
     *
     * @param newAddress адрес, который будет добавлен клиенту.
     */
    public void setAddress(String newAddress) {
        address = new Address(newAddress);
    }

    /**
     * Добавление паспорта клиенту.
     *
     * @param id id паспорта, по которому создастся паспокт, который будет добавлен клиенту.
     */
    public void setPassport(int id) {
        passport = new Passport(id);
    }

    /**
     * Из всех имеющихся данных создаёт клиента.
     *
     * @return созданный клиент.
     */
    public ClientAccount createAndGetClient() {
        ClientAccount clientAccount = new ClientAccount(bank, name, address, passport);
        return clientAccount;
    }
}