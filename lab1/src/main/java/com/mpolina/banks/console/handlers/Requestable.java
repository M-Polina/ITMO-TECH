package com.mpolina.banks.console.handlers;

import java.util.Scanner;

/**
 * Интерфейс запроса, передоваемого в цепочке обработчиков.
 */
public interface Requestable {
    /**
     * @return строковую введённую команду.
     */
    String getRequest();

    /**
     * @return поток, из которого осуществляется чтение команды.
     */
    Scanner getStream();
}
