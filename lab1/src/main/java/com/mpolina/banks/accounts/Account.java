package com.mpolina.banks.accounts;

import com.mpolina.banks.client.ClientAccount;
import com.mpolina.banks.transactions.TransactionConditions;

/**
 * Интерфейс счёта, который может открыть клиент.
 */
public interface Account {
    /**
     * @return Id счёта.
     */
    public int getId();

    /**
     * @return количество денег на счету.
     */
    public double getMoney();

    /**
     * @return клиента, на которого зарегестирирован данный счёт.
     */
    public ClientAccount getClientAccount();

    /**
     * Начисляет деньги в аккаунт.
     *
     * @param newAmount количество денег, которое кладём на счёт.
     * @return условия транзакции {@see TransactionConditions}
     */
    TransactionConditions addMoney(double newAmount);
    /**
     * Списывает деньги из аккаунта.
     *
     * @param newAmount количество денег, которое списываем со счёта.
     * @return условия транзакции {@see TransactionConditions}
     */
    TransactionConditions withdrawMoney(double newAmount);
    /**
     * Откатываем операцию, согласно переданным условиям транзакции
     *
     * @param newConditions условия транзакции, которую хотим откатить
     * @param newRollBackType тип транзакции, будет учтён как коэффициент,
     *                        учитывая который произойдёт либо списание,
     *                        либо пополнение при откате транзакции.
     * @return условия новой транзакции, являющейся откатом
     */
    TransactionConditions rollBack(TransactionConditions newConditions, int newRollBackType);

    /**
     * Считает, возможно ли списать определённое количество денег со счёта.
     *
     * @param newAmount количество денег, на которое проверяем
     * @return boolean ответ на возможность списать деньги: да => true, нет => false
     */
    boolean canWithdrawMoney(double newAmount);

    /**
     * Запасает процент {@code interestRateStorage}, суммируя месячную процентную ставку
     * помноженную на текущее количество денег на счету.
     * При наступлении следующего месяца вызывает метод по начислению запасённых процентов
     * на счёт.
     */
    public void accumulateInterestRate();

    /**
     * Изменяет условия счета - процентую ставку, лимит списывания и подобное.
     */
    public void changeConditions();
}
