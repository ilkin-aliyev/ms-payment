package com.example.ms.payment.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageablePaymentResponse {
    private List<PaymentResponse> payments;
    private long totalElements;
    private int totalPages;
    private boolean hasNextPage;
}
