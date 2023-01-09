package com.unosquare;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class APItests {

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = "https://reqres.in/api/";
    }

    @Test
    public void httpStructureTest1() {
        RestAssured.baseURI = "https://reqres.in/api/";
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.get("/users/2");

        int statusCode = response.getStatusCode();

        // Assert that correct status code is returned.
        Assert.assertEquals(statusCode,200);

        Reporter.log("Success 200 validation");

        String responseString = response.asString();
        JsonPath json = new JsonPath(responseString);
        int id = json.getInt("data.id");
        String email = json.get("data.email");
        String avatar = json.get("data.avatar");
        String url = json.get("support.url");
        String text = json.get("support.text");
        Assert.assertEquals(id, 2);
        Assert.assertEquals(email, "janet.weaver@reqres.in");
        Assert.assertEquals(avatar, "https://reqres.in/img/faces/2-image.jpg");
        Assert.assertEquals(url, "https://reqres.in/#support-heading");
        Assert.assertEquals(text, "To keep ReqRes free, contributions towards server costs are appreciated!");
    }

    @Test
    public void httpStructureTest2() {
        RestAssured.baseURI = "https://reqres.in/api/";
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.get("/unknown/2");

        int statusCode = response.getStatusCode();

        // Assert that correct status code is returned.
        Assert.assertEquals(statusCode,200);

        Reporter.log("Success 200 validation");

        String responseString = response.asString();

        JsonPath json = new JsonPath(responseString);
        int id = json.getInt("data.id");
        int year = json.getInt("data.year");
        String color = json.get("data.color");
        String pantone = json.get("data.pantone_value");
        String url = json.get("support.url");
        String text = json.get("support.text");
        Assert.assertEquals(id, 2);
        Assert.assertEquals(year, 2001);
        Assert.assertEquals(color, "#C74375");
        Assert.assertEquals(pantone, "17-2031");
        Assert.assertEquals(url, "https://reqres.in/#support-heading");
        Assert.assertEquals(text, "To keep ReqRes free, contributions towards server costs are appreciated!");
    }
}
