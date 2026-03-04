package org.mashal.tasks.service;

import org.mashal.tasks.errors.WalletInsufficientFundsException;
import org.mashal.tasks.errors.WalletNotFoundException;
import org.mashal.tasks.errors.WalletServiceException;
import org.mashal.tasks.model.OperationType;
import org.mashal.tasks.model.Wallet;
import org.mashal.tasks.model.WalletOperation;
import org.mashal.tasks.repositories.WalletOperationRepository;
import org.mashal.tasks.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    private final WalletOperationRepository operationRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository, WalletOperationRepository operationRepository) {
        this.walletRepository = walletRepository;
        this.operationRepository = operationRepository;
    }

    public Double getAmount(UUID walletId) throws WalletServiceException {
        return getWallet(walletId).getAmount();
    }

    public void addWalletOperation(UUID walletId, OperationType operation, Double amount) throws WalletServiceException {
        Wallet wallet = getWallet(walletId);

        if (operation == OperationType.WITHDRAW) {
            Double currentAmount = wallet.getAmount();
            if (currentAmount < amount) {
                throw new WalletInsufficientFundsException(walletId);
            }
            amount *= -1;
        }

        WalletOperation walletOperation = new WalletOperation(walletId, Instant.now(), operation, amount);
        operationRepository.save(walletOperation);

    }

    private Wallet getWallet(UUID walletId) throws WalletNotFoundException {
        Optional<Wallet> wallet = walletRepository.findById(walletId);
        if (wallet.isEmpty()) {
            throw new WalletNotFoundException(walletId);
        }
        return wallet.get();
    }
}
