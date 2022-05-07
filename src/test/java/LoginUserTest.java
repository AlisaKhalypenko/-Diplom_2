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

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = new User(email, userPassword, userFirstName, "");
        userClient.create(user);
    }

    @After
    public void tearDown(){
        userClient.delete(user);
    }

    @Test
    @DisplayName("User can login with valid credentials")
    public void courierCanLoginWithValidCredentials() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(user.email, user.password));
        int statusCode = loginResponse.extract().statusCode();
        boolean success = loginResponse.extract().path("success");

        assertThat("User can login", statusCode, equalTo(SC_OK));
        assertThat("Login state", success, equalTo(true));
    }

    @Test
    @DisplayName("User cannot login with incorrect email")
    public void courierCannotLoginWithIncorrectLogin() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials("s@bhh.kk", user.password));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("User cannot login", statusCode, equalTo(401));
        assertThat("Message if cannot login", message, equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("User cannot login with incorrect password")
    public void courierCannotLoginWithIncorrectPassword() {
        ValidatableResponse loginResponse = userClient.login(new UserCredentials(user.email, "hjnvb"));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("User cannot login", statusCode, equalTo(401));
        assertThat("Message if cannot login", message, equalTo("email or password are incorrect"));
    }

}
