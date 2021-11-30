package com.starbux.starbuxapi.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import com.starbux.starbuxapi.entity.AddToCart;

import java.util.List;
import java.util.Optional;
@Repository
public interface AddToCartRepository extends JpaRepository<AddToCart,Long> {


    //remove cart by userid and cartId,
    //getCartByuserId
    List<AddToCart> findAllByUserid(int userId);
    Long countByUserid(Long userid);
    List<AddToCart> findByOrderByPriceDesc();
    @Query("Select sum(addCart.price) FROM AddToCart addCart WHERE addCart.userid=:user_id")
    double getTotalAmountByUserId(@Param("user_id")Long user_id);
    @Query("Select addCart  FROM AddToCart addCart WHERE addCart.userid=:user_id")
    List<AddToCart> getCartByuserId(@Param("user_id")Long user_id);
    @Query("Select addCart  FROM AddToCart addCart ")
    Optional<AddToCart> getCartByuserIdtest();
    @Query("Select addCart  FROM AddToCart addCart WHERE addCart.drinks.id= :drinks_id and addCart.toppings.id=:toppings_id and addCart.userid=:user_id")
    Optional<AddToCart> getCartByDrinksIdAnduserId(@Param("user_id")Long user_id,@Param("drinks_id")Integer drinks_id,@Param("toppings_id")Integer toppings_id);
    @Modifying
    @Transactional
    @Query("DELETE  FROM AddToCart addCart WHERE addCart.id =:cart_id   and addCart.userid=:user_id")
    void deleteCartByIdAndUserId(@Param("user_id")Long user_id,@Param("cart_id")Long cart_id);
    @Modifying
    @Transactional
    @Query("DELETE  FROM AddToCart addCart WHERE   addCart.userid=:user_id")
    void deleteAllCartByUserId(@Param("user_id")Long user_id);

    @Modifying
    @Transactional
    @Query("DELETE  FROM AddToCart addCart WHERE addCart.userid=:user_id")
    void deleteAllCartUserId(@Param("user_id")Long user_id);
    @Modifying
    @Transactional
    @Query("update AddToCart addCart set addCart.qty=:qty,addCart.price=:price WHERE addCart.id=:cart_id")
    void updateQtyByCartId(@Param("cart_id")Long cart_id,@Param("qty")Integer qty,@Param("price")double price);

}
