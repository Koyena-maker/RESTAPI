package Resources;

import Pojo.LoginRequest;

public class TestData
{
public LoginRequest loginPayload(String username,String password)
{
    LoginRequest lr = new LoginRequest();
    lr.setUserEmail(username);
    lr.setUserPassword(password);
    return lr;
}
}
