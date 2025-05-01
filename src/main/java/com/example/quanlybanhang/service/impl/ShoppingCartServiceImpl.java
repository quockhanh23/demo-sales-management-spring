package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.common.CommonUtils;
import com.example.quanlybanhang.common.ShoppingCartDetailStatus;
import com.example.quanlybanhang.constant.MessageConstants;
import com.example.quanlybanhang.constant.SalesManagementConstants;
import com.example.quanlybanhang.dto.ProductDTO;
import com.example.quanlybanhang.dto.ShoppingCartDTO;
import com.example.quanlybanhang.dto.ShoppingCartDetailDTO;
import com.example.quanlybanhang.exeption.InvalidException;
import com.example.quanlybanhang.models.Product;
import com.example.quanlybanhang.models.ShoppingCart;
import com.example.quanlybanhang.models.ShoppingCartDetail;
import com.example.quanlybanhang.models.User;
import com.example.quanlybanhang.repository.ShoppingCartDetailRepository;
import com.example.quanlybanhang.repository.ShoppingCartRepository;
import com.example.quanlybanhang.service.ProductService;
import com.example.quanlybanhang.service.ShoppingCartService;
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
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final UserService userService;
    private final ProductService productService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartDetailRepository shoppingCartDetailRepository;

    @Override
    public Optional<ShoppingCart> findById(Long id) {
        return shoppingCartRepository.findById(id);
    }

    @Override
    public ShoppingCart checkExistOrderProduct(Long idOrder) {
        Optional<ShoppingCart> optionalOrderProduct = findById(idOrder);
        if (optionalOrderProduct.isEmpty()) {
            throw new InvalidException(MessageConstants.NOT_FOUND_ORDER);
        }
        return optionalOrderProduct.get();
    }

    @Override
    public long countAllByUser(Long idUser) {
        User user = userService.checkExistUser(idUser);
        List<ShoppingCart> shoppingCartList = shoppingCartRepository
                .getAllByIdUserAndStatus(user.getId(), SalesManagementConstants.STATUS_ORDER_PENDING);
        if (CollectionUtils.isEmpty(shoppingCartList)) return 0;
        ShoppingCart shoppingCart = shoppingCartList.get(0);
        List<Long> productDetailList = shoppingCart.getProductDetails().stream()
                .filter(item -> ShoppingCartDetailStatus.IN_CART.equals(item.getStatus()))
                .map(ShoppingCartDetail::getIdProduct).distinct().toList();
        return productDetailList.size();
    }

    @Override
    public void addToCart(Long idUser, ProductDTO productDTO) {
        User user = userService.checkExistUser(idUser);
        Optional<Product> product = productService.findById(productDTO.getId());
        if (product.isEmpty()) throw new InvalidException(MessageConstants.NOT_FOUND_PRODUCT);
        Optional<ShoppingCart> optionalOrderProduct = shoppingCartRepository
                .findAllByIdUserAndStatus(user.getId(), SalesManagementConstants.STATUS_ORDER_PENDING);
        if (optionalOrderProduct.isEmpty()) {
            ShoppingCartDetail shoppingCartDetail = initOrderProductDetail(product.get());
            shoppingCartDetail.setStatus(ShoppingCartDetailStatus.IN_CART);
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setCreatedAt(new Date());
            shoppingCart.setIdUser(user.getId());
            shoppingCart.setStatus(SalesManagementConstants.STATUS_ORDER_PENDING);
            shoppingCartDetail.setPrice(product.get().getPrice());
            shoppingCart.setProductDetails(Collections.singletonList(shoppingCartDetail));
            ShoppingCart saveAndFlush = shoppingCartRepository.saveAndFlush(shoppingCart);
            shoppingCartDetail.setIdShoppingCart(saveAndFlush.getId());
            shoppingCartDetailRepository.save(shoppingCartDetail);

        } else {
            ShoppingCartDetail shoppingCartDetail = initOrderProductDetail(product.get());
            shoppingCartDetail.setStatus(ShoppingCartDetailStatus.IN_CART);
            ShoppingCart shoppingCart = optionalOrderProduct.get();
            List<ShoppingCartDetail> list = shoppingCart.getProductDetails();
            shoppingCartDetail.setIdShoppingCart(shoppingCart.getId());
            shoppingCartDetail.setPrice(product.get().getPrice());
            list.add(shoppingCartDetail);
            shoppingCartRepository.save(shoppingCart);
        }
    }

    @Override
    public void removeToCart(Long idUser, Long idProduct) {
        User user = userService.checkExistUser(idUser);
        List<ShoppingCart> shoppingCartList = shoppingCartRepository
                .getAllByIdUserAndStatus(user.getId(), SalesManagementConstants.STATUS_ORDER_PENDING);
        if (CollectionUtils.isEmpty(shoppingCartList)) return;
        ShoppingCart shoppingCart = shoppingCartList.get(0);
        List<ShoppingCartDetail> productDetails = shoppingCart.getProductDetails();
        if (CollectionUtils.isEmpty(productDetails)) return;
        productDetails.stream()
                .filter(item -> item.getIdProduct().equals(idProduct))
                .forEach(shoppingCartDetail -> {
                    shoppingCartDetail.setStatus(ShoppingCartDetailStatus.OUT_CART);
                    shoppingCartDetail.setUpdatedAt(new Date());
                    shoppingCart.setUpdatedAt(new Date());
                });
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void decreaseProduct(Long idUser, Long idProduct) {
        ShoppingCart shoppingCart = checkOrderProduct(idUser);
        if (Objects.isNull(shoppingCart)) throw new InvalidException(MessageConstants.NOT_FOUND_ORDER);
        List<ShoppingCartDetail> productDetails = shoppingCart.getProductDetails();
        List<ShoppingCartDetail> productInCart = productDetails
                .stream()
                .filter(item -> item.getStatus().equals(ShoppingCartDetailStatus.IN_CART)
                        && item.getIdProduct().equals(idProduct)).toList();
        if (CollectionUtils.isEmpty(productInCart)) return;
        if (productDetails.size() == 1) {
            removeToCart(idUser, idProduct);
        } else {
            Optional<Product> product = productService.findById(idProduct);
            if (product.isEmpty()) throw new InvalidException(MessageConstants.NOT_FOUND_PRODUCT);
            ShoppingCartDetail shoppingCartDetail = productInCart.get(productInCart.size() - 1);
            shoppingCartDetail.setUpdatedAt(new Date());
            shoppingCartDetail.setStatus(ShoppingCartDetailStatus.OUT_CART);
            productDetails.remove(shoppingCartDetail);
            shoppingCartRepository.save(shoppingCart);
        }
    }

    @Override
    public void increaseProduct(Long idUser, Long idProduct) {
        ShoppingCart shoppingCart = checkOrderProduct(idUser);
        if (Objects.isNull(shoppingCart)) throw new InvalidException(MessageConstants.NOT_FOUND_ORDER);
        List<ShoppingCartDetail> productDetails = shoppingCart.getProductDetails();
        if (CollectionUtils.isEmpty(productDetails)) return;
        Optional<Product> product = productService.findById(idProduct);
        if (product.isEmpty()) throw new InvalidException(MessageConstants.NOT_FOUND_PRODUCT);
        ShoppingCartDetail shoppingCartDetail = initOrderProductDetail(product.get());
        shoppingCartDetail.setQuantity(1);
        shoppingCartDetail.setStatus(ShoppingCartDetailStatus.IN_CART);
        shoppingCartDetail.setIdShoppingCart(shoppingCart.getId());
        shoppingCartDetail.setPrice(product.get().getPrice());
        productDetails.add(shoppingCartDetail);
        shoppingCartRepository.save(shoppingCart);
    }

    private ShoppingCart checkOrderProduct(Long idUser) {
        User user = userService.checkExistUser(idUser);
        List<ShoppingCart> shoppingCartList = shoppingCartRepository
                .getAllByIdUserAndStatus(user.getId(), SalesManagementConstants.STATUS_ORDER_PENDING);
        if (CollectionUtils.isEmpty(shoppingCartList)) return null;
        return shoppingCartList.get(0);
    }

    @Override
    public ShoppingCartDTO getAllProductInCart(Long idUser) throws IOException {
        User user = userService.checkExistUser(idUser);
        ShoppingCartDTO orderProductDTO = new ShoppingCartDTO();
        List<ShoppingCart> shoppingCartList = shoppingCartRepository
                .getAllByIdUserAndStatus(user.getId(), SalesManagementConstants.STATUS_ORDER_PENDING);
        if (CollectionUtils.isEmpty(shoppingCartList)) return orderProductDTO;
        ShoppingCart shoppingCart = shoppingCartList.get(0);
        List<ShoppingCartDetail> productDetails = shoppingCart.getProductDetails();
        if (CollectionUtils.isEmpty(productDetails)) return orderProductDTO;
        List<ShoppingCartDetailDTO> productDetailDTOList = new ArrayList<>();
        List<Long> idProductList = productDetails.stream()
                .filter(item -> ShoppingCartDetailStatus.IN_CART.equals(item.getStatus()))
                .map(ShoppingCartDetail::getIdProduct).distinct().toList();
        if (CollectionUtils.isEmpty(productDetails)) return orderProductDTO;
        Set<Product> productSet = productService.findAllByIdProductIn(idProductList);
        BigDecimal totalValue = BigDecimal.ZERO;
        for (Long aLong : idProductList) {
            ShoppingCartDetailDTO shoppingCartDetailDTO = new ShoppingCartDetailDTO();
            Product product = productSet.stream().filter(item -> aLong.equals(item.getIdProduct()))
                    .findFirst().orElse(null);
            if (Objects.isNull(product)) continue;
            List<Long> totalQuantity = productDetails.stream()
                    .filter(item -> ShoppingCartDetailStatus.IN_CART.equals(item.getStatus()))
                    .map(ShoppingCartDetail::getIdProduct).filter(aLong::equals).toList();
            shoppingCartDetailDTO.setIdOrderProduct(shoppingCart.getId());
            shoppingCartDetailDTO.setTotalQuantity(totalQuantity.size());
            shoppingCartDetailDTO.setIdProduct(aLong);
            shoppingCartDetailDTO.setProductName(product.getProductName());
            shoppingCartDetailDTO.setPrice(product.getPrice());
            shoppingCartDetailDTO.setImage(CommonUtils.convertStringImageToByte(product.getImage()));
            if (StringUtils.isNotEmpty(product.getPrice())) {
                BigDecimal price = new BigDecimal(product.getPrice());
                BigDecimal total = price.multiply(BigDecimal.valueOf(totalQuantity.size()));
                shoppingCartDetailDTO.setTotalPrice(String.valueOf(total));
                totalValue = totalValue.add(total);
            }
            productDetailDTOList.add(shoppingCartDetailDTO);
        }
        orderProductDTO.setTotalPrice(String.valueOf(totalValue));
        orderProductDTO.setIdOrderProduct(shoppingCart.getId());
        orderProductDTO.setStatus(shoppingCart.getStatus());
        orderProductDTO.setShoppingCartDetailDTOList(productDetailDTOList);
        return orderProductDTO;
    }

    @Override
    public ShoppingCartDTO getAllProductInOrder(Long idShoppingCart) throws IOException {
        ShoppingCartDTO orderProductDTO = new ShoppingCartDTO();
        Optional<ShoppingCart> shoppingCart = findById(idShoppingCart);
        if (shoppingCart.isEmpty()) throw new InvalidException("Không tìm thấy");
        List<ShoppingCartDetail> productDetails = shoppingCart.get().getProductDetails();
        if (CollectionUtils.isEmpty(productDetails)) return orderProductDTO;
        List<ShoppingCartDetailDTO> productDetailDTOList = new ArrayList<>();
        List<Long> idProductList = productDetails.stream()
                .filter(item -> ShoppingCartDetailStatus.IN_CART.equals(item.getStatus()))
                .map(ShoppingCartDetail::getIdProduct).distinct().toList();
        if (CollectionUtils.isEmpty(productDetails)) return orderProductDTO;
        Set<Product> productSet = productService.findAllByIdProductIn(idProductList);
        BigDecimal totalValue = BigDecimal.ZERO;
        for (Long aLong : idProductList) {
            ShoppingCartDetailDTO shoppingCartDetailDTO = new ShoppingCartDetailDTO();
            Product product = productSet.stream().filter(item -> aLong.equals(item.getIdProduct()))
                    .findFirst().orElse(null);
            if (Objects.isNull(product)) continue;
            List<Long> totalQuantity = productDetails.stream()
                    .filter(item -> ShoppingCartDetailStatus.IN_CART.equals(item.getStatus()))
                    .map(ShoppingCartDetail::getIdProduct).filter(aLong::equals).toList();
            shoppingCartDetailDTO.setIdOrderProduct(shoppingCart.get().getId());
            shoppingCartDetailDTO.setTotalQuantity(totalQuantity.size());
            shoppingCartDetailDTO.setIdProduct(aLong);
            shoppingCartDetailDTO.setProductName(product.getProductName());
            shoppingCartDetailDTO.setPrice(product.getPrice());
            shoppingCartDetailDTO.setImage(CommonUtils.convertStringImageToByte(product.getImage()));
            if (StringUtils.isNotEmpty(product.getPrice())) {
                BigDecimal price = new BigDecimal(product.getPrice());
                BigDecimal total = price.multiply(BigDecimal.valueOf(totalQuantity.size()));
                shoppingCartDetailDTO.setTotalPrice(String.valueOf(total));
                totalValue = totalValue.add(total);
            }
            productDetailDTOList.add(shoppingCartDetailDTO);
        }
        orderProductDTO.setTotalPrice(String.valueOf(totalValue));
        orderProductDTO.setIdOrderProduct(shoppingCart.get().getId());
        orderProductDTO.setStatus(shoppingCart.get().getStatus());
        orderProductDTO.setShoppingCartDetailDTOList(productDetailDTOList);
        return orderProductDTO;
    }

    @Override
    public void changeStatus(Long idOrderProduct, Long idUser, String status) {
        ShoppingCart shoppingCart = checkExistOrderProduct(idOrderProduct);
        if (!shoppingCart.getIdUser().equals(idUser)) {
            throw new InvalidException(MessageConstants.ORDER_IS_NOT_OF_USE);
        }
        if (SalesManagementConstants.STATUS_ORDER_BOUGHT.equals(shoppingCart.getStatus())) {
            throw new InvalidException(MessageConstants.ORDER_HAS_BEEN_COMPLETED);
        }
        shoppingCart.setStatus(status);
        shoppingCart.setUpdatedAt(new Date());
        shoppingCartRepository.save(shoppingCart);

        List<ShoppingCartDetail> productDetails = shoppingCart.getProductDetails();
        List<ShoppingCartDetail> productInCart = productDetails.stream()
                .filter(item -> ShoppingCartDetailStatus.IN_CART.equals(item.getStatus())).toList();
        List<Long> productIdList = productDetails.stream()
                .filter(item -> ShoppingCartDetailStatus.IN_CART.equals(item.getStatus()))
                .map(ShoppingCartDetail::getIdProduct).distinct().toList();
        Map<Long, Integer> productIdAndQuantity = new HashMap<>();
        for (ShoppingCartDetail shoppingCartDetail : productInCart) {
            if (productIdAndQuantity.containsKey(shoppingCartDetail.getIdProduct())) {
                // Nếu có, cộng dồn giá trị
                productIdAndQuantity.put(shoppingCartDetail.getIdProduct(), productIdAndQuantity.get(shoppingCartDetail.getIdProduct()) + shoppingCartDetail.getQuantity());
            } else {
                // Nếu chưa, thêm mới
                productIdAndQuantity.put(shoppingCartDetail.getIdProduct(), shoppingCartDetail.getQuantity());
            }
        }
        Set<Product> productSet = productService.findAllByIdProductIn(productIdList);
        for (Product product : productSet) {
            Long productId = product.getIdProduct();
            Optional<Integer> optionalValue = productIdAndQuantity.entrySet()
                    .stream()
                    .filter(item -> item.getKey().equals(productId))
                    .map(Map.Entry::getValue)
                    .findFirst();
            if (optionalValue.isPresent()) {
                int value = optionalValue.get();
                product.setQuantity(product.getQuantity() - value);
                if (product.getQuantity() <= 0) {
                    product.setOutOfStock(true);
                }
            }
        }
        productService.saveAll(productSet);
    }

    private ShoppingCartDetail initOrderProductDetail(Product product) {
        ShoppingCartDetail shoppingCartDetail = new ShoppingCartDetail();
        shoppingCartDetail.setIdProduct(product.getIdProduct());
        shoppingCartDetail.setCreatedAt(new Date());
        shoppingCartDetail.setQuantity(1);
        return shoppingCartDetail;
    }
}
