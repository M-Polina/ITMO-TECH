package com.mpolina.banks.console.handlers;

import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.exceptions.BanksException;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.util.Scanner;

/**
 * Обработчик вывода всех клиентов в банке
 */
public class ShowCientsHandler extends AbstractHandler {
    /**
     * При вводе команды "Show clients" выполняет бизнеслогику  {@see DoBusinessLogic()}
     *
     * @param request переданный запрос
     */
    public void handle(Requestable request) {
        if (Strings.isNullOrEmpty(request.getRequest())) {
            return;
        }
        if (request.getRequest().equals("Show clients")) {
            doBusinessLogic();
        }
        else {
            if (nextHendler != null) {
                nextHendler.handle(request);
            }
        }

    }

    /**
     * Выводит все клиенты в банке: Имя, фамилия, адрес, Id пасспорта
     * Ввод с консоли - номер банка(целое неотрицательное)
     * При вводе некорректной комманды вывода не происходит, метод заканчивается.
     */
    private void doBusinessLogic() {
        CentralBank centralBank = CentralBank.getInstance();
        Scanner stream = new Scanner(System.in);

        try {
            System.out.println("Set Bank Id:");
            int bankId = Integer.parseInt(stream.nextLine());
            Bank bank = centralBank.getBankById(bankId);

            for (var acc : bank.getClientAccountsList()) {
                String address = "";
                if (acc.getAddress() != null)
                    address = acc.getAddress().getValue();

                String id = "";
                if (acc.getPassport() != null)
                    id = String.format("%d", acc.getPassport().getId());

                System.out.println(acc.getFullName().getName() + " : " +
                        acc.getFullName().getSurname() + " : " + acc.getId().toString() + " : " +
                        address + " : " + id + "");
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
