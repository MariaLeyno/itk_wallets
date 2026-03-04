package org.mashal.tasks.repositories;

import org.mashal.tasks.model.WalletOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletOperationRepository extends JpaRepository<WalletOperation, WalletOperationId> {
}
