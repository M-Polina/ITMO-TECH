package com.mpolina.banks.observer;

import com.mpolina.banks.exceptions.BanksException;
import lombok.Getter;
import com.mpolina.banks.client.Subscriberable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс дебетовой процентной ставки. Хранит список подписчиков, уведомляет их о своём изменении.
 */
public class DebitInterestRate implements Observable {
    private final double MIN_INTEREST_RATE = 0;
    private final double ONE_ELEMENT = 1;

    @Getter
    private double interestRate;

    /**
     * Список подписчиков, которых надо уведомлять при изменении.
     */
    private ArrayList<Subscriberable> subscribersList = new ArrayList<Subscriberable>();

    /**
     * Конструктор класса
     *
     * @param newInterestRate процентрная ставка
     */
    public DebitInterestRate(double newInterestRate) {
        if (newInterestRate <= MIN_INTEREST_RATE) {
            throw new BanksException("debitInterestRate is incorrect while creating DebitInterestRate.");
        }

        interestRate = newInterestRate;
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
     * Добавления подписчика, который будет получать учедомления об изменении дебетовой процентной ставки.
     *
     * @param newSubscriber подписчик для добавления.
     */
    public void addSubscriber(Subscriberable newSubscriber) {
        if (newSubscriber == null) {
            throw new BanksException("Null Subscriber while Adding DebitInterestRate Subscriber.");
        }
        if (!subscribersList.contains(newSubscriber)) {
            subscribersList.add(newSubscriber);
        }
    }

    /**
     * Удаления подписчика, чтобы он больше неполучал учедомления об изменении ставки.
     *
     * @param newSubscriber подписчик для удаления.
     */
    public void removeSubscriber(Subscriberable newSubscriber) {
        if (newSubscriber == null) {
            throw new BanksException("Null Subscriber while removing DebitInterestRate Subscriber.");
        }
        if (!hasSubscriber(newSubscriber)) {
            throw new BanksException("Subscriber doesn't exists, so it can't be added to DebitInterestRate Subscribers.");
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
     * Изменение дебетовой процентной ставки с уведомлением об этом подписчиков {@see notifySubscribers}
     *
     * @param newRate новое значение кредитной комиссии
     */
    public void changeDebitInterestRate(double newRate) {
        if (newRate <= MIN_INTEREST_RATE) {
            throw new BanksException("debitInterestRate is incorrect while changing.");
        }

        interestRate = newRate;
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
