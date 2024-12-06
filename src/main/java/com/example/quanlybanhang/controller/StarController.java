package com.example.quanlybanhang.controller;


import com.example.quanlybanhang.models.Star;
import com.example.quanlybanhang.service.StarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/stars")
@RequiredArgsConstructor
public class StarController {

    private final StarService starService;

    @GetMapping("/get-star")
    public ResponseEntity<Object> getAllStarByProduct(@RequestParam Long idProduct) {
        List<Star> list = starService.findAllByProductId(idProduct);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/get-star-by-user")
    public ResponseEntity<Object> getAllStarByUserAndProduct(@RequestParam Long idUser,
                                                             @RequestParam Long idProduct) {
        Star star = starService.findStarByUserAndProduct(idUser, idProduct);
        return new ResponseEntity<>(star, HttpStatus.OK);
    }

    @GetMapping("/star-level")
    public ResponseEntity<Object> getAllStarByProductAndLevel(@RequestParam Long idProduct,
                                                              @RequestParam String type) {
        List<Star> list = starService.findAllByProductIdAndType(idProduct, type);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/create-star")
    public ResponseEntity<Object> create(@RequestParam Long idProduct,
                                         @RequestParam Long idUser,
                                         @RequestParam String type) {
        Star star = starService.initStar(idUser, idProduct, type);
        starService.save(star);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
