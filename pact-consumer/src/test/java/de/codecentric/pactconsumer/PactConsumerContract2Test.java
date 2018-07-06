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
import java.util.UUID;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class PactConsumerContract2Test {

    @Rule
    public PactProviderRuleMk2 mockProvider
            = new PactProviderRuleMk2("test_provider", "localhost", 8080, this);

    @Pact(consumer = "test_consumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        return builder
                .given("Sending a POST")
                    .uponReceiving("POST")
                    .path("/post")
                    .method("POST")
                    .body(newJsonBody(lambdaDslJsonBody -> lambdaDslJsonBody
                            .stringMatcher("name", "[a-zA-Z]*")
                            .nullValue("id"))
                            .build())
                    .willRespondWith()
                        .status(200)
                        .headers(headers)
                        .body(newJsonBody(lambdaDslJsonBody ->
                                lambdaDslJsonBody
                                        .stringMatcher("id", "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}"))
                                .build())
                .toPact();
    }

    @Test
    @PactVerification
    public void should_accept_post(){
        ResponseEntity<ObjectNode> response = new RestTemplate().postForEntity("http://localhost:8080/post", new SimpleClass("name"),ObjectNode.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThatCode(() -> UUID.fromString(response.getBody().get("id").textValue())).doesNotThrowAnyException();
    }
}
