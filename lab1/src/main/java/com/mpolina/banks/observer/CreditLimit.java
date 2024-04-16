package com.mpolina.banks.observer;

import com.mpolina.banks.exceptions.BanksException;
import lombok.Getter;
import com.mpolina.banks.client.Subscriberable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс кредитного лимита. Хранит список подписчиков, уведомляет их о своём изменении.
 */
public class CreditLimit implements Observable {
    private final double MAX_CREDIT_LIMIT = 0;
    private final double ONE_ELEMENT = 1;

    @Getter
    private double limit;

    /**
     * Список подписчиков, которых надо уведомлять при изменении.
     */
    private ArrayList<Subscriberable> subscribersList = new ArrayList<Subscriberable>();

    /**
     * Конструктор класса кредитного лимита.
     *
     * @param newLimit кридитный предел, за который не может выйти клиент - отрицательное число.
     */
    public CreditLimit(double newLimit) {
        if (newLimit >= MAX_CREDIT_LIMIT) {
            throw new BanksException("CreditLimit is incorrect while creating CreditLimit.");
        }

        limit = newLimit;
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
     * Добавления подписчика, который будет получать учедомления об изменении кредитного лимита.
     *
     * @param newSubscriber подписчик для добавления.
     */
    public void addSubscriber(Subscriberable newSubscriber) {
        if (newSubscriber == null) {
            throw new BanksException("Null Subscriber while Adding CreditLimit Subscriber.");
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
    public void removeSubscriber(Subscriberable newSubscriber) {
        if (newSubscriber == null) {
            throw new BanksException("Null Subscriber while removing CreditLimit Subscriber.");
        }
        if (!hasSubscriber(newSubscriber)) {
            throw new BanksException("Subscriber doesn't exists, so it can't be added to CreditLimit Subscribers.");
        }

        subscribersList.remove(newSubscriber);
    }

    /**
     * Оповещение всех подписчиков об изменении данного объекта об изменении.
     */
    public void notifySubscribers() {
        for (Subscriberable sr : subscribersList) {
            sr.update(this);
        }
    }

    /**
     * Изменение кредитного лимита с уведомлением об этом подписчиков {@see notifySubscribers}
     *
     * @param newCreditLimit новое значение кредитного лимита
     */
    public void changeCreditLimit(double newCreditLimit) {
        if (newCreditLimit >= MAX_CREDIT_LIMIT) {
            throw new BanksException("CreditLimit is incorrect while changing.");
        }

        limit = newCreditLimit;
        notifySubscribers();
    }

    /**
     * Проверка присутствует ли подписчик среди подписчиков данного объекта.
     *
     * @param newSubscriber подписчик на проверку
     * @return truе если есть, иначе false
     */
    private boolean hasSubscriber(Subscriberable newSubscriber) {
        if (newSubscriber == null) {
            return false;
        }
        return subscribersList.stream().filter(sb -> sb.getId() == newSubscriber.getId()).count() == ONE_ELEMENT;
    }
}