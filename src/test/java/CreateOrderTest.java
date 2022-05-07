import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateOrderTest {
    UserClient userClient;
    User user;
    String email = RandomStringUtils.randomAlphabetic(10)+"@gmail.com";;
    String userPassword = RandomStringUtils.randomAlphabetic(10);
    String userFirstName = RandomStringUtils.randomAlphabetic(10);
    Order order;
    List<String> ingredients = new ArrayList<>();
    String accessToken;
    ValidatableResponse loginResponse;

    @Before
    public void setUp() {
        userClient = new UserClient();
        order = new Order(ingredients);
        user = new User(email, userPassword, userFirstName, accessToken);
        userClient.create(user);
    }

    @After
    public void tearDown(){
        String accessTokenExtract = loginResponse.extract().path("accessToken");
        accessToken = accessTokenExtract.replace("Bearer ", "");
        user.setAccessToken(accessToken);
        userClient.delete(user);
    }

    //Та-даам! ))) тест прошёл
    @Test
    @DisplayName("Order was created with authorisation")
    public void orderWasCreatedWithAuthorisation() {
        loginResponse = userClient.login(new UserCredentials(user.email, user.password));
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        String accessTokenExtract = loginResponse.extract().path("accessToken");
        accessToken = accessTokenExtract.replace("Bearer ", "");
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
        loginResponse = userClient.login(new UserCredentials(user.email, user.password));
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
        loginResponse = userClient.login(new UserCredentials(user.email, user.password));
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
        loginResponse = userClient.login(new UserCredentials(user.email, user.password));
        ValidatableResponse orderCreated = userClient.orderCreating(order);
        int statusCode = orderCreated.extract().statusCode();
        String message = orderCreated.extract().path("message");

        assertThat("Order wasn't created", statusCode, equalTo(400));
        assertThat("Message if cannot make order", message, equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Order with incorrect hash")
    public void orderWithIncorrectHash() {
        loginResponse = userClient.login(new UserCredentials(user.email, user.password));
        ingredients.add("61c0c5a71d1f82001bdaaad");
        ValidatableResponse orderCreated = userClient.orderCreating(order);
        int statusCode = orderCreated.extract().statusCode();

        assertThat("Internal Server Error", statusCode, equalTo(500));
    }

}
