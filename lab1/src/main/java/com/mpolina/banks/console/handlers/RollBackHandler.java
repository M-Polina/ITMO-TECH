package com.mpolina.banks.console.handlers;

import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.exceptions.BanksException;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.util.Scanner;
import java.util.UUID;

/**
 * Обработчик по откату последней транзакции в банке.
 */
public class RollBackHandler extends AbstractHandler {
    /**
     * При вводе команды "RollBack" выполняет бизнеслогику  {@see DoBusinessLogic()}
     *
     * @param request переданный запрос
     */
    public void handle(Requestable request) {
        if (Strings.isNullOrEmpty(request.getRequest())) {
            return;
        }
        if (request.getRequest().equals("RollBack")) {
            doBusinessLogic();
        }
        else {
            if (nextHendler != null) {
                nextHendler.handle(request);
            }
        }

    }

    /**
     * Откат последней транзакции в банке в банке.
     * Ввод с консоли - номер банка(целое неотрицательное число)
     * При вводе некорректной комманды отката не происходит, метод заканчивается.
     */
    private void doBusinessLogic() {
        CentralBank centralBank = CentralBank.getInstance();
        Scanner stream = new Scanner(System.in);

        try {
            System.out.println("Rollback of last transaction in bank will be created.");

            System.out.println("Set Bank Id:");
            int bankId = Integer.parseInt(stream.nextLine());
            Bank bank = centralBank.getBankById(bankId);

            UUID transactionId = bank.getLastTransactionId();
            bank.rollbackTrabsaction(transactionId);

            System.out.println("RollBack was created!");
        }
        catch (BanksException ex1){
            System.out.println(ex1.getMessage());
        }
        catch (Exception ex) {
            return;
        }
    }
}
