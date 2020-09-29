package com.app.utb.springrestsecurityapp.service.impl;

import com.app.utb.springrestsecurityapp.dto.AddressDto;
import com.app.utb.springrestsecurityapp.entity.AddressEntity;
import com.app.utb.springrestsecurityapp.entity.UserEntity;
import com.app.utb.springrestsecurityapp.repositories.AddressRepository;
import com.app.utb.springrestsecurityapp.repositories.UserRepository;
import com.app.utb.springrestsecurityapp.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public AddressServiceImpl(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }


    /*@Override
    public List<AddressDto> getAddresses(String userId) {

        UserEntity byUserId = userRepository.findByUserId(userId);
        if(byUserId == null)
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        List<AddressEntity> entityAddressesList = byUserId.getAddresses();
        ModelMapper modelMapper = new ModelMapper();

       *//* List<AddressDto> returnedValue = entityAddressesList.stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .collect(Collectors.toList());*//*

        Type listType = new TypeToken<List<AddressDto>>() {}.getType();
        List<AddressDto> characters = modelMapper.map(entityAddressesList, listType);

        return characters;
    }*/


    @Override
    public List<AddressDto> getAddresses(String userId) {

        List<AddressDto> returnedValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();


        UserEntity byUserId = userRepository.findByUserId(userId);
        if (byUserId == null)
            return returnedValue;

        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(byUserId);
        List<AddressEntity> entityAddressesList = byUserId.getAddresses();
        for (AddressEntity addressEntity : addresses) {
            returnedValue.add(modelMapper.map(addressEntity, AddressDto.class));
        }
        return returnedValue;
    }

    @Override
    public AddressDto getAddressByAddressId(String addressId) {
        AddressDto returnedValue = new AddressDto();
        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
        if(addressEntity == null)
            return returnedValue;
        ModelMapper modelMapper = new ModelMapper();
        returnedValue = modelMapper.map(addressEntity, AddressDto.class);
        return returnedValue;
    }
}


       /* List<AddressDto> returnedValue = entityAddressesList.stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .collect(Collectors.toList());*//*

        Type listType = new TypeToken<List<AddressDto>>() {}.getType();
        List<AddressDto> characters = modelMapper.map(entityAddressesList, listType);

        return characters;
    }

}*/
