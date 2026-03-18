package com.paymentpredictor.dto;

import java.util.List;

public record PagedInvoiceResponse(
    List<InvoiceDto> data, int page, int size, long total, boolean hasNext) {}
