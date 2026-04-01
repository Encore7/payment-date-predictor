package com.paymentpredictor.service;

import com.paymentpredictor.dto.CreateInvoiceRequest;
import com.paymentpredictor.dto.InvoiceDto;
import com.paymentpredictor.dto.PagedInvoiceResponse;
import com.paymentpredictor.dto.PredictItemResponse;
import com.paymentpredictor.dto.PredictResponse;
import com.paymentpredictor.dto.UpdateInvoiceRequest;
import com.paymentpredictor.entity.Invoice;
import com.paymentpredictor.repository.InvoiceRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InvoiceService {

  private final InvoiceRepository invoiceRepository;
  private final PredictionService predictionService;

  public PagedInvoiceResponse listInvoices(int page, int size, String search) {
    int safePage = Math.max(page, 1) - 1;
    int safeSize = Math.min(Math.max(size, 1), 500);

    PageRequest pageable =
        PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.ASC, "invoiceId"));

    Page<Invoice> result;
    if (search == null || search.isBlank()) {
      result = invoiceRepository.findAll(pageable);
    } else {
      result =
          invoiceRepository
              .findByNameCustomerContainingIgnoreCaseOrCustNumberContainingIgnoreCaseOrInvoiceIdContainingIgnoreCase(
                  search, search, search, pageable);
    }

    List<InvoiceDto> data = result.getContent().stream().map(InvoiceMapper::toDto).toList();

    return new PagedInvoiceResponse(
        data, safePage + 1, safeSize, result.getTotalElements(), result.hasNext());
  }

  @Transactional
  public InvoiceDto createInvoice(CreateInvoiceRequest request) {
    Invoice invoice = new Invoice();
    invoice.setInvoiceId(request.invoiceId());
    invoice.setNameCustomer(request.nameCustomer());
    invoice.setCustNumber(request.custNumber());
    invoice.setTotalOpenAmount(request.totalOpenAmount());
    invoice.setDueInDate(request.dueInDate());
    invoice.setNotes(request.notes());
    invoice.setBusinessCode("U001");
    invoice.setInvoiceCurrency("USD");
    invoice.setDocumentType("RV");
    invoice.setIsOpen((byte) 1);
    invoice.setBusinessYear(request.dueInDate() != null ? request.dueInDate().getYear() : LocalDate.now().getYear());
    predictionService.applyPrediction(invoice);

    return InvoiceMapper.toDto(invoiceRepository.save(invoice));
  }

  @Transactional
  public InvoiceDto updateInvoice(String invoiceId, UpdateInvoiceRequest request) {
    Invoice invoice =
        invoiceRepository
            .findById(invoiceId)
            .orElseThrow(() -> new EntityNotFoundException("Invoice not found: " + invoiceId));

    if (request.totalOpenAmount() != null) {
      invoice.setTotalOpenAmount(request.totalOpenAmount());
    }
    if (request.dueInDate() != null) {
      invoice.setDueInDate(request.dueInDate());
    }
    if (request.notes() != null) {
      invoice.setNotes(request.notes());
    }

    predictionService.applyPrediction(invoice);
    return InvoiceMapper.toDto(invoiceRepository.save(invoice));
  }

  @Transactional
  public int deleteInvoices(List<String> invoiceIds) {
    long before = invoiceRepository.count();
    invoiceRepository.deleteAllById(invoiceIds);
    long after = invoiceRepository.count();
    return (int) Math.max(before - after, 0);
  }

  @Transactional
  public PredictResponse predictInvoices(List<String> invoiceIds) {
    List<Invoice> invoices = invoiceRepository.findAllById(invoiceIds);
    List<PredictItemResponse> items = new ArrayList<>();

    for (Invoice invoice : invoices) {
      if (invoice.getTotalOpenAmount() == null) {
        invoice.setTotalOpenAmount(BigDecimal.ZERO);
      }
      predictionService.applyPrediction(invoice);
      items.add(
          new PredictItemResponse(
              invoice.getInvoiceId(), invoice.getPredictedPaymentDate(), invoice.getPredictedAgeingBucket()));
    }

    invoiceRepository.saveAll(invoices);
    return new PredictResponse(items.size(), items);
  }
}
