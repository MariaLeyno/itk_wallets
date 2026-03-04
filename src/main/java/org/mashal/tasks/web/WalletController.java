package org.mashal.tasks.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.mashal.tasks.errors.WalletInsufficientFundsException;
import org.mashal.tasks.errors.WalletNotFoundException;
import org.mashal.tasks.errors.WalletServiceException;
import org.mashal.tasks.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@Tag(name = "wallets", description = "REST API to manage to manage wallet operations and get actual wallet amount")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @Operation(summary = "Get wallet's amount")
    @GetMapping(value = "/api/v1/wallets/{walletId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Double getWalletAmount(@PathVariable UUID walletId) {
        try {
            return walletService.getAmount(walletId);
        } catch (WalletNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (WalletServiceException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @Operation(summary = "Add a new wallet operation")
    @PostMapping(value = "/api/v1/wallet", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addWalletOperation(@RequestBody WalletDTO walletDTO) {
        try {
            walletService.addWalletOperation(walletDTO.walletId(), walletDTO.operationType(), walletDTO.amount());
        } catch (WalletNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (WalletInsufficientFundsException ex) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, ex.getMessage());
        } catch (WalletServiceException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
}
