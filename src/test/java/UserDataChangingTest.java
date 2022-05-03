import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserDataChangingTest {
    UserClient userClient;
    User user = new User();
    String userEmail = RandomStringUtils.randomAlphabetic(10)+"@gmail.com";
    String userPassword = RandomStringUtils.randomNumeric(7);
    String userFirstName = RandomStringUtils.randomAlphabetic(10 );
    String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = new User(userEmail, userPassword, userFirstName, accessToken);
        userClient.create(user);
    }

    @After
    public void tearDown(){
        userClient.delete(user);
    }

    @Test
    @DisplayName("User data changing without authorisation")
    public void userDataChangingWithoutAuthorisation() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(userEmail, userPassword));
        accessToken = loginResponse.extract().path("accessToken");
        user.setAccessToken(accessToken);
        ValidatableResponse changingResponse = userClient.changingWithoutAuthorisation(user);

        int statusCode = changingResponse.extract().statusCode();
        String message = changingResponse.extract().path("message");

        assertThat("User cannot change data", statusCode, equalTo(401));
        assertThat("Message if cannot change data", message, equalTo("You should be authorised"));
    }

    //этот тест не проходит, заказ не создаётся, статус код 403. что-то не то с передачей accessToken?
    @Test
    @DisplayName("User data changing with authorisation")
    public void userDataChangingWithAuthorisation() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(userEmail, userPassword));
        accessToken = loginResponse.extract().path("accessToken");
        user.setAccessToken(accessToken);
        ValidatableResponse changingResponse = userClient.changingWithAuthorisation(user);

        int statusCode = changingResponse.extract().statusCode();
        boolean success = loginResponse.extract().path("success");

        assertThat("Courier can change data", statusCode, equalTo(200));
        assertThat("State if can change data", success, equalTo(true));
    }
}
