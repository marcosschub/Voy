package com.grupo12.Voy.features.receipts.models;

import com.grupo12.Voy.features.users.models.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "facturas")
public class ReceiptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recibo_id")
    private Long id;

    private UUID externalId;

    @Column(name = "precio")
    private BigDecimal price;

    @Column(name = "metodo_de_pago")
    private String paymentMethod;

    @Column(name = "precio_final")
    private BigDecimal finalPrice;

    @Column(name = "fecha_compra")
    private LocalDateTime paymentDate;

    @Column(name = "cantidad")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UserEntity user;

    public ReceiptEntity(BigDecimal price, String paymentMethod, Integer quantity, UserEntity user) {
        this.externalId = UUID.randomUUID();
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDateTime.now();
        this.quantity = quantity;
        this.finalPrice = price.multiply(BigDecimal.valueOf(quantity));
        this.user = user;
    }
}
