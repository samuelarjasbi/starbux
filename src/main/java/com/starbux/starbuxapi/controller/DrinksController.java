package com.starbux.starbuxapi.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.starbux.starbuxapi.service.DrinksService;
import com.starbux.starbuxapi.entity.Drinks;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class DrinksController {

    @Autowired
    private DrinksService service;
    private String response;

    @PostMapping("/add/drink")
    @RolesAllowed({"ADMIN","MODERATOR"})
    public Drinks addDrink(@RequestBody Drinks drinks) {

        return service.saveDrink(drinks);
    }

    @PostMapping("/add/drinks")
    @RolesAllowed({"ADMIN","MODERATOR"})
    public List<Drinks> addDrinks(@RequestBody List<Drinks> Drinks) {
        return service.saveDrinks(Drinks);
    }

    @GetMapping("/Drinks")
    public List<Drinks> findAllProducts() {
        return service.getDrinks();
    }

    @RequestMapping(value = "/DrinksById/{id}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public Drinks findDrinkById(@PathVariable int id) {
        return service.getDrinksById(id);
    }

    @GetMapping("/Drinks/{name}")
    public Drinks findDrinkByName(@PathVariable String name) {
        return service.getDrinksByName(name);
    }

    @PutMapping("/update/drink")
    @RolesAllowed({"ADMIN","MODERATOR"})
    public Drinks updateDrink(@RequestBody Drinks drinks) {
        return service.updateDrinks(drinks);
    }

    @DeleteMapping("/delete/Drink/{id}")
    @RolesAllowed("ADMIN")
    public String deleteDrink(@PathVariable int id) {
        return service.deleteDrinks(id);
    }
}
