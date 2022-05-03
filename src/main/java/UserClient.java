
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

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

        String registerRequestBody = "{\"email\":\"" + userEmail + "\","
                + "\"password\":\"" + userPassword + "\","
                + "\"name\":\"" + userFirstName + "\"}";

        return given()
                .spec(getBaseSpec())
                .body(registerRequestBody)
                .when()
                .post(USER_PATH + "/register")
                .then();
    }

    @Step("User deleting")
    public void delete (User user){
         given()
                .spec(getBaseSpec())
                .auth().oauth2(user.getAccessToken())
                .body(user)
                .when()
                .delete(USER_PATH +  "/user")
                .then();
                 /*.statusCode(202); в этом месте проблема. Если добавить эту строку, то после удаления юзера получаю ошибку Expected status code <202> but was <403>.
                 Что-то некорректно удаляется, подозреваю, что проблема связана с user.getAccessToken(). Хэлп плиз!..
                  */
    }

    @Step("User changing without authorisation")
    public ValidatableResponse changingWithoutAuthorisation (User user){

        String changeRequestBody = "{\"email\": \"123@mail.ru\", \"password\": \"12345678\",\"name\": \"name\"}";
        return given()
                .spec(getBaseSpec())
                .body(changeRequestBody)
                .when()
                .patch(USER_PATH +  "/user")
                .then();
    }

    @Step("User changing with authorisation")
    public ValidatableResponse changingWithAuthorisation (User user){

        String changeRequestBody = "{\"email\": \"123@mail.ru\", \"password\": \"12345678\",\"name\": \"name\"}";
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(user.getAccessToken())
                .body(changeRequestBody)
                .when()
                .patch(USER_PATH +  "/user")
                .then();
    }
     @Step("Order creating without authorisation")
    public ValidatableResponse orderCreating (Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post("/api/orders")
                .then();
    }

    @Step("Order creating with authorisation")
    public ValidatableResponse orderCreatingWithAuthorisation (Order order, User user) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(user.getAccessToken())
                .body(order)
                .when()
                .post("/api/orders")
                .then();
    }


    @Step("Order order list getting")
    public OrderList orderListGetting () {
        return given()
                .spec(getBaseSpec())
                .when()
                .get("/api/orders")
                .body().as(OrderList.class);
    }

    @Step("Order order list getting by authorised user")
    public OrderList orderListGettingByAuthorisedUser (User user) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(user.getAccessToken())
                .when()
                .get("/api/orders")
                .body().as(OrderList.class);
    }

}
