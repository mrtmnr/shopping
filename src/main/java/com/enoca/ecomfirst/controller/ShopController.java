package com.enoca.ecomfirst.controller;
import com.enoca.ecomfirst.DTOs.EntryDTO;
import com.enoca.ecomfirst.DTOs.PriceChange;
import com.enoca.ecomfirst.DTOs.ProductsDTO;
import com.enoca.ecomfirst.entity.*;
import com.enoca.ecomfirst.repository.EntryRepository;
import com.enoca.ecomfirst.repository.OrderRepository;
import com.enoca.ecomfirst.repository.UserRepository;
import com.enoca.ecomfirst.service.CartService;
import com.enoca.ecomfirst.service.EntryService;
import com.enoca.ecomfirst.service.ProductService;
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

    @Autowired
    public ShopController(ProductService productService, CartService cartService, UserRepository userRepository, OrderRepository orderRepository,EntryService entryService) {
        this.productService = productService;
        this.cartService=cartService;
        this.userRepository=userRepository;
        this.orderRepository=orderRepository;
        this.entryService=entryService;
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
    };


    //add selected product to cart
    /*
    @PostMapping("/addToCart/{id}")
    public String addToCard(@PathVariable int id){


        User user= getCurrentUser();
        //get his cart
        Cart theCart= user.getCart();
        //get product
        Product product=productService.findById(id);
        int stock=product.getStock();
        if (stock==0){
            return "out of stock!";
        }
        theCart.addProduct(product);
        cartService.save(theCart);

        return "added: "+product;
    }

*/


    @PostMapping("/addToCart/{id}")
    public String addToCard(@PathVariable int id){

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

                cartService.save(cart);

                return "added to cart! - quantity: "+productEntry.getQuantity();

            }
        }

        Entry newEntry=new Entry();
        newEntry.setProduct(product);
        newEntry.setQuantity(1);
        cart.addEntry(newEntry);

        cartService.save(cart);
        return "added to cart! ";

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
                    entry.setDiscountPrice(chance);
                    entry.setTotalPriceWithDiscount(firstPrice + chance);
                }
                }
        );
        int newPrice=firstPrice+chance;
        product.setPrice(newPrice);
        productService.save(product);
        return "price succesfully changed! "+firstPrice+" to "+newPrice;
    }

    //give order
    @PostMapping("/cart/buy")
    public String buy(){
        User user=getCurrentUser();
        Cart cart=user.getCart();
        List<Entry>entries=cart.getEntries();
        //decrement stocks
        entries.forEach((ent)->
                {   int stock= ent.getProduct().getStock();
                    ent.getProduct().setStock(stock-1);
                }
        );
        Order order=new Order();
        order.setUser(user);
        // order.setCart(cart);
        orderRepository.save(order);

        return "thank you for your order! orderID: "+order.getId() ;
    }

    @GetMapping("/cart")
    public List<EntryDTO> displayCart(){
        User user= getCurrentUser();
        List<Entry> entries=user.getCart().getEntries();
        return entryService.fromEntryListToDTO(entries);
    }


}
