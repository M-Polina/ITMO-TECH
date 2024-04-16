package com.mpolina.banks.observer;

import com.mpolina.banks.exceptions.BanksException;
import lombok.Getter;
import com.mpolina.banks.client.Subscriberable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс предела денег, которые может снять подозрительный клиент за одну транзакцию.
 * Подозрительный клиент - без паспорта или адреса.
 * Класс хранит список подписчиков, уведомляет их о своём изменении.
 */
public class SuspiciousClientMoneyLimit implements Observable {
    private final double MIN_MONEY_LIMIT = 0;
    private final double ONE_ELEMENT = 1;
    @Getter
    private double moneyLimit;

    /**
     * Список подписчиков, которых надо уведомлять при изменении.
     */
    private ArrayList<Subscriberable> subscribersList = new ArrayList<Subscriberable>();

    /**
     * Конструктор класса
     *
     * @param limit предел снятия денег за одну транзакцию для подозрительного клиента.
     */
    public SuspiciousClientMoneyLimit(double limit) {
        if (limit < MIN_MONEY_LIMIT) {
            throw new BanksException("suspiciousClientMoneyLimit is incorrect while creating it.");
        }

        moneyLimit = limit;
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
     * Добавления подписчика, который будет получать учедомления об изменении лимита списывания для
     * подозрительного клиента.
     *
     * @param subscriber подписчик для добавления.
     */
    public void addSubscriber(Subscriberable subscriber) {
        if (subscriber == null) {
            throw new BanksException("Null Subscriber while Adding suspiciousClientMoneyLimit Subscriber.");
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
    public void removeSubscriber(Subscriberable subscriber) {
        if (subscriber == null) {
            throw new BanksException("Null Subscriber while removing suspiciousClientMoneyLimit Subscriber.");
        }
        if (!hasSubscriber(subscriber)) {
            throw new BanksException("Subscriber doesn't exists, so it can't be added to suspiciousClientMoneyLimit Subscribers.");
        }
        subscribersList.remove(subscriber);
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
     * Изменение депозитных условий с уведомлением об этом подписчиков {@see notifySubscribers}
     *
     * @param newMoneyLimit новое значение предела снятия денег
     *                      за одну транзакцию для подозрительного клиента.
     */
    public void changeSuspiciousClientMoneyLimit(double newMoneyLimit) {
        if (newMoneyLimit < MIN_MONEY_LIMIT) {
            throw new BanksException("SuspiciousClientMoneyLimit is incorrect while changing.");
        }

        moneyLimit = newMoneyLimit;
        notifySubscribers();
    }

    /**
     * Проверка присутствует ли подписчик среди подписчиков данного объекта.
     *
     * @param subscriber подписчик на проверку
     * @return truе если есть, иначе false
     */
    private boolean hasSubscriber(Subscriberable subscriber) {
        if (subscriber == null) {
            return false;
        }

        return subscribersList.stream().filter(sb -> sb.getId() == subscriber.getId()).count() == ONE_ELEMENT;
    }
}