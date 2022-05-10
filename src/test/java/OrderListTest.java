import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
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
    OrderClient orderClient;
    User user;
    String userEmail = RandomStringUtils.randomAlphabetic(10)+"@gmail.com";
    String userFirstName = RandomStringUtils.randomAlphabetic(10);
    String userPassword = RandomStringUtils.randomAlphabetic(10);
    String accessToken;
    Order order;
    List<String> ingredients = new ArrayList<>();
    String ingredient = "61c0c5a71d1f82001bdaaa6d";

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = new User(userEmail, userPassword, userFirstName, accessToken);
        ValidatableResponse createResponse = userClient.create(user);
        String accessTokenExtract = createResponse.extract().path("accessToken");
        accessToken = accessTokenExtract.replace("Bearer ", "");
        user.setAccessToken(accessToken);
        order = new Order(ingredients);
        ingredients.add(ingredient);
    }

    @After
    public void tearDown(){
        userClient.delete(user);
    }

    @Test
    @DisplayName("Orders list has received by authorised user")
    public void ordersListReceived() {
        OrderList ordersList = orderClient.orderListGetting();
        assertThat("Order list is correct", ordersList, is(not(0)));
    }

    @Test
    @DisplayName("Orders list has received by authorised user")
    public void ordersListReceivedByAuthorisedUser() {
        OrderList ordersList = orderClient.orderListGettingByAuthorisedUser(user);
        assertThat("Order list is correct", ordersList, is(not(0)));
    }
}
