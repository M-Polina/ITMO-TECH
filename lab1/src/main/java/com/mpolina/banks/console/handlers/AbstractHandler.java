package com.mpolina.banks.console.handlers;


/**
 * Абстрактный класс обработчика комманд консольного приложения "Банки".
 * Если послупила определённая команда, обработчик выполняет встроенную бизнес логику,
 * иначе передаёт управление следующему обработчику, если он есть.
 */
public abstract class AbstractHandler {

    /**
     * Следующий за данным обработчик.
     * Null, если данный обработчик последний в цепочке.
     */
    public AbstractHandler nextHendler;

    /**
     * Устанавливает следующий после данного обработчик.
     *
     * @param handler следующий обработчик
     * @return присвоенный обработчик
     */
    public AbstractHandler setNext(AbstractHandler handler) {
        nextHendler = handler;
        return handler;
    }

    /**
     * Выполняет бизнес логику, если запрос содержит определённую команду.
     * Иначе передаёт запрос следующему обработчику {@code nextHendler}
     *
     * @param request переданный запрос
     */
    public abstract void handle(Requestable request);
}