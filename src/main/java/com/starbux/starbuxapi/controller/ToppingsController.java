package com.starbux.starbuxapi.controller;

import com.starbux.starbuxapi.entity.Toppings;
import com.starbux.starbuxapi.service.ToppingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ToppingsController {

    @Autowired
    private ToppingsService service;

    @PostMapping("/add/topping")
    @RolesAllowed({"ADMIN","MODERATOR"})
    public Toppings addTopping(@RequestBody Toppings toppings) {
        return service.saveTopping(toppings);
    }

    @PostMapping("/add/toppings")
    @RolesAllowed({"ADMIN","MODERATOR"})
    public List<Toppings> addToppings(@RequestBody List<Toppings> Toppings) {
        return service.saveToppings(Toppings);
    }

    @GetMapping("/toppings")
    public List<Toppings> findAllToppings() {
        return service.getToppings();
    }

    @GetMapping("/toppingsById/{id}")
    public Toppings findToppingById(@PathVariable int id) {
        return service.getToppingsById(id);
    }

    @GetMapping("/topping/{name}")
    public Toppings findToppingsByName(@PathVariable String name) {
        return service.getToppingsByName(name);
    }

    @PutMapping("/update/topping")
    @RolesAllowed({"ADMIN","MODERATOR"})
    public Toppings updateTopping(@RequestBody Toppings toppings) {
        return service.updateToppings(toppings);
    }

    @DeleteMapping("/delete/topping/{id}")
    @RolesAllowed("ADMIN")
    public String deleteTopping(@PathVariable int id) {
        return service.deleteToppings(id);
    }
}
