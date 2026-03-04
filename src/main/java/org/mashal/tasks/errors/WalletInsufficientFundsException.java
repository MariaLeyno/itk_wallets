package org.mashal.tasks.errors;

import java.util.UUID;

public class WalletInsufficientFundsException extends WalletServiceException {
    private static final String message = "Wallet '%s' does not have enough money for withdraw operation";

    public WalletInsufficientFundsException(UUID walletId) {
        super(String.format(message, walletId));
    }
}
