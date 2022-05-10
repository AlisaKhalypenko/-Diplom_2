import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    UserClient userClient;
    User user;
    String email = RandomStringUtils.randomAlphabetic(10)+"@gmail.com";
    String userPassword = RandomStringUtils.randomNumeric(7);
    String userFirstName = RandomStringUtils.randomAlphabetic(10);
    String incorrectEmail = RandomStringUtils.randomAlphabetic(5)+"@gmail.com";
    String incorrectPassword = RandomStringUtils.randomNumeric(6);
    String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = new User(email, userPassword, userFirstName, accessToken);
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
    @DisplayName("User can login with valid credentials")
    public void userCanLoginWithValidCredentials() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(email, userPassword));
        int statusCode = loginResponse.extract().statusCode();
        boolean success = loginResponse.extract().path("success");

        assertThat("User can login", statusCode, equalTo(SC_OK));
        assertThat("Login state", success, equalTo(true));
    }

    @Test
    @DisplayName("User cannot login with incorrect email")
    public void userCannotLoginWithIncorrectLogin() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(incorrectEmail, userPassword));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("User cannot login", statusCode, equalTo(401));
        assertThat("Message if cannot login", message, equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("User cannot login with incorrect password")
    public void userCannotLoginWithIncorrectPassword() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(email, incorrectPassword));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("User cannot login", statusCode, equalTo(401));
        assertThat("Message if cannot login", message, equalTo("email or password are incorrect"));
    }

}
