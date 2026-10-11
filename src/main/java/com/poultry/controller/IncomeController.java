package com.poultry.controller;

import com.poultry.entity.Income;
import com.poultry.repository.IncomeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/income")
@CrossOrigin(origins = "*")
public class IncomeController {

    private final IncomeRepository repository;

    public IncomeController(IncomeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Income> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Income save(@RequestBody Income income) {
        return repository.save(income);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}