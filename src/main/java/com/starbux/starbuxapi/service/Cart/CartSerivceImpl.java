package com.starbux.starbuxapi.service.Cart;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.starbux.starbuxapi.repository.AddToCartRepository;
import com.starbux.starbuxapi.repository.CheckoutRepository;
import com.starbux.starbuxapi.service.ToppingsService;
import com.starbux.starbuxapi.service.DrinksService;
import com.starbux.starbuxapi.entity.CheckoutCart;
import com.starbux.starbuxapi.entity.AddToCart;
import com.starbux.starbuxapi.entity.Toppings;
import com.starbux.starbuxapi.entity.Drinks;

import java.util.List;

@Service
public class CartSerivceImpl implements CartService {

    @Autowired
    AddToCartRepository addToCartRepository;
    @Autowired
    CheckoutRepository checkoutRepository;
    @Autowired
    DrinksService drinksServices;
    @Autowired
    ToppingsService toppingService;
    private static final Logger logger = LoggerFactory.getLogger(CartSerivceImpl.class);

    @Override
    public List<AddToCart> addCartbyUserIdAndDrinksId(int drinks_id,int toppings_id, long userId,int qty,double price) throws Exception {
        try {
            if(addToCartRepository.getCartByDrinksIdAnduserId(userId, drinks_id,toppings_id).isPresent()){
                throw new Exception("Drink is already exist.");
            }
            AddToCart obj = new AddToCart();
            obj.setQty(qty);
            obj.setUser_id(userId);
            Drinks Dri = drinksServices.getDrinksById(drinks_id);
            obj.setDrinks(Dri);
            Toppings Top = toppingService.getToppingsById(toppings_id);
            obj.setToppings(Top);
            obj.setPrice(price);
            addToCartRepository.save(obj);

            return this.getCartByUserId(userId);
        }catch(Exception e) {
            e.printStackTrace();
            logger.error(""+e.getMessage());
            throw new Exception(e.getMessage());
        }

    }




    @Override
    public List<AddToCart> getCartByUserId(long userId) {
        return addToCartRepository.getCartByuserId(userId);
    }

    @Override
    public List<AddToCart> removeCartByUserId(long cartId, long userId) {
        addToCartRepository.deleteCartByIdAndUserId(userId, cartId);
        return this.getCartByUserId(userId);
    }

    @Override
    public void updateQtyByCartId(long cartId, int qty,double price) throws Exception {
        addToCartRepository.updateQtyByCartId(cartId,qty,price);
    }

    @Override
    public Double checkTotalAmountAgainstCart(long userId) {
        double total_amount =addToCartRepository.getTotalAmountByUserId(userId);
        return total_amount;
    }

    @Override
    public List<CheckoutCart> getAllCheckoutByUserId(long userId) {
        return checkoutRepository.getByuserId(userId);
    }

    @Override
    public List<CheckoutCart> saveProductsForCheckout(List<CheckoutCart> tmp) throws Exception {
        try {
            long user_id = tmp.get(0).getUser_id();
            if(tmp.size() >0) {
                checkoutRepository.saveAll(tmp);
                this.removeAllCartByUserId(user_id);
                return this.getAllCheckoutByUserId(user_id);
            }
            else {
                throw  new Exception("Should not be empty");
            }
        }catch(Exception e) {
            throw new Exception("Error while checkout "+e.getMessage());
        }

    }
    @Override
    public Double discountCart(long drinks_amount,double price){
        List<AddToCart>prices = addToCartRepository.findByOrderByPriceDesc();
        if(drinks_amount > 3 && price > 12){
            if(prices.get(0).getPrice() - price > price - (price*(25.0f/100.0f))){
                double final_price = prices.get(0).getPrice() - price;
                return final_price;
            }else{
                double final_price = price - (price*(25.0f/100.0f));
                return final_price;
            }
        }else if(price > 12){
            double final_price = price - (price*(25.0f/100.0f));
            return final_price;
        }else if(drinks_amount > 3){
            double final_price = prices.get(0).getPrice() - price;
            return final_price;
        }
        return price;
    }

    @Override
    public Long cartsNumber(long userId){
        return addToCartRepository.countByUserid(userId);
    }

    @Override
    public List<AddToCart> removeAllCartByUserId(long userId) {
        addToCartRepository.deleteAllCartByUserId(userId);
        return null;
    }

}