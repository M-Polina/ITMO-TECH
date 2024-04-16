package com.mpolina.banks.observer;

import com.mpolina.banks.exceptions.BanksException;
import lombok.Getter;
import com.mpolina.banks.client.Subscriberable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс кредитной комиссии. Хранит список подписчиков, уведомляет их о своём изменении.
 */
public class CreditCommission implements Observable{
    private final double MIN_COMISSION = 0;
    private final int ONE_ELEMENT = 1;

    /**
     * Список подписчиков, которых надо уведомлять при изменении.
     */
    private ArrayList<Subscriberable> subscribersList = new ArrayList<Subscriberable>();

    @Getter
    private double commission;

    /**
     * Конструктор класса комиссии.
     *
     * @param newCommission комиссия, которая быдет списываться при уходе в минус на кредитном счету,
     *                      положительное число.
     */
    public CreditCommission(double newCommission)
    {
        if (newCommission <= MIN_COMISSION) {
            throw new BanksException("Commission is incorrect while creating Commission.");
        }

        commission = newCommission;
    }

    /**
     * Получение списка подписчиков на изменения
     *
     * @return Список подписчиков
     */
    public List<Subscriberable> getSubscribersList() {
        return  Collections.unmodifiableList(subscribersList);
    }

    /**
     * Добавления подписчика, который будет получать учедомления об изменении кредитной комиссии.
     *
     * @param newSubscriber подписчик для добавления.
     */
    public void addSubscriber(Subscriberable newSubscriber)
    {
        if (newSubscriber == null) {
            throw new BanksException("Null Subscriber while Adding Commission Subscriber.");
        }
        if (!subscribersList.contains(newSubscriber)) {
            subscribersList.add(newSubscriber);
        }
    }

    /**
     * Удаления подписчика, чтобы он больше неполучал учедомления об изменении комиссии.
     *
     * @param newSubscriber подписчик для удаления.
     */
    public void removeSubscriber(Subscriberable newSubscriber)
    {
        if (newSubscriber == null) {
            throw new BanksException("Null Subscriber while removing Commission Subscriber.");
        }
        if (!hasSubscriber(newSubscriber)) {
            throw new BanksException("Subscriber doesn't exists, so it can't be added to Commission Subscribers.");
        }

        subscribersList.remove(newSubscriber);
    }


    /**
     * Оповещение всех подписчиков об изменении данного объекта об изменении.
     */
    public void notifySubscribers()
    {
        for(Subscriberable sr : subscribersList ){
            sr.update(this);
        }
    }

    /**
     * Изменение кредитной комиссии с уведомлением об этом подписчиков {@see notifySubscribers}
     *
     * @param newCommission новое значение кредитной комиссии
     */
    public void changeCreditComission(double newCommission)
    {
        if (newCommission <= MIN_COMISSION) {
            throw new BanksException("Commission is incorrect while changing.");
        }

        commission = newCommission;
        notifySubscribers();
    }

    /**
     * Проверка присутствует ли подписчик среди подписчиков данного объекта.
     *
     * @param newSubscriber подписчик на проверку
     * @return truе если есть, иначе false
     */
    private boolean hasSubscriber(Subscriberable newSubscriber)
    {
        if (newSubscriber == null) {
            return false;
        }
        return subscribersList.stream().filter(sb -> sb.getId() == newSubscriber.getId()).count() == ONE_ELEMENT;
    }
}
