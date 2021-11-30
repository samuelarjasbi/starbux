package com.starbux.starbuxapi.service.Cart;


import org.springframework.stereotype.Service;

import com.starbux.starbuxapi.entity.CheckoutCart;
import com.starbux.starbuxapi.entity.AddToCart;

import java.util.List;

@Service
public interface CartService {

    List<AddToCart> addCartbyUserIdAndDrinksId(int drinks_id,int toppings_id,long userId,int qty,double price) throws Exception;
    void updateQtyByCartId(long cartId,int qty,double price) throws Exception;
    List<AddToCart> getCartByUserId(long userId);
    List<AddToCart> removeCartByUserId(long cartId,long userId);
    List<AddToCart> removeAllCartByUserId(long userId);
    Double checkTotalAmountAgainstCart(long userId);
    Double discountCart(long drinks_amount,double price);
    List<CheckoutCart> getAllCheckoutByUserId(long userId);
    List<CheckoutCart> saveProductsForCheckout(List<CheckoutCart> tmp)  throws Exception;
    Long cartsNumber(long user_id);



    //CheckOutCart
}