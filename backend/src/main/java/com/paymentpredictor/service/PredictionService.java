package com.paymentpredictor.service;

import com.paymentpredictor.entity.Invoice;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {

  public void applyPrediction(Invoice invoice) {
    if (invoice.getDueInDate() == null) {
      return;
    }

    int offset = baseOffsetFromAmount(invoice.getTotalOpenAmount());

    if ("USD".equalsIgnoreCase(invoice.getInvoiceCurrency())) {
      offset -= 1;
    }

    if ("RV".equalsIgnoreCase(invoice.getDocumentType())) {
      offset += 2;
    }

    if (offset < 0) {
      offset = 0;
    }

    LocalDate predicted = invoice.getDueInDate().plusDays(offset);
    invoice.setPredictedPaymentDate(predicted);
    invoice.setPredictedAgeingBucket(toBucket(invoice.getDueInDate(), predicted));
  }

  private int baseOffsetFromAmount(BigDecimal amount) {
    if (amount == null) {
      return 5;
    }
    if (amount.compareTo(new BigDecimal("1000")) <= 0) {
      return 2;
    }
    if (amount.compareTo(new BigDecimal("10000")) <= 0) {
      return 5;
    }
    if (amount.compareTo(new BigDecimal("50000")) <= 0) {
      return 8;
    }
    return 12;
  }

  private String toBucket(LocalDate dueDate, LocalDate predictedDate) {
    long days = ChronoUnit.DAYS.between(dueDate, predictedDate);
    if (days <= 0) {
      return "On time";
    }
    if (days <= 7) {
      return "1-7 days late";
    }
    if (days <= 15) {
      return "8-15 days late";
    }
    if (days <= 30) {
      return "16-30 days late";
    }
    return "30+ days late";
  }
}
