package com.poultry.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class RootController {

    @GetMapping("/")
    public Map<String, Object> index() {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("status", "UP");
        res.put("service", "Smart Poultry Management API");
        res.put("message", "Backend is live and running successfully on Render!");

        Map<String, String> endpoints = new LinkedHashMap<>();
        endpoints.put("login", "/api/auth/login");
        endpoints.put("register", "/api/auth/register");
        endpoints.put("dashboard", "/api/dashboard/summary");
        endpoints.put("stock_summary", "/api/stock/summary");
        endpoints.put("customers", "/api/customers");
        endpoints.put("sales", "/api/sales");
        endpoints.put("income", "/api/income");
        endpoints.put("expenses", "/api/expenses");
        endpoints.put("tax", "/api/tax");
        endpoints.put("reports", "/api/reports/daily?date=YYYY-MM-DD");

        res.put("endpoints", endpoints);
        return res;
    }
}
