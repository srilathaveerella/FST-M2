package LiveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    Map<String, String> headers = new HashMap<String, String>();

    String createUser = "/api/users";

    @Pact(provider = "UserProvider", consumer = "UserConsumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) throws ParseException {
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        DslPart d=new PactDslJsonBody()
                .numberType("id")
                .stringType("firstName")
                .stringType("lastName")
                .stringType("email");
        return builder.given("request to create a user").uponReceiving("request to create a user")
                .path(createUser).method("POST").headers(headers)
                .body(d).willRespondWith()
                .status(201).body(d).toPact();

    }


    @Test
    @PactTestFor(providerName = "UserProvider", port = "8282")
    public void runTest() {
        RestAssured.baseURI = "http://localhost:8282";
        RequestSpecification rq = RestAssured.given().headers(headers).when();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 1);
        map.put("firstName", "Justin");
        map.put("lastName", "Case");
        map.put("email", "justincase@mail.com");

        Response response = rq.body(map).post(createUser);
        assert (response.getStatusCode() == 201);
    }
}
