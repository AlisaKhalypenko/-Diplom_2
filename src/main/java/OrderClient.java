import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient{
    private static final String ORDER_PATH = "/api/orders";

    @Step("Order creating without authorisation")
    public ValidatableResponse orderCreating (Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Order creating with authorisation")
    public ValidatableResponse orderCreatingWithAuthorisation (Order order, User user) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(user.getAccessToken())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Order order list getting")
    public OrderList orderListGetting () {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .body().as(OrderList.class);
    }

    @Step("Order order list getting by authorised user")
    public OrderList orderListGettingByAuthorisedUser (User user) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(user.getAccessToken())
                .when()
                .get(ORDER_PATH)
                .body().as(OrderList.class);
    }
}
