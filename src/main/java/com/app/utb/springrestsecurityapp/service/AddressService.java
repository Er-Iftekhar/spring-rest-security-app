package com.app.utb.springrestsecurityapp.service;

import com.app.utb.springrestsecurityapp.dto.AddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> getAddresses(String userId);

    AddressDto getAddressByAddressId(String addressId);

}
