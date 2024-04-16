package com.mpolina.banks.console;

import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.console.handlers.*;
import com.mpolina.banks.time.OwnTime;

import java.util.Scanner;


/**
 * Консольное приложение для создания банков, клиентов этих банков
 * и аккаунтов у данных клиентов, позволяющее взаимодействовать с ними
 * и совершать опереции перевода, пополнения и снятия денег.
 */
public class Main {
    public static void main(String[] args) {

        OwnTime.getInstance();
        CentralBank.getInstance();
        System.out.println("List of commands available:");
        System.out.println("-Help");
        System.out.println("-Create bank");
        System.out.println("-Create client");
        System.out.println("-Create account");
        System.out.println("-Set address");
        System.out.println("-Set passport");
        System.out.println("-Change debit interest rate");
        System.out.println("-Change deposit interest rate conditions");
        System.out.println("-Change credit commission");
        System.out.println("-Change credit limit");
        System.out.println("-Change deposit interest rate conditions");
        System.out.println("-Show accounts of client");
        System.out.println("-Show banks");
        System.out.println("-Show accounts");
        System.out.println("-Subscribe to notifications");
        System.out.println("-Speed up time");
        System.out.println("-Add");
        System.out.println("-Withdraw");
        System.out.println("-Transfer");
        System.out.println("-Rollback");

        BankCreationHandler bankCreationHandler = new BankCreationHandler();
        ClientCreationHandler clientCreationHandler = new ClientCreationHandler();
        AccountCreationHandler accountCreationHandler = new AccountCreationHandler();
        AdressAddingHandler adressAddingHandler = new AdressAddingHandler();
        PassportAddingHandler passportAddingHandler = new PassportAddingHandler();
        ChangeDebitInterestRateHandler changeDebitInterestRateHandler = new ChangeDebitInterestRateHandler();
        ChangeDepositInterestRateHandler changeDepositInterestRateHandler = new ChangeDepositInterestRateHandler(); // l
        ChangeCreditCommissionHandler changeCreditCommissionHandler = new ChangeCreditCommissionHandler();
        ChangeCreditLimitHandler changeCreditLimitHandler = new ChangeCreditLimitHandler();
        ChangeSuspiciousClientLimit changeSuspiciousClientLimit = new ChangeSuspiciousClientLimit();
        AddMoneyHandler addMoneyHandler = new AddMoneyHandler();
        WithdrawHandler withdrawHandler = new WithdrawHandler();
        TransferHandler transferHandler = new TransferHandler();
        RollBackHandler rollBackHandler = new RollBackHandler();
        ShowAccountsHandler showAccountsHandler = new ShowAccountsHandler();
        ShowCientsHandler showCientsHandler = new ShowCientsHandler();
        ShowBanksHendler showBanksHendler = new ShowBanksHendler();
        SpeedUpTimeHandler speedUpTimeHandler = new SpeedUpTimeHandler();
        SubscribeHandler subscribeHandler = new SubscribeHandler();
        ShowHelpHandler showHelpHandler = new ShowHelpHandler();
        FinalHandler finalHandler = new FinalHandler();

        // Генерации послеовательности обработчиков (handler), реализующих вместе паттерн "цепочка обязанностей".
        bankCreationHandler.setNext(clientCreationHandler).setNext(accountCreationHandler).setNext(adressAddingHandler);
        adressAddingHandler.setNext(passportAddingHandler).setNext(changeDebitInterestRateHandler).setNext(changeDepositInterestRateHandler);
        changeDepositInterestRateHandler.setNext(changeCreditCommissionHandler).setNext(changeCreditLimitHandler);
        changeCreditLimitHandler.setNext(changeSuspiciousClientLimit).setNext(addMoneyHandler).setNext(withdrawHandler);
        withdrawHandler.setNext(transferHandler).setNext(rollBackHandler).setNext(showAccountsHandler).setNext(showCientsHandler);
        showCientsHandler.setNext(showBanksHendler).setNext(speedUpTimeHandler).setNext(subscribeHandler).setNext(showHelpHandler);
        showHelpHandler.setNext(finalHandler);

        String request = "";
        Scanner in = new Scanner(System.in);

        // Цикл ввода комманд, работающий пока не будет введена команда "exit"
        while (!request.equals("exit")) {
            request = in.nextLine();
            bankCreationHandler.handle(new StringRequest(request, in));
        }

        in.close();
    }
}