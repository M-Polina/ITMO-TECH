package com.mpolina.banks.console.handlers;

import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.exceptions.BanksException;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

/**
 * Обработчик вывода всех банков
 */
public class ShowBanksHendler extends AbstractHandler {
    /**
     * При вводе команды "Show banks" выполняет бизнеслогику  {@see DoBusinessLogic()}
     *
     * @param request переданный запрос
     */
    public void handle(Requestable request) {
        if (Strings.isNullOrEmpty(request.getRequest())) {
            return;
        }
        if (request.getRequest().equals("Show banks")) {
            doBusinessLogic();
        }
        else {
            if (nextHendler != null) {
                nextHendler.handle(request);
            }
        }

    }

    /**
     * Выводит все банки: Название, Id банка
     * Ввод с консоли - номер банка(целое неотрицательное)
     * При вводе некорректной комманды вывода не происходит, метод заканчивается.
     */
    private void doBusinessLogic() {
        CentralBank centralBank = CentralBank.getInstance();

        try {
            for (var acc : centralBank.getBanksList()) {
                System.out.println(acc.getName() + " : " + acc.getId() + "");
            }
        }
        catch (BanksException ex1){
            System.out.println(ex1.getMessage());
        }
        catch (Exception ex) {
            return;
        }
    }
}
