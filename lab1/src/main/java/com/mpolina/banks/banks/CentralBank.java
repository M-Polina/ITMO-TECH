package com.mpolina.banks.banks;

import com.mpolina.banks.accounts.Account;
import com.mpolina.banks.client.ClientAccount;
import com.mpolina.banks.exceptions.BanksException;
import com.mpolina.banks.time.OwnTime;
import com.mpolina.banks.transactions.MoneyTransaction;
import com.mpolina.banks.transactions.RollBackTransaction;
import com.mpolina.banks.transactions.TransferTransaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Класс центрального банка, хранящий в себе все существующие банки и клиенты,
 * отвечающий за создание банков, их поиск, ускорение времени, уведомление банков
 * и совершение транзакций между банками.
 * Этот класс - singletone - существует в единственном экземпляре.
 */
public class CentralBank {
    private final int MIN_ID = 0;
    private final int MIN_NUM = 0;
    private final int ONE_ELEMENT = 1;

    private static CentralBank instance = new CentralBank();

    private int bankId = MIN_ID;
    private ArrayList<Bank> banksList = new ArrayList<Bank>();

    private CentralBank() { }

    /**
     * Возвращает существующий экземпляр класса CentralBank или создаёт новый
     * если его пока не существует.
     *
     * @return экземпляр класса CentralBank
     */
    public static CentralBank getInstance()
    {
        if (instance == null) {
            instance = new CentralBank();
        }

        return instance;
    }

    /**
     * Поиск банка среди существующих по Id.
     *
     * @param bankIdToFind - Id банка, который хотим найти.
     * @return искомый банк, если есть, иначе null.
     */
    public Bank findBankById(int bankIdToFind) {
        if (banksList.stream().filter(bank -> bank.getId() == bankIdToFind).count() == MIN_NUM) {
            return null;
        }
        if (banksList.stream().filter(bank -> bank.getId() == bankIdToFind).count() > ONE_ELEMENT){
            throw new BanksException("Two banks with same Id.");
        }
              return banksList.stream().filter(bank -> bank.getId() == bankIdToFind).findFirst().get();
    }

    /**
     * Поиск банка среди существующих по Id.
     *
     * @param bankIdToFind - Id банка, который хотим найти.
     * @return искомый банк, если есть.
     * @throws BanksException если такого банка нет.
     */
    public Bank getBankById(int bankIdToFind) {
        if (banksList.stream().filter(bank -> bank.getId() == bankIdToFind).count() == MIN_NUM) {
            throw new BanksException("No bank with this Id.");
        }
        if (banksList.stream().filter(bank -> bank.getId() == bankIdToFind).count() > ONE_ELEMENT){
            throw new BanksException("Two banks with same Id.");
        }
        return banksList.stream().filter(bank -> bank.getId() == bankIdToFind).findFirst().get();
    }

    /**
     * Возвращает список всех существующих клиентов.
     *
     * @return список всех существующих клиентов.
     */
    public List<ClientAccount> getClientAccounts()
    {
        var list = new ArrayList<ClientAccount>();
        for(var bank : banksList)
        {
            list.addAll(bank.getClientAccountsList());
        }

        return  Collections.unmodifiableList(list);
    }

    /**
     * Возвращает список всех существующих счетов
     *
     * @return список всех существующих счетов
     */
    public List<Account> getAccounts()
    {
        var list = new ArrayList<Account>();
        for(var bank : banksList)
        {
            list.addAll(bank.getAccountsList());
        }

        return Collections.unmodifiableList(list);
    }

    /**
     * Возвращает список всех существующих банков
     *
     * @return список всех существующих банков
     */
    public List<Bank> getBanksList() {

        return Collections.unmodifiableList(banksList);
    }

