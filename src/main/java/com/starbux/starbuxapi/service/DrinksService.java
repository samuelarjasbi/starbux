package com.starbux.starbuxapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starbux.starbuxapi.repository.DrinksRepository;
import com.starbux.starbuxapi.entity.Drinks;

import java.util.List;


@Service
public class DrinksService {

    @Autowired
    private DrinksRepository repository;

    public Drinks saveDrink(Drinks drinks) {
        return repository.save(drinks);
    }

    public List<Drinks> saveDrinks(List<Drinks> drinks) {
        return repository.saveAll(drinks);
    }

    public List<Drinks> getDrinks() {
        return repository.findAll();
    }

    public Drinks getDrinksById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Drinks getDrinksByName(String name) {
        return repository.findByName(name);
    }

    public String deleteDrinks(int id) {
        repository.deleteById(id);
        return "Drink removed !! " + id;
    }

    public Drinks updateDrinks(Drinks drinks) {
        Drinks existingDrinks = repository.findById(drinks.getId()).orElse(null);
        existingDrinks.setName(drinks.getName());
        existingDrinks.setQuantity(drinks.getQuantity());
        existingDrinks.setPrice(drinks.getPrice());
        return repository.save(existingDrinks);
    }

}
