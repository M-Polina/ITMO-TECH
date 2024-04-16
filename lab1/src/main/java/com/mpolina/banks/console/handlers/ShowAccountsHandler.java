package com.mpolina.banks.console.handlers;

import com.mpolina.banks.accounts.CreditAccount;
import com.mpolina.banks.accounts.DebitAccount;
import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.client.ClientAccount;
import com.mpolina.banks.exceptions.BanksException;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.util.List;
import java.util.Scanner;

/**
 * Обработчик вывода всех аккаунтов клиента в банке
 */
public class ShowAccountsHandler extends AbstractHandler {
    private final int MIN_ID = 0;

    /**
     * При вводе команды "Show accounts" выполняет бизнеслогику  {@see DoBusinessLogic()}
     *
     * @param request переданный запрос
     */
    public void handle(Requestable request) {
        if (Strings.isNullOrEmpty(request.getRequest())) {
            return;
        }
        if (request.getRequest().equals("Show accounts")) {
            doBusinessLogic();
        }
        else {
            if (nextHendler != null) {
                nextHendler.handle(request);
            }
        }

    }

    /**
     * Выводит все аккаунты в банке: Тип, Id, количество денег на счету
     * Ввод с консоли - номер банка(целое неотрицательное), номер клиента (неотрицательное целое)
     * При вводе некорректной комманды вывода не происходит, метод заканчивается.
     */
    private void doBusinessLogic() {
        CentralBank centralBank = CentralBank.getInstance();
        Scanner stream = new Scanner(System.in);

        try {
            System.out.println("Set Bank Id:");
            int bankId = Integer.parseInt(stream.nextLine());
            Bank bank = centralBank.getBankById(bankId);

            System.out.println("Shose clients from:");
            for (int i = MIN_ID; i < bank.getClientAccountsList().size(); i++) {
                List<ClientAccount> acc = bank.getClientAccountsList();
                System.out.println(i + " " + acc.get(i).getFullName().getName() + " " +
                        acc.get(i).getFullName().getSurname() + " " + acc.get(i).getPassport());
            }

            int clientInd = Integer.parseInt(stream.nextLine());
            if (clientInd < MIN_ID || clientInd >= bank.getClientAccountsList().size()) {
                System.out.println("Wrong client number.");
                return;
            }

            ClientAccount client = bank.getClientAccountsList().get(clientInd);

            for (var acc : client.getAccountsList()) {
                String type = "";
                if (acc instanceof DebitAccount) {
                    type = "Debit Account";
                }
                else if (acc instanceof CreditAccount) {
                    type = "Credit Account";
                }
                else {
                    type = "Deposit Account";
                }

                System.out.println(type + " : " + acc.getId() + " : " + acc.getMoney() + "");
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
