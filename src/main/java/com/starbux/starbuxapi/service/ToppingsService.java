package com.starbux.starbuxapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starbux.starbuxapi.repository.ToppingsRepository;
import com.starbux.starbuxapi.entity.Toppings;

import java.util.List;


@Service
public class ToppingsService {

    @Autowired
    private ToppingsRepository repository;

    public Toppings saveTopping(Toppings toppings) {
        return repository.save(toppings);
    }

    public List<Toppings> saveToppings(List<Toppings> toppings) {
        return repository.saveAll(toppings);
    }

    public List<Toppings> getToppings() {
        return repository.findAll();
    }

    public List<Toppings> ToppingsReport() {
        return repository.findAllByOrderByOrdersDesc();
    }

    public Toppings getToppingsById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Toppings getToppingsByName(String name) {
        return repository.findByName(name);
    }

    public void IncreaseToppingRecord(int id){
        repository.addRecordByUserId(id);
    }

    public String deleteToppings(int id) {
        repository.deleteById(id);
        return "Topping removed !! " + id;
    }

    public Toppings updateToppings(Toppings toppings) {
        Toppings existingToppings = repository.findById(toppings.getId()).orElse(null);
        existingToppings.setName(toppings.getName());
        existingToppings.setQuantity(toppings.getQuantity());
        existingToppings.setPrice(toppings.getPrice());
        return repository.save(existingToppings);
    }

}
