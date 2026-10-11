package com.poultry.controller;

import com.poultry.entity.EggSale;
import com.poultry.repository.EggSaleRepository;
import com.poultry.repository.ExpenseRepository;
import com.poultry.repository.IncomeRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final EggSaleRepository eggSaleRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    public DashboardController(
            EggSaleRepository eggSaleRepository,
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository) {

        this.eggSaleRepository = eggSaleRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {

        // Total eggs sold
        long eggsSold = eggSaleRepository.findAll()
                .stream()
                .mapToLong(sale ->
                        sale.getEggsSold() == null
                                ? 0
                                : sale.getEggsSold())
                .sum();

        // Total egg sales
        BigDecimal sales = eggSaleRepository.findAll()
                .stream()
                .map(EggSale::getSubtotal)
                .filter(value -> value != null)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        // Total tax
        BigDecimal tax = eggSaleRepository.findAll()
                .stream()
                .map(EggSale::getTaxAmount)
                .filter(value -> value != null)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        // Other income
        BigDecimal otherIncome = incomeRepository.findAll()
                .stream()
                .map(income -> income.getAmount())
                .filter(value -> value != null)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        // Expenses
        BigDecimal expenses = expenseRepository.findAll()
                .stream()
                .map(expense -> expense.getAmount())
                .filter(value -> value != null)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        // Total income
        BigDecimal totalIncome =
                sales.add(otherIncome);

        // Net profit / loss
        BigDecimal profit =
                totalIncome
                        .subtract(expenses)
                        .subtract(tax);

        Map<String, Object> result =
                new HashMap<>();

        result.put("eggsSold", eggsSold);
        result.put("sales", sales);
        result.put("otherIncome", otherIncome);
        result.put("totalIncome", totalIncome);
        result.put("expenses", expenses);
        result.put("tax", tax);
        result.put("profit", profit);

        return result;
    }
}