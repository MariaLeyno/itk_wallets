package org.mashal.tasks.repositories;

import org.mashal.tasks.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    @Transactional(readOnly = true)
    Optional<Wallet> findById(UUID id);
}
