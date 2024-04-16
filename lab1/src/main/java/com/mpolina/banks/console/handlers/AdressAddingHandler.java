package com.mpolina.banks.console.handlers;

import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.client.ClientAccount;
import com.mpolina.banks.exceptions.BanksException;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.util.Scanner;

/**
 * Обработчик, позволяющий добавить адрес клиенту при вводе команды "Set address"
 */
public class AdressAddingHandler extends AbstractHandler {
    private final int MIN_ID = 0;

    /**
     * При вводе команды "Set address" выполняет бизнеслогику  {@see DoBusinessLogic()}
     *
     * @param request переданный запрос
     */
    public void handle(Requestable request) {
        if (Strings.isNullOrEmpty(request.getRequest())) {
            return;
        }
        if (request.getRequest().equals("Set address")) {
            doBusinessLogic();
        }
        else {
            if (nextHendler != null) {
                nextHendler.handle(request);
            }
        }

    }

    /**
     * Добавление адреса клиенту по номеру банка и номеру клиента в этом банке.
     * Все необходимые данные получаеют из консольного ввода.
     * При вводе некорректной комманды адрес не добавляется, метод заканчивается.
     */
    private void doBusinessLogic() {
        CentralBank centralBank = CentralBank.getInstance();
        Scanner stream = new Scanner(System.in);

        try {
            System.out.println("Set Bank Id:");
            int bankId = Integer.parseInt(stream.nextLine());
            Bank bank = centralBank.getBankById(bankId);

            System.out.println("Chose clients from:");

            int ind = 0;
            for (var acc : bank.getClientAccountsList()) {

                String address = acc.getAddress().getValue();

                String id = String.format("%d", acc.getPassport().getId());

                System.out.println(ind +") "+ acc.getFullName().getName() + " : " +
                        acc.getFullName().getSurname() + " : " + acc.getId().toString() + " : " +
                        address + " : " + id + "");

                ind++;
            }

            int clientInd = Integer.parseInt(stream.nextLine());
            if (clientInd < MIN_ID || clientInd >= bank.getClientAccountsList().size()) {
                System.out.println("Wrong client number. ");
                return;
            }

            ClientAccount client = bank.getClientAccountsList().get(clientInd);

            System.out.println("Set Client address:");
            String address = stream.nextLine();

            if (address == null || address.trim().isEmpty()) {
                return;
            }

            bank.addClientAdress(client, address);

            System.out.println("Address was set!");
        }
        catch (BanksException ex1){
            System.out.println(ex1.getMessage());
        }
        catch (Exception ex) {
            return;
        }
    }
}
