package StepDefinition;


import Resources.Utils;
import io.cucumber.java.After;


import java.io.IOException;

public class Hooks
{
    @After("@Tagging")
    public void  afterScenario() throws IOException {
        System.out.println("I am from hook class");
        stepDefinitions sd=new stepDefinitions() ;
        sd.add_login_payload_with(Utils.currentUsername,Utils.currentPassword);
        sd.user_calls_with_Post_http_request("LoginAPI");

    }
}
