package com.paymentpredictor.service;

import com.paymentpredictor.dto.InvoiceDto;
import com.paymentpredictor.entity.Invoice;

public final class InvoiceMapper {
  private InvoiceMapper() {}

  public static InvoiceDto toDto(Invoice invoice) {
    return InvoiceDto.builder()
        .businessCode(invoice.getBusinessCode())
        .custNumber(invoice.getCustNumber())
        .nameCustomer(invoice.getNameCustomer())
        .clearDate(invoice.getClearDate())
        .businessYear(invoice.getBusinessYear())
        .docId(invoice.getDocId())
        .postingDate(invoice.getPostingDate())
        .documentCreateDate(invoice.getDocumentCreateDate())
        .dueInDate(invoice.getDueInDate())
        .invoiceCurrency(invoice.getInvoiceCurrency())
        .documentType(invoice.getDocumentType())
        .postingId(invoice.getPostingId())
        .areaBusiness(invoice.getAreaBusiness())
        .totalOpenAmount(invoice.getTotalOpenAmount())
        .baselineCreateDate(invoice.getBaselineCreateDate())
        .customerPaymentTerms(invoice.getCustomerPaymentTerms())
        .invoiceId(invoice.getInvoiceId())
        .isOpen(invoice.getIsOpen() == null ? null : invoice.getIsOpen().intValue())
        .notes(invoice.getNotes())
        .predictedPaymentDate(invoice.getPredictedPaymentDate())
        .predictedAgeingBucket(invoice.getPredictedAgeingBucket())
        .build();
  }
}
