package LiveProject;

import au.com.dius.pact.consumer.junit5.PactTestFor;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.authentication;
import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GitHubTest {
    RequestSpecification rq;
    String SSH_KEY="ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCMGBzJv7dM6+zWW0anyXrPRZn7z/jS6+LFGHSqrv5epC2AfadED1TT2tyyaP4c3qnrXck2Mf+OGzZK68uMKR7fbKnD75Cm9PB+p9RkXwHYoQDiW8POfrEhrWOlgKWMcPhsC++RdyWOypozQGvfVucbHNeRGDKvHJ1MS9fLcIzbU0gytDyVtZP8VNl4JaXhEjPXc1QQN/pnDFvv6Hvak4jxPbuJe1S/sLFaGZtrO9uyXw28un77VbxWd7q5KmXDhg0Pa1M4iPeMgiPgaHPgmSYIvkvdsyzd26+8vS+umhGnZX06SPnjrn3MDQq4RoncNRIxtEIlU6CXBL7zsuiCupwh";
    static int tokenId;

    @BeforeEach
    public void BeforeEachTest()
    {
        rq = new RequestSpecBuilder().setBaseUri("https://api.github.com").
                setContentType(ContentType.JSON).
                addHeader("Authorization","token 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").build();
    }

    @Test
    @Order(1)
    public void PostSSHKey() {
        String reqBody = "{"
                + "\"title\": \"TestAPIKey\","
                + "\"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCMGBzJv7dM6+zWW0anyXrPRZn7z/jS6+LFGHSqrv5epC2AfadED1TT2tyyaP4c3qnrXck2Mf+OGzZK68uMKR7fbKnD75Cm9PB+p9RkXwHYoQDiW8POfrEhrWOlgKWMcPhsC++RdyWOypozQGvfVucbHNeRGDKvHJ1MS9fLcIzbU0gytDyVtZP8VNl4JaXhEjPXc1QQN/pnDFvv6Hvak4jxPbuJe1S/sLFaGZtrO9uyXw28un77VbxWd7q5KmXDhg0Pa1M4iPeMgiPgaHPgmSYIvkvdsyzd26+8vS+umhGnZX06SPnjrn3MDQq4RoncNRIxtEIlU6CXBL7zsuiCupwh\""
                + "}";
        System.out.println(reqBody);
    Response response;
        response = given().spec(rq).body(reqBody).when().post("/user/keys");
        String body = response.getBody().asPrettyString();
    System.out.println(body);
        tokenId=response.then().extract().path("id");
        System.out.println(tokenId);
        assert (response.getStatusCode() == 201);
    }


    @Test
    @Order(2)
    public void GetAllSSHKeys() {

        Response response = given().spec(rq).pathParam("KeyId",tokenId).when().get("/user/keys/{KeyId}");
        String body = response.getBody().asPrettyString();
        System.out.println(body);
        assert (response.getStatusCode() == 200);
    }


    @Test
    @Order(3)
    public void DeleteSSHKey() {

        String reqBody = "{"
                + "\"title\": \"TestAPIKey\","
                + "\"key\": SSH_KEY,"
                + "}";

        Response response = given().spec(rq).pathParam("KeyId",tokenId).when().delete("/user/keys/{KeyId}");
        String body = response.getBody().asPrettyString();
        System.out.println(body);
        assert (response.getStatusCode() == 204);
    }
}
