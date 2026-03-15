package com.paymentpredictor.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record DeleteInvoicesRequest(@NotEmpty List<String> invoiceIds) {}
