package com.mpolina.banks.messagehandlers;

import com.mpolina.banks.exceptions.BanksException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс обработчика сообщений, позволяет сохранять все сообщения
 * По сути играет роль устройства, хранящего историю сообщений.
 */
public class InMemoryMessageHandler implements MessageHandler{

    /**
     * Список сохранённых сообщений.
     */
    private ArrayList<String> messages = new ArrayList<String>();

    /**
     * @return Получение списка сохранённых сообщений.
     */
    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    /**
     * Сохраняет сообщение в списке {@code messages}
     *
     * @param newMessage сообщение, которое надо сохранить.
     */
    public void sendMessage(Message newMessage)
    {
        if (newMessage == null) {
            throw new BanksException("NullMessage while sending message in ConsoleMessageHandler.");
        }

        messages.add(newMessage.getStringMessage());
    }
}