package com.grupo12.Voy.features.receipts;

import com.grupo12.Voy.features.receipts.models.ReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceiptRepository extends JpaRepository<ReceiptEntity,Long> {
    Optional<ReceiptEntity> findById(Long id);
    Optional<ReceiptEntity> findByExternalId(UUID id);
    List<ReceiptEntity> findByPaymentMethod(String method);
}
