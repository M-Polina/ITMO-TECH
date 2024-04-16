package com.mpolina.banks.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.mpolina.banks.exceptions.BanksException;
import com.mpolina.banks.messagehandlers.MessageHandler;
import lombok.Getter;
import com.mpolina.banks.accounts.Account;
import com.mpolina.banks.banks.Bank;
import com.mpolina.banks.messagehandlers.ObservableMessage;
import com.mpolina.banks.observer.Observable;

/**
 * Класс клиента, принадлежащего банку. Хранит счета клиента,
 * позволяет добавить недотающую клиенту информацию.
 * Клиент может подписаться на уведомления об изменении условий счетов в банке.
 */
public class ClientAccount implements Subscriberable {
    private final int ONE_ELEMENT = 1;
    @Getter
    private UUID id;
    /**
     * Банк, к которому принадлежит клиент.
     */
    @Getter
    private Bank bank;
    /**
     * Полное имя клиента.
     */
    @Getter
    private FullName fullName;
    /**
     * Адрес клиента, которого может не быть, тогда клиент подозрительный.
     * Подозрительный = есть ограничения на транзакции со счетами
     */
    @Getter
    private Address address = null;
    /**
     * Паспорт клиента, которого может не быть, тогда клиент подозрительный.
     * Подозрительный = есть ограничения на транзакции со счетами.
     */
    @Getter
    private Passport passport = null;
    /**
     * Список счетов клиента.
     */
    private ArrayList<Account> accountsList = new ArrayList<Account>();
    /**
     * Список обработчиков сообщений (устройств), позволяющих доставить сообщение на нужный носитель
     * (отправить его).
     */
    private ArrayList<MessageHandler> handlersList = new ArrayList<MessageHandler>();

    /**
     * Конструктор клиента.
     *
     * @param newBank банк, которому принадлежит клиент.
     * @param newName  полное имя клиента
     * @param newAddress адрес клиента (или null если нет)
     * @param newPassport паспорт клиента (или null если нет)
     */
    public ClientAccount(Bank newBank, FullName newName, Address newAddress, Passport newPassport) {
        if (newName == null) {
            throw new BanksException("FullName is null while creating ClientAccount.");
        }

        if (newBank == null) {
            throw new BanksException("Bank is null while creating ClientBuilder.");
        }

        bank = newBank;
        fullName = newName;
        address = newAddress;
        passport = newPassport;
        id = UUID.randomUUID();
    }

    /**
     * @return список счетов, открытых клиентом.
     */
    public List<Account> getAccountsList() {
        return Collections.unmodifiableList(accountsList);
    }

    /**
     * @return список обработчик сообщений, отправляющих сообщение в нужное место.
     */
    public List<MessageHandler> getHandlersList() {
        return Collections.unmodifiableList(handlersList);
    }

    /**
     * Ищет счёт по id
     *
     * @param accountId id счёта, который надо найти
     * @return найденный счёт, если не нашёл, то null
     */
    public Account findAccount(int accountId) {
        if (accountsList.stream().filter(acc -> acc.getId() == accountId).count() > ONE_ELEMENT) {
            throw new BanksException("There were found > 1 accounts for client with this id");
        }
        var found = accountsList.stream().filter(acc -> acc.getId() == accountId);
        if (found.count() == 0) {
            return null;
        }
        return found.findFirst().get();
    }

    /**
     * Ищет счёт по id
     *
     * @param accountId id счёта, который надо найти
     * @return найденный счёт
     * @throws BanksException когда не нашёл счёт
     */
    public Account getAccount(int accountId) {
        if (accountsList.stream().filter(acc -> acc.getId() == accountId).count() > ONE_ELEMENT) {
            throw new BanksException("There were found > 1 accounts for client with this id");
        }
        var found = accountsList.stream().filter(acc -> acc.getId() == accountId);
        if (found.count() == 0) {
            throw new BanksException("No account with this Id");
        }
        return found.findFirst().get();
    }

    /**
     * Проверяет клиента на подозрительность, то есть есть ли у него и адрес, и паспорт.
     *
     * @return true, если подозрительный, инче - false
     */
    public boolean isNotSuspicious() {
        return !(fullName == null) && !(address == null) && !(passport == null);
    }

    /**
     * Добавление счёта клиенту.
     *
     * @param newAccount счёт, который надо добавить клиенту.
     */
    public void addAccount(Account newAccount) {
        if (newAccount == null) {
            throw new BanksException("Account is null while adding it to account");
        }
        if (accountsList.contains(newAccount)) {
            throw new BanksException("Client already has this account.");
        }
        if (newAccount.getClientAccount().getId() != id || newAccount.getClientAccount().getBank().getId() != bank.getId()) {
            throw new BanksException("Incorrect account while adding it to client.");
        }

        accountsList.add(newAccount);
    }

    /**
     * Добавление адреса клиенту.
     *
     * @param newAddress новый адрес клиента.
     */
    public void setAddress(Address newAddress) {
        if (newAddress == null) {
            throw new BanksException("Address is null while creating ClientAccount.");
        }

        address = newAddress;
    }

    /**
     * Добавление паспорта клиенту.
     *
     * @param newPassport новый паспорт клиента.
     */
    public void setPassport(Passport newPassport) {
        if (newPassport == null) {
            throw new BanksException("Passport is null while creating ClientAccount.");
        }

        passport = newPassport;
    }

    /**
     * Добавление обработчиков сообщений (устройств, доставляющих сообщения в разные места)
     *
     * @param newHandler обработчик (устройство), которое добавляем клиенту,
     *                   чтобы на него приходили сообщения (чтобы оно "доставляло" сообщение)
     */
    public void addHandler(MessageHandler newHandler) {
        if (newHandler == null) {
            throw new BanksException("Null handler while adding it");
        }
        if (!handlersList.contains(newHandler)) {
            handlersList.add(newHandler);
        }
    }

    /**
     * Получение сообщения об изменении того, на что подписан и отправка этого сообщения
     * всеми обработчиками (устройствами)
     *
     * @param observable - то, на изменение чего подписан.
     */
    public void update(Observable observable) {
        ObservableMessage message = new ObservableMessage(observable);
        for (var handler : handlersList) {
            handler.sendMessage(message);
        }
    }
}
