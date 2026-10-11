package com.poultry.repository;

import com.poultry.entity.EggStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EggStockRepository
        extends JpaRepository<EggStock, Long> {
}