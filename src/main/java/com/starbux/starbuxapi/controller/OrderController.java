package com.starbux.starbuxapi.controller;

import com.starbux.starbuxapi.repository.AddToCartRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import com.starbux.starbuxapi.security.services.UserDetailsImpl;
import com.starbux.starbuxapi.service.Report.ReportService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.starbux.starbuxapi.controller.Requests.ApiResponse;
import com.starbux.starbuxapi.security.jwt.ShoppingConfig;
import com.starbux.starbuxapi.service.Cart.CartService;
import com.starbux.starbuxapi.entity.CheckoutCart;
import com.starbux.starbuxapi.entity.AddToCart;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


@RestController
@RequestMapping("api/order")
public class OrderController {
    @Autowired
    CartService cartService;

    @Autowired
    ReportService reportService;

    @Autowired
    AddToCartRepository addToCartRepository;

    @PostMapping("/checkout_order")
    public ResponseEntity<?> checkout_order(@RequestBody HashMap<String,String> addCartRequest) {
        try {
            String keys[] = {"userId","deliveryAddress"};

            if(ShoppingConfig.validationWithHashMap(keys, addCartRequest)) {}

            long user_Id = Long.parseLong(addCartRequest.get("userId"));


            double total_amt = cartService.checkTotalAmountAgainstCart(user_Id);
            double price = cartService.discountCart(cartService.cartsNumber(user_Id),total_amt);
            List<AddToCart> cartItems = cartService.getCartByUserId(user_Id);
            List<CheckoutCart> tmp = new ArrayList<CheckoutCart>();


            for(AddToCart addCart : cartItems) {
                String orderId = ""+getOrderId();
                CheckoutCart cart = new CheckoutCart();
                cart.setPrice(price);
                cart.setUser_id(user_Id);
                cart.setOrder_id(orderId);
                cart.setDrinks(addCart.getDrinks());
                cart.setToppings(addCart.getToppings());
                cart.setQty(addCart.getQty());
                cart.setDelivery_address(addCartRequest.get("deliveryAddress"));
                tmp.add(cart);
            }
            cartService.saveProductsForCheckout(tmp);
            if(reportService.existsByUser_Id(user_Id)){
                reportService.RecordOrderByUserId(user_Id);
            }else{
                reportService.addRecordByUserId(user_Id);
            }




            return ResponseEntity.ok(new ApiResponse("Order placed successfully", ""));
        }catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), ""));
        }
    }
    public int getOrderId() {
        Random r = new Random( System.currentTimeMillis() );
        return 10000 + r.nextInt(20000);
    }




    @PostMapping("getOrdersByUserId")
    public ResponseEntity<?> getOrdersByUserId(@RequestBody HashMap<String,String> ordersRequest) {
        try {
            String keys[] = {"userId"};
            return ResponseEntity.ok(new ApiResponse("Order placed successfully", ""));
        }catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), ""));
        }

    }

    @DeleteMapping("deleteOrderById/{id}")
    @RolesAllowed("ADMIN")
    public void deleteOrderById(@PathVariable long id){
        addToCartRepository.deleteAllCartByUserId(id);

    }
}