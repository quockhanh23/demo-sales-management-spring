package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.constant.MessageConstants;
import com.example.quanlybanhang.exeption.BadRequestException;
import com.example.quanlybanhang.models.OrderProduct;
import com.example.quanlybanhang.models.User;
import com.example.quanlybanhang.repository.OrderProductRepository;
import com.example.quanlybanhang.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderProductServiceImpl implements OrderProductService {

    private final OrderProductRepository orderProductRepository;

    @Override
    public Optional<OrderProduct> findById(Long id) {
        return orderProductRepository.findById(id);
    }

    @Override
    public void save(OrderProduct orderProduct) {
        orderProductRepository.save(orderProduct);
    }

    @Override
    public void delete(Long id) {
        orderProductRepository.deleteById(id);
    }

    @Override
    public OrderProduct checkExistOrderProduct(Long idOrder) {
        Optional<OrderProduct> optionalOrderProduct = findById(idOrder);
        if (optionalOrderProduct.isEmpty()) {
            throw new BadRequestException(MessageConstants.NOT_FOUND_ORDER);
        }
        return optionalOrderProduct.get();
    }

    @Override
    public long countAllByUser(User user) {
        return orderProductRepository.countAllByUser(user);
    }
}
