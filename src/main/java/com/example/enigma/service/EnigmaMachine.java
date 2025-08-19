package com.example.enigma.service;

import com.example.enigma.model.EnigmaRequest;
import com.example.enigma.model.RotorSpec;
import java.util.*;

public class EnigmaMachine {
    private final List<Rotor> rotors = new ArrayList<>(); // rightmost first
    private final int[] reflector;
    private final int[] plugboard; // mapping 0..25

    public EnigmaMachine(EnigmaRequest req) {
        // Plugboard
        plugboard = new int[26];
        for (int i = 0; i < 26; i++) plugboard[i] = i;
        if (req.getPlugboard() != null && !req.getPlugboard().isBlank()) {
            String[] pairs = req.getPlugboard().toUpperCase(Locale.ROOT).replaceAll("[^A-Z ]","").trim().split("\s+");
            for (String p : pairs) {
                if (p.length() == 2) {
                    int a = p.charAt(0) - 'A';
                    int b = p.charAt(1) - 'A';
                    if (a >= 0 && a < 26 && b >= 0 && b < 26 && a != b) {
                        int aa = plugboard[a], bb = plugboard[b];
                        // unmap existing
                        plugboard[aa] = aa;
                        plugboard[bb] = bb;
                        plugboard[a] = b;
                        plugboard[b] = a;
                    }
                }
            }
        }

        // Rotors (right to left order)
        List<RotorSpec> specs = req.getRotors();
        Collections.reverse(specs); // user sends left-to-right, we store right-to-left
        for (RotorSpec spec : specs) {
            rotors.add(new Rotor(spec.getWiring(), spec.getPosition().charAt(0), spec.getRingSetting(), spec.getNotch().charAt(0)));
        }

        // Reflector mapping
        reflector = wiringToMap(req.getReflector().getWiring());
    }

    public String process(String textRaw) {
        StringBuilder out = new StringBuilder();
        for (char ch : textRaw.toUpperCase(Locale.ROOT).toCharArray()) {
            if (ch < 'A' || ch > 'Z') {
                out.append(ch);
                continue;
            }
            stepRotors(); // stepping before encoding per Enigma behavior

            int c = ch - 'A';
            c = plugboard[c];

            // forward through rotors (rightmost to leftmost)
            for (Rotor r : rotors) c = r.forward(c);

            // reflector
            c = reflector[c];

            // backward through rotors (leftmost back to rightmost)
            for (int i = rotors.size() - 1; i >= 0; i--) c = rotors.get(i).backward(c);

            c = plugboard[c];

            out.append((char) (c + 'A'));
        }
        return out.toString();
    }

    private void stepRotors() {
        // Double-stepping mechanism
        boolean stepMiddle = false;
        boolean stepLeft = false;

        if (rotors.size() >= 2) {
            Rotor right = rotors.get(0);
            Rotor middle = rotors.get(1);

            // if right at notch, middle will step
            if (right.atNotch()) stepMiddle = true;

            // double-step: if middle at notch, it will step AND cause left to step
            if (middle.atNotch()) { stepMiddle = true; stepLeft = true; }
        }

        // always step rightmost
        rotors.get(0).step();
        if (stepMiddle && rotors.size() >= 2) rotors.get(1).step();
        if (stepLeft && rotors.size() >= 3) rotors.get(2).step();
    }

    // ----- helpers -----
    private static int[] wiringToMap(String wiring) {
        int[] map = new int[26];
        for (int i = 0; i < 26; i++) map[i] = wiring.charAt(i) - 'A';
        return map;
    }

    // Rotor class with ring setting and position
    static class Rotor {
        private final int[] map;      // forward mapping (0..25)
        private final int[] invMap;   // backward mapping
        private int position;         // 0..25 window letter
        private final int ring;       // ring setting 0..25 (ringSetting-1)
        private final int notch;      // 0..25

        Rotor(String wiring, char posLetter, int ringSetting, char notchLetter) {
            this.map = wiringToMap(wiring);
            this.invMap = new int[26];
            for (int i = 0; i < 26; i++) invMap[map[i]] = i;
            this.position = (posLetter - 'A' + 26) % 26;
            this.ring = ((ringSetting - 1) % 26 + 26) % 26;
            this.notch = (notchLetter - 'A' + 26) % 26;
        }

        void step() { position = (position + 1) % 26; }

        boolean atNotch() { return position == notch; }

        int forward(int c) {
            int shifted = (c + position - ring + 26) % 26;
            int mapped = map[shifted];
            return (mapped - position + ring + 26) % 26;
        }

        int backward(int c) {
            int shifted = (c + position - ring + 26) % 26;
            int mapped = invMap[shifted];
            return (mapped - position + ring + 26) % 26;
        }
    }
}
