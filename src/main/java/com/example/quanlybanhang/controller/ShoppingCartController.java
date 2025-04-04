package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.dto.ProductDTO;
import com.example.quanlybanhang.dto.ShoppingCartDTO;
import com.example.quanlybanhang.exeption.InvalidException;
import com.example.quanlybanhang.models.ShoppingCart;
import com.example.quanlybanhang.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<Object> addToCart(@RequestParam Long idUser,
                                            @RequestBody ProductDTO productDTO) {
        shoppingCartService.addToCart(idUser, productDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/remove-from-cart")
    public ResponseEntity<Object> removeFromCart(@RequestParam Long idUser,
                                                 @RequestParam Long idProduct) {
        shoppingCartService.removeToCart(idUser, idProduct);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/decrease-product")
    public ResponseEntity<Object> decreaseProduct(@RequestParam Long idUser,
                                                  @RequestParam Long idProduct) {
        shoppingCartService.decreaseProduct(idUser, idProduct);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/increase-product")
    public ResponseEntity<Object> increaseProduct(@RequestParam Long idUser,
                                                  @RequestParam Long idProduct) {
        shoppingCartService.increaseProduct(idUser, idProduct);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all-in-cart")
    public ResponseEntity<Object> getAllProductInCart(@RequestParam Long idUser) {
        ShoppingCartDTO getAllProductInCart;
        try {
            getAllProductInCart = shoppingCartService.getAllProductInCart(idUser);
            return new ResponseEntity<>(getAllProductInCart, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-detail-shopping-cart")
    public ResponseEntity<Object> getDetailShoppingCartById(@RequestParam Long idShoppingCart) {
        Optional<ShoppingCart> shoppingCart = shoppingCartService.findById(idShoppingCart);
        if (shoppingCart.isEmpty()) throw new InvalidException("Không tìm thấy");
        return new ResponseEntity<>(shoppingCart.get(), HttpStatus.OK);
    }

    @GetMapping("/change-status")
    public ResponseEntity<Object> changeStatus(@RequestParam Long idOrderProduct,
                                               @RequestParam Long idUser,
                                               @RequestParam String status) {
        shoppingCartService.changeStatus(idOrderProduct, idUser, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/count-order")
    public ResponseEntity<Object> countOrder(@RequestParam Long idUser) {
        long count = shoppingCartService.countAllByUser(idUser);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
