package com.paymentpredictor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.paymentpredictor.entity.Invoice;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PredictionServiceTest {

  private final PredictionService predictionService = new PredictionService();

  @Test
  void shouldPredictUsingAmountCurrencyAndDocTypeRules() {
    Invoice invoice = new Invoice();
    invoice.setInvoiceId("1001");
    invoice.setDueInDate(LocalDate.of(2024, 1, 10));
    invoice.setTotalOpenAmount(new BigDecimal("12000"));
    invoice.setInvoiceCurrency("USD");
    invoice.setDocumentType("RV");

    predictionService.applyPrediction(invoice);

    assertEquals(LocalDate.of(2024, 1, 19), invoice.getPredictedPaymentDate());
    assertEquals("8-15 days late", invoice.getPredictedAgeingBucket());
  }

  @Test
  void shouldUseOnTimeBucketWhenNoDelay() {
    Invoice invoice = new Invoice();
    invoice.setInvoiceId("1002");
    invoice.setDueInDate(LocalDate.of(2024, 1, 10));
    invoice.setTotalOpenAmount(BigDecimal.ZERO);
    invoice.setInvoiceCurrency("INR");
    invoice.setDocumentType("AB");

    predictionService.applyPrediction(invoice);

    assertEquals(LocalDate.of(2024, 1, 12), invoice.getPredictedPaymentDate());
    assertEquals("1-7 days late", invoice.getPredictedAgeingBucket());
  }
}
