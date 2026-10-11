package com.poultry.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "egg_stock")
public class EggStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate stockDate;

    @Column(nullable = false)
    private Integer eggsCollected;

    @Column(nullable = false)
    private Integer damagedEggs = 0;

    private String notes;

    public EggStock() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getStockDate() {
        return stockDate;
    }

    public void setStockDate(LocalDate stockDate) {
        this.stockDate = stockDate;
    }

    public Integer getEggsCollected() {
        return eggsCollected;
    }

    public void setEggsCollected(Integer eggsCollected) {
        this.eggsCollected = eggsCollected;
    }

    public Integer getDamagedEggs() {
        return damagedEggs;
    }

    public void setDamagedEggs(Integer damagedEggs) {
        this.damagedEggs = damagedEggs;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}