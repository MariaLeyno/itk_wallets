package org.mashal.tasks.errors;

public abstract class WalletServiceException extends Exception {

    protected WalletServiceException(String message) {
        super(message);
    }
}
