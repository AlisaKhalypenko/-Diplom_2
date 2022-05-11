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
        ValidatableResponse createResponse = userClient.create(user);
        String accessTokenExtract = createResponse.extract().path("accessToken");
        accessToken = accessTokenExtract.replace("Bearer ", "");
        user.setAccessToken(accessToken);
    }

    @After
    public void tearDown(){
        userClient.delete(user);
    }

    @Test
    @DisplayName("User data changing without authorisation")
    public void userDataChangingWithoutAuthorisation() {
        ValidatableResponse changingResponse = userClient.changingWithoutAuthorisation(user);

        int statusCode = changingResponse.extract().statusCode();
        String message = changingResponse.extract().path("message");

        assertThat("User cannot change data", statusCode, equalTo(401));
        assertThat("Message if cannot change data", message, equalTo("You should be authorised"));
    }

    // побеждён))
    @Test
    @DisplayName("User data changing with authorisation")
    public void userDataChangingWithAuthorisation() {
        loginResponse = userClient.login(new UserCredentials(userEmail, userPassword));
        ValidatableResponse changingResponse = userClient.changingWithAuthorisation(user);

        int statusCode = changingResponse.extract().statusCode();
        boolean success = loginResponse.extract().path("success");

        assertThat("Courier can change data", statusCode, equalTo(200));
        assertThat("State if can change data", success, equalTo(true));
    }
}
