package com.grupo12.Voy.features.receipts.controller;

import com.grupo12.Voy.features.receipts.DTO.ReceiptRequestDTO;
import com.grupo12.Voy.features.receipts.DTO.ReceiptResponseDTO;
import com.grupo12.Voy.features.receipts.service.IReceiptService;
import com.grupo12.Voy.features.tickets.models.DTO.TicketRequestDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/voy/receipts")
public class ReceiptsController {
    private final IReceiptService receiptsService;


    @GetMapping
    ResponseEntity<List<ReceiptResponseDTO>> getAll(){
        return ResponseEntity.ok(receiptsService.getAll());
    }

    @GetMapping("/{receiptExtId}")
    ResponseEntity<ReceiptResponseDTO> getByExternalId(@PathVariable UUID receiptExtId){
        return ResponseEntity.ok(receiptsService.getByExternalId(receiptExtId));
    }

    @GetMapping("/{userExtId}")
    ResponseEntity<List<ReceiptResponseDTO>> getByUser(@PathVariable UUID userExtId){
        return ResponseEntity.ok(receiptsService.getByUser(userExtId));
    }

    @GetMapping("/filter/{paymentMethod}")
    ResponseEntity<List<ReceiptResponseDTO>> getByPaymentMethod(@PathVariable String paymentMethod){
        return ResponseEntity.ok(receiptsService.getByPaymentMethod(paymentMethod));
    }

    @PostMapping
    ResponseEntity<ReceiptResponseDTO> createReceipt(@RequestBody @Valid ReceiptRequestDTO request){
        return ResponseEntity.ok(receiptsService.createReceipt(request));
    }

    @DeleteMapping("/{receiptExtId}/{userExtId}")
    ResponseEntity<ReceiptResponseDTO> deleteReceipt(@PathVariable UUID receiptExtId,
                                                     @PathVariable UUID userExtId){
        receiptsService.deleteReceipt(receiptExtId,userExtId);
        return ResponseEntity.noContent().build();
    }

}
