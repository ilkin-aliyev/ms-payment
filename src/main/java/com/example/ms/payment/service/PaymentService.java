package com.example.ms.payment.service;

import com.example.ms.payment.dao.entity.PaymentEntity;
import com.example.ms.payment.dao.repository.PaymentRepository;
import com.example.ms.payment.model.constant.ExceptionConstants;
import com.example.ms.payment.model.request.PaymentCriteria;
import com.example.ms.payment.model.request.PaymentRequest;
import com.example.ms.payment.model.response.PaymentResponse;
import com.example.ms.payment.client.CountryClient;
import com.example.ms.payment.exception.NotFoundException;
import com.example.ms.payment.mapper.PaymentMapper;
import com.example.ms.payment.model.response.PageablePaymentResponse;
import com.example.ms.payment.service.specification.PaymentSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.example.ms.payment.mapper.PaymentMapper.mapEntityToResponse;
import static com.example.ms.payment.mapper.PaymentMapper.mapRequestToEntity;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CountryClient countryClient;

    public void savePayment(PaymentRequest request) {
        log.info("ActionLog.savePayment.start");
        countryClient.getAvailableCountries(request.getCurrency())
                .stream()
                .filter(country -> country.getRemainingLimit().compareTo(request.getAmount()) > 0)
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException(String.format(ExceptionConstants.COUNTRY_NOT_FOUND_MESSAGE,
                                request.getAmount(), request.getCurrency()),
                                ExceptionConstants.COUNTRY_NOT_FOUND_CODE));

        paymentRepository.save(mapRequestToEntity(request));
        log.info("ActionLog.savePayment.success");
    }

    public PaymentResponse getPayment(Long id) {
        log.info("ActionLog.getPayment.start id:{}", id);
        var payment = fetchPaymentIfExist(id);
        log.info("ActionLog.getPayment.success id:{}", id);
        return mapEntityToResponse(payment);
    }

    public PageablePaymentResponse getPayments(int page, int count, PaymentCriteria paymentCriteria) {
        log.info("ActionLog.getPayments.start");
        var pageable = PageRequest.of(page, count, Sort.by(ASC, "amount"));

        var pageablePayments =
                paymentRepository.findAll(
                        new PaymentSpecification(paymentCriteria), pageable);

        var payments = pageablePayments.getContent()
                .stream()
                .map(PaymentMapper::mapEntityToResponse)
                .toList();

        log.info("ActionLog.getPayments.success");
        return PageablePaymentResponse.builder()
                .payments(payments)
                .totalPages(pageablePayments.getTotalPages())
                .totalElements(pageablePayments.getTotalElements())
                .hasNextPage(pageablePayments.hasNext())
                .build();
    }

    public void updatePayment(Long id, PaymentRequest request) {
        log.info("ActionLog.updatePayment.start id:{}", id);
        var payment = fetchPaymentIfExist(id);
        PaymentMapper.updatePayment(request, payment);
        paymentRepository.save(payment);
        log.info("ActionLog.updatePayment.success id:{}", id);
    }

    private PaymentEntity fetchPaymentIfExist(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }
}
