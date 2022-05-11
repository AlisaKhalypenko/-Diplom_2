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
    OrderClient orderClient;
    User user;
    Order order;
    String email = RandomStringUtils.randomAlphabetic(10)+"@gmail.com";;
    String userPassword = RandomStringUtils.randomAlphabetic(10);
    String userFirstName = RandomStringUtils.randomAlphabetic(10);
    List<String> ingredients = new ArrayList<>();
    String accessToken;
    String ingredient = "61c0c5a71d1f82001bdaaa6d";
    String incorrectIngredient = "61c0c5a71d1f82001bdaaa";

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        order = new Order(ingredients);
        user = new User(email, userPassword, userFirstName, accessToken);
        ValidatableResponse createResponse = userClient.create(user);
        String accessTokenExtract = createResponse.extract().path("accessToken");
        accessToken = accessTokenExtract.replace("Bearer ", "");
        user.setAccessToken(accessToken);
    }

    @After
    public void tearDown(){
        if( accessToken != null){
            userClient.delete(user);
        }
    }

    //Та-даам! ))) тест прошёл
    @Test
    @DisplayName("Order was created with authorisation")
    public void orderWasCreatedWithAuthorisation() {
        ingredients.add(ingredient);
        ValidatableResponse orderCreated = orderClient.orderCreatingWithAuthorisation(order, user);
        int statusCode = orderCreated.extract().statusCode();
        boolean success = orderCreated.extract().path("success");

        assertThat("Order was created", statusCode, equalTo(200));
        assertThat("State of order's creating", success, equalTo(true));
    }

    @Test
    @DisplayName("Order was created without authorisation")
    public void orderWasCreatedWithoutAuthorisation() {
        ingredients.add(ingredient);
        ValidatableResponse orderCreated = orderClient.orderCreating(order);
        int statusCode = orderCreated.extract().statusCode();
        boolean success = orderCreated.extract().path("success");

        assertThat("Order was created", statusCode, equalTo(200));
        assertThat("State of order's creating", success, equalTo(true));
    }

    @Test
    @DisplayName("Order with ingredients")
    public void orderWithIngredients() {
        ingredients.add(ingredient);
        ValidatableResponse orderCreated = orderClient.orderCreating(order);
        int statusCode = orderCreated.extract().statusCode();
        boolean success = orderCreated.extract().path("success");

        assertThat("Order was created", statusCode, equalTo(200));
        assertThat("State of order's creating", success, equalTo(true));
    }

    @Test
    @DisplayName("Order without ingredients")
    public void orderWithoutIngredients() {
        ValidatableResponse orderCreated = orderClient.orderCreating(order);
        int statusCode = orderCreated.extract().statusCode();
        String message = orderCreated.extract().path("message");

        assertThat("Order wasn't created", statusCode, equalTo(400));
        assertThat("Message if cannot make order", message, equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Order with incorrect hash")
    public void orderWithIncorrectHash() {
        ingredients.add(incorrectIngredient);
        ValidatableResponse orderCreated = orderClient.orderCreating(order);
        int statusCode = orderCreated.extract().statusCode();

        assertThat("Internal Server Error", statusCode, equalTo(500));
    }

}
