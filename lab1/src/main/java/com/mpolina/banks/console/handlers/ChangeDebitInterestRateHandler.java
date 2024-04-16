package com.mpolina.banks.console.handlers;

import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.exceptions.BanksException;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.util.Scanner;

/**
 * Обработчик по изменению дебетовой процентной ставки в банке.
 */
public class ChangeDebitInterestRateHandler extends AbstractHandler {
    private final double MIN_NUMBER = 0;

    /**
     * При вводе команды "Change debit interest rate" выполняет бизнеслогику  {@see DoBusinessLogic()}
     *
     * @param request переданный запрос
     */
    public void handle(Requestable request) {
        if (Strings.isNullOrEmpty(request.getRequest())) {
            return;
        }
        if (request.getRequest().equals("Change debit interest rate")) {
            doBusinessLogic();
        }
        else {
            if (nextHendler != null) {
                nextHendler.handle(request);
            }
        }

    }

    /**
     * Изменение дебетовой процентной ставки
     * Ввод с консоли - номер банка(целое неотрицательное), новая ставка (положительные числа).
     * При вводе некорректной комманды изменение ставки не происходит, метод заканчивается.
     */
    private void doBusinessLogic() {
        CentralBank centralBank = CentralBank.getInstance();
        Scanner stream = new Scanner(System.in);

        try {
            System.out.println("Set Bank Id:");
            int bankId = Integer.parseInt(stream.nextLine());
            Bank bank = centralBank.getBankById(bankId);

            System.out.println("Set debit interest rate:");
            double debitRate = Double.parseDouble(stream.nextLine());
            if (debitRate <= MIN_NUMBER) {
                return;
            }

            bank.changeDebitInterestRate(debitRate);
            System.out.println("Debit interest rate was changed!");
        }
        catch (BanksException ex1){
            System.out.println(ex1.getMessage());
        }
        catch (Exception ex) {
            return;
        }
    }
}
