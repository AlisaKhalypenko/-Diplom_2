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
    User user;
    String userEmail = RandomStringUtils.randomAlphabetic(10)+"@gmail.com";
    String userPassword = RandomStringUtils.randomNumeric(7);
    String userFirstName = RandomStringUtils.randomAlphabetic(10 );
    String accessToken;
    ValidatableResponse loginResponse;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = new User(userEmail, userPassword, userFirstName, accessToken);
        userClient.create(user);
    }

    @After
    public void tearDown(){
        String accessTokenExtract = loginResponse.extract().path("accessToken");
        accessToken = accessTokenExtract.replace("Bearer ", "");
        user.setAccessToken(accessToken);
        userClient.delete(user);
    }

    @Test
    @DisplayName("User data changing without authorisation")
    public void userDataChangingWithoutAuthorisation() {
        loginResponse = userClient.login(new UserCredentials(user.email, user.password));
        ValidatableResponse changingResponse = userClient.changingWithoutAuthorisation(user);

        int statusCode = changingResponse.extract().statusCode();
        String message = changingResponse.extract().path("message");

        assertThat("User cannot change data", statusCode, equalTo(401));
        assertThat("Message if cannot change data", message, equalTo("You should be authorised"));
    }

    //всё ещё не проходит((, статус код 403.
    @Test
    @DisplayName("User data changing with authorisation")
    public void userDataChangingWithAuthorisation() {
        loginResponse = userClient.login(new UserCredentials(user.email, user.password));
        String accessTokenExtract = loginResponse.extract().path("accessToken");
        accessToken = accessTokenExtract.replace("Bearer ", "");
        user.setAccessToken(accessToken);
        ValidatableResponse changingResponse = userClient.changingWithAuthorisation(user);

        int statusCode = changingResponse.extract().statusCode();
        boolean success = loginResponse.extract().path("success");

        assertThat("Courier can change data", statusCode, equalTo(200));
        assertThat("State if can change data", success, equalTo(true));
    }
}
