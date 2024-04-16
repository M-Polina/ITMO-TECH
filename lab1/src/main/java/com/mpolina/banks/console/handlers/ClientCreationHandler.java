package com.mpolina.banks.console.handlers;

import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.client.Builderable;
import com.mpolina.banks.client.ClientAccount;
import com.mpolina.banks.client.ClientBuilder;
import com.mpolina.banks.exceptions.BanksException;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.util.Scanner;

public class ClientCreationHandler extends AbstractHandler {
    private final double MIN_NUMBER = 0;
    private final int MIN_ID = 0;

    public void handle(Requestable request) {
        if (Strings.isNullOrEmpty(request.getRequest())) {
            return;
        }
        if (request.getRequest().equals("Create client")) {
            doBusinessLogic();
        }
        else {
            if (nextHendler != null) {
                nextHendler.handle(request);
            }
        }

    }

    /**
     * Метод для создания клиента в банке.
     * С консоли вводятся название банка, имя и фамилия клиента.
     * Спрашивается о желании ввести паспорт и адресс, при согласии эти данные вводятся
     * соответственно, иначе они не заполняются.
     *
     * При вводе некорректной комманды клиент не создаётся, метод заканчивается.
     * При вводе некорректной команды при выражении согласия/отказа о внесении данных
     * адреса и пасспорта или неверном вводе данных адрес и пасспорт не добавляются соответственно.
     */
    private void doBusinessLogic() {
        CentralBank centralBank = CentralBank.getInstance();
        Scanner stream = new Scanner(System.in);

        try {
            System.out.println("Set Bank Id:");
            int bankId = Integer.parseInt(stream.nextLine());
            Bank bank = centralBank.getBankById(bankId);

            System.out.println("Set Client name:");
            String name = stream.nextLine();
            if (name == null || name.trim().isEmpty()) {
                return;
            }

            System.out.println("Set Client surname:");
            String surname = stream.nextLine();
            if (surname == null || surname.trim().isEmpty()) {
                return;
            }

            Builderable builder = new ClientBuilder(bank, name, surname);

            System.out.println("Do you want to set address?");
            boolean answer;
            answer = whantSet(stream);
            if (answer) {
                System.out.println("Set address:");
                String address = stream.nextLine();

                if (address != null && !(address.trim().isEmpty())) {
                    builder.setAddress(address);
                }
                else{
                    System.out.println("Wrong address, so it won't be set.");
                }
            }

            System.out.println("Do you want to set passport?");
            answer = whantSet(stream);
            if (answer) {
                System.out.println("Set Client Passport Id:");
                int passportId = Integer.parseInt(stream.nextLine());

                if (!(passportId < MIN_ID || bank.clientPassportExists(passportId))) {
                    builder.setPassport(passportId);
                }
                else if (bank.clientPassportExists(passportId)){
                    System.out.println("This passport Id already exists, so passport won't be set.");
                }
                else{
                    System.out.println("Wrong passport Id, so it won't be set.");
                }
            }

            ClientAccount client = builder.createAndGetClient();
            bank.addClientAccount(client);

            System.out.println("Client was created!");
        }
        catch (BanksException ex1){
            System.out.println(ex1.getMessage());
        }
        catch (Exception ex) {
            return;
        }
    }

    private boolean whantSet(Scanner stream) {
        String answer = stream.nextLine();

        if (answer == null || answer.trim().isEmpty()) {
            return false;
        }

        if (answer.equals("no")) {
            return false;
        }

        if (answer.equals("yes")) {
            return true;
        }

        return whantSet(stream);
    }
}
