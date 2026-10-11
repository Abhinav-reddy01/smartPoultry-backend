package com.poultry.controller;

import com.poultry.entity.EggSale;
import com.poultry.entity.Expense;
import com.poultry.entity.Income;
import com.poultry.repository.EggSaleRepository;
import com.poultry.repository.ExpenseRepository;
import com.poultry.repository.IncomeRepository;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    private final EggSaleRepository eggSaleRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    public ReportController(
            EggSaleRepository eggSaleRepository,
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository) {

        this.eggSaleRepository = eggSaleRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
    }

    // =========================
    // DAILY REPORT
    // =========================

    @GetMapping("/daily")
    public Map<String, Object> getDailyReport(
            @RequestParam String date) {

        LocalDate reportDate =
                LocalDate.parse(date);

        List<EggSale> sales =
                eggSaleRepository.findAll()
                        .stream()
                        .filter(sale ->
                                sale.getSaleDate() != null &&
                                sale.getSaleDate()
                                        .equals(reportDate))
                        .toList();

        List<Income> incomes =
                incomeRepository.findAll()
                        .stream()
                        .filter(income ->
                                income.getIncomeDate() != null &&
                                income.getIncomeDate()
                                        .equals(reportDate))
                        .toList();

        List<Expense> expenses =
                expenseRepository.findAll()
                        .stream()
                        .filter(expense ->
                                expense.getExpenseDate() != null &&
                                expense.getExpenseDate()
                                        .equals(reportDate))
                        .toList();

        return buildReport(
                "Daily Report - " + reportDate,
                sales,
                incomes,
                expenses
        );
    }

    // =========================
    // MONTHLY REPORT
    // =========================

    @GetMapping("/monthly")
    public Map<String, Object> getMonthlyReport(
            @RequestParam String month) {

        YearMonth reportMonth =
                YearMonth.parse(month);

        List<EggSale> sales =
                eggSaleRepository.findAll()
                        .stream()
                        .filter(sale ->
                                sale.getSaleDate() != null &&
                                YearMonth.from(
                                        sale.getSaleDate()
                                ).equals(reportMonth))
                        .toList();

        List<Income> incomes =
                incomeRepository.findAll()
                        .stream()
                        .filter(income ->
                                income.getIncomeDate() != null &&
                                YearMonth.from(
                                        income.getIncomeDate()
                                ).equals(reportMonth))
                        .toList();

        List<Expense> expenses =
                expenseRepository.findAll()
                        .stream()
                        .filter(expense ->
                                expense.getExpenseDate() != null &&
                                YearMonth.from(
                                        expense.getExpenseDate()
                                ).equals(reportMonth))
                        .toList();

        return buildReport(
                "Monthly Report - " + reportMonth,
                sales,
                incomes,
                expenses
        );
    }

    // =========================
    // REPORT CALCULATION
    // =========================

    private Map<String, Object> buildReport(
            String period,
            List<EggSale> sales,
            List<Income> incomes,
            List<Expense> expenses) {

        // Eggs sold
        long eggsSold =
                sales.stream()
                        .mapToLong(sale ->
                                sale.getEggsSold() == null
                                        ? 0
                                        : sale.getEggsSold())
                        .sum();

        // Egg sales subtotal
        BigDecimal salesAmount =
                sales.stream()
                        .map(EggSale::getSubtotal)
                        .filter(value -> value != null)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        // Tax
        BigDecimal tax =
                sales.stream()
                        .map(EggSale::getTaxAmount)
                        .filter(value -> value != null)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        // Other income
        BigDecimal otherIncome =
                incomes.stream()
                        .map(Income::getAmount)
                        .filter(value -> value != null)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        // Expenses
        BigDecimal totalExpenses =
                expenses.stream()
                        .map(Expense::getAmount)
                        .filter(value -> value != null)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        // Total income
        BigDecimal totalIncome =
                salesAmount.add(otherIncome);

        // Net profit
        BigDecimal profit =
                totalIncome
                        .subtract(totalExpenses)
                        .subtract(tax);

        Map<String, Object> report =
                new HashMap<>();

        report.put("period", period);
        report.put("eggsSold", eggsSold);
        report.put("sales", salesAmount);
        report.put("otherIncome", otherIncome);
        report.put("totalIncome", totalIncome);
        report.put("expenses", totalExpenses);
        report.put("tax", tax);
        report.put("profit", profit);

        return report;
    }
}