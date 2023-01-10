package com.unosquare.tests;

import com.unosquare.Controller.ApiCore;
import com.unosquare.Controller.Constants;
import io.restassured.response.Response;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.IOException;

public class APItests {

    ApiCore apiCore;

    @DataProvider(name = "users")
    public Object[][] usersProvider(){
        return new Object[][]
                {{2,"Janet"},{3,"Emma"}};
    }

    @BeforeMethod
    public void setUp() {
        apiCore = new ApiCore(Constants.BASE_URL);
    }

    @Test
    public void getAllUsersTest(){
        Response response = apiCore.getAllUsers();
        String totalUsers = apiCore.getValueFromJson(response,"total");
        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertTrue(Integer.parseInt(totalUsers)>0);
        String responseFirstName = apiCore.getValueFromJson(response,"data[0].first_name");
        Assert.assertEquals(responseFirstName,"George");
        apiCore.addMessagesToReport(response,"https://reqres.in/api/users/");
    }

    @Test(dataProvider = "users")
    public void specificUserTest(int id,String firstName){
        Response response = apiCore.getSpecificUser(id);
        String responseFirstName = apiCore.getValueFromJson(response,"data.first_name");
        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertEquals(responseFirstName,firstName);
        apiCore.addMessagesToReport(response,"https://reqres.in/api/users/"+Integer.toString(id)+'/');
    }

    @Test
    public void notFoundUserTest(){
        Response response = apiCore.getSpecificUser(100);
        Assert.assertEquals(response.getStatusCode(),404);
        apiCore.addMessagesToReport(response,"https://reqres.in/api/users/"+Integer.toString(100)+'/');
    }

    @DataProvider(name = "createUsers")
    public Object[][] createUsersDataProvider(){
        return new Object[][]
        { {"Juan","QA"},{"Camilo","DevOps"}};
    }

    @Test(dataProvider = "createUsers")
    public void createUsersTest(String name,String job) {
        Response response = apiCore.postUser(name,job);
        String id = apiCore.getValueFromJson(response,"id");
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertTrue(Integer.parseInt(id) > 0);
        apiCore.addMessagesToReport(response,"https://reqres.in/api/users/");
    }

    @DataProvider(name = "createUsersExternalFile")
    public Object[][] createUsersFromExternalFileDataProvider(){
        return new Object[][]
                { {Constants.FILE_PATH},{Constants.FILE_PATH2}};
    }

    @Test(dataProvider = "createUsersExternalFile")
    public void createUsersFromJSONfile(String filePath) throws IOException, ParseException {
        Response response = apiCore.createUserFromFile(filePath,Constants.USERS_URL);
        String id = apiCore.getValueFromJson(response,"id");
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertTrue(Integer.parseInt(id) > 0);
        apiCore.addMessagesToReport(response,"https://reqres.in/api/users/");
    }

    @Test
    public void updateUserFromJSONfile() throws IOException, ParseException {
        Response response = apiCore.updateUserFromFile(Constants.FILE_PATH2,Constants.USERS_URL,1);
        String updatedAt = apiCore.getValueFromJson(response,"updatedAt");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(updatedAt.contains("2023"));
        apiCore.addMessagesToReport(response,"https://reqres.in/api/users/"+Integer.toString(1)+'/');
    }
}
