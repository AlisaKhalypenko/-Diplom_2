import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class OrderListTest {
    UserClient userClient;
    User user = new User();
    String email = "mir@mail.ru";
    String userPassword = "1234567";
    Order order;
    List<String> ingredients = new ArrayList<>();

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = new User(email, userPassword, "Germiona", "");
        userClient.create(user);
        order = new Order(ingredients);
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
    }

    @After
    public void tearDown(){
        userClient.delete(user);
    }

    @Test
    @DisplayName("Orders list has received by authorised user")
    public void ordersListReceived() {
        OrderList ordersList = userClient.orderListGetting();
        assertThat("Order list is correct", ordersList, is(not(0)));
    }

    @Test
    @DisplayName("Orders list has received by authorised user")
    public void ordersListReceivedByAuthorisedUser() {
        OrderList ordersList = userClient.orderListGettingByAuthorisedUser(user);
        assertThat("Order list is correct", ordersList, is(not(0)));
    }
}
