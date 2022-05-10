
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {
    private static final String USER_PATH = "/api/auth";

    @Step("User login")
    public ValidatableResponse login(UserCredentials credentials){
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(USER_PATH + "/login")
                .then();
    }

    @Step("User register")
    public ValidatableResponse create (User user){
        String userFirstName = user.getFirstName();
        String userEmail = user.getEmail();
        String userPassword = user.getPassword();

        HashMap<String,String> dataBody = new HashMap<String,String>();

        dataBody.put("email", userEmail);
        dataBody.put("password", userPassword);
        dataBody.put("name", userFirstName);

        return given()
                .spec(getBaseSpec())
                .body(dataBody)
                .when()
                .post(USER_PATH + "/register")
                .then();
    }

    @Step("User deleting")
    public void delete (User user){
         given()
                .spec(getBaseSpec())
                .auth().oauth2(user.getAccessToken())
                .when()
                .delete(USER_PATH +  "/user")
                .then();
    }

    @Step("User changing without authorisation")
    public ValidatableResponse changingWithoutAuthorisation (User user){
        String userFirstName = user.getFirstName();
        String userEmail = user.getEmail();
        String userPassword = user.getPassword();

        HashMap<String,String> dataBody = new HashMap<String,String>();

        dataBody.put("email", userEmail);
        dataBody.put("password", userPassword);
        dataBody.put("name", userFirstName);

        return given()
                .spec(getBaseSpec())
                .body(dataBody)
                .when()
                .patch(USER_PATH +  "/user")
                .then();
    }

    @Step("User changing with authorisation")
    public ValidatableResponse changingWithAuthorisation (User user){

        String userFirstName = user.getFirstName();
        String userEmail = user.getEmail();
        String userPassword = user.getPassword();

        HashMap<String,String> dataBody = new HashMap<String,String>();

        dataBody.put("email", userEmail);
        dataBody.put("password", userPassword);
        dataBody.put("name", userFirstName);

        return given()
                .spec(getBaseSpec())
                .auth().oauth2(user.getAccessToken())
                .body(dataBody)
                .when()
                .patch(USER_PATH +  "/user")
                .then();
    }

}
