package de.codecentric.pactconsumer;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.assertj.core.api.Assertions.assertThat;

public class PactConsumerContractTest {

    @Rule
    public PactProviderRuleMk2 mockProvider
            = new PactProviderRuleMk2("test_provider", "localhost", 8080, this);

    @Pact(consumer = "test_consumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        return builder
                .given("test GET")
                    .uponReceiving("GET REQUEST")
                    .path("/pact")
                    .method("GET")
                    .willRespondWith()
                    .status(200)
                    .headers(headers)
                    .body(newJsonBody((body) -> body.numberType("foo")).build())
                .toPact();
    }

    @Test
    @PactVerification
    public void should_respond(){
        ResponseEntity<ObjectNode> response = new RestTemplate().getForEntity("http://localhost:8080/pact", ObjectNode.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody().toString()).isEqualTo("{\"foo\":\"bar\"}");
    }
}
