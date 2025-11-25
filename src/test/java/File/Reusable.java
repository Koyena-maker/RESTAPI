package File;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public  class Reusable
{
    public static JsonPath rawTojson(String response)
    {
        JsonPath js = new JsonPath(response);
        return js;
    }
    public static String getString(Response response, String key)
    {
        String resp=response.asString();
        JsonPath js = new JsonPath(resp);
        return js.getString(key);
    }

}
