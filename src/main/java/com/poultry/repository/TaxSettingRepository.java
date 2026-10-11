package com.poultry.repository;

import com.poultry.entity.TaxSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxSettingRepository
        extends JpaRepository<TaxSetting, Long> {
}