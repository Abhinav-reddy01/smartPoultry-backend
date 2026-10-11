package com.poultry.repository;

import com.poultry.entity.EggSale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EggSaleRepository extends JpaRepository<EggSale, Long> {

    List<EggSale> findBySaleDate(LocalDate saleDate);

}