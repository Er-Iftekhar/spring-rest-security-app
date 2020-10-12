package com.app.utb.springrestsecurityapp.repositories;

import com.app.utb.springrestsecurityapp.entity.AddressEntity;
import com.app.utb.springrestsecurityapp.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    public static boolean areRecordsCreated = false;

    String userId = "asas";

    @BeforeEach
    void setUp() {
        if(!areRecordsCreated)
            createRecords();
    }

    @Test
    void findAllUsersWithConfirmedEmailAddress() {
        Pageable pageableRequest = PageRequest.of(1, 1);
        Page<UserEntity> pages = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
        List<UserEntity> users = pages.getContent();
        assertNotNull(pages);
        assertTrue(users.size() == 1);
    }

    @Test
    void findByFirstName(){
        String firstName = "SYed";
        List<UserEntity> users = userRepository.findUserByFirstName(firstName);
        assertNotNull(users);
        assertTrue(users.size()==1);

        UserEntity userEntity = users.get(0);
        assertTrue(userEntity.getFirstName().equals(firstName));
    }

    @Test
    void findUserByLastNameTest(){
        String lastName= "LastName";
        List<UserEntity> users = userRepository.findUserByLastName(lastName);

        assertNotNull(users);
        assertTrue(users.size() == 2);
        UserEntity user = users.get(0);

        assertTrue(user.getLastName().equals(lastName));

    }

    @Test
    void findUserByKeyword(){
        String keyword= "SY";
        List<UserEntity> users = userRepository.findUserByKeyword(keyword);

        assertNotNull(users);
        assertTrue(users.size() == 2);
        UserEntity user = users.get(0);

        assertTrue(user.getFirstName().contains(keyword));

    }

    @Test
    void findUserFirstNameAndLastNameByKeyword(){
        String keyword = "SY";
        List<Object[]> list = userRepository.findUserFirstNameAndLastNameByKeyword(keyword);
        assertNotNull(list);
        Object[] names = list.get(0);
        String firstName = (String)names[0];
        String lastName = (String)names[1];
        assertTrue(firstName.equals("SYed"));
        assertTrue(lastName.equals("LastName"));
    }


    @Test
    void updateEmailVerificationStatusUsingUserId(){
        boolean email_verification_status = false;


        userRepository.updateEmailVerificationStatusUsingUserId(email_verification_status, userId);
        UserEntity byUserId = userRepository.findByUserId(userId);

        assertEquals(byUserId.getUserId(), userId);
        assertEquals(byUserId.isEmailVerificationStatus(), email_verification_status);
    }

    @Test
    final void testFindUserEntityByUserId(){

        UserEntity userEntity = userRepository.findUserEntitybyUserId(userId);

        assertEquals(userEntity.getUserId(), userId);

    }


    @Test
    final void testGetUserFullNameByUserId(){
        List<Object[]> fullNameByUserId = userRepository.getUserFullNameByUserId(userId);
        Object[] objects = fullNameByUserId.get(0);
        String firstName = String.valueOf(objects[0]);
        String lastName = String.valueOf(objects[1]);
        assertEquals(String.valueOf(firstName),"SYed");
        assertEquals(String.valueOf(lastName),"LastName");
    }

    @Test
    final void updateEmailVerificationStatusUsingUserIdJPQL(){
        boolean status = true;
        userRepository.updateEmailVerificationStatusUsingUserIdJPQL(status, userId);
        UserEntity byUserId = userRepository.findByUserId(userId);
        assertEquals(byUserId.isEmailVerificationStatus(), status);
    }

    private void createRecords(){
        UserEntity userEntity = new UserEntity();
//        userEntity.setId(1L);
        userEntity.setFirstName("SYed");
        userEntity.setLastName("LastName");
        userEntity.setEmail("syedy636");
        userEntity.setEmailVerificationStatus(false);
        userEntity.setEncryptedPassword("pass");
        userEntity.setUserId("asas");

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setAddressId("asas1");
//        addressEntity.setId(1L);
        addressEntity.setCity("Hyd");
        addressEntity.setCountry("India");
        addressEntity.setPostalCode("j1j1");
        addressEntity.setStreetName("Jamun");
        addressEntity.setType("billing");

        List<AddressEntity>  addresses = new ArrayList<>();
        addresses.add(addressEntity);
        userEntity.setAddresses(addresses);

        userRepository.save(userEntity);



        UserEntity userEntity2 = new UserEntity();
//        userEntity.setId(1L);
        userEntity2.setFirstName("SYed1");
        userEntity2.setLastName("LastName");
        userEntity2.setEmail("syedy636");
        userEntity2.setEmailVerificationStatus(false);
        userEntity2.setEncryptedPassword("pass");
        userEntity2.setUserId("asas2");

        AddressEntity addressEntity2 = new AddressEntity();
        addressEntity2.setAddressId("asas1");
//        addressEntity.setId(1L);
        addressEntity2.setCity("Hyd");
        addressEntity2.setCountry("India");
        addressEntity2.setPostalCode("j1j1");
        addressEntity2.setStreetName("Jamun");
        addressEntity2.setType("billing");

        List<AddressEntity>  addresses2 = new ArrayList<>();
        addresses.add(addressEntity2);
        userEntity.setAddresses(addresses2);

        userRepository.save(userEntity2);
        areRecordsCreated = true;
    }
}