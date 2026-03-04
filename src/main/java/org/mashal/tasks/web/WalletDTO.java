package org.mashal.tasks.web;

import org.mashal.tasks.model.OperationType;

import java.util.Objects;
import java.util.UUID;

public record WalletDTO(UUID walletId, OperationType operationType, Double amount) {
    public WalletDTO {
        Objects.requireNonNull(walletId);
        Objects.requireNonNull(operationType);
        Objects.requireNonNull(amount);
    }
}
