package com.mpolina.banks.client;

import com.mpolina.banks.exceptions.BanksException;
import lombok.Data;
import lombok.Getter;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

/**
 * Класс Адреса - не null строка, состоящая не только из символов пробелов.
 */
@Data
public class Address
{
    /**
     * Конструктор класса адреса
     *
     * @param address строка - адрес который будем хранить
     */
    public Address(String address)
    {

        if (Strings.isNullOrEmpty(address))
        {
            throw new BanksException("Address is incorrect, so Address can't be created.");
        }

        value = address;
    }

    /**
     * Строковое значение адреса - сам адрес.
     */
    @Getter
    private String value;
}
