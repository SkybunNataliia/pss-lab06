package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the {@link StrictBankAccount} class.
 */
class TestStrictBankAccount {

    // Create a new AccountHolder and a StrictBankAccount for it each time tests are executed.
    private AccountHolder mRossi;
    private BankAccount bankAccount;

    private static final int AMOUNT = 100;
    private static final int ACCEPTABLE_MESSAGE_LENGTH = 10;

    /**
     * Prepare the tests.
     */
    @BeforeEach
    public void setUp() {
        this.mRossi = new AccountHolder("Mario", "Rossi", 1);
        this.bankAccount = new StrictBankAccount(mRossi, 0.0);
    }

    /**
     * Test the initial state of the StrictBankAccount.
     */
    @Test
    public void testInitialization() {
        assertEquals(0.0, bankAccount.getBalance());
        assertEquals(0, bankAccount.getTransactionsCount());
        assertEquals(mRossi, bankAccount.getAccountHolder());
    }

    /**
     * Perform a deposit of 100€, compute the management fees, and check that the balance is correctly reduced.
     */
    @Test
    public void testManagementFees() {
        bankAccount.deposit(mRossi.getUserID(), AMOUNT);
        assertEquals(1, bankAccount.getTransactionsCount());
        assertEquals(AMOUNT, bankAccount.getBalance());

        double feeAmount = StrictBankAccount.MANAGEMENT_FEE + bankAccount.getTransactionsCount() * StrictBankAccount.TRANSACTION_FEE;
        bankAccount.chargeManagementFees(mRossi.getUserID());
        assertEquals(AMOUNT-feeAmount, bankAccount.getBalance());
        assertEquals(0, bankAccount.getTransactionsCount());
    }

    /**
     * Test that withdrawing a negative amount causes a failure.
     */
    @Test
    public void testNegativeWithdraw() {
        try {
            bankAccount.withdraw(mRossi.getUserID(), -AMOUNT);
            Assertions.fail("Withdrawing a negative amount.");
        } catch(IllegalArgumentException i) {
            assertEquals(0, bankAccount.getTransactionsCount());
            assertNotNull(i.getMessage());
            assertFalse(i.getMessage().isBlank());
            assertTrue(i.getMessage().length() >= ACCEPTABLE_MESSAGE_LENGTH);
        }
    }

    /**
     * Test that withdrawing more money than it is in the account is not allowed.
     */
    @Test
    public void testWithdrawingTooMuch() {
        try {
            bankAccount.withdraw(mRossi.getUserID(), 2*AMOUNT);
            Assertions.fail("Withdrawing more money than it is in the account.");
        } catch(IllegalArgumentException i) {
            assertEquals(0, bankAccount.getTransactionsCount());
            assertNotNull(i.getMessage());
            assertFalse(i.getMessage().isBlank());
            assertTrue(i.getMessage().length() >= ACCEPTABLE_MESSAGE_LENGTH);
        }
    }
}
