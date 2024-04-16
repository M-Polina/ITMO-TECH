package com.mpolina.banks.client;

import com.mpolina.banks.observer.Observable;

import java.util.UUID;

/**
 * Интерфейс объектов, на которые можно подписаться.
 */
public interface Subscriberable {
    /**
     * @return id объекта, на который можно подписаться.
     */
    UUID getId();

    /**
     * Действия связанные с изменением того, на что подписан.
     *
     * @param observable - то, на изменение чего подписан.
     */
    void update(Observable observable);
}
