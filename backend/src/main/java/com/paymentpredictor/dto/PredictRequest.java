package com.paymentpredictor.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record PredictRequest(@NotEmpty List<String> invoiceIds) {}
