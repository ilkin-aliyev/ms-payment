package com.example.ms.payment.client;

import com.example.ms.payment.model.client.CountryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-country", url = "${client.ms-country.endpoint}")
public interface CountryClient {
    @GetMapping("/v1/countries")
    List<CountryDto> getAvailableCountries(@RequestParam String currency);
}
