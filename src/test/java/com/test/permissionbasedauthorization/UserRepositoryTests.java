package com.test.permissionbasedauthorization;

import com.test.user.Role;
import com.test.user.User;
import com.test.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Test
    public void testCreateFirstUser(){
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        String rawPassword="manik";
        String encodedPassword=passwordEncoder.encode(rawPassword);
        User newUser=new User("setia.manik001@gmail.com", encodedPassword);

        User savedUser=userRepository.save(newUser);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSecondUser(){
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        String rawPassword="maniksetia";
        String encodedPassword=passwordEncoder.encode(rawPassword);
        User newUser=new User("setia.manik002@gmail.com", encodedPassword);

        User savedUser=userRepository.save(newUser);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateThirdUser(){
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        String rawPassword="maniksetia";
        String encodedPassword=passwordEncoder.encode(rawPassword);
        User newUser=new User("setia.manik003@gmail.com", encodedPassword);

        User savedUser=userRepository.save(newUser);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testAssignRolesToFirstUser(){
        Integer userId=1;
        Integer roleId=2;

        User user=userRepository.findById(userId).get();
        user.addRole(new Role(roleId));

        User updatedUser=userRepository.save(user);

        assertThat(updatedUser.getRoles().size()).isEqualTo(1);
    }

    @Test
    public void testAssignRolesToSecondUser(){
        Integer userId=2;

        User user=userRepository.findById(userId).get();
        user.addRole(new Role(2));
        user.addRole(new Role(3));

        User updatedUser=userRepository.save(user);

        assertThat(updatedUser.getRoles().size()).isEqualTo(2);
    }

    @Test
    public void testAssignRolesToThirdUser(){
        Integer userId=9;

        User user=userRepository.findById(userId).get();
        user.addRole(new Role(3));//customer

        User updatedUser=userRepository.save(user);

        assertThat(updatedUser.getRoles().size()).isEqualTo(1);
    }



}
