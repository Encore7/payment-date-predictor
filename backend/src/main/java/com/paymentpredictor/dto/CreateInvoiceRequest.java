package com.paymentpredictor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateInvoiceRequest(
    @NotBlank String nameCustomer,
    @NotBlank String custNumber,
    @NotBlank String invoiceId,
    @NotNull BigDecimal totalOpenAmount,
    @NotNull LocalDate dueInDate,
    String notes) {}
