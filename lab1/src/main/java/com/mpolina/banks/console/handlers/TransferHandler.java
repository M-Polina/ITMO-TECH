package com.mpolina.banks.console.handlers;

import com.mpolina.banks.accounts.Account;
import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.exceptions.BanksException;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.util.Scanner;

/**
 * Обработчик по переводу денег со счёта на счёт.
 */
public class TransferHandler extends AbstractHandler {
    private final double MIN_NUMBER = 0;

    /**
     * При вводе команды "Transfer" выполняет бизнеслогику  {@see DoBusinessLogic()}
     *
     * @param request переданный запрос
     */
    public void handle(Requestable request) {
        if (Strings.isNullOrEmpty(request.getRequest())) {
            return;
        }
        if (request.getRequest().equals("Transfer")) {
            doBusinessLogic();
        }
        else {
            if (nextHendler != null) {
                nextHendler.handle(request);
            }
        }

    }

    /**
     *Перевод денег со счёта в банке на счёт в банке.
     * Ввод с консоли - номер банка для снятия(целое неотрицательное число),
     * номер счёта для снятия(целое неотрицательное число),
     * номер банка для перевода туда(целое неотрицательное число),
     * номер счёта для перевода туда(целое неотрицательное число),
     * количество денег на перевод (положительное число).
     * При вводе некорректной комманды списания денег не происходит, метод заканчивается.
     */
    private void doBusinessLogic() {
        CentralBank centralBank = CentralBank.getInstance();
        Scanner stream = new Scanner(System.in);

        try {
            System.out.println("Set information about account to transfer from:");
            System.out.println("Set Bank Id:");
            int bankId = Integer.parseInt(stream.nextLine());
            Bank bankFrom = centralBank.getBankById(bankId);

            System.out.println("Set account Id:");
            int accountId = Integer.parseInt(stream.nextLine());
            Account accountFrom = bankFrom.getAccountById(accountId);

            System.out.println("Set information about account to transfer to:");
            System.out.println("Set Bank Id:");
            bankId = Integer.parseInt(stream.nextLine());
            Bank bankTo = centralBank.getBankById(bankId);

            System.out.println("Set account Id:");
            accountId = Integer.parseInt(stream.nextLine());
            Account accountTo = bankTo.getAccountById(accountId);

            System.out.println("Set ammount of money to transfer:");
            double amount = Double.parseDouble(stream.nextLine());

            if (amount <= MIN_NUMBER) {
                return;
            }

            bankFrom.transferMoney(accountFrom, accountTo, amount);
            System.out.println("Transfer was created!");
        }
        catch (BanksException ex1){
            System.out.println(ex1.getMessage());
        }
        catch (Exception ex) {
            return;
        }
    }
}
