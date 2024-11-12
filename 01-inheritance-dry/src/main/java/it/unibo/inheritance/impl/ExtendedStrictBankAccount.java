package it.unibo.inheritance.impl;

public class ExtendedStrictBankAccount extends SimpleBankAccount {
    
    private static final double TRANSACTION_FEE = 0.1;

    public ExtendedStrictBankAccount (final int id, final double balance) {
        super(id, balance);
    }

    @Override
    public void chargeManagementFees(final int id) {
        final double feeAmount = MANAGEMENT_FEE + getTransactionsCount() * TRANSACTION_FEE;
        if (checkUser(id) && (getBalance() >= feeAmount)) {
            final double balance = getBalance() - feeAmount;
            setBalance(balance);
            resetTransactions();
        }
    }

    @Override
    public void withdraw(final int id, final double amount) {
        if (getBalance() >= amount) {
            super.withdraw(id, amount);
        }
    }

}
