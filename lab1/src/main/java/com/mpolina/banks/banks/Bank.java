package com.mpolina.banks.banks;

import com.mpolina.banks.accounts.*;
import com.mpolina.banks.client.Address;
import com.mpolina.banks.client.ClientAccount;
import com.mpolina.banks.client.Passport;
import com.mpolina.banks.exceptions.BanksException;
import com.mpolina.banks.messagehandlers.MessageHandler;
import com.mpolina.banks.observer.Observable;
import com.mpolina.banks.observer.ObservablesNames;
import com.mpolina.banks.transactions.*;
import lombok.Getter;
import name.remal.gradle_plugins.lombok.internal._relocated.com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


/**
 * Класс банка, отвечающий за создание счетов и клиентов, хранящий их и позволяющий их найти.
 * Отвечает за совершение транзакций мжду счетами.
 */
public class Bank {
    private final int ONE_ELEMENT = 1;
    private final int ZERO_ELEMENTS = 0;
    private final int MIN_ID = 0;
    private int accountId = MIN_ID;

    private int id;
    @Getter
    private String name;
    /**
     * Условия банка для счетов - разные процентные ставки и денежные комиссии и пределы {@see BankConditions}
     */
    @Getter
    private BankConditions conditions;

    /**
     * Список счетов в банке
     */
    private ArrayList<Account> accountsList = new ArrayList<Account>();
    /**
     * Список клиентов банка.
     */
    private ArrayList<ClientAccount> clientAccountsList = new ArrayList<ClientAccount>();
    /**
     * Список совершённых транзакций в порядке их совершения.
     */
    private ArrayList<MoneyTransaction> transactionsList = new ArrayList<MoneyTransaction>();

    /**
     * Конструктор банка.
     *
     * @param newId -id банка
     * @param newName имя банка
     * @param newDebitInterestRate депозитная ставка банка  (положительная)
     * @param newCommission кредитная комиссия банка (положительная)
     * @param newCreditLimit кредитный предел банка (отрицательный)
     * @param newDepositCoditions  депозитные условия банка {@see DepositInterestRateConditions}
     * @param newSuspiciousClientMoneyLimit  преел на снятия людьми без пасспорта или адреса (положительная)
     */
    public Bank(int newId, String newName,
                double newDebitInterestRate,
                double newCommission,
                double newCreditLimit,
                DepositInterestRateConditions newDepositCoditions,
                double newSuspiciousClientMoneyLimit) {
        if (newId < MIN_ID) {
            throw new BanksException("Id <0 while creating Bank.");
        }
        if (Strings.isNullOrEmpty(newName)) {
            throw new BanksException("Name is incorrect while creating Bank.");
        }

        id = newId;
        name = newName;
        conditions = new BankConditions(newDebitInterestRate, newCommission, newCreditLimit, newDepositCoditions, newSuspiciousClientMoneyLimit);
    }

    public int getId() {
        return id;
    }

    public List<Account> getAccountsList() {
        return Collections.unmodifiableList(accountsList);
    }

    public List<MoneyTransaction> getTransactionsList() {
        return Collections.unmodifiableList(transactionsList);
    }

    public List<ClientAccount> getClientAccountsList() {
        return Collections.unmodifiableList(clientAccountsList);
    }

    public MoneyTransaction getLastTransaction() {
        return transactionsList.get(transactionsList.size() - ONE_ELEMENT);
    }

    public UUID getLastTransactionId() {
        return getLastTransaction().getId();
    }

    /**
     * Поиск клиента по id
     *
     * @param clientAccountId id клиента, который хотим найти
     * @return найденный клиент, null если не нашли
     */
    public ClientAccount findClientAccountById(UUID clientAccountId) {
        if (clientAccountsList.stream().filter(acc -> acc.getId() == clientAccountId).count() == ZERO_ELEMENTS) {
            return null;
        }
        if (clientAccountsList.stream().filter(acc -> acc.getId() == clientAccountId).count() != ONE_ELEMENT) {
            throw new BanksException("Not one client while getting it.");
        }
        return clientAccountsList.stream().filter(acc -> acc.getId() == clientAccountId).findFirst().get();
    }

    /**
     * Поиск клиента по id
     *
     * @param clientAccountId id клиента, который хотим найти
     * @return найденный клиент
     * @throws BanksException если клиент не нашли
     */
    public ClientAccount getClientAccountById(UUID clientAccountId) {
        if (clientAccountsList.stream().filter(acc -> acc.getId() == clientAccountId).count() != ONE_ELEMENT) {
            throw new BanksException("Not one client while getting it.");
        }
        return clientAccountsList.stream().filter(acc -> acc.getId() == clientAccountId).findFirst().get();
    }

