package com.paymentpredictor.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InvoiceDto {
  String businessCode;
  String custNumber;
  String nameCustomer;
  LocalDate clearDate;
  Integer businessYear;
  String docId;
  LocalDate postingDate;
  LocalDate documentCreateDate;
  LocalDate dueInDate;
  String invoiceCurrency;
  String documentType;
  Integer postingId;
  String areaBusiness;
  BigDecimal totalOpenAmount;
  LocalDate baselineCreateDate;
  String customerPaymentTerms;
  String invoiceId;
  Integer isOpen;
  String notes;
  LocalDate predictedPaymentDate;
  String predictedAgeingBucket;
}
