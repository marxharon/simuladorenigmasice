package com.example.enigma.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class EnigmaRequest {
    @NotBlank
    private String text;
    @NotNull
    private List<RotorSpec> rotors;
    @NotNull
    private ReflectorSpec reflector;
    private String plugboard; // e.g., "AB CD EF" pairs

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<RotorSpec> getRotors() { return rotors; }
    public void setRotors(List<RotorSpec> rotors) { this.rotors = rotors; }

    public ReflectorSpec getReflector() { return reflector; }
    public void setReflector(ReflectorSpec reflector) { this.reflector = reflector; }

    public String getPlugboard() { return plugboard; }
    public void setPlugboard(String plugboard) { this.plugboard = plugboard; }
}
