package com.mpolina.banks.console.handlers;

import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.client.ClientAccount;
import com.mpolina.banks.exceptions.BanksException;
import com.mpolina.banks.messagehandlers.ConsoleMessageHandler;
import com.mpolina.banks.observer.ObservablesNames;
import lombok.Getter;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.util.List;
import java.util.Scanner;

/**
 * Обрабтчик предоставляющий возможность подписать клиента на конкретные изменения в банке.
 * Тип изменений: изменение кредитного лимита, лимита снятия денег подозрительного клиента,
 * дебетовой и депозитной ставки, кредитной комиссии.
 */
public class SubscribeHandler extends AbstractHandler {
    private final int MIN_ID = 0;

    @Getter
    private ConsoleMessageHandler Handler = new ConsoleMessageHandler();

    /**
     * При вводе команды "Subscribe to notifications" выполняет бизнеслогику  {@see DoBusinessLogic()}
     *
     * @param request переданный запрос
     */
    public void handle(Requestable request) {
        if (Strings.isNullOrEmpty(request.getRequest())) {
            return;
        }
        if (request.getRequest().equals("Subscribe to notifications")) {
            doBusinessLogic();
        }
        else {
            if (nextHendler != null) {
                nextHendler.handle(request);
            }
        }

    }

    /**
     * Подписка клиента на изменения в банке.
     * Ввод с консоли - номер банка(целое неотрицательное),
     * номер клиента(целое неотрицательное),
     * Тип изменений: изменение кредитного лимита, лимита снятия денег подозрительного клиента,
     *  * дебетовой и депозитной ставки, кредитной комиссии.
     * При вводе некорректной комманды подписки не происходит, метод заканчивается.
     */
    private void doBusinessLogic() {
        CentralBank centralBank = CentralBank.getInstance();
        Scanner stream = new Scanner(System.in);

        try {
            System.out.println("Types of objects, you can subsctribe to:\n" +
                    "Debit interest rate\nDeposit conditions\n" +
                    "Credit commission\nCredit limit\nSuspicious client limit");

            System.out.println("Set Bank Id:");
            int bankId = Integer.parseInt(stream.nextLine());
            Bank bank = centralBank.getBankById(bankId);

            System.out.println("Chose clients from: ");
            for (int i = MIN_ID; i < bank.getClientAccountsList().size(); i++) {
                List<ClientAccount> acc = bank.getClientAccountsList();
                System.out.println(i + ") " + acc.get(i).getFullName().getName() + " " +
                        acc.get(i).getFullName().getSurname() + " " + acc.get(i).getPassport());
            }

            int clientInd = Integer.parseInt(stream.nextLine());
            if (clientInd < MIN_ID || clientInd >= bank.getClientAccountsList().size()) {
                System.out.println("Wrong client number.");
                return;
            }

            ClientAccount client = bank.getClientAccountsList().get(clientInd);

            System.out.println("Set condition to subscribe:");
            String str = stream.nextLine();

            if (str == null || str.trim().isEmpty()) {
                return;
            }

            ObservablesNames observableType;
            if (str.equals("Credit commission")) {
                observableType = ObservablesNames.CREDIT_COMMISSION;
            }
            else if (str.equals("Credit limit")) {
                observableType = ObservablesNames.CREDIT_LIMIT;
            }
            else if (str.equals("Deposit conditions")) {
                observableType = ObservablesNames.DEPOSIT_CONDITIONS;
            }
            else if (str.equals("Debit interest rate")) {
                observableType = ObservablesNames.DEBIT_INTEREST_RATE;
            }
            else if (str.equals("Suspicious client limit")) {
                observableType = ObservablesNames.SUSPISIOUS_CLIENT_LIMIT;
            }
            else {
                return;
            }

            bank.subscribe(client, Handler, observableType);

            System.out.println("Subscribe was created!");
        }
        catch (BanksException ex1){
            System.out.println(ex1.getMessage());
        }
        catch (Exception ex) {
            return;
        }
    }
}
