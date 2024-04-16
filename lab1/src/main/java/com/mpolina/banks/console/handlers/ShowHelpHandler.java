package com.mpolina.banks.console.handlers;

import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

/**
 * Обработчик вывода всех возможных комманд
 */
public class ShowHelpHandler extends AbstractHandler {
    /**
     * При вводе команды "Help" выполняет бизнеслогику  {@see DoBusinessLogic()}
     *
     * @param request переданный запрос
     */
    public void handle(Requestable request) {
        if (Strings.isNullOrEmpty(request.getRequest())) {
            return;
        }
        if (request.getRequest().equals("Help")) {
            doBusinessLogic();
        }
        else {
            if (nextHendler != null) {
                nextHendler.handle(request);
            }
        }

    }

    /**
     * Выводит все возможные комманды, которые обрабатываются приложением.
     */
    private void doBusinessLogic() {
        System.out.println("List of commands available:");
        System.out.println("-Help");
        System.out.println("-Create bank");
        System.out.println("-Create client");
        System.out.println("-Create account");
        System.out.println("-Set address");
        System.out.println("-Set passport");
        System.out.println("-Change debit interest rate");
        System.out.println("-Change deposit interest rate conditions");
        System.out.println("-Change credit commission");
        System.out.println("-Change credit limit");
        System.out.println("-Change deposit interest rate conditions");
        System.out.println("-Show clients");
        System.out.println("-Show banks");
        System.out.println("-Show accounts");
        System.out.println("-Subscribe to notifications");
        System.out.println("-Speed up time");
        System.out.println("-Add");
        System.out.println("-Withdraw");
        System.out.println("-Transfer");
        System.out.println("-Rollback");
    }
}
