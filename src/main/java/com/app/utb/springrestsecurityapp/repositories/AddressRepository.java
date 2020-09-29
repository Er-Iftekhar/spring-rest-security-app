package com.app.utb.springrestsecurityapp.repositories;

import com.app.utb.springrestsecurityapp.entity.AddressEntity;
import com.app.utb.springrestsecurityapp.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

    AddressEntity findByAddressId(String addressId);
}
