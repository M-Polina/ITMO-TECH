package com.mpolina.banks.messagehandlers;

/**
 * Класс сообщения позволяющий получить сообщение об изменении конкретного объекта в нужной форме.
 */
public interface Message {
    /**
     * Получение сообщения в строковом формате об изменении определённого объекта.
     *
     * @return строковое сообщение
     */
    String getStringMessage();
}
