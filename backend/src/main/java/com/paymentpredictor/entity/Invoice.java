package com.paymentpredictor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invoices")
@Getter
@Setter
public class Invoice {

  @Id
  @Column(name = "invoice_id", nullable = false, length = 32)
  private String invoiceId;

  @Column(name = "business_code", length = 20)
  private String businessCode;

  @Column(name = "cust_number", length = 32)
  private String custNumber;

  @Column(name = "name_customer")
  private String nameCustomer;

  @Column(name = "clear_date")
  private LocalDate clearDate;

  @Column(name = "business_year")
  private Integer businessYear;

  @Column(name = "doc_id", length = 32)
  private String docId;

  @Column(name = "posting_date")
  private LocalDate postingDate;

  @Column(name = "document_create_date")
  private LocalDate documentCreateDate;

  @Column(name = "due_in_date")
  private LocalDate dueInDate;

  @Column(name = "invoice_currency", length = 10)
  private String invoiceCurrency;

  @Column(name = "document_type", length = 10)
  private String documentType;

  @Column(name = "posting_id")
  private Integer postingId;

  @Column(name = "area_business", length = 100)
  private String areaBusiness;

  @Column(name = "total_open_amount", precision = 15, scale = 2)
  private BigDecimal totalOpenAmount;

  @Column(name = "baseline_create_date")
  private LocalDate baselineCreateDate;

  @Column(name = "customer_payment_terms", length = 30)
  private String customerPaymentTerms;

  @Column(name = "is_open")
  private Byte isOpen;

  @Column(name = "notes")
  private String notes;

  @Column(name = "predicted_payment_date")
  private LocalDate predictedPaymentDate;

  @Column(name = "predicted_ageing_bucket", length = 50)
  private String predictedAgeingBucket;
}
