package org.mashal.tasks.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "wallets")
@Data
@NoArgsConstructor
public class Wallet {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "user", length = 255)
    private String user;

    @Column(name = "amount")
    private Double amount;
}
