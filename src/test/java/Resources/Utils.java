package Resources;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.io.*;
import java.util.Properties;

public class Utils {
    public static RequestSpecification reqObject;
    public static String token;
    public static String userId;
    public static String currentUsername;
    public static String currentPassword;
    public RequestSpecification commonForAll() throws IOException {
        if(reqObject==null) {
            PrintStream ps = new PrintStream(new FileOutputStream("request.txt"));
            PrintStream ps1 = new PrintStream(new FileOutputStream("response.txt"));
            reqObject = new RequestSpecBuilder().setBaseUri(getGlobalProperties("baseUrl").trim()).
                    addFilter(RequestLoggingFilter.logRequestTo(ps))
                    .addFilter(ResponseLoggingFilter.logResponseTo(ps1)).build();

            return reqObject;
        }
        return reqObject;
    }
    public String getGlobalProperties(String key) throws IOException {
        Properties prop=new Properties();
        FileInputStream fi=new FileInputStream("C:\\Users\\HP\\MyProject\\Apiframework\\src\\test\\java\\Resources\\globalProperties.properties");
        prop.load(fi);
        return prop.getProperty(key);

    }
}
