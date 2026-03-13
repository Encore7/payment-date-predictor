package com.paymentpredictor.controller;

import com.paymentpredictor.dto.CreateInvoiceRequest;
import com.paymentpredictor.dto.DeleteInvoicesRequest;
import com.paymentpredictor.dto.InvoiceDto;
import com.paymentpredictor.dto.MessageResponse;
import com.paymentpredictor.dto.PagedInvoiceResponse;
import com.paymentpredictor.dto.PredictRequest;
import com.paymentpredictor.dto.PredictResponse;
import com.paymentpredictor.dto.UpdateInvoiceRequest;
import com.paymentpredictor.service.InvoiceService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InvoiceController {

  private final InvoiceService invoiceService;

  @GetMapping("/health")
  public Map<String, String> health() {
    return Map.of("status", "ok");
  }

  @GetMapping("/invoices")
  public PagedInvoiceResponse getInvoices(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "50") int size,
      @RequestParam(required = false) String search) {
    return invoiceService.listInvoices(page, size, search);
  }

  @PostMapping("/invoices")
  public InvoiceDto createInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
    return invoiceService.createInvoice(request);
  }

  @PutMapping("/invoices/{invoiceId}")
  public InvoiceDto updateInvoice(
      @PathVariable String invoiceId, @RequestBody UpdateInvoiceRequest request) {
    return invoiceService.updateInvoice(invoiceId, request);
  }

  @DeleteMapping("/invoices")
  public MessageResponse deleteInvoices(@Valid @RequestBody DeleteInvoicesRequest request) {
    int deleted = invoiceService.deleteInvoices(request.invoiceIds());
    return new MessageResponse("Deleted " + deleted + " invoices");
  }

  @PostMapping("/invoices/predict")
  public PredictResponse predictInvoices(@Valid @RequestBody PredictRequest request) {
    return invoiceService.predictInvoices(request.invoiceIds());
  }
}
