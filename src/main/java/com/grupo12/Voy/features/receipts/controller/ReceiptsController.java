package com.grupo12.Voy.features.receipts.controller;

import com.grupo12.Voy.features.receipts.DTO.ReceiptResponseDTO;
import com.grupo12.Voy.features.receipts.service.IReceiptService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/receipts")
public class ReceiptsController {
    private final IReceiptService receiptsService;


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<List<ReceiptResponseDTO>> searchAdmin(
            @RequestParam(required = false) UUID externalId,
            @RequestParam(required = false) UUID userExtId,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) BigDecimal minFinalPrice,
            @RequestParam(required = false) BigDecimal maxFinalPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Integer minQuantity,
            @RequestParam(required = false) Integer maxQuantity) {

        return ResponseEntity.ok(receiptsService.getAllAdmin(
                externalId, userExtId, paymentMethod,
                minPrice, maxPrice,
                minFinalPrice, maxFinalPrice,
                from, to,
                minQuantity, maxQuantity));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<List<ReceiptResponseDTO>> searchUser(
            @RequestParam(required = false) UUID externalId,
            @AuthenticationPrincipal(expression = "usuario.externalId") UUID userExtId,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) BigDecimal minFinalPrice,
            @RequestParam(required = false) BigDecimal maxFinalPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Integer minQuantity,
            @RequestParam(required = false) Integer maxQuantity) {

        return ResponseEntity.ok(receiptsService.getAll(
                externalId, userExtId, paymentMethod,
                minPrice, maxPrice,
                minFinalPrice, maxFinalPrice,
                from, to,
                minQuantity, maxQuantity));
    }

    @DeleteMapping("/{receiptExtId}/{userExtId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ReceiptResponseDTO> deleteReceipt(@PathVariable UUID receiptExtId,
                                                     @PathVariable UUID userExtId){
        receiptsService.deleteReceipt(receiptExtId,userExtId);
        return ResponseEntity.noContent().build();
    }

}
