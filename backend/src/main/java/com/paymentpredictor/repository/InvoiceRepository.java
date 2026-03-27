package com.paymentpredictor.repository;

import com.paymentpredictor.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {
  Page<Invoice> findByNameCustomerContainingIgnoreCaseOrCustNumberContainingIgnoreCaseOrInvoiceIdContainingIgnoreCase(
      String nameCustomer, String custNumber, String invoiceId, Pageable pageable);
}
