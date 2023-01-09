package com.unosquare.tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;

public class APItests {

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = "https://reqres.in/api/";
    }
    @Test
    public void postTest() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "JohnAPI");
        requestParams.put("job", "QA");
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.headers("Content-Type", "application/json");
        httpRequest.body(requestParams.toString());
        Response response = httpRequest.post("/users");
        Assert.assertEquals(response.getStatusCode(), 201);
        Reporter.log("Response Body " + response.prettyPrint());
        Reporter.log("Response code " + response.getStatusCode());
        Reporter.log("Request Body " + requestParams.toString());
        Reporter.log("Request URL " + "https://reqres.in/api/users/");
    }

    @Test
    public void postTestWithJSONfile() throws IOException, ParseException {
        try {
            JSONParser json = new JSONParser();
            FileReader reader = new FileReader("src/test/java/com/unosquare/resources/Register.json");
            Object requestParams = json.parse(reader);
            RequestSpecification httpRequest = RestAssured.given();
            httpRequest.headers("Content-Type", "application/json");
            httpRequest.body(requestParams.toString());
            Response response = httpRequest.post("/users");
            Assert.assertEquals(response.getStatusCode(), 201);
            Reporter.log("Response Body " + response.prettyPrint());
            Reporter.log("Response code " + response.getStatusCode());
            Reporter.log("Request Body " + requestParams.toString());
            Reporter.log("Request URL " + "https://reqres.in/api/users/");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
