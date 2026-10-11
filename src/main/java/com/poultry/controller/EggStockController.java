package com.poultry.controller;

import com.poultry.entity.EggSale;
import com.poultry.entity.EggStock;
import com.poultry.repository.EggSaleRepository;
import com.poultry.repository.EggStockRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stock")
@CrossOrigin(origins = "*")
public class EggStockController {

    private final EggStockRepository stockRepository;
    private final EggSaleRepository salesRepository;

    public EggStockController(
            EggStockRepository stockRepository,
            EggSaleRepository salesRepository) {
        this.stockRepository = stockRepository;
        this.salesRepository = salesRepository;
    }

    @GetMapping
    public List<EggStock> getAllStock() {
        return stockRepository.findAll();
    }

    @GetMapping("/summary")
    public Map<String, Object> getStockSummary() {

        List<EggStock> stockRows = stockRepository.findAll();
        List<EggSale> salesRows = salesRepository.findAll();

        long collected = stockRows.stream()
                .mapToLong(s ->
                        s.getEggsCollected() == null
                                ? 0 : s.getEggsCollected())
                .sum();

        long damaged = stockRows.stream()
                .mapToLong(s ->
                        s.getDamagedEggs() == null
                                ? 0 : s.getDamagedEggs())
                .sum();

        long sold = salesRows.stream()
                .mapToLong(s ->
                        s.getEggsSold() == null
                                ? 0 : s.getEggsSold())
                .sum();

        long available = collected - damaged - sold;

        Map<String, Object> result = new HashMap<>();
        result.put("totalCollected", collected);
        result.put("damagedEggs", damaged);
        result.put("eggsSold", sold);
        result.put("availableStock", available);

        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EggStock addStock(@RequestBody EggStock stock) {

        if (stock.getStockDate() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Stock date is required");
        }

        if (stock.getEggsCollected() == null
                || stock.getEggsCollected() < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Eggs collected cannot be negative");
        }

        if (stock.getDamagedEggs() == null) {
            stock.setDamagedEggs(0);
        }

        if (stock.getDamagedEggs() < 0
                || stock.getDamagedEggs() > stock.getEggsCollected()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Damaged eggs must be between zero and eggs collected");
        }

        if (stock.getEggsCollected() == 0
                && stock.getDamagedEggs() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Enter a positive quantity of eggs collected");
        }

        return stockRepository.save(stock);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        stockRepository.deleteById(id);
    }
}