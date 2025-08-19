package com.example.enigma.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RotorSpec {
    /** 26-letter wiring mapping from A..Z to letters, e.g. Rotor I: EKMFLGDQVZNTOWYHXUSPAIBRCJ */
    @NotBlank
    @Pattern(regexp = "^[A-Z]{26}$", message = "wiring must be 26 uppercase letters A-Z")
    private String wiring;
    /** Initial rotor window letter A..Z */
    @NotBlank
    @Pattern(regexp = "^[A-Z]$", message = "position must be a single uppercase letter A-Z")
    private String position;
    /** Ring setting 1..26 (default 1) */
    private int ringSetting = 1;
    /** Turnover notch letter A..Z (e.g. Q for Rotor I) */
    @NotBlank
    @Pattern(regexp = "^[A-Z]$", message = "notch must be a single uppercase letter A-Z")
    private String notch;

    public String getWiring() { return wiring; }
    public void setWiring(String wiring) { this.wiring = wiring; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public int getRingSetting() { return ringSetting; }
    public void setRingSetting(int ringSetting) { this.ringSetting = ringSetting; }

    public String getNotch() { return notch; }
    public void setNotch(String notch) { this.notch = notch; }
}