    /**
     * Поиск счёта по id
     *
     * @param accountIdToFind id счёта, который хотим найти
     * @return найденный счёт, null если не нашли
     */
    public Account findAccountById(int accountIdToFind) {
        if (accountsList.stream().filter(acc -> acc.getId() == accountIdToFind).count() == ZERO_ELEMENTS) {
            return null;
        }
        if (accountsList.stream().filter(acc -> acc.getId() == accountIdToFind).count() != ONE_ELEMENT) {
            throw new BanksException("Not one client while getting it.");
        }
        return accountsList.stream().filter(acc -> acc.getId() == accountIdToFind).findFirst().get();
    }

    /**
     * Поиск счёта по id
     *
     * @param accountIdToFind id счёта, который хотим найти
     * @return найденный счёт
     * @throws BanksException если счёт не нашли
     */
    public Account getAccountById(int accountIdToFind) {
        if (accountsList.stream().filter(acc -> acc.getId() == accountIdToFind).count() != ONE_ELEMENT) {
            throw new BanksException("Not one client while getting it.");
        }
        return accountsList.stream().filter(acc -> acc.getId() == accountIdToFind).findFirst().get();
    }

    /**
     * Поиск транзакции по id
     *
     * @param transactionId id транзакции
     * @return найденная транзакция
     * @throws BanksException если транзакция не найдена
     */
    public MoneyTransaction getTransactionById(UUID transactionId) {
        if (transactionsList.stream().filter(acc -> acc.getId() == transactionId).count() != ONE_ELEMENT) {
            throw new BanksException("Not one client while getting it.");
        }
        return transactionsList.stream().filter(acc -> acc.getId() == transactionId).findFirst().get();
    }

    /**
     * Поиск транзакции по id
     *
     * @param transactionId id транзакции
     * @return найденная транзакция, null если не нашли
     */
    public MoneyTransaction findTransactionById(UUID transactionId) {
        if (transactionsList.stream().filter(acc -> acc.getId() == transactionId).count() == ZERO_ELEMENTS) {
            return null;
        }
        if (transactionsList.stream().filter(acc -> acc.getId() == transactionId).count() != ONE_ELEMENT) {
            throw new IllegalArgumentException("Not one client while getting it.");
        }
        return transactionsList.stream().filter(acc -> acc.getId() == transactionId).findFirst().get();
    }

    /**
     * Поиск обратной транзакции по id транзакции
     *
     * @param transactionId id транзакции, обратную (откат) к которой ищем
     * @return найденная транзакция (обратная), null если не нашли
     */
    public MoneyTransaction findTransactionRollBackById(UUID transactionId) {
        for (MoneyTransaction tr : transactionsList) {
            if (tr instanceof RollBackTransaction) {
                if (((RollBackTransaction) tr).getTransaction().getId() == transactionId)
                    return tr;
            }
        }

        return null;
    }

    /**
     * Проверка существования паспорта у клиента.
     *
     * @param newId if пасспорта на проверку.
     * @return true - если существует, иначе false.
     */
    public boolean clientPassportExists(int newId) {
        for (var cl : clientAccountsList) {
            if (cl.getPassport() != null) {
                if (cl.getPassport().getId() == newId)
                    return true;
            }
        }

        return false;
    }

