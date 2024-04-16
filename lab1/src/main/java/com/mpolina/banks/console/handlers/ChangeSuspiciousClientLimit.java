package com.mpolina.banks.console.handlers;

import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.exceptions.BanksException;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.util.Scanner;

/**
 * Обработчик по изменению предела снятия денег для клиентов без адреса или пасспорта в банке
 * для кридитного счёта.
 */
public class ChangeSuspiciousClientLimit extends AbstractHandler {
    private final double MIN_NUMBER = 0;

    /**
     * При вводе команды "Change credit suspicious client limit" выполняет бизнеслогику  {@see DoBusinessLogic()}
     *
     * @param request переданный запрос
     */
    public void handle(Requestable request) {
        if (Strings.isNullOrEmpty(request.getRequest())) {
            return;
        }
        if (request.getRequest().equals("Change credit suspicious client limit")) {
            doBusinessLogic();
        }
        else {
            if (nextHendler != null) {
                nextHendler.handle(request);
            }
        }

    }

    /**
     * Изменение предела снятия денег для клиентов без адреса или пасспорта в банке
     * для кридитного счёта.
     * Ввод с консоли - номер банка(целое неотрицательное), новый предел (положительные числа).
     * При вводе некорректной комманды изменение комиссии не происходит, метод заканчивается.
     */
    private void doBusinessLogic() {
        CentralBank centralBank = CentralBank.getInstance();
        Scanner stream = new Scanner(System.in);

        try {
            System.out.println("Set Bank Id:");
            int bankId = Integer.parseInt(stream.nextLine());
            Bank bank = centralBank.getBankById(bankId);

            System.out.println("Set Suspicious Client Limit:");
            double suspiciusClientLimit = Double.parseDouble(stream.nextLine());
            if (suspiciusClientLimit <= MIN_NUMBER) {
                return;
            }
            bank.changeSuspiciousClientMoneyLimit(suspiciusClientLimit);

            System.out.println("Credit suspicious client limit was changed!");
        }
        catch (BanksException ex1){
            System.out.println(ex1.getMessage());
        }
        catch (Exception ex) {
            return;
        }
    }
}
