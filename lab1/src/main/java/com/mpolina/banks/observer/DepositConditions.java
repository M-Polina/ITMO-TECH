package com.mpolina.banks.observer;

import com.mpolina.banks.banks.DepositInterestRateConditions;
import com.mpolina.banks.exceptions.BanksException;
import lombok.Getter;
import com.mpolina.banks.client.Subscriberable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс депозитных условий. Хранит список подписчиков, уведомляет их о своём изменении.
 */
public class DepositConditions implements Observable{
    private final int ONE_ELEMENT = 1;

    /**
     * Список подписчиков, которых надо уведомлять при изменении.
     */
    private ArrayList<Subscriberable> subscribersList = new ArrayList<Subscriberable>();

    @Getter
    private DepositInterestRateConditions depositInterestRateConditions;

    /**
     * Конструктор класса
     *
     * @param newConditions депозитные условия, с которыми будет создан класс
     */
    public DepositConditions(DepositInterestRateConditions newConditions)
    {
        if (newConditions == null)
        {
            throw new BanksException("Coditions is incorrect while creating Bank.");
        }

        depositInterestRateConditions = newConditions;
    }

    /**
     * Получение списка подписчиков на изменения
     *
     * @return Список подписчиков
     */
    public List<Subscriberable> getSubscribersList() {
        return Collections.unmodifiableList(subscribersList);
    }

    /**
     * Добавления подписчика, который будет получать учедомления об изменении депозитных условий.
     *
     * @param subscriber подписчик для добавления.
     */
    public void addSubscriber(Subscriberable subscriber)
    {
        if (subscriber == null) {
            throw new BanksException("Null Subscriber while Adding DepositConditions Subscriber.");
        }
        if (!subscribersList.contains(subscriber)) {
            subscribersList.add(subscriber);
        }
    }

    /**
     * Удаления подписчика, чтобы он больше неполучал учедомления об изменении.
     *
     * @param subscriber подписчик для удаления.
     */
    public void removeSubscriber(Subscriberable subscriber)
    {
        if (subscriber == null)
        {
            throw new BanksException("Null Subscriber while removing DebitInterestRate Subscriber.");
        }
        if (!hasSubscriber(subscriber)) {
            throw new BanksException("Subscriber doesn't exists, so it can't be added to DepositConditions Subscribers.");
        }

        subscribersList.remove(subscriber);
    }

    /**
     * Оповещение всех подписчиков об изменении данного объекта об изменении.
     */
    public void notifySubscribers()
    {
        for(Subscriberable sr : subscribersList)
        {
            sr.update(this);
        }
    }

    /**
     * Изменение депозитных условий с уведомлением об этом подписчиков {@see notifySubscribers}
     *
     * @param coditions новое значение депозитных условий
     */
    public void changeDepositConditions(DepositInterestRateConditions coditions)
    {
        if (coditions == null) {
            throw new BanksException("DepositCoditions is incorrect while changing.");
        }

        depositInterestRateConditions = coditions;
        notifySubscribers();
    }

    /**
     * Проверка присутствует ли подписчик среди подписчиков данного объекта.
     *
     * @param subscriber подписчик на проверку
     * @return truе если есть, иначе false
     */
    private boolean hasSubscriber(Subscriberable subscriber)
    {
        if (subscriber == null) {
            return false;
        }
        return subscribersList.stream ().filter(sb -> sb.getId() == subscriber.getId()).count() == ONE_ELEMENT;
    }
}