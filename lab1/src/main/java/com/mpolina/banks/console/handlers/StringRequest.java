package com.mpolina.banks.console.handlers;

import lombok.Getter;

import java.util.Scanner;

/**
 * Конкретный тип запроса, который передаётся в цпочке обработчиков
 */
@Getter
public class StringRequest implements Requestable {
    private String request;
    private Scanner stream;

    /**
     * @param newRequest строковая комманда запроса
     * @param in поток ввода команды
     */
    public StringRequest(String newRequest, Scanner in) {
        request = newRequest;
        stream = in;
    }
}
