package com.paymentpredictor.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateInvoiceRequest(BigDecimal totalOpenAmount, LocalDate dueInDate, String notes) {}
