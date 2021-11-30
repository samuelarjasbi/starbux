package com.starbux.starbuxapi.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.starbux.starbuxapi.entity.Drinks;

import com.starbux.starbuxapi.security.services.UserDetailsImpl;
import com.starbux.starbuxapi.security.jwt.ShoppingConfig;
import com.starbux.starbuxapi.security.jwt.JwtUtils;
import com.starbux.starbuxapi.controller.Requests.ApiResponse;
import com.starbux.starbuxapi.entity.AddToCart;
import com.starbux.starbuxapi.repository.AddToCartRepository;
import com.starbux.starbuxapi.repository.ToppingsRepository;
import com.starbux.starbuxapi.repository.DrinksRepository;
import com.starbux.starbuxapi.service.Cart.CartService;


import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("api/addtocart")
public class AddtoCartController {

    @Autowired
    CartService cartService;
    @Autowired
    DrinksRepository drinksRepository;
    @Autowired
    ToppingsRepository toppingsRepository;
    @Autowired
    AddToCartRepository addToCartRepository;
    @Autowired
    JwtUtils jwtUtils;





    @PostMapping("/addProduct")
    public ResponseEntity<?> addCartwithProduct(@RequestBody HashMap<String,String> addCartRequest) {

        try {
            String keys[] = {"drinks_id","toppings_id","userId","qty"};
            if(ShoppingConfig.validationWithHashMap(keys, addCartRequest)) {

            }

            long userId =  Long.parseLong(addCartRequest.get("userId"));
            int drinks_id = Integer.parseInt(addCartRequest.get("drinks_id"));
            int toppings_id = Integer.parseInt(addCartRequest.get("toppings_id"));
            int qty = Integer.parseInt(addCartRequest.get("qty"));
            double drink_price = drinksRepository.findById(drinks_id).get().getPrice();
            double topping_price = toppingsRepository.findById(toppings_id).get().getPrice();
            double price = (drink_price + topping_price) * qty;


            List<AddToCart> obj = cartService.addCartbyUserIdAndDrinksId(drinks_id, toppings_id, userId, qty, price);
            toppingsRepository.addRecordByUserId(toppings_id);
            System.out.println(obj);
            return ResponseEntity.ok(obj);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), ""));
        }

    }

    @PutMapping("/updateQtyForCart")
    public ResponseEntity<?> updateQtyForCart(@RequestBody HashMap<String,String> addCartRequest) {

        try {
            String keys[] = {"cartId","userId","qty"};
            if(ShoppingConfig.validationWithHashMap(keys, addCartRequest)) {

            }
            long userId = Long.parseLong(addCartRequest.get("userId"));


            long cartId = Long.parseLong(addCartRequest.get("cartId"));
            int qty = Integer.parseInt(addCartRequest.get("qty"));
            List<AddToCart> obj = cartService.getCartByUserId(userId);
            System.out.println(obj.get(0).getDrinks().getName());
            double Drink_price = obj.get(0).getDrinks().getPrice();
            double toppings_price = obj.get(0).getToppings().getPrice();
            double price = (Drink_price + toppings_price) * qty;
            cartService.updateQtyByCartId(cartId, qty, price);

            return ResponseEntity.ok(obj);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), ""));
        }

    }


    @DeleteMapping("/removeProductFromCart")
    public ResponseEntity<?> removeCartwithProductId(@RequestBody HashMap<String,String> removeCartRequest) {

        try {
            String keys[] = {"userId","cartId"};
            if(ShoppingConfig.validationWithHashMap(keys, removeCartRequest)) {

            }
            long userId =  Long.parseLong(removeCartRequest.get("userId"));

            List<AddToCart> obj = cartService.removeCartByUserId(Long.parseLong(removeCartRequest.get("cartId")),userId);
            return ResponseEntity.ok(obj);

        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), ""));
        }
    }

    @PostMapping("/getCartsByUserId")
    public ResponseEntity<?> getCartsByUserId(@RequestBody HashMap<String,String> getCartRequest) {

        try {
            String keys[] = {"userId"};
            if(ShoppingConfig.validationWithHashMap(keys, getCartRequest)) {
            }
            long userId =  Long.parseLong(getCartRequest.get("userId"));

            List<AddToCart> obj = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(obj);


        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), ""));
        }
    }
}