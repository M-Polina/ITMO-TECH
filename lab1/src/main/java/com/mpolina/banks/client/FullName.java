package com.mpolina.banks.client;

import com.mpolina.banks.exceptions.BanksException;
import lombok.Getter;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

/**
 * Полное имя человека, состоящее из строкового имени и фамилии, которые не null
 * и состоят не только из пробелов.
 */
public class FullName {
    /**
     * Конструктор полного имени
     *
     * @param newName имя для создания
     * @param newSurname фамилия для создания
     */
    public FullName(String newName, String newSurname)
    {
        if (Strings.isNullOrEmpty(newName))
        {
            throw new BanksException("Name is incorrect, so FullName can't be created.");
        }

        if (Strings.isNullOrEmpty(newSurname))
        {
            throw new BanksException("Surname is incorrect, so FullName can't be created.");
        }

        surname = newSurname;
        name = newName;
    }

    /**
     * Имя
     */
    @Getter
    private String name;
    /**
     * Фамилия
     */
    @Getter
    private String surname;
}
