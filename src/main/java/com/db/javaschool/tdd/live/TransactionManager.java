package com.db.javaschool.tdd.live;

/**
 * User: Yury
 */
public class TransactionManager {


    public void doTransaction(IBankAccount debited, IBankAccount credited, long amount) {

        if (debited == credited) {
            throw new IllegalArgumentException("Accounts should be different");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount should not be less or equal to zero");
        }

        debited.setAmount(debited.getAmount() - amount);
        credited.setAmount(credited.getAmount() + amount);

        if (debited.getAmount() < 0 || credited.getAmount() < 0) {
            throw new IllegalArgumentException("Account amount should not become negative");
        }
    }
}
