package com.mpolina.banks.client;

/**
 * Интерфейс "Строителя", отвечает за создание клиента с необходимыми данными.
 */
public interface Builderable {
    /**
     * Добавление адреса.
     *
     * @param address адрес, который будет добавлен
     */
    void setAddress(String address);

    /**
     * Добавление паспорта
     *
     * @param id id паспорта, по которому создастся паспокт, который будет добавлен
     */
    void setPassport(int id);

    /**
     * Из всех имеющихся данных создаёт клиента.
     *
     * @return созданный клиент.
     */
    ClientAccount createAndGetClient();
}
