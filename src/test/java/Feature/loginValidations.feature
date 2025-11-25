Feature: Validating login API's
  @PlaceOrder @Regression
  Scenario Outline: Verify if login is being successfully done using LoginAPI
    Given Add Login Payload with  "<username>" "<password>"
    When user calls "LoginAPI" with Post http request
    Then the Api call is success with status code 200
    And "message" in response body is "Login Successfully"
    And Token is extracted and product is added with "AddProductAPI"
    Then Order is created with "CreateOrderAPI"
    And Order details are viewed with "ViewOrderAPI"
    And Product is deleted with "DeleteProductAPI"
    And Order is deleted with "DeleteOrderAPI"
Examples:
         |username|password|
         |jay@yopmail.com|Host@1234|
         |rik@yopmail.com|Host@12345|

@Tagging  @Regression
  Scenario Outline: Print message
    Given Print to see if it runs with  "<username>" "<password>"
  Examples:
            |username|password|
            |jay@yopmail.com|Host@1234|
            |rik@yopmail.com|Host@12345|