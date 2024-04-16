package com.mpolina.banks.time;


import lombok.Getter;

import java.time.LocalDateTime;
import java.time.Period;

/**
 * Класс времени, хранящий текущее время, позволяющий его ускорить и посчитать сколько месяцев
 * прошло с какой-то даты.
 */
public class OwnTime {
    private final int ONE_DAY = 1;
    private final int MIN_DAYS_NUM = 0;
    private final int DAYS_IN_MONTH = 30;

    private static OwnTime instance;

    /**
     * Возвращает текущее время на компьютере.
     */
    private OwnTime() {
        timeNow = LocalDateTime.now();
    }

    /**
     * Текущее время в классе
     */
    @Getter
    private LocalDateTime timeNow;


    /**
     * Возвращает существующий экземпляр класса OwnTime или создаёт новый
     * если его пока не существует.
     *
     * @return экземпляр класса OwnTime
     */
    public static OwnTime getInstance() {
        if (instance == null)
            instance = new OwnTime();

        return instance;
    }

    /**
     * Ускоряет время на один день
     */
    public void speedUp() {

        timeNow = timeNow.plusDays(ONE_DAY);
    }

    /**
     * Считает сколько месяцев прошло с переданной даты
     *
     * @param time время, относительно которого надо считать месяцы
     * @return сколько месяцев прошло с переданной даты {@code time}
     */
    public boolean countMonthPassed(LocalDateTime time) {
        Period period = Period.between(time.toLocalDate(), timeNow.toLocalDate());
        return ((period.getDays() % DAYS_IN_MONTH) == MIN_DAYS_NUM) && (timeNow.isAfter(time));
    }
}