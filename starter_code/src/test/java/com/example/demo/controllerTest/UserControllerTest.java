package com.example.demo.controllerTest;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private CartRepository cartRepository = mock(CartRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    private UserController userController;
    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void addUserWithHappyPath() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("khacv1");
        userRequest.setPassword("kha153759");
        userRequest.setConfirmPassword("kha153759");
        // verify encode return thisIsHashed if encode success
        when(bCryptPasswordEncoder.encode("kha153759")).thenReturn("passwordEncodeSuccess");

        final ResponseEntity<User> response = userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("khacv1", user.getUsername());
        assertEquals("passwordEncodeSuccess", user.getPassword());
    }

    @Test
    public void addUserWithPasswordTooShort() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("khacv1");
        userRequest.setPassword("123");
        userRequest.setConfirmPassword("123");
        final ResponseEntity<User> response = userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findUserById() {
        long id = 0L;
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("khacv1");
        request.setPassword("kha153759");
        request.setConfirmPassword("kha153759");
        when(bCryptPasswordEncoder.encode("kha153759")).thenReturn("passwordEncodeSuccess");
        final ResponseEntity<User> response = userController.createUser(request);
        User user = response.getBody();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        final ResponseEntity<User> userResponseEntity = userController.findById(id);
        assertNotNull(response);
        assertEquals(200, userResponseEntity.getStatusCodeValue());

        User actualUser = userResponseEntity.getBody();
        assertNotNull(actualUser);
        assertEquals(id, actualUser.getId());
        assertEquals("khacv1", actualUser.getUsername());
        assertEquals("passwordEncodeSuccess", actualUser.getPassword());
    }

    @Test
    public void findByUserName() {
        String userName = "khacv1";
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(userName);
        request.setPassword("kha153759");
        request.setConfirmPassword("kha153759");
        when(bCryptPasswordEncoder.encode("kha153759")).thenReturn("passwordEncodeSuccess");
        final ResponseEntity<User> response = userController.createUser(request);
        User user = response.getBody();
        when(userRepository.findByUsername(userName)).thenReturn(user);

        final ResponseEntity<User> userResponseEntity = userController.findByUserName(userName);
        assertNotNull(response);
        assertEquals(200, userResponseEntity.getStatusCodeValue());

        User actualUser = userResponseEntity.getBody();
        assertNotNull(actualUser);
        assertEquals(userName, actualUser.getUsername());
    }

}
