package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.exeption.BadRequestException;
import com.example.quanlybanhang.models.Star;
import com.example.quanlybanhang.repository.StarRepository;
import com.example.quanlybanhang.service.StarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StarServiceImpl implements StarService {

    private final StarRepository starRepository;

    @Override
    public Optional<Star> findById(Long id) {
        return starRepository.findById(id);
    }

    @Override
    public void save(Star star) {
        starRepository.save(star);
    }

    @Override
    public void delete(Long id) {
        starRepository.deleteById(id);
    }

    @Override
    public List<Star> findAllByProductIdAndType(Long idProduct, String type) {
        List<Star> starList = starRepository.findAllByProductIdAndNumberOfStars(idProduct, type);
        if (CollectionUtils.isEmpty(starList)) starList = new ArrayList<>();
        return starList;
    }

    @Override
    public List<Star> findAllByProductId(Long idProduct) {
        List<Star> starList = starRepository.findAllByProductId(idProduct);
        if (CollectionUtils.isEmpty(starList)) starList = new ArrayList<>();
        return starList;
    }

    @Override
    public Star initStar(Long idUser, Long idProduct, String type) {
        String[] rangeOfType = {"1", "2", "3", "4", "5"};
        int count = 0;
        for (String s : rangeOfType) {
            if (type.equals(s)) {
                break;
            } else {
                count++;
            }
        }
        if (count == 5) {
            throw new BadRequestException("Chỉ có thể đánh giá từ 1 đến 5 sao");
        }
        Star star = new Star();
        star.setNumberOfStars(type);
        star.setProductId(idProduct);
        star.setIdUser(idUser);
        return star;
    }
}
