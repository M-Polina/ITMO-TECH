package com.mpolina.banks.messagehandlers;

/**
 * Класс обработчика сообщений, позволяет отправлять сообщение в нужное место.
 * По сути играет роль устройства, передающего сообщение.
 */
public interface MessageHandler {
    /**
     * Отправляет сообщение, то есть "доставляет" его в нужное место.
     *
     * @param message сообщение, которое надо отправить.
     */
    void sendMessage(Message message);
}
