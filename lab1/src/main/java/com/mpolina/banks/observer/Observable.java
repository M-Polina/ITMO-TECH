package com.mpolina.banks.observer;

import com.mpolina.banks.client.Subscriberable;

/**
 * Интрефейс объекта, на изменение которого можно подписаться.
 */
public interface Observable {
    /**
     * Добавления подписчика, который будет получать учедомления об изменении объекта.
     *
     * @param subscriber подписчик для добавления.
     */
    void addSubscriber(Subscriberable subscriber);

    /**
     * Удаления подписчика, чтобы он больше неполучал учедомления об изменении объекта.
     *
     * @param subscriber подписчик для удаления.
     */
    void removeSubscriber(Subscriberable subscriber);

    /**
     * Оповещение всех подписчиков об изменении данного объекта.
     */
    void notifySubscribers();
}
