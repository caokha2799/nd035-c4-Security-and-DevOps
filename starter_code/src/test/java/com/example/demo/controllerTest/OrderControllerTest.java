package com.example.demo.controllerTest;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);

    private OrderController orderController;

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);

        Item item = new Item();
        item.setId(1L);
        item.setName("Square Widget");
        item.setPrice(BigDecimal.valueOf(1.99));
        item.setDescription("A widget that is square");
        List<Item> items = new ArrayList<>();
        items.add(item);

        User user = new User();
        Cart cart = new Cart();
        user.setUsername("khacv1");
        user.setPassword("kha123456");
        user.setCart(cart);
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(1.99));

        when(userRepository.findByUsername("khacv1")).thenReturn(user);
    }

    @Test
    public void submitOrder(){
        ResponseEntity<UserOrder> response = orderController.submit("khacv1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();
        assertNotNull(order);

        assertEquals("Square Widget", order.getItems().get(0).getName());
        assertEquals(0, order.getUser().getId());
    }

    @Test
    public void getOrdersForUser() {
        ResponseEntity<UserOrder> submit = orderController.submit("khacv1");
        assertNotNull(submit);
        assertEquals(200, submit.getStatusCodeValue());

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("khacv1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> orders = response.getBody();
        assertNotNull(orders);

        assertEquals(0, orders.size());
    }
}
