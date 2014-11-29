package com.db.javaschool.tdd.live;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * User: Yury
 */
public class TransactionManagerTest {

    private TransactionManager tm;
    private IBankAccount debited;
    private IBankAccount credited;

    @Before
    public void init() {
        tm = new TransactionManager();
        debited = new TestBankAccount(1);
        credited = new TestBankAccount(1);

    }

    @After
    public void tearDown() {
        tm = null;
        debited = null;
        credited = null;
    }

    private static class TestBankAccount implements IBankAccount{

        private long amount;

        public TestBankAccount(long initialAmount) {
            this.amount = initialAmount;
        }

        @Override
        public long getAmount() {
            return amount;
        }

        @Override
        public void setAmount(long newAmount) {
            this.amount = newAmount;
        }
    }


    @Test
    public void testDoTransaction_shouldDeductTrAmountFromDebitedAndAddItToCredited() {
        tm.doTransaction(debited, credited, 1);
        assertEquals(0, debited.getAmount());
        assertEquals(2, credited.getAmount());
    }



    @Test(expected=IllegalArgumentException.class)
    public void testDoTransaction_shouldThrowException_whenAccountAreNotDifferent() {
        tm.doTransaction(debited, debited, 1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testDoTransaction_shouldThrowException_whenTransactionAmountIsZero() {
        tm.doTransaction(debited, credited, 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testDoTransaction_shouldThrowException_whenTransactionAmountIsNegative() {
        tm.doTransaction(debited, credited, -1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testDoTransaction_shouldThrowException_whenDebitedAmountIsNegative() {
        tm.doTransaction(debited, credited, 2);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testDoTransaction_shouldThrowException_whenCreditedAmountIsNegative() {
        credited = new TestBankAccount(Long.MAX_VALUE);
        tm.doTransaction(debited, credited, 1);
    }


}
