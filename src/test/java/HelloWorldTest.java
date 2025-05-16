import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void Ex7() {
        String url = "https://playground.learnqa.ru/api/long_redirect";
        System.out.println("First URL: " + url);

        while (true) {
            Response response = RestAssured
                    .given()
                    .redirects().follow(false)
                    .when()
                    .get(url);

            String location = response.getHeader("Location");

            if (location == null || response.getStatusCode() == 200) {
                break;
            }

            System.out.println("Redirected to: " + location);
            url = location;
        }

        System.out.println("Final URL: " + url);
    }

    @Test
    public void Ex8() throws InterruptedException {

        Response firstResponse = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job");
        JsonPath json = firstResponse.jsonPath();
        String token = json.getString("token");
        int secondsToReady = json.getInt("seconds");
        System.out.println("Token: " + token);
        System.out.println("Seconds to wait: " + secondsToReady);

        Response beforeReadyResponse = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job");
        JsonPath beforeReadyJson = beforeReadyResponse.jsonPath();
        String beforeReadyStatus = beforeReadyJson.getString("status");
        assertEquals("Job is NOT ready", beforeReadyStatus, "Another value is expected");
        System.out.println("Status before: " + beforeReadyStatus);

        Thread.sleep((secondsToReady + 1) * 1000L);

        Response afterReadyResponse = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job");
        JsonPath afterReadyJson = afterReadyResponse.jsonPath();
        String afterReadyStatus = afterReadyJson.getString("status");
        assertEquals("Job is ready", afterReadyStatus, "Another value is expected");
        String result = afterReadyJson.getString("result");
        assertEquals("42", result, "Another value is expected");
    }

    @Test
    public void Ex9() {

        String login = "super_admin";
        List<String> passwords = Arrays.asList(
                "1234",
                "12345",
                "111111",
                "121212",
                "123123",
                "123456",
                "555555",
                "654321",
                "666666",
                "696969",
                "888888",
                "1234567",
                "7777777",
                "12345678",
                "123456789",
                "1234567890",
                "!@#$%^&*",
                "000000",
                "123qwe",
                "1q2w3e4r",
                "1qaz2wsx",
                "aa123456",
                "abc123",
                "access",
                "admin",
                "adobe123",
                "ashley",
                "azerty",
                "bailey",
                "baseball",
                "batman",
                "charlie",
                "donald",
                "dragon",
                "flower",
                "Football",
                "football",
                "freedom",
                "hello",
                "hottie",
                "iloveyou",
                "jesus",
                "letmein",
                "login",
                "lovely",
                "loveme",
                "master",
                "michael",
                "monkey",
                "mustang",
                "ninja",
                "passw0rd",
                "password",
                "password1",
                "photoshop",
                "princess",
                "qazwsx",
                "qwerty",
                "qwerty123",
                "qwertyuiop",
                "shadow",
                "solo",
                "starwars",
                "sunshine",
                "superman",
                "trustno1",
                "welcome",
                "whatever",
                "zaq1zaq1"
        );

        for (String password : passwords) {
            Map<String, String> data = new HashMap<>();
            data.put("login", login);
            data.put("password", password);
            Response response = RestAssured
                    .given()
                    .body(data)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();
            String auth_cookie = response.getCookie("auth_cookie");
            Map<String, String> cookies = new HashMap<>();
            if (auth_cookie != null) {
                cookies.put("auth_cookie", auth_cookie);
            }

            Response checkResponse = RestAssured
                    .given()
                    .cookies(cookies)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();
            String result = checkResponse.getBody().asString();
            System.out.println(password + " -> " +result);

            if (result.equals("You are authorized")) {
                break;
            }
        }
    }
}
