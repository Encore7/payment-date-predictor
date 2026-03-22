package com.paymentpredictor.dto;

import java.util.List;

public record PredictResponse(int updated, List<PredictItemResponse> items) {}
