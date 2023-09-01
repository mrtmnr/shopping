package com.enoca.ecomfirst.controller;
import com.enoca.ecomfirst.DTOs.EntryDTO;
import com.enoca.ecomfirst.DTOs.PriceChange;
import com.enoca.ecomfirst.DTOs.ProductsDTO;
import com.enoca.ecomfirst.entity.*;
import com.enoca.ecomfirst.repository.OrderRepository;
import com.enoca.ecomfirst.repository.UserRepository;
import com.enoca.ecomfirst.service.CartService;
import com.enoca.ecomfirst.service.EntryService;
import com.enoca.ecomfirst.service.ProductService;
import com.enoca.ecomfirst.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/shop")
public class ShopController {

    private ProductService productService;
    private CartService cartService;
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private EntryService entryService;
    private PromotionService promotionService;

    @Autowired
    public ShopController(ProductService productService, CartService cartService, UserRepository userRepository, OrderRepository orderRepository,EntryService entryService,PromotionService promotionService) {
        this.productService = productService;
        this.cartService=cartService;
        this.userRepository=userRepository;
        this.orderRepository=orderRepository;
        this.entryService=entryService;
        this.promotionService=promotionService;
    }

    @GetMapping("/products")
    private List<ProductsDTO>products(){

        return productService.findAll();
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username= userDetails.getUsername();
        User user=userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found with username: " + username));
        return user;
    }


    @PostMapping("/addToCart/{id}")
    public String addToCart(@PathVariable int id){

        User user= getCurrentUser();
        //get his cart
        Cart cart= user.getCart();
        //get product
        Product product=productService.findById(id);

        int stock=product.getStock();
        if (stock==0){
            return "out of stock!";
        }
        List<Entry>entries=cart.getEntries();

        if(!(entries.isEmpty())){
          Entry productEntry =entries.stream().filter((n->n.getProduct().getId()==id)).findAny().orElse(null);

          if (productEntry!=null){
            int quantity=productEntry.getQuantity();
            productEntry.setQuantity(quantity+1);

           //adding cart price
           // int addCartPrice=cart.getCartPrice()+product.getPrice();
           // cart.setCartPrice(addCartPrice);

            cartService.save(cart);

            return product.getTitle()+ " added to cart! - quantity: "+productEntry.getQuantity();

          }
        }

            Entry newEntry=new Entry();
            newEntry.setProduct(product);
            newEntry.setQuantity(1);
            cart.addEntry(newEntry);

            cartService.save(cart);
            return product.getTitle()+" added to cart!";

    }
/*
    List<Entry>findDifference(){
        List<Entry>allEntries=entryService.findAll();
        List<Order>orders= orderRepository.findAll();
        List<Entry>orderEntries=new ArrayList<>();
        orders.forEach((o)->
                {
                    List<Entry>entries=o.getEntries();
                    orderEntries.addAll(entries);
                }
        );
         allEntries.removeAll(orderEntries);
         return allEntries;
    }
*/
    List<Entry>NoOrderEntry(){
        List<Entry>allEntries=entryService.findAll();
        List<Entry>withoutOrderEntry=new ArrayList<>();
        allEntries.forEach((e)->
                {
                  if(e.getOrder()==null){
                      withoutOrderEntry.add(e);
                  };
                }
        );
        return withoutOrderEntry;
    }

    @PutMapping("/changePrice/{productId}")
    public String changePrice(@PathVariable int productId,@RequestBody PriceChange priceChange){
        int chance=priceChange.getChange();
        Product product=productService.findById(productId);
        int firstPrice=product.getPrice();
        List<Entry>entries=NoOrderEntry();
        entries.forEach((entry)->
                    {   if (entry.getProduct().getId()==productId) {

                            entry.setTotalPrice(firstPrice);
                            entry.setChangeOfPrice(chance);
                            entry.setTotalPriceWithChange(firstPrice + chance);
                          }
                    }
                );
        int newPrice=firstPrice+chance;
        product.setPrice(newPrice);
        productService.save(product);
        return product.getTitle()+" price changed to "+newPrice;
    }
    private int cartPriceWithProm(Cart cart){
        List<Entry>entries=cart.getEntries();
        cart.setCartPrice(0);
        entries.forEach((entry) ->{
           int price= entry.getProduct().getPrice();
           int quantity=entry.getQuantity();
           int cartPrice=cart.getCartPrice();
           cart.setCartPrice(cartPrice+(price*quantity));
        } );
           List<Promotion>promotions=promotionService.findAll();
           Promotion previousPromotion=promotionService.findById(1);
           for (Promotion prom:promotions) {
               int cartPrice = cart.getCartPrice();
               int promLimit = prom.getCartLimit();
               int promDiscount = prom.getDiscount();
               if (cartPrice >= promLimit && promLimit >= previousPromotion.getCartLimit()) {

                   cart.setCartPrice(cartPrice - promDiscount);
                   previousPromotion = prom;
               }
           }
           return cart.getCartPrice();
    }

    //give order
    @PostMapping("/cart/buy")
    public String buy(){
        User user=getCurrentUser();
        Cart cart=user.getCart();
        Order order=new Order();
        order.setUser(user);
        List<Entry>entries=cart.getEntries();
        //decrement stocks
        entries.forEach((ent)->
                {   int stock= ent.getProduct().getStock();
                    ent.getProduct().setStock(stock-1);
                    ent.setOrder(order);
                    //default totalPrice saving when price has not changed yet
                    if (ent.getTotalPrice()==0){
                        ent.setTotalPrice(ent.getProduct().getPrice());
                    }
                }
        );

        //promotions
        int priceWithProm=cartPriceWithProm(cart);
        order.setOrderPrice(priceWithProm);

        orderRepository.save(order);
        cart.setEntries(null);
        //empty cart
        cartService.save(cart);

        return "thank you for your order! orderID: "+order.getId()+" order price: "+priceWithProm;
    }
    @GetMapping("/cart")
    public List<EntryDTO> displayCart(){
        User user= getCurrentUser();
        List<Entry> entries=user.getCart().getEntries();
        return entryService.fromEntryListToDTO(entries);
    }



}
