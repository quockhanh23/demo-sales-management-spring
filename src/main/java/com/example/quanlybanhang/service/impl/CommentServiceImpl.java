package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.dto.CommentDTO;
import com.example.quanlybanhang.exeption.InvalidException;
import com.example.quanlybanhang.models.Comment;
import com.example.quanlybanhang.models.Product;
import com.example.quanlybanhang.models.User;
import com.example.quanlybanhang.repository.CommentRepository;
import com.example.quanlybanhang.service.CommentService;
import com.example.quanlybanhang.service.ProductService;
import com.example.quanlybanhang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ProductService productService;

    @Override
    public List<Comment> findAllCommentByUserId(Long idUser) {
        List<Comment> commentList = commentRepository.findCommentByUserId(idUser);
        if (CollectionUtils.isEmpty(commentList)) commentList = new ArrayList<>();
        return commentList;
    }

    @Override
    public List<Comment> findAllCommentByProductId(Long idProduct) {
        List<Comment> commentList = commentRepository.findCommentByProductId(idProduct);
        if (CollectionUtils.isEmpty(commentList)) commentList = new ArrayList<>();
        return commentList;
    }

    @Override
    public List<Comment> findAllCommentByProductIdAndUserId(Long idProduct, Long idUser) {
        List<Comment> commentList = commentRepository.findAllCommentByProductIdAndUserId(idProduct, idUser);
        if (CollectionUtils.isEmpty(commentList)) commentList = new ArrayList<>();
        return commentList;
    }

    @Override
    public void save(Comment comment) {
        if (Objects.isNull(comment)) return;
        commentRepository.save(comment);
    }

    @Override
    public void delete(Long id) {
        if (null == id) {
            throw new InvalidException("id null");
        }
        commentRepository.deleteById(id);
    }

    @Override
    public Comment validateCommentAndInit(CommentDTO commentDTO) {
        if (null == commentDTO.getIdProduct() || null == commentDTO.getIdUser()) {
            throw new InvalidException("không có id người dùng hoặc id sản phẩm");
        }
        if (StringUtils.isEmpty(commentDTO.getContent())) {
            throw new InvalidException("Không có nội dung");
        }
        User user = userService.checkExistUser(commentDTO.getIdUser());
        Optional<Product> productOptional = productService.findById(commentDTO.getIdProduct());
        if (productOptional.isEmpty()) {
            throw new InvalidException("không tìm thấy người dùng hoặc sản phẩm");
        }
        Comment comment = new Comment();
        comment.setCreatedAt(new Date());
        comment.setContent(commentDTO.getContent());
        comment.setProduct(productOptional.get());
        comment.setUser(user);
        return comment;
    }
}
