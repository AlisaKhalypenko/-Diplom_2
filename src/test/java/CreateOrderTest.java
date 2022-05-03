import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateOrderTest {
    UserClient userClient;
    User user = new User();
    String email = "mir@mail.ru";
    String userPassword = "1234567";
    Order order;
    List<String> ingredients = new ArrayList<>();
    String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        order = new Order(ingredients);
        user = new User(email, userPassword, "Germiona", "");
        userClient.create(user);
    }

    @After
    public void tearDown(){
        userClient.delete(user);
    }

    //этот тест не проходит, заказ не создаётся, статус код 403. что-то не то с передачей accessToken?
    @Test
    @DisplayName("Order was created with authorisation")
    public void orderWasCreatedWithAuthorisation() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(email, userPassword));
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        accessToken = loginResponse.extract().path("accessToken");
        user.setAccessToken(accessToken);
        ValidatableResponse orderCreated = userClient.orderCreatingWithAuthorisation(order, user);
        int statusCode = orderCreated.extract().statusCode();
        boolean success = orderCreated.extract().path("success");

        assertThat("Order was created", statusCode, equalTo(200));
        assertThat("State of order's creating", success, equalTo(true));
    }

    @Test
    @DisplayName("Order was created without authorisation")
    public void orderWasCreatedWithoutAuthorisation() {
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ValidatableResponse orderCreated = userClient.orderCreating(order);
        int statusCode = orderCreated.extract().statusCode();
        boolean success = orderCreated.extract().path("success");

        assertThat("Order was created", statusCode, equalTo(200));
        assertThat("State of order's creating", success, equalTo(true));
    }

    @Test
    @DisplayName("Order with ingredients")
    public void orderWithIngredients() {
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ValidatableResponse orderCreated = userClient.orderCreating(order);
        int statusCode = orderCreated.extract().statusCode();
        boolean success = orderCreated.extract().path("success");

        assertThat("Order was created", statusCode, equalTo(200));
        assertThat("State of order's creating", success, equalTo(true));
    }

    @Test
    @DisplayName("Order without ingredients")
    public void orderWithoutIngredients() {
        ValidatableResponse orderCreated = userClient.orderCreating(order);
        int statusCode = orderCreated.extract().statusCode();
        String message = orderCreated.extract().path("message");

        assertThat("Order wasn't created", statusCode, equalTo(400));
        assertThat("Message if cannot make order", message, equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Order with incorrect hash")
    public void orderWithIncorrectHash() {
        ingredients.add("61c0c5a71d1f82001bdaaad");
        ValidatableResponse orderCreated = userClient.orderCreating(order);
        int statusCode = orderCreated.extract().statusCode();

        assertThat("Internal Server Error", statusCode, equalTo(500));
    }

}
