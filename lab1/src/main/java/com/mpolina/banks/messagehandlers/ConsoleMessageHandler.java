package com.mpolina.banks.messagehandlers;

import com.mpolina.banks.exceptions.BanksException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс обработчика сообщений, позволяет выводить сообщения в консоль.
 * Хранит историю сообщений.
 */
public class ConsoleMessageHandler implements MessageHandler{
    /**
     * Список сохранённых сообщений в строковом формате.
     */
    private ArrayList<String> messages = new ArrayList<String>();

    /**
     * @return Получение списка сохранённых сообщений.
     */
    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    /**
     * Выводит сообщение в консоль и сохраняет его в списке {@code messages}
     *
     * @param newMessage сообщение, которое надо вывести.
     */
    public void sendMessage(Message newMessage)
    {
        if (newMessage == null) {
            throw new BanksException("NullMessage while sending message in ConsoleMessageHandler.");
        }

        System.out.println(newMessage.getStringMessage());
    }
}

