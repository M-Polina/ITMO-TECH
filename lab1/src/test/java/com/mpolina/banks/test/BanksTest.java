package com.mpolina.banks.test;

import com.mpolina.banks.accounts.*;
import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.banks.CentralBank;
import com.mpolina.banks.banks.DepositInterestRateConditions;
import com.mpolina.banks.client.Builderable;
import com.mpolina.banks.client.ClientAccount;
import com.mpolina.banks.client.ClientBuilder;
import com.mpolina.banks.messagehandlers.InMemoryMessageHandler;
import com.mpolina.banks.observer.ObservablesNames;
import com.mpolina.banks.time.OwnTime;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BanksTest {
    private OwnTime time;
    private CentralBank centralBank;

    public BanksTest()
    {
        time = OwnTime.getInstance();
        centralBank = CentralBank.getInstance();
    }

    @Test
    public void addMoneyMoneyAreInAccount()
    {
        DepositInterestRateConditions deposintConditions = new DepositInterestRateConditions(0.01);
        Bank bank1 = centralBank.createBank("Tinkoff", 0.01, 1000, -100000.0, deposintConditions, 1000.0);

        Builderable builder1 = new ClientBuilder(bank1, "Den", "Milik");
        builder1.setAddress("Gavai");
        builder1.setPassport(1);
        ClientAccount client1 = builder1.createAndGetClient();
        bank1.addClientAccount(client1);

        DebitAccount debit1 = (DebitAccount)bank1.createAccount(client1, new DebitAccountCreator());
        CreditAccount credit1 = (CreditAccount)bank1.createAccount(client1, new CreditAccountCreator());

        DepositAccount deposit1 =  (DepositAccount)bank1.createAccount(client1, new DepositAccountCreator(1000, 12));

        bank1.addMoney(debit1.getId(), 1000.1);

        bank1.addMoney(credit1.getId(), 1000.1);

        bank1.addMoney(deposit1.getId(), 1000.1);

        assertEquals(1000.1, debit1.getMoney());
        assertEquals(1000.1, credit1.getMoney());
        assertEquals(2000.1, deposit1.getMoney());
    }

    @Test
    public void withdrawMoneyCorrectAmountOfMoneyInAccount()
    {
        DepositInterestRateConditions deposintConditions = new DepositInterestRateConditions(0.01);
        Bank bank1 = centralBank.createBank("Tinkoff", 0.01, 100, -100000, deposintConditions, 1000);

        Builderable builder1 = new ClientBuilder(bank1, "Den", "Milik");
        builder1.setAddress("Gavai");
        builder1.setPassport(1);
        ClientAccount client1 = builder1.createAndGetClient();
        bank1.addClientAccount(client1);

        DebitAccount debit1 = (DebitAccount)bank1.createAccount(client1, new DebitAccountCreator());
        CreditAccount credit1 = (CreditAccount)bank1.createAccount(client1, new CreditAccountCreator());

        bank1.addMoney(debit1.getId(), 1000.1);
        bank1.withdrawMoney(debit1.getId(), 500);

        bank1.addMoney(credit1.getId(), 1000.1);
        bank1.withdrawMoney(credit1.getId(), 500);

        assertEquals(500.1, debit1.getMoney());
        assertEquals(500.1, credit1.getMoney());

        bank1.withdrawMoney(credit1.getId(), 1000.1);
        assertEquals(-600, credit1.getMoney());
    }

    @Test
    public void transferMoneyCorrectAmountOfMoneyInAccounts()
    {
        DepositInterestRateConditions deposintConditions = new DepositInterestRateConditions(0.01);
        Bank bank1 = centralBank.createBank("Tinkoff", 0.01, 100, -100000, deposintConditions, 1000);

        Builderable builder1 = new ClientBuilder(bank1, "Den", "Milik");
        builder1.setAddress("Gavai");
        builder1.setPassport(1);
        ClientAccount client1 = builder1.createAndGetClient();
        bank1.addClientAccount(client1);

        DebitAccount debit1 = (DebitAccount)bank1.createAccount(client1, new DebitAccountCreator());
        CreditAccount credit1 = (CreditAccount)bank1.createAccount(client1, new CreditAccountCreator());

        bank1.addMoney(debit1.getId(), 1000);
        bank1.transferMoney(debit1, credit1, 500);

        assertEquals(500, debit1.getMoney());
        assertEquals(500, credit1.getMoney());
    }

    @Test
    public void transferMoneyBetweenBanksCorrectAmountOfMoneyInAccounts()
    {
        DepositInterestRateConditions deposintConditions = new DepositInterestRateConditions(0.01);
        Bank bank1 = centralBank.createBank("Tinkoff", 0.01, 100, -100000, deposintConditions, 1000);

        Builderable builder1 = new ClientBuilder(bank1, "Den", "Milik");
        builder1.setAddress("Gavai");
        builder1.setPassport(1);
        ClientAccount client1 = builder1.createAndGetClient();
        bank1.addClientAccount(client1);

        Bank bank2 = centralBank.createBank("Sber", 0.01, 100, -100000, deposintConditions, 1000);

        Builderable builder2 = new ClientBuilder(bank2, "Den2", "Milik2");
        builder2.setAddress("Gavai");
        builder2.setPassport(1);
        ClientAccount client2 = builder2.createAndGetClient();
        bank2.addClientAccount(client2);

        DebitAccount debit1 = (DebitAccount)bank1.createAccount(client1, new DebitAccountCreator());
        bank1.addMoney(debit1.getId(), 1000);
        DebitAccount debit2 = (DebitAccount)bank2.createAccount(client2, new DebitAccountCreator());

        bank1.transferMoney(debit1, debit2, 600);

        assertEquals(400, debit1.getMoney());
        assertEquals(600, debit2.getMoney());
    }

    @Test
    public void rollBackSimpleCorrectAmountOfMoneyInAccounts()
    {
        DepositInterestRateConditions deposintConditions = new DepositInterestRateConditions(0.01);
        Bank bank1 = centralBank.createBank("Tinkoff", 0.01, 100, -100000, deposintConditions, 1000);

        Builderable builder1 = new ClientBuilder(bank1, "Den", "Milik");
        builder1.setAddress("Gavai");
        builder1.setPassport(1);
        ClientAccount client1 = builder1.createAndGetClient();
        bank1.addClientAccount(client1);

        DebitAccount debit1 = (DebitAccount)bank1.createAccount(client1, new DebitAccountCreator());

        bank1.addMoney(debit1.getId(), 1000);
        bank1.rollbackTrabsaction(bank1.getTransactionsList().get(0).getId());

        assertEquals(0, debit1.getMoney());
    }

    @Test
    public void rollBackCorrectAmountOfMoneyInAccounts()
    {
        DepositInterestRateConditions deposintConditions = new DepositInterestRateConditions(0.01);
        Bank bank1 = centralBank.createBank("Tinkoff", 0.01, 100, -100000, deposintConditions, 1000);

        Builderable builder1 = new ClientBuilder(bank1, "Den", "Milik");
        builder1.setAddress("Gavai");
        builder1.setPassport(1);
        ClientAccount client1 = builder1.createAndGetClient();
        bank1.addClientAccount(client1);

        DebitAccount debit1 = (DebitAccount)bank1.createAccount(client1, new DebitAccountCreator());
        CreditAccount credit1 = (CreditAccount)bank1.createAccount(client1, new CreditAccountCreator());

        bank1.addMoney(debit1.getId(), 1000);
        bank1.transferMoney(debit1, credit1, 500);
        bank1.rollbackTrabsaction(bank1.getTransactionsList().get(1).getId());

        assertEquals(1000, debit1.getMoney());
        assertEquals(0, credit1.getMoney());
    }

    @Test
    public void rollBackTransferBetweenBanksCorrectAmountOfMoneyInAccounts()
    {
        DepositInterestRateConditions deposintConditions = new DepositInterestRateConditions(0.01);
        Bank bank1 = centralBank.createBank("Tinkoff", 0.01, 100, -100000, deposintConditions, 1000);
        Builderable builder1 = new ClientBuilder(bank1, "Den", "Milik");
        builder1.setAddress("Gavai");
        builder1.setPassport(1);
        ClientAccount client1 = builder1.createAndGetClient();
        bank1.addClientAccount(client1);

        Bank bank2 = centralBank.createBank("Sber", 0.01, 100, -100000, deposintConditions, 1000);
        Builderable builder2 = new ClientBuilder(bank2, "Den", "Milik");
        builder2.setAddress("Gavai");
        builder2.setPassport(1);
        ClientAccount client2 = builder2.createAndGetClient();
        bank2.addClientAccount(client2);

        DebitAccount debit1 = (DebitAccount)bank1.createAccount(client1, new DebitAccountCreator());
        bank1.addMoney(debit1.getId(), 1000);

        CreditAccount credit2 = (CreditAccount)bank2.createAccount(client2, new CreditAccountCreator());
        bank1.transferMoney(debit1, credit2, 600);

        bank1.rollbackTrabsaction(bank1.getTransactionsList().get(1).getId());

        assertEquals(1000, debit1.getMoney());
        assertEquals(0, credit2.getMoney());

        bank2.transferMoney(credit2, debit1, 600);

        bank2.rollbackTrabsaction(bank2.getTransactionsList().get(2).getId());

        assertEquals(1000, debit1.getMoney());
        assertEquals(0, credit2.getMoney());
    }

    @Test
    public void afterMothInterastRateCreatesCorrectAmountOfMoneyInAccount()
    {
        DepositInterestRateConditions deposintConditions = new DepositInterestRateConditions(0.012);
        Bank bank1 = centralBank.createBank("Tinkoff", 0.012, 100, -100000, deposintConditions, 1000);

        Builderable builder = new ClientBuilder(bank1, "Den", "Milik");
        ClientAccount client1 = builder.createAndGetClient();
        bank1.addClientAccount(client1);

        bank1.addClientAdress(client1, "Gavai");
        bank1.addClientPassport(client1, 1);
        DebitAccount debit1 = (DebitAccount)bank1.createAccount(client1, new DebitAccountCreator());

        bank1.addMoney(debit1.getId(), 1000);

        DepositAccount depos1 = (DepositAccount)bank1.createAccount(client1, new DepositAccountCreator(1000, 5));
        centralBank.speedUpTime(40);
        System.out.println(debit1.getMoney());
        System.out.println( depos1.getMoney());

        assertEquals(1030, debit1.getMoney());
        assertEquals(1030, depos1.getMoney());
    }

    @Test
    public void updateInfoConditionsConditionsChangedAmmountOfMoneyCorrrect()
    {
        ArrayList<Double> lms = new ArrayList<Double>() {{ add(10000.0); add(100000.0); add(5000000.0); }};
        ArrayList<Double> irs = new ArrayList<Double>() {{ add(0.012); add(0.02);add(0.025);add(0.03); }};

        DepositInterestRateConditions deposintConditions = new DepositInterestRateConditions(lms, irs);
        Bank bank1 = centralBank.createBank("Tinkoff", 0.024, 100, -100000, deposintConditions, 1000);

        Builderable builder = new ClientBuilder(bank1, "Den", "Milik");
        ClientAccount client1 = builder.createAndGetClient();

        bank1.addClientAccount(client1);
        bank1.addClientAdress(client1, "Gavai");
        bank1.addClientPassport(client1, 1);
        DebitAccount debit1 = (DebitAccount)bank1.createAccount(client1, new DebitAccountCreator());
        bank1.addMoney(debit1.getId(), 1000);

        centralBank.speedUpTime(30);

        assertEquals(1056, debit1.getMoney());

        bank1.changeDebitInterestRate(0.048);
        centralBank.speedUpTime(30);

        assertEquals(0.048, debit1.getInterestRate());
    }

    @Test
    public void subscribeMessageHandlerRecivesNotifications()
    {
        DepositInterestRateConditions deposintConditions = new DepositInterestRateConditions(0.01);
        Bank bank1 = centralBank.createBank("Tinkoff", 0.01, 100, -100000, deposintConditions, 100);

        Builderable builder = new ClientBuilder(bank1, "Den", "Milik");
        ClientAccount client1 = builder.createAndGetClient();
        bank1.addClientAccount(client1);

        DebitAccount debit1 = (DebitAccount)bank1.createAccount(client1, new DebitAccountCreator());
        CreditAccount credit1 = (CreditAccount)bank1.createAccount(client1, new CreditAccountCreator());

        bank1.addMoney(debit1.getId(), 1000);

        InMemoryMessageHandler handler = new InMemoryMessageHandler();
        bank1.subscribe(client1, handler, ObservablesNames.DEBIT_INTEREST_RATE);
        bank1.changeDebitInterestRate(0.048);

        assertEquals("Debit interest rate was changed to 0.048!", handler.getMessages().get(0));
    }
}
