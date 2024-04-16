package com.mpolina.banks.console.handlers;

import com.mpolina.banks.accounts.CreditAccountCreator;
import com.mpolina.banks.accounts.DebitAccountCreator;
import com.mpolina.banks.accounts.DepositAccountCreator;
import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.client.ClientAccount;
import com.mpolina.banks.exceptions.BanksException;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.util.Scanner;

/**
 * Обработчик команды "Create account".
 * При передаче соответствующей команды создаёт дебетовый, кредитный или депозитный счёт у клиента.
 */
public class AccountCreationHandler extends AbstractHandler {
    private final int MIN_ID = 0;
    private final double MIN_NUMBER = 0;

    /**
     * При получении команды "Create account" выполняет бизнеслогику {@see DoBusinessLogic()}
     *
     * @param request переданный запрос
     */
    public void handle(Requestable request) {
        if (Strings.isNullOrEmpty(request.getRequest())) {
            return;
        }
        if (request.getRequest().equals("Create account")) {
            doBusinessLogic();
        }
        else {
            if (nextHendler != null) {
                nextHendler.handle(request);
            }
        }

    }

    /**
     * Создаёт дебетовый, кредитный или депозитный счёт у клиента в зависимости от ввода команды.
     * При вводе некорректной комманды аккаунт не создаётся, метод заканчивается.
     */
    private void doBusinessLogic() {
        CentralBank centralBank = CentralBank.getInstance();
        Scanner stream = new Scanner(System.in);
        try {

            System.out.println("Available Banks:");
            for (var acc : centralBank.getBanksList()) {
                System.out.println(acc.getName() + " : " + acc.getId());
            }

            System.out.println("Set bank Id:");

            int bankId = Integer.parseInt(stream.nextLine());

            Bank bank = CentralBank.getInstance().getBankById(bankId);

            System.out.println("Chose clients from:");
            for (int i = MIN_ID; i < bank.getClientAccountsList().size(); i++) {
                ClientAccount acc = bank.getClientAccountsList().get(i);
                String address = acc.getAddress().getValue();
                String id = String.format("%d", acc.getPassport().getId());

                System.out.println(i + ") " + acc.getFullName().getName() + " : " +
                        acc.getFullName().getSurname() + " : " + acc.getId().toString() + " : " +
                        address + " : " + id + "");
            }

            System.out.println("Set client number:");
            int clientInd = Integer.parseInt(stream.nextLine());

            if (clientInd < MIN_ID || clientInd >= bank.getClientAccountsList().size()) {
                System.out.println("Wrong client number.");
                return;
            }

            ClientAccount client = bank.getClientAccountsList().get(clientInd);

            System.out.println("Available accounts: Debit, Deposit, Credit");
            System.out.println("Set Account type:");
            AccountTypes accountType;
            String str = stream.nextLine();

            if (str.equals("Debit")) {
                accountType = AccountTypes.DEBIT_ACCOUNT;
            }
            else if (str.equals("Deposit")) {
                accountType = AccountTypes.DEPOSIT_ACCOUNT;
            }
            else if (str.equals("Credit")) {
                accountType = AccountTypes.CREDIT_ACCOUNT;
            }
            else {
                return;
            }

            switch (accountType) {
                case DEBIT_ACCOUNT:
                    bank.createAccount(client, new DebitAccountCreator());
                    System.out.println("DebitAccount created");
                    break;
                case DEPOSIT_ACCOUNT:
                    System.out.println("Set initial amount of money to create Deposit account:");
                    double amountOfMoney = Double.parseDouble(stream.nextLine());

                    if (amountOfMoney <= MIN_NUMBER) {
                        return;
                    }

                    System.out.println("Set duration of Deposit account in months:");
                    int monthDuration = Integer.parseInt(stream.nextLine());
                    if (monthDuration <= MIN_NUMBER)
                        return;

                    bank.createAccount(client, new DepositAccountCreator(amountOfMoney, monthDuration));
                    System.out.println("DepositAccount created");
                    break;
                default:
                    bank.createAccount(client, new CreditAccountCreator());
                    System.out.println("CreditAccount created");
                    break;
            }
        }
        catch (BanksException ex1) {
            System.out.println(ex1.getMessage());
        }
        catch (Exception ex) {
            return;
        }


    }
}
