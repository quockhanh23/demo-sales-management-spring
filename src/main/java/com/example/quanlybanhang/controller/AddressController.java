package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.common.CommonUtils;
import com.example.quanlybanhang.dto.LocationDTO;
import com.example.quanlybanhang.models.Address;
import com.example.quanlybanhang.service.AddressService;
import com.example.quanlybanhang.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final UserService userService;

    private final AddressService addressService;

    private final RestTemplate restTemplate;

    @GetMapping("/get-all-address-by-user")
    public ResponseEntity<Object> getAllAddressByUser(@RequestParam Long idUser) {
        userService.checkExistUser(idUser);
        List<Address> addressList = addressService.getAllByIdUser(idUser);
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @GetMapping("/create-address")
    public ResponseEntity<Object> createAddress(@RequestParam Long idUser,
                                                @RequestBody Address address) {
        userService.checkExistUser(idUser);
        addressService.createAddress(address);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAllProvince")
    public ResponseEntity<?> getAllProvince() throws JsonProcessingException {
        String url = "https://open.oapi.vn/location/provinces?page=0&size=1000";
        String jsonResponse = getJsonResponse(url);
        LocationDTO responseData = CommonUtils.intObjectMapper().readValue(jsonResponse, LocationDTO.class);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/getAllDistrict")
    public ResponseEntity<?> getAllDistrict(@RequestParam String idProvince,
                                            @RequestParam(required = false) String query) throws JsonProcessingException {
        query = StringUtils.isNotEmpty(query) ? "&query=" + query : "";
        String url = "https://open.oapi.vn/location/districts/" + idProvince + "?page=0&size=1000" + query;
        String jsonResponse = getJsonResponse(url);
        LocationDTO responseData = CommonUtils.intObjectMapper().readValue(jsonResponse, LocationDTO.class);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/getAllWard")
    public ResponseEntity<?> getAllWard(@RequestParam String districtId,
                                        @RequestParam(required = false) String query) throws JsonProcessingException {
        query = StringUtils.isNotEmpty(query) ? "&query=" + query : "";
        String url = "https://open.oapi.vn/location/wards/" + districtId + "?page=0&size=1000" + query;
        String jsonResponse = getJsonResponse(url);
        LocationDTO responseData = CommonUtils.intObjectMapper().readValue(jsonResponse, LocationDTO.class);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    private String getJsonResponse(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<?> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return (String) responseEntity.getBody();
    }
}
