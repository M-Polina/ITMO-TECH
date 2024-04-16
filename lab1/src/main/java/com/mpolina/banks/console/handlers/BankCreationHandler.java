package com.mpolina.banks.console.handlers;

import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.banks.DepositInterestRateConditions;
import com.mpolina.banks.exceptions.BanksException;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.util.Scanner;

/**
 * Обработчик, позволяющий создать банк при вводе команды "Create bank".
 */
public class BankCreationHandler extends AbstractHandler {
    private final double MIN_NUMBER = 0;

    /**
     * При вводе команды "Create bank" выполняет бизнеслогику  {@see DoBusinessLogic()}.
     * Если команда в запросе отличается, то передаёт запрос следующему обработчику.
     *
     * @param request переданный запрос
     */
    public void handle(Requestable request) {
        if (Strings.isNullOrEmpty(request.getRequest())) {
            return;
        }
        if (request.getRequest().equals("Create bank")) {
            doBusinessLogic();
        }
        else {
            if (nextHendler != null) {
                nextHendler.handle(request);
            }
        }

    }

    /**
     * Метод для создания банка.
     * С консоли вводятся название банка, кредитная комиссия, депозитный и дебетовый проценты,
     * лимит на снятие если клиент подозрительный (не имеет адреса или паспорта)
     * и отрицательный лимит ухода в минус на кредитном счёте.
     *
     * При вводе некорректной комманды банк не создаётся, метод заканчивается.
     */
    private void doBusinessLogic() {
        CentralBank centralBank = CentralBank.getInstance();
        Scanner stream = new Scanner(System.in);

        try {
            System.out.println("Set Bank name:");

            String name = stream.nextLine();

            if (name == null || name.trim().isEmpty()) {
                return;
            }

            System.out.println("Set Debit Rate:");
            double debitRate = Double.parseDouble(stream.nextLine());

            if (debitRate <= MIN_NUMBER) {
                return;
            }

            System.out.println("Set Deposit Rate:");
            double depositRate = Double.parseDouble(stream.nextLine());

            if (depositRate <= MIN_NUMBER) {
                return;
            }


            System.out.println("Set commission:");
            double commission = Double.parseDouble(stream.nextLine());

            if (commission <= MIN_NUMBER) {
                return;
            }

            System.out.println("Set Credit Limit:");
            double creditLim = Double.parseDouble(stream.nextLine());

            if (creditLim > MIN_NUMBER) {
                return;
            }

            System.out.println("Set Suspicious Client Limit:");
            double suspiciousClientLimit = Double.parseDouble(stream.nextLine());

            if (suspiciousClientLimit <= MIN_NUMBER) {
                return;
            }

            DepositInterestRateConditions deposintConditions = new DepositInterestRateConditions(depositRate);
            Bank bank1 = centralBank.createBank(name, debitRate, commission, creditLim, deposintConditions, suspiciousClientLimit);
            System.out.println("Bank was created!");
        }
        catch (BanksException ex1){
            System.out.println(ex1.getMessage());
        }
        catch (Exception ex) {
            return;
        }
    }
}
