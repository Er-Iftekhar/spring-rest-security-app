package com.app.utb.springrestsecurityapp.repositories;

import com.app.utb.springrestsecurityapp.entity.AddressEntity;
import com.app.utb.springrestsecurityapp.entity.UserEntity;
import com.app.utb.springrestsecurityapp.service.UserService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AddressRepository  extends CrudRepository<AddressEntity, Long>  {
    List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

    AddressEntity findByAddressId(String addressId);
}
