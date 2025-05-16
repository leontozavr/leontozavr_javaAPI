import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {

    @Test
    public void Ex4() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();
    }

    @Test
    public void Ex5() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework");
        String responseBody = response.getBody().asString();
        JsonPath json = new JsonPath(responseBody);
        String secondMessage = json.get("messages[1].message");
        System.out.println(secondMessage);
    }

    @Test
    public void Ex6() {
        Response response = RestAssured
                .given()
                .redirects().follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect");
        String url = response.getHeader("Location");
        System.out.println(url);
    }
}
