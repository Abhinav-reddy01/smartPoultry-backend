package com.poultry.controller;

import com.poultry.entity.EggSale;
import com.poultry.service.EggSaleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class EggSaleController {

    private final EggSaleService service;

    public EggSaleController(EggSaleService service) {
        this.service = service;
    }

    @GetMapping
    public List<EggSale> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public EggSale getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public EggSale save(@RequestBody EggSale sale) {
        return service.save(sale);
    }

    @PutMapping("/{id}")
    public EggSale update(@PathVariable Long id, @RequestBody EggSale sale) {
        return service.update(id, sale);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}