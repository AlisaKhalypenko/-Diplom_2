import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    UserClient userClient;
    String userEmail = RandomStringUtils.randomAlphabetic(10)+"@gmail.com";
    String userFirstName = RandomStringUtils.randomAlphabetic(10);
    String userPassword = RandomStringUtils.randomAlphabetic(10);
    String accessToken;
    User user;

    @Before
    public void setUp(){
        userClient = new UserClient();
        user = new User(userEmail, userPassword, userFirstName,accessToken);
    }

    @After
    public void tearDown(){
        if( accessToken != null){
            userClient.delete(user);
        }
    }

    @Test
    @DisplayName("User can be created")
    public void userCanBeCreated(){
        ValidatableResponse createResponse = userClient.create(user);
        String accessTokenExtract = createResponse.extract().path("accessToken");
        accessToken = accessTokenExtract.replace("Bearer ", "");
        user.setAccessToken(accessToken);

        int statusCode = createResponse.extract().statusCode();
        boolean success = createResponse.extract().path("success");

        assertThat("User has created", statusCode, equalTo(200));
        assertThat("State if user has created", success, equalTo(true));
    }

    @Test
    @DisplayName("User must be unique")
    public void courierMustBeUnique(){
        ValidatableResponse createResponse = userClient.create(user);
        ValidatableResponse createResponse1 = userClient.create(user);
        String accessTokenExtract = createResponse.extract().path("accessToken");
        accessToken = accessTokenExtract.replace("Bearer ", "");
        user.setAccessToken(accessToken);

        int statusCode = createResponse1.extract().statusCode();
        String message = createResponse1.extract().path("message");

        assertThat("User hasn't created", statusCode, equalTo(403));
        assertThat("Message if cannot create", message, equalTo("User already exists"));
    }

    @Test
    @DisplayName("User cannot created without password")
    public void courierCannotCreatedWithoutPassword(){
        ValidatableResponse createResponse = userClient.create(new User(userEmail, "", userFirstName, accessToken));
        int statusCode = createResponse.extract().statusCode();
        String message = createResponse.extract().path("message");

        assertThat("User hasn't created", statusCode, equalTo(403));
        assertThat("Message if cannot create", message, equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("User cannot created without email")
    public void courierCannotCreatedWithoutEmail(){
        ValidatableResponse createResponse = userClient.create(new User("", userPassword, userFirstName, accessToken));
        int statusCode = createResponse.extract().statusCode();
        String message = createResponse.extract().path("message");

        assertThat("User hasn't created", statusCode, equalTo(403));
        assertThat("Message if cannot create", message, equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("User cannot created without first name")
    public void courierCannotCreatedWithoutFirstName(){
        ValidatableResponse createResponse = userClient.create(new User(userEmail, userPassword, "", accessToken));
        int statusCode = createResponse.extract().statusCode();
        String message = createResponse.extract().path("message");

        assertThat("User hasn't created", statusCode, equalTo(403));
        assertThat("Message if cannot create", message, equalTo("Email, password and name are required fields"));
    }
}
