package com.poultry.service;

import com.poultry.entity.EggSale;
import com.poultry.repository.EggSaleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class EggSaleService {

    private final EggSaleRepository repository;

    public EggSaleService(EggSaleRepository repository) {
        this.repository = repository;
    }

    public EggSale save(EggSale sale) {
        BigDecimal pricePerEgg = sale.getPricePerEgg() != null ? sale.getPricePerEgg() : BigDecimal.ZERO;
        int eggsSold = sale.getEggsSold() != null ? sale.getEggsSold() : 0;
        BigDecimal taxRate = sale.getTaxRate() != null ? sale.getTaxRate() : BigDecimal.ZERO;

        BigDecimal subtotal = pricePerEgg.multiply(BigDecimal.valueOf(eggsSold));

        BigDecimal taxAmount = subtotal
                .multiply(taxRate)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal total = subtotal.add(taxAmount);

        sale.setSubtotal(subtotal);
        sale.setTaxAmount(taxAmount);
        sale.setTotalAmount(total);

        if (sale.getPaymentStatus() == null || sale.getPaymentStatus().isBlank()) {
            sale.setPaymentStatus("PENDING");
        }

        return repository.save(sale);
    }

    public List<EggSale> getAll() {
        return repository.findAll();
    }

    public EggSale getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Egg sale not found with id: " + id));
    }

    public EggSale update(Long id, EggSale updateData) {
        EggSale existing = getById(id);
        existing.setCustomerId(updateData.getCustomerId());
        existing.setSaleDate(updateData.getSaleDate());
        existing.setEggsSold(updateData.getEggsSold());
        existing.setPricePerEgg(updateData.getPricePerEgg());
        existing.setTaxRate(updateData.getTaxRate());
        existing.setPaymentStatus(updateData.getPaymentStatus());
        return save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}