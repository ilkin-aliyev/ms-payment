package com.example.ms.payment.controller;

import com.example.ms.payment.model.request.PaymentCriteria;
import com.example.ms.payment.model.request.PaymentRequest;
import com.example.ms.payment.model.response.PaymentResponse;
import com.example.ms.payment.model.response.PageablePaymentResponse;
import com.example.ms.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void savePayment(@RequestBody PaymentRequest request) {
        paymentService.savePayment(request);
    }

    @GetMapping("/{id}")
    public PaymentResponse getPayment(@PathVariable Long id) {
        return paymentService.getPayment(id);
    }

    @GetMapping
    public PageablePaymentResponse getPayments(@RequestParam int page,
                                               @RequestParam int count,
                                               PaymentCriteria paymentCriteria) {
        return paymentService.getPayments(page, count, paymentCriteria);
    }
}
