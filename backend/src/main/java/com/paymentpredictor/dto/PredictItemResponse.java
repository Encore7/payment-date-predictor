package com.paymentpredictor.dto;

import java.time.LocalDate;

public record PredictItemResponse(
    String invoiceId, LocalDate predictedPaymentDate, String predictedAgeingBucket) {}
