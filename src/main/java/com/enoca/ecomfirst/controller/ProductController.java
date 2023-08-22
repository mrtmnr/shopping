package com.enoca.ecomfirst.controller;
import com.enoca.ecomfirst.DTOs.ProductsDTO;
import com.enoca.ecomfirst.Security.jwt.JwtUtils;
import com.enoca.ecomfirst.entity.Cart;
import com.enoca.ecomfirst.entity.Order;
import com.enoca.ecomfirst.entity.Product;
import com.enoca.ecomfirst.entity.User;
import com.enoca.ecomfirst.repository.OrderRepository;
import com.enoca.ecomfirst.repository.UserRepository;
import com.enoca.ecomfirst.service.CartService;
import com.enoca.ecomfirst.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shop")
public class ProductController {

    private ProductService productService;
    private CartService cartService;
    private UserRepository userRepository;

    private OrderRepository orderRepository;

    @Autowired
    public ProductController(ProductService productService,CartService cartService,UserRepository userRepository,OrderRepository orderRepository) {
        this.productService = productService;
        this.cartService=cartService;
        this.userRepository=userRepository;
        this.orderRepository=orderRepository;
    }


    @GetMapping("/products")
    private List<ProductsDTO>products(){

        return productService.findAll();
    }


    @GetMapping("/products/{id}")
    private Product singleProduct(@PathVariable int id){

        return productService.findById(id);
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username= userDetails.getUsername();
        User user=userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found with username: " + username));
        return user;
    };

    //add selected product to cart
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

    //give order
    @PostMapping("/cart/buy")
    public String buy(){
        User user=getCurrentUser();
        Cart cart=user.getCart();
        List<Product>products=cart.getProducts();
        //decrement stocks
        products.forEach((n)->
                {   int stock= n.getStock();
                    n.setStock(stock-1);
                }
        );
        Order order=new Order();
        order.setUser(user);
        order.setCart(cart);
        orderRepository.save(order);

        return "your order: "+ order;
    }

    @GetMapping("/cart")
    public List<ProductsDTO> displayCart(){
        User user= getCurrentUser();
        List<Product> products=user.getCart().getProducts();
        return productService.fromEntityListToDTO(products);
    }

}
