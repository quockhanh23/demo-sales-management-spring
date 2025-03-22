package com.example.quanlybanhang.controller;


import com.example.quanlybanhang.models.ProductRating;
import com.example.quanlybanhang.service.ProductRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/stars")
@RequiredArgsConstructor
public class ProductRatingController {

    private final ProductRatingService productRatingService;

    @GetMapping("/get-star")
    public ResponseEntity<Object> getAllStarByProduct(@RequestParam Long idProduct) {
        List<ProductRating> list = productRatingService.findAllByProductId(idProduct);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/get-star-by-user")
    public ResponseEntity<Object> getAllStarByUserAndProduct(@RequestParam Long idUser,
                                                             @RequestParam Long idProduct) {
        ProductRating productRating = productRatingService.findStarByUserAndProduct(idUser, idProduct);
        return new ResponseEntity<>(productRating, HttpStatus.OK);
    }

    @GetMapping("/star-level")
    public ResponseEntity<Object> getAllStarByProductAndLevel(@RequestParam Long idProduct,
                                                              @RequestParam String type) {
        List<ProductRating> list = productRatingService.findAllByProductIdAndType(idProduct, type);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/create-star")
    public ResponseEntity<Object> create(@RequestParam Long idProduct,
                                         @RequestParam Long idUser,
                                         @RequestParam String type) {
        ProductRating productRating = productRatingService.initStar(idUser, idProduct, type);
        productRatingService.save(productRating);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
