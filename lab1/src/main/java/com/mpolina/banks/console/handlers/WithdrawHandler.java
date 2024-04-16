package com.mpolina.banks.console.handlers;

import com.mpolina.banks.accounts.Account;
import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.exceptions.BanksException;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.util.Scanner;

/**
 * Обработчик по  снятию денег со счёта.
 */
public class WithdrawHandler extends AbstractHandler {
    private final double MIN_NUMBER = 0;

    /**
     * При вводе команды "Withdraw" выполняет бизнеслогику  {@see DoBusinessLogic()}
     *
     * @param request переданный запрос
     */
    public void handle(Requestable request) {
        if (Strings.isNullOrEmpty(request.getRequest())) {
            return;
        }
        if (request.getRequest().equals("Withdraw")) {
            doBusinessLogic();
        }
        else {
            if (nextHendler != null) {
                nextHendler.handle(request);
            }
        }

    }

    /**
     * Снятие денег со счёта в банке.
     * Ввод с консоли - номер банка(целое неотрицательное число),
     * номер счёта (целое неотрицательное число),
     * количество денег на списание (положительное число).
     * При вводе некорректной комманды списания денег не происходит, метод заканчивается.
     */
    private void doBusinessLogic() {
        CentralBank centralBank = CentralBank.getInstance();
        Scanner stream = new Scanner(System.in);

        try {
            System.out.println("Set information about account to withdraw from:");
            System.out.println("Set Bank Id:");
            int bankId = Integer.parseInt(stream.nextLine());
            Bank bankFrom = centralBank.getBankById(bankId);

            System.out.println("Set account Id:");
            int accountId = Integer.parseInt(stream.nextLine());
            Account accountFrom = bankFrom.getAccountById(accountId);

            System.out.println("Set ammount of money to withdraw:");
            double amount = Double.parseDouble(stream.nextLine());

            if (amount <= MIN_NUMBER) {
                return;
            }

            bankFrom.withdrawMoney(accountFrom.getId(), amount);

            System.out.println("Money were withrew!");
        }
        catch (BanksException ex1) {
            System.out.println(ex1.getMessage());
        }
        catch (Exception ex) {
            return;
        }
    }
}
