package com.grupo12.Voy.features.tickets.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Date;

@Embeddable
public class TicketDescriptionEntity {
    @Column(name = "precio")
    private BigDecimal price;

    @Column(name = "metodo_de_pago")
    private String paymentMethod;

    @Column(name = "precio_final")
    private BigDecimal finalPrice;

    @Column(name = "fecha_compra")
    private Date paymentDate;

    @Column(name = "cantidad")
    private Integer quantity;
}
