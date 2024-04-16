package com.mpolina.banks.console.handlers;

import com.mpolina.banks.accounts.Account;
import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.exceptions.BanksException;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.util.Scanner;

/**
 * Обработчик, позволяющий добавить деньги на счёт при вводе команды "Add".
 */
public class AddMoneyHandler extends AbstractHandler {
    private final double MIN_NUMBER = 0;

    /**
     * При вводе команды "Add" выполняет бизнеслогику  {@see DoBusinessLogic()}
     *
     * @param request переданный запрос
     */
    public void handle(Requestable request) {
        if (Strings.isNullOrEmpty(request.getRequest())) {
            return;
        }
        if (request.getRequest().equals("Add")) {
            doBusinessLogic();
        }
        else {
            if (nextHendler != null) {
                nextHendler.handle(request);
            }
        }

    }

    /**
     * Пополнение счёта клиента по номеру банка и номеру счёта в этом банке, а также сумме.
     * Все необходимые данные вводятся с консоли.
     * При вводе некорректной комманды пополнение не происходит, метод заканчивается.
     */
    private void doBusinessLogic() {
        CentralBank centralBank = CentralBank.getInstance();
        Scanner stream =new Scanner(System.in);

        try {
            System.out.println("Set information about account to add to:");
            System.out.println("Set bank id:");
            int number = Integer.parseInt(stream.nextLine());
            Bank bank = centralBank.getBankById(number);

            System.out.println("Set account id:");
            number = Integer.parseInt(stream.nextLine());
            Account account =  bank.getAccountById(number);

            System.out.println("Set  amount of money to add:");
            double amount = Double.parseDouble(stream.nextLine());

            if (amount <= MIN_NUMBER){
                return;
            }

            bank.addMoney(account.getId(), amount);

            System.out.println("Money were added!");
        }
        catch (BanksException ex1){
            System.out.println(ex1.getMessage());
        }
        catch (Exception ex) {
            return;
        }
    }
}
