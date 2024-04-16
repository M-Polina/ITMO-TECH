package com.mpolina.banks.client;

import com.mpolina.banks.exceptions.BanksException;
import lombok.Getter;

/**
 * Пасспорт, имеющий целый неотрицательный идентефикатор
 */
public class Passport
{
    private final int MIN_ID = 0;

    /**
     * Конструктор паспорта
     *
     * @param newId id для создаваемого паспорта.
     */
    public Passport(int newId)
    {
        if (id < MIN_ID) {
            throw new BanksException("Id is incorrect, so Passport can't be created.");
        }

        id = newId;
    }

    /**
     * идентефекатор паспорта.
     */
    @Getter
    private int id;
}