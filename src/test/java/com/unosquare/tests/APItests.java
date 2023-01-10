package com.unosquare.tests;

import com.unosquare.Controller.ApiCore;
import com.unosquare.Controller.Constants;
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

    ApiCore apiCore;
    @BeforeMethod
    public void setUp() {
        apiCore = new ApiCore(Constants.BASE_URL);
    }
    @Test
    public void postTest() {
        Response response = apiCore.postUser();
        Assert.assertEquals(response.getStatusCode(), 201);
        apiCore.addMessagesToReport(response);
    }

    @Test
    public void postTestWithJSONfile() throws IOException, ParseException {
        Response response = apiCore.postUserFromFile();
        Assert.assertEquals(response.getStatusCode(), 201);
        apiCore.addMessagesToReport(response);
    }
}
