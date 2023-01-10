package com.unosquare.Controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.Reporter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ApiCore {
    JSONObject requestParams;
    public ApiCore(String url) {
        RestAssured.baseURI = url;
        requestParams = new JSONObject();
    }

    public Response postUser(){
        requestParams.put("name", "JohnAPI");
        requestParams.put("job", "QA");
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.headers("Content-Type", "application/json");
        httpRequest.body(requestParams.toString());
        Response response = httpRequest.post("/users");
        return response;
    }

    public void addMessagesToReport(Response response){
        Reporter.log("Response Body " + response.prettyPrint());
        Reporter.log("Response code " + response.getStatusCode());
        Reporter.log("Request Body " + requestParams.toString());
        Reporter.log("Request URL " + "https://reqres.in/api/users/");
    }

    public Response postUserFromFile() throws IOException, ParseException {
            Response response = null;
            try {
                JSONParser json = new JSONParser();
                FileReader reader = new FileReader("src/test/java/com/unosquare/resources/Register.json");
                Object requestParams = json.parse(reader);
                RequestSpecification httpRequest = RestAssured.given();
                httpRequest.headers("Content-Type", "application/json");
                httpRequest.body(requestParams.toString());
                response = httpRequest.post("/users");
            }catch (IOException exception){
                exception.printStackTrace();
            }catch (ParseException exception){
                exception.printStackTrace();
            }
            return response;
    }
}
