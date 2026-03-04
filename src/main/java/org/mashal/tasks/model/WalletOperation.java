package org.mashal.tasks.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mashal.tasks.repositories.WalletOperationId;

import java.time.Instant;
import java.util.UUID;

@Entity
@IdClass(WalletOperationId.class)
@Table(name = "wallet_operations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletOperation {

    @Id
    @Column(name = "wallet_id")
    private UUID walletId;

    @Id
    @Column(name = "time")
    private Instant time;

    @Column(name = "operation")
    @Enumerated(EnumType.STRING)
    private OperationType operation;

    @Column(name = "amount")
    private Double amount;
}
