package com.app.utb.springrestsecurityapp.repositories;

import com.app.utb.springrestsecurityapp.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);

    @Query(value = "select * from users_table u where u.email_verification_status ='false'",
            countQuery = "select count(*) from users_table u where u.email_verification_status='false'",
            nativeQuery = true
    )
    Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pageableRequest);

    @Query(value="select * from users_table u where u.first_name = ?1", nativeQuery=true)
    List<UserEntity> findUserByFirstName(String firstName);


    @Query(value = "select * from users_table u where u.last_name = :lastName", nativeQuery = true)
    List<UserEntity> findUserByLastName(@Param("lastName") String lastName);


}
