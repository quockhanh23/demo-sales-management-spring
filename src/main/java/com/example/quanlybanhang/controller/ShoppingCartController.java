package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.dto.ProductDTO;
import com.example.quanlybanhang.dto.ShoppingCartDTO;
import com.example.quanlybanhang.exeption.InvalidException;
import com.example.quanlybanhang.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<Object> addToCart(@RequestParam Long idUser,
                                            @RequestBody ProductDTO productDTO) {
        try {
            shoppingCartService.addToCart(idUser, productDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/remove-from-cart")
    public ResponseEntity<Object> removeFromCart(@RequestParam Long idUser,
                                                 @RequestParam Long idProduct) {
        try {
            shoppingCartService.removeToCart(idUser, idProduct);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/decrease-product")
    public ResponseEntity<Object> decreaseProduct(@RequestParam Long idUser,
                                                  @RequestParam Long idProduct) {
        try {
            shoppingCartService.decreaseProduct(idUser, idProduct);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/increase-product")
    public ResponseEntity<Object> increaseProduct(@RequestParam Long idUser,
                                                  @RequestParam Long idProduct) {
        try {
            shoppingCartService.increaseProduct(idUser, idProduct);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all-in-cart")
    public ResponseEntity<Object> getAllProductInCart(@RequestParam Long idUser) {
        ShoppingCartDTO getAllProductInCart;
        try {
            getAllProductInCart = shoppingCartService.getAllProductInCart(idUser);
            return new ResponseEntity<>(getAllProductInCart, HttpStatus.OK);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/change-status")
    public ResponseEntity<Object> changeStatus(@RequestParam Long idOrderProduct,
                                               @RequestParam Long idUser,
                                               @RequestParam String status) {
        try {
            shoppingCartService.changeStatus(idOrderProduct, idUser, status);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count-order")
    public ResponseEntity<Object> countOrder(@RequestParam Long idUser) {
        try {
            long count = shoppingCartService.countAllByUser(idUser);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all-complete")
    public ResponseEntity<Object> getAllComplete(@RequestParam Long idUser) {
        try {
            shoppingCartService.getAllComplete(idUser);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
