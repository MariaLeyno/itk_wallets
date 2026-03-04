package org.mashal.tasks.errors;

import java.util.UUID;

public class WalletNotFoundException extends WalletServiceException {
    private static final String message = "Wallet '%s' does not exist";

    public WalletNotFoundException(UUID walletId) {
        super(String.format(message, walletId));
    }
}
