package com.poultry.controller;

import com.poultry.entity.TaxSetting;
import com.poultry.repository.TaxSettingRepository;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/tax")
@CrossOrigin(origins = "*")
public class TaxController {

    private final TaxSettingRepository repository;

    public TaxController(TaxSettingRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TaxSetting> getTaxSettings() {

        List<TaxSetting> settings = repository.findAll();

        // Create a default tax setting if database is empty
        if (settings.isEmpty()) {

            TaxSetting defaultTax =
                    new TaxSetting(
                            "Configured Tax",
                            BigDecimal.ZERO
                    );

            TaxSetting saved =
                    repository.save(defaultTax);

            settings.add(saved);
        }

        return settings;
    }

    @GetMapping("/{id}")
    public TaxSetting getTaxById(
            @PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Tax setting not found"
                        )
                );
    }

    @PutMapping("/{id}")
    public TaxSetting updateTax(
            @PathVariable Long id,
            @RequestBody TaxSetting updatedTax) {

        TaxSetting existing =
                repository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Tax setting not found"
                                )
                        );

        existing.setTaxName(
                updatedTax.getTaxName()
        );

        existing.setTaxRate(
                updatedTax.getTaxRate()
        );

        return repository.save(existing);
    }
}