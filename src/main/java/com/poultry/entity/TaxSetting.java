package com.poultry.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tax_settings")
public class TaxSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String taxName;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal taxRate;

    public TaxSetting() {
    }

    public TaxSetting(String taxName, BigDecimal taxRate) {
        this.taxName = taxName;
        this.taxRate = taxRate;
    }

    public Long getId() {
        return id;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
}