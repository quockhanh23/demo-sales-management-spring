package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.common.CommonUtils;
import com.example.quanlybanhang.common.OrderProductDetailStatus;
import com.example.quanlybanhang.constant.MessageConstants;
import com.example.quanlybanhang.constant.SalesManagementConstants;
import com.example.quanlybanhang.dto.OrderProductDTO;
import com.example.quanlybanhang.dto.OrderProductDetailDTO;
import com.example.quanlybanhang.dto.ProductDTO;
import com.example.quanlybanhang.exeption.BadRequestException;
import com.example.quanlybanhang.models.OrderProduct;
import com.example.quanlybanhang.models.OrderProductDetail;
import com.example.quanlybanhang.models.Product;
import com.example.quanlybanhang.models.User;
import com.example.quanlybanhang.repository.OrderProductDetailRepository;
import com.example.quanlybanhang.repository.OrderProductRepository;
import com.example.quanlybanhang.service.OrderProductService;
import com.example.quanlybanhang.service.ProductService;
import com.example.quanlybanhang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderProductServiceImpl implements OrderProductService {

    private final UserService userService;
    private final ProductService productService;
    private final OrderProductRepository orderProductRepository;
    private final OrderProductDetailRepository orderProductDetailRepository;

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
    public long countAllByUser(Long idUser) {
        User user = userService.checkExistUser(idUser);
        List<OrderProduct> orderProductList = orderProductRepository.getAllByUser(user);
        if (CollectionUtils.isEmpty(orderProductList)) return 0;
        OrderProduct orderProduct = orderProductList.get(0);
        List<OrderProductDetail> list = orderProductDetailRepository.findAllByIdOrderProduct(orderProduct.getId());
        List<Long> productDetailList = list.stream()
                .filter(item -> OrderProductDetailStatus.IN_CART.equals(item.getStatus()))
                .map(OrderProductDetail::getIdProduct).distinct().toList();
        return productDetailList.size();
    }

    @Override
    public void addToCart(Long idUser, ProductDTO productDTO) {
        User user = userService.checkExistUser(idUser);
        Optional<Product> product = productService.findById(productDTO.getId());
        if (product.isEmpty()) throw new BadRequestException(MessageConstants.NOT_FOUND_PRODUCT);
        Optional<OrderProduct> optionalOrderProduct = orderProductRepository
                .findAllByUserAndStatus(user, SalesManagementConstants.STATUS_ORDER_PENDING);
        OrderProduct orderProduct;
        OrderProductDetail orderProductDetail = initOrderProductDetail(product.get());
        orderProductDetail.setQuantity(productDTO.getQuantity());
        orderProductDetail.setStatus(OrderProductDetailStatus.IN_CART);
        if (optionalOrderProduct.isEmpty()) {
            orderProduct = new OrderProduct();
            orderProduct.setCreatedAt(new Date());
            orderProduct.setUser(user);
            orderProduct.setStatus(SalesManagementConstants.STATUS_ORDER_PENDING);
            orderProductRepository.save(orderProduct);

            orderProductDetail.setQuantity(1);
            orderProductDetail.setPrice(product.get().getPrice());

            Optional<OrderProduct> orderProductOptional = orderProductRepository
                    .findAllByUserAndStatus(user, SalesManagementConstants.STATUS_ORDER_PENDING);
            if (orderProductOptional.isPresent()) {
                orderProductDetail.setIdOrderProduct(orderProductOptional.get().getId());
                orderProductOptional.get().setProductDetails(Collections.singletonList(orderProductDetail));
            }
        } else {
            orderProduct = optionalOrderProduct.get();
            List<OrderProductDetail> list = orderProduct.getProductDetails();
            orderProductDetail.setIdOrderProduct(orderProduct.getId());
            orderProductDetail.setQuantity(1);
            orderProductDetail.setPrice(product.get().getPrice());
            list.add(orderProductDetail);
            orderProductRepository.save(orderProduct);
        }
    }

    @Override
    public void removeToCart(Long idUser, Long idProduct) {
        User user = userService.checkExistUser(idUser);
        List<OrderProduct> orderProductList = orderProductRepository.getAllByUser(user);
        if (CollectionUtils.isEmpty(orderProductList)) return;
        OrderProduct orderProduct = orderProductList.get(0);
        List<OrderProductDetail> productDetails = orderProduct.getProductDetails();
        if (CollectionUtils.isEmpty(productDetails)) return;
        productDetails.stream()
                .filter(item -> item.getIdProduct().equals(idProduct))
                .forEach(orderProductDetail -> {
                    orderProductDetail.setStatus(OrderProductDetailStatus.OUT_CART);
                    orderProductDetail.setUpdatedAt(new Date());
                    orderProduct.setUpdatedAt(new Date());
                });
        orderProductRepository.save(orderProduct);
    }

    @Override
    public void decreaseProduct(Long idUser, Long idProduct) {

    }

    @Override
    public void increaseProduct(Long idUser, Long idProduct) {

    }

    @Override
    public OrderProductDTO getAllProductInCart(Long idUser) throws IOException {
        User user = userService.checkExistUser(idUser);
        OrderProductDTO orderProductDTO = new OrderProductDTO();
        List<OrderProduct> orderProductList = orderProductRepository.getAllByUser(user);
        if (CollectionUtils.isEmpty(orderProductList)) return orderProductDTO;
        OrderProduct orderProduct = orderProductList.get(0);
        List<OrderProductDetail> productDetails = orderProduct.getProductDetails();
        if (CollectionUtils.isEmpty(productDetails)) return orderProductDTO;
        List<OrderProductDetailDTO> productDetailDTOList = new ArrayList<>();
        List<Long> idProductList = productDetails.stream()
                .filter(item -> OrderProductDetailStatus.IN_CART.equals(item.getStatus()))
                .map(OrderProductDetail::getIdProduct).distinct().toList();
        if (CollectionUtils.isEmpty(productDetails)) return orderProductDTO;
        Set<Product> productSet = productService.findAllByIdProductIn(idProductList);
        for (Long aLong : idProductList) {
            OrderProductDetailDTO orderProductDetailDTO = new OrderProductDetailDTO();
            Product product = productSet.stream().filter(item -> aLong.equals(item.getIdProduct()))
                    .findFirst().orElse(null);
            if (Objects.isNull(product)) continue;
            List<Long> totalQuantity = productDetails.stream()
                    .filter(item -> OrderProductDetailStatus.IN_CART.equals(item.getStatus()))
                    .map(OrderProductDetail::getIdProduct).filter(aLong::equals).toList();
            orderProductDetailDTO.setIdOrderProduct(orderProduct.getId());
            orderProductDetailDTO.setTotalQuantity(totalQuantity.size());
            orderProductDetailDTO.setIdProduct(aLong);
            orderProductDetailDTO.setProductName(product.getProductName());
            orderProductDetailDTO.setPrice(product.getPrice());
            orderProductDetailDTO.setImage(CommonUtils.convertStringImageToByte(product.getImage()));
            if (StringUtils.isNotEmpty(product.getPrice())) {
                BigDecimal price = new BigDecimal(product.getPrice());
                BigDecimal total = price.multiply(BigDecimal.valueOf(totalQuantity.size()));
                orderProductDetailDTO.setTotalPrice(String.valueOf(total));
            }
            productDetailDTOList.add(orderProductDetailDTO);
        }
        orderProductDTO.setIdOrderProduct(orderProduct.getId());
        orderProductDTO.setStatus(orderProduct.getStatus());
        orderProductDTO.setOrderProductDetailDTOList(productDetailDTOList);
        return orderProductDTO;
    }

    @Override
    public void changeStatus(Long idOrderProduct, Long idUser, String status) {
        OrderProduct orderProduct = checkExistOrderProduct(idOrderProduct);
        if (!orderProduct.getUser().getId().equals(idUser)) {
            throw new BadRequestException(MessageConstants.ORDER_IS_NOT_OF_USE);
        }
        if (SalesManagementConstants.STATUS_ORDER_BOUGHT.equals(orderProduct.getStatus())) {
            throw new BadRequestException(MessageConstants.ORDER_HAS_BEEN_COMPLETED);
        }
        orderProduct.setStatus(status);
        orderProduct.setUpdatedAt(new Date());
        orderProductRepository.save(orderProduct);
    }

    private OrderProductDetail initOrderProductDetail(Product product) {
        OrderProductDetail orderProductDetail = new OrderProductDetail();
        orderProductDetail.setIdProduct(product.getIdProduct());
        orderProductDetail.setCreatedAt(new Date());
        return orderProductDetail;
    }
}