    /**
     * Создаёт банк с заданными параметрами.
     *
     * @param newName имя банка
     * @param newDebitInterestRate депозитная ставка банка  (положительная)
     * @param newCommission кредитная комиссия банка (положительная)
     * @param newCreditLimit кредитный предел банка (отрицательный)
     * @param newDepositCoditions  депозитные условия банка {@see DepositInterestRateConditions}
     * @param newSuspiciousClientMoneyLimit  преел на снятия людьми без пасспорта или адреса (положительная)
     * @return созданный банк.
     */
   public Bank createBank(String newName,
                          double newDebitInterestRate,
                          double newCommission,
                          double newCreditLimit,
                          DepositInterestRateConditions newDepositCoditions,
                          double newSuspiciousClientMoneyLimit)
    {
        var newBank = new Bank(bankId, newName, newDebitInterestRate, newCommission,
                newCreditLimit, newDepositCoditions, newSuspiciousClientMoneyLimit);

        banksList.add(newBank);
        bankId++;

        return newBank;
    }

    /**
     * Уведомление всех банков, чтобы они накапливали деньги по процентной ставке.
     */
    public void notifyBanksToAccumulateInterestRate()
    {
        for(var bank : banksList)
        {
            bank.accumulateInterestRate();
        }
    }

    /**
     * Перевод денег между счетами в разных банках
     *
     * @param transactionFrom транзакция, которую хотим совершить для перевода денег между банками.
     */
    public void transferMoneyBetweenBanks(MoneyTransaction transactionFrom)
    {
        if (transactionFrom == null) {
            throw new BanksException("Null transactionFrom while creating Bank to Bank transfer.");
        }
        if (!(transactionFrom instanceof TransferTransaction)) {
            throw new BanksException("Wrong transaction while creating Bank to Bank transfer.");
        }
        Account accountFrom = ((TransferTransaction)transactionFrom).getAccountFrom();
        if (!accountFrom.canWithdrawMoney(transactionFrom.getTransactionConditions().getAmountOfMoney()))
        {
            throw new BanksException("Can't withdraw money, not enought money in account.");
        }

        transactionFrom.createTransaction();
        Bank bankTo = ((TransferTransaction)transactionFrom).getAccountTo().getClientAccount().getBank();
        bankTo.addTransaction(transactionFrom);
        Bank bankFrom = ((TransferTransaction)transactionFrom).getAccountFrom().getClientAccount().getBank();
        bankFrom.addTransaction(transactionFrom);
    }

    /**
     * Откат транзакции между банками.
     *
     * @param transactionToRollbackId  id транзакции, которую надо откатить
     * @param rollbackTransaction сама транзакция по откату денег, которая будет выполнена.
     */
    public void rollBackMoneyBetweenBanks(UUID transactionToRollbackId, RollBackTransaction rollbackTransaction)
    {
        if (rollbackTransaction == null) {
            throw new BanksException("Null rollbackTransaction while creating Bank to Bank Rollback.");
        }
        if (rollbackTransaction.getTransaction() instanceof RollBackTransaction)
        {
            throw new BanksException("Not BankToBank transaction rollback while creating Bank to Bank Rollback.");
        }

        Bank bankTo = ((TransferTransaction)rollbackTransaction.getTransaction()).getAccountTo().getClientAccount().getBank();
        Bank bankFrom = ((TransferTransaction)rollbackTransaction.getTransaction()).getAccountFrom().getClientAccount().getBank();

        if (!bankTo.hasTransactionById(transactionToRollbackId)) {
            throw new BanksException("No transaction to RollBack in BankTo.");
        }
        if (!bankFrom.hasTransactionById(transactionToRollbackId)) {
            throw new BanksException("No transaction to RollBack in BankFrom.");
        }

        rollbackTransaction.createTransaction();
        bankTo.addTransaction(rollbackTransaction);
        bankFrom.addTransaction(rollbackTransaction);
    }

    /**
     * Ускоряет время и напоминает всем банкам накапливать деньги по проценту {@see notifyBanksToAccumulateInterestRate}
     *
     * @param days количество дней (положительное), на которое надо ускорить время.
     */
    public void speedUpTime(int days)
    {
        OwnTime time = OwnTime.getInstance();
        for (int i = 0; i < days; i++)
        {
            time.speedUp();
            notifyBanksToAccumulateInterestRate();
        }
    }
}
