package com.example.enigma.web;

import com.example.enigma.model.EnigmaRequest;
import com.example.enigma.model.EnigmaResponse;
import com.example.enigma.service.EnigmaMachine;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enigma")
@CrossOrigin
public class EnigmaController {

    @PostMapping(value = "/encode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EnigmaResponse encode(@Valid @RequestBody EnigmaRequest request) {
        EnigmaMachine machine = new EnigmaMachine(request);
        String result = machine.process(request.getText());
        return new EnigmaResponse(result);
    }
}
