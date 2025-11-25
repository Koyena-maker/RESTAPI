package StepDefinition;

import File.Reusable;
import Pojo.LoginResponse;
import Pojo.OrderDetails;
import Pojo.ViewOrder;
import Resources.APIResources;
import Resources.TestData;
import Resources.Utils;
import io.cucumber.java.en.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static File.Reusable.getString;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;


public class stepDefinitions extends Utils {
    RequestSpecification reqLogin;
    ResponseSpecification resLogin;
    Response loginresponse;
    LoginResponse loginresponses;

    JsonPath js;

    RequestSpecification addProductRequest;
    RequestSpecification addProductBaseRequest;
    APIResources resourceAPI;

    String productId;
    String createOrderResponse;
    String orderId;
    final RequestSpecification baseSpec = commonForAll();


    public stepDefinitions() throws IOException {
    }

    @Given("Add Login Payload with  {string} {string}")
    public void add_login_payload_with(String username, String password) throws IOException {
        TestData td=new TestData();
        reqLogin = given().relaxedHTTPSValidation().header("Content-Type", "application/json; charset=UTF-8").spec(commonForAll()).body(td.loginPayload(username,password));
    }

    @When("user calls {string} with Post http request")
    public void user_calls_with_Post_http_request(String resource) {
        // Write code here that turns the phrase above into concrete actions
        resourceAPI=APIResources.valueOf(resource);
        resLogin=new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
        loginresponse = reqLogin.when().post(resourceAPI.getRequest()).then().spec(resLogin).extract().response();
    }

    @Then("the Api call is success with status code {int}")
    public void the_api_call_is_success_with_status_code(Integer int1) {
        System.out.println("Response Code: " + loginresponse.getStatusCode());
        System.out.println("Response Body: " + loginresponse.asString());
        assertEquals(int1.intValue(), loginresponse.getStatusCode());
    }


    @Then("{string} in response body is {string}")
    public void in_response_body_is(String key, String value) {
        // Write code here that turns the phrase above into concrete actions


            String logresp = loginresponse.asString();
            System.out.println(">>> Raw Response: " + logresp);
            js = Reusable.rawTojson(logresp);
            Utils.userId=js.get("userId").toString();
            Utils.token= js.get("token").toString();
            assertEquals(js.get(key).toString(), value);



    }

    @Then("Token is extracted and product is added with {string}")
    public void Token_is_extracted_and_product_is_added_with(String method) throws IOException


        {
            resourceAPI=APIResources.valueOf(method);
            addProductBaseRequest=new RequestSpecBuilder().addRequestSpecification(baseSpec).addHeader("authorization",Utils.token).build();
            addProductRequest=given().spec(addProductBaseRequest).log().all().param("productName","qwerty").param("productAddedBy",Utils.userId).param("productCategory","fashion").param("productSubCategory","shirts").param("productPrice","11500").param("productDescription","Addias Originals").param("productFor","women").multiPart("productImage",new File("C:\\Users\\HP\\Downloads\\prodimage.jpg"));
            String addProductResponse= addProductRequest.when().post(resourceAPI.getRequest()).then().log().all().extract().response().asString();
            JsonPath js= Reusable.rawTojson(addProductResponse);
            productId=js.getString("productId");
            System.out.println("Product ID: " + productId);
        }

        @Then("Order is created with {string}")
        public void order_is_created_with(String apiName)
        {
            System.out.println("Below starts creating order-");
            resourceAPI=APIResources.valueOf(apiName);
            OrderDetails od=new OrderDetails();
            od.setCountry("India");
            od.setProductOrderedId(productId);
            List<OrderDetails> orderdetaillist=new ArrayList<OrderDetails>();
            orderdetaillist.add(od);
            EcommerceE2E.Order or=new EcommerceE2E.Order();
            or.setOrders(orderdetaillist);
            RequestSpecification createOrderBaseRequest=new RequestSpecBuilder().addRequestSpecification(baseSpec).addHeader("authorization",Utils.token).build();
            RequestSpecification createOrderRequest=given().spec(createOrderBaseRequest).contentType(ContentType.JSON).log().all().body(or);
            createOrderResponse=createOrderRequest.when().post(resourceAPI.getRequest()).then().log().all().extract().response().asString();
            System.out.println(createOrderResponse);
        }

        @And("Order details are viewed with {string}")
        public void order_details_are_viewed_with(String apiName)
        {
            resourceAPI=APIResources.valueOf(apiName);
            System.out.println("Below starts viewing order details-");
            JsonPath js1=Reusable.rawTojson(createOrderResponse);
             orderId= js1.getString("orders[0]");
            RequestSpecification viewOrderBaseRequest=new RequestSpecBuilder().addRequestSpecification(baseSpec).addHeader("authorization",Utils.token).build();
            RequestSpecification viewOrderRequest=given().spec(viewOrderBaseRequest).relaxedHTTPSValidation().log().all().queryParam("id",orderId);
            ViewOrder vo=viewOrderRequest.when().get(resourceAPI.getRequest()).then().log().all().assertThat().statusCode(200).extract().response().as(ViewOrder.class);
        }

        @And("Product is deleted with {string}")
         public void Product_is_deleted_with(String deleteProductApi)
        {
            resourceAPI=APIResources.valueOf(deleteProductApi);
            System.out.println("Below starts deleting product-");
            RequestSpecification delProdBaseReq=new RequestSpecBuilder().addRequestSpecification(baseSpec).addHeader("authorization",Utils.token).build();
            RequestSpecification delProdReq=given().spec(delProdBaseReq).relaxedHTTPSValidation().log().all().pathParam("productId",productId);
            Response delProdRes=delProdReq.when().delete(resourceAPI.getRequest()).then().log().all().assertThat().statusCode(200).extract().response();
            Assert.assertEquals("Product Deleted Successfully", getString(delProdRes,"message"));
        }

        @And("Order is deleted with {string}")
        public void Order_is_deleted_with(String deleteOrderApi)
        {
            resourceAPI=APIResources.valueOf(deleteOrderApi);
            System.out.println("Below starts deleting order-");
            RequestSpecification delOrdBaeReq=new RequestSpecBuilder().addRequestSpecification(baseSpec).addHeader("authorization",Utils.token).build();
            RequestSpecification delOrdReq=given().spec(delOrdBaeReq).relaxedHTTPSValidation().log().all().pathParam("orderId",orderId);
            delOrdReq.when().delete(resourceAPI.getRequest()).then().log().all().assertThat().statusCode(200);
         }


    @Given("Print to see if it runs with  {string} {string}")
    public void Print_to_see_if_it_runs_with(String username, String password)
    {
        Utils.currentUsername=username;
        Utils.currentPassword=password;
        System.out.println("To check tagging");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
    }
}
