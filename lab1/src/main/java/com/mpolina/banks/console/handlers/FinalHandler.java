package com.mpolina.banks.console.handlers;

/**
 * Последниый обработчик в цепочке.
 * Если до него дошла очередь, значит для введённой команды не существует обработчика,
 * следовательно команда не верна, что и выводит обработчик.
 */
public class FinalHandler extends AbstractHandler {

    /**
     * Выводит, что введённая команда не верна.
     *
     * @param request переданный запрос
     */
    public void handle(Requestable request) {
        System.out.println("Wrong command! Try again!");
    }
}
