package com.example.quanlybanhang.controller;


import com.example.quanlybanhang.dto.CommentDTO;
import com.example.quanlybanhang.exeption.InvalidException;
import com.example.quanlybanhang.models.Comment;
import com.example.quanlybanhang.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/get-comment-by-user")
    public ResponseEntity<Object> getAllCommentByUser(@RequestParam Long idUser) {
        List<Comment> list = commentService.findAllCommentByUserId(idUser);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/get-comment-by-product")
    public ResponseEntity<Object> getAllCommentByProduct(@RequestParam Long idProduct) {
        List<Comment> list = commentService.findAllCommentByProductId(idProduct);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/get-comment")
    public ResponseEntity<Object> getAllCommentByProductAndUser(@RequestParam Long idProduct,
                                                                @RequestParam Long idUer) {
        List<Comment> list = commentService.findAllCommentByProductIdAndUserId(idProduct, idUer);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createComment(@RequestBody CommentDTO commentDTO) {
        try {
            Comment comment = commentService.createComment(commentDTO);
            commentService.save(comment);
            return new ResponseEntity<>(comment, HttpStatus.CREATED);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteComment(@RequestParam Long idComment) {
        try {
            commentService.delete(idComment);
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
