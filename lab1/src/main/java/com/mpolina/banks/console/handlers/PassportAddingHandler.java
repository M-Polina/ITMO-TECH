package com.mpolina.banks.console.handlers;

import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.client.ClientAccount;
import com.mpolina.banks.exceptions.BanksException;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.util.List;
import java.util.Scanner;

/**
 * Обработчик, позволяющий добавить пасспорт клиенту при вводе команды "Set passport"
 */
public class PassportAddingHandler extends AbstractHandler {
    private final int MIN_ID = 0;

    /**
     * При вводе команды "Set passport" выполняет бизнеслогику  {@see DoBusinessLogic()}
     *
     * @param request переданный запрос
     */
    public void handle(Requestable request) {
        if (Strings.isNullOrEmpty(request.getRequest())) {
            return;
        }
        if (request.getRequest().equals("Set passport")) {
            doBusinessLogic();
        }
        else {
            if (nextHendler != null) {
                nextHendler.handle(request);
            }
        }

    }

    /**
     * Добавление пасспорта клиенту по номеру банка (положительное целое число)
     * и номеру клиента в этом банке (положительное целое число).
     * Все необходимые данные получаеют из консольного ввода.
     * Id пасспорта - положительное целое число.
     * При вводе некорректной комманды пасспорт не добавляется, метод заканчивается.
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

            System.out.println("Set Client Passport Id:");
            int passportId = Integer.parseInt(stream.nextLine());

            if (passportId < MIN_ID || bank.clientPassportExists(passportId)) {
                return;
            }

            bank.addClientPassport(client, passportId);

            System.out.println("Passport was set!");
        }
        catch (BanksException ex1){
            System.out.println(ex1.getMessage());
        }
        catch (Exception ex) {
            return;
        }
    }
}

