package com.example.enigma.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ReflectorSpec {
    /** 26-letter permutation, e.g. Reflector B: YRUHQSLDPXNGOKMIEBFZCWVJAT */
    @NotBlank
    @Pattern(regexp = "^[A-Z]{26}$", message = "wiring must be 26 uppercase letters A-Z")
    private String wiring;
    private String name;

    public String getWiring() { return wiring; }
    public void setWiring(String wiring) { this.wiring = wiring; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
