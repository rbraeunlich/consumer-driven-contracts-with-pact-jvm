package de.codecentric.pactprovider;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

@RestController
public class FooController {

    private Random random = new Random(new Date().getTime());

    @GetMapping(path = "/pact", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> foo() {
        int nextInt = random.nextInt();
        return new ResponseEntity<>("{\"foo\":" + nextInt + "}", HttpStatus.OK);
    }

    @PostMapping(path = "/post", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> acceptPost(SimpleClass simpleClass){
        UUID id = UUID.randomUUID();
        simpleClass.setId(id);
        //persist
        return new ResponseEntity<>("{\"id\":\"" + id + "\"}", HttpStatus.OK);
    }
}