    /**
     * Проверяет на существование транзакции по её id.
     *
     * @param newTransactionId id транзакции на проверку.
     * @return true, если нашлась, иначе false.
     */
    public boolean hasTransactionById(UUID newTransactionId) {
        var foundTransaction = findTransactionById(newTransactionId);
        if (foundTransaction == null) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет существует ли обратная транзакция (откат) к транзакции по id
     *
     * @param newTransactionId id транзакции, существование обратной к которой проверяем
     * @return true, если нашлась, иначе false.
     */
    public boolean hasTransactionRollBackById(UUID newTransactionId) {
        var foundTransaction = findTransactionRollBackById(newTransactionId);
        if (foundTransaction == null) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет существование клиента в банке
     *
     * @param newClient клиент на проверку
     * @return true, если нашлась, иначе false.
     */
    public boolean hasClient(ClientAccount newClient) {
        if (newClient == null) {
            return false;
        }
        if (newClient.getBank().getId() != id) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет существование счёта в банке
     *
     * @param newAccount счёт на проверку
     * @return true, если нашлась, иначе false.
     */
    public boolean hasAccount(Account newAccount) {
        if (newAccount == null) {
            return false;
        }
        if (newAccount.getClientAccount().getBank().getId() != id) {
            return false;
        }
        return true;
    }

    /**
     * Изменяет процентную ставку в банке и во всех его счетах.
     *
     * @param newRate новая дебетовая процентная ставка.
     */
    public void changeDebitInterestRate(double newRate) {
        conditions.changeDebitInterestRate(newRate);
        changeAllAccountsConditions();
    }

    /**
     * Изменяет комиссию в банке и во всех его счетах.
     *
     * @param newCommission новая комиссия
     */
    public void changeCreditComission(double newCommission) {
        conditions.changeCreditComission(newCommission);
        changeAllAccountsConditions();
    }

    /**
     * Изменяет депозитные условия в банке и во всех счетах.
     *
     * @param newCoditions новые депозитные условия {@see DepositInterestRateConditions}
     */
    public void changeDepositConditions(DepositInterestRateConditions newCoditions) {
        conditions.changeDepositConditions(newCoditions);
        changeAllAccountsConditions();
    }

    /**
     * Изменяет кредитный лимит в банке и во всех его счетах.
     *
     * @param newCreditLimit новый кредитный лимит
     */
    public void changeCreditLimit(double newCreditLimit) {
        conditions.changeCreditLimit(newCreditLimit);
        changeAllAccountsConditions();
    }

    /**
     * Изменяет лимит на снятие денег людьми без паспорта или адреса в банке и во всех его счетах.
     *
     * @param newLimit новый лимит на снятие денег людьми без паспорта или адреса
     */
    public void changeSuspiciousClientMoneyLimit(double newLimit) {
        conditions.changeSuspiciousClientMoneyLimit(newLimit);
        changeAllAccountsConditions();
    }

    /**
     * Добавляет клиента в банк.
     *
     * @param newClient новый клиент, которого добавляем
     */
    public void addClientAccount(ClientAccount newClient) {
        if (newClient == null) {
            throw new IllegalArgumentException("Null client while adding it to bank");
        }
        clientAccountsList.add(newClient);
    }

    /**
     * Создаёт счёт для конкретного клиента в этом банке с помощью фабрики {@see AccountCreator}
     *
     * @param newClient клиента, счёт которому создаём
     * @param creator - фабрика-создаватель конкретного счёта
     * @return созданный счёт
     */
    public Account createAccount(ClientAccount newClient, AccountCreator creator) {
        if (newClient == null) {
            throw new IllegalArgumentException("No such client, so can't create account");
        }

        if (newClient.getBank().getId() != id) {
            throw new IllegalArgumentException("No such client, so can't create account");
        }

        var newAccount = creator.CreateAccount(accountId, newClient, this);

        newClient.addAccount(newAccount);
        accountsList.add(newAccount);
        accountId++;

        return newAccount;
    }

    /**
     * Добавление адреса клиенту.
     *
     * @param newAccount клиент, которому добавляем адрес
     * @param newAddress адрес, чтобы добавить
     */
    public void addClientAdress(ClientAccount newAccount, String newAddress) {
        if (newAccount == null) {
            throw new IllegalArgumentException("Account is null while adding adress.");
        }
        if (!hasClient(newAccount)) {
            throw new IllegalArgumentException("Account not in bank while adding Address.");
        }

        newAccount.setAddress(new Address(newAddress));
    }

    /**
     * Добавление паспорта клиенту.
     *
     * @param newAccount клиент, которому добавляем паспорт.
     * @param passportId id пасспорта, который создаётся и добавляется клиенту.
     */
    public void addClientPassport(ClientAccount newAccount, int passportId) {
        if (newAccount == null) {
            throw new IllegalArgumentException("Account is null while adding passport.");
        }
        if (!hasClient(newAccount)) {
            throw new IllegalArgumentException("Account not in bank while adding Passport.");
        }

        newAccount.setPassport(new Passport(passportId));
    }

    /**
     * Пополнение счёта
     *
     * @param accountId id счёта, куда добавлять деньги
     * @param amount количество денег чтобы добавить
     */
    public void addMoney(int accountId, double amount) {
        var account = getAccountById(accountId);
        var transaction = new AddTransaction(amount, account);
        transaction.createTransaction();

        transactionsList.add(transaction);
    }

    /**
     * Снятие денег со счёта
     *
     * @param accountId id счёта, откуда снять деньги
     * @param amount количество денег чтобы снять
     */
    public void withdrawMoney(int accountId, double amount) {
        var account = getAccountById(accountId);
        var transaction = new WithdrawTransaction(amount, account);
        transaction.createTransaction();

        transactionsList.add(transaction);
    }

    /**
     * Перевод денег со счёта на счёт.
     *
     * @param newAccountFrom аккаунт, откуда снять деньги.
     * @param newAccountTo аккаунт, куда перевести деньги.
     * @param amount количество денег для перевода.
     */
    public void transferMoney(Account newAccountFrom, Account newAccountTo, double amount) {
        if (newAccountFrom == null || newAccountTo == null) {
            throw new IllegalArgumentException("null account while transfering money");
        }
        if (!hasAccount(newAccountFrom)) {
            throw new IllegalArgumentException("Can't tansfer money, as Account is not in Bank.");
        }

        if (!newAccountFrom.canWithdrawMoney(amount)) {
            throw new IllegalArgumentException("Can't withdraw money, not enought money in account.");
        }

        var transaction = new TransferTransaction(amount, newAccountFrom, newAccountTo);

        if (!hasAccount(newAccountTo)) {
            CentralBank.getInstance().transferMoneyBetweenBanks(transaction);
        }
        else {
            transaction.createTransaction();
            transactionsList.add(transaction);
        }
    }

    /**
     * Добавление транзакции в банк.
     *
     * @param transaction транзакция для добавления.
     */
    public void addTransaction(MoneyTransaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Null transactian while adding it to Bank.");
        }

        transactionsList.add(transaction);
    }

    /**
     * Откат транзакции по её id.
     * Создаёт транзакцию отката транзакции и выполняет её.
     * Если транзакция между счетами в этом банке - то выполняется в данном методе,
     * если между разными банками - то через центральный банк {@see CentralBank}
     *
     * @param transactionId - id транзакции для отката.
     */
    public void rollbackTrabsaction(UUID transactionId) {
        MoneyTransaction transaction = getTransactionById(transactionId);

        if (transaction instanceof RollBackTransaction) {
            throw new IllegalArgumentException("Can't Rollback Rollbacks.");
        }
        if (hasTransactionRollBackById(transactionId)) {
            throw new IllegalArgumentException("Rollback already exist, can't creat more.");
        }

        RollBackTransaction rollbackTransaction = new RollBackTransaction(transaction);
        if (isTransactionBetweenBanks(transactionId)) {
            CentralBank.getInstance().rollBackMoneyBetweenBanks(transactionId, rollbackTransaction);
        }
        else {
            rollbackTransaction.createTransaction();
            transactionsList.add(rollbackTransaction);
        }
    }

    /**
     * Даёт команду всем счетам накапливать деньги по процентной ставке.
     */
    public void accumulateInterestRate() {
        for (var accaunt : accountsList) {
            accaunt.accumulateInterestRate();
        }
    }

    /**
     * Подписывает клиента на уведомления об изменении определённого условия счёта в банке.
     *
     * @param newClient клиент, которого подписывает
     * @param newHandler обработчик сообщений
     * @param observableName тип того, на что клиента надо подписать
     */
    public void subscribe(ClientAccount newClient, MessageHandler newHandler, ObservablesNames observableName) {
        if (newHandler == null) {
            throw new IllegalArgumentException("Null handler while subscribing in bank.");
        }
        if (newClient == null) {
            throw new IllegalArgumentException("No such client to subscribe.");
        }
        if (newClient.getBank().getId() != id) {
            throw new IllegalArgumentException("No such client to subscribe.");
        }

        Observable observable;

        switch (observableName) {
            case CREDIT_COMMISSION:
                observable = conditions.getComissionClass();
                break;
            case CREDIT_LIMIT:
                observable = conditions.getCreditLimitClass();
                break;
            case DEPOSIT_CONDITIONS:
                observable = conditions.getDepositConditionsClass();
                break;
            case DEBIT_INTEREST_RATE:
                observable = conditions.getDebitInterestRateClass();
                break;
            case SUSPISIOUS_CLIENT_LIMIT:
                observable = conditions.getSuspiciousClientMoneyLimitClass();
                break;
            default:
                throw new IllegalArgumentException("Wrong ObservablesName enum.");
        }

        newClient.addHandler(newHandler);
        observable.addSubscriber(newClient);
    }

    private boolean isTransactionBetweenBanks(UUID transactionId) {
        MoneyTransaction foundTransaction = findTransactionById(transactionId);

        if (foundTransaction == null) {
            throw new IllegalArgumentException("Null transaction");
        }
        if (!(foundTransaction instanceof TransferTransaction)) {
            return false;
        }

        return true;
    }

    /**
     * Обновляет все условия во всех счетах банка.
     */
    private void changeAllAccountsConditions() {
        for (var account : accountsList) {
            account.changeConditions();
        }
    }
}
