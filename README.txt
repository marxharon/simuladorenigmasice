README.txt
============

Simulador da Máquina Enigma — Projeto Maven com REST (Spring Boot) + Frontend Estático
--------------------------------------------------------------------------------------

### Como executar

1) Requisitos:
   - Java 17+
   - Maven 3.9+

2) Build e execução:

   ```bash
   mvn spring-boot:run
   ```

3) Acesse o frontend:
   - http://localhost:8080/

4) API REST:
   - POST http://localhost:8080/api/enigma/encode
   - Exemplo de payload:
     ```json
     {
       "text": "HELLO WORLD",
       "rotors": [
         {"wiring":"EKMFLGDQVZNTOWYHXUSPAIBRCJ","position":"A","ringSetting":1,"notch":"Q"},
         {"wiring":"AJDKSIRUXBLHWTMCQGZNPYFVOE","position":"A","ringSetting":1,"notch":"E"},
         {"wiring":"BDFHJLCPRTXVZNYEIWGAKMUSQO","position":"A","ringSetting":1,"notch":"V"}
       ],
       "reflector": {"wiring":"YRUHQSLDPXNGOKMIEBFZCWVJAT","name":"B"},
       "plugboard": "AB CD EF"
     }
     ```

### Algoritmo (passo a passo)

Abaixo está o fluxo completo que o código do serviço executa para cada caractere A..Z da mensagem. Caracteres fora de A..Z são retornados sem alteração.

1. **Normalização**: converta o texto para maiúsculas.
2. **Stepping (avanço dos rotores)** a cada tecla pressionada:
   - O rotor direito (R1) **sempre** avança uma posição.
   - Se o rotor direito está em seu **notch** (posição de virada), o rotor do meio (R2) também avança.
   - **Double-step**: se o rotor do meio está em seu notch, ele avança e o rotor esquerdo (R3) também avança.
3. **Plugboard (Steckerbrett)** — opcional:
   - Se configurado, aplique a substituição de pares (ex.: A↔B, C↔D, ...).
4. **Caminho direto (da direita para a esquerda através dos rotores)**:
   - Para cada rotor, calcule um índice levando em conta **posição** (window letter) e **ring setting**:
     - `shifted = (entrada + position - ring) mod 26`
   - Mapeie com a fiação (wiring) do rotor:
     - `mapped = wiring[shifted]` (onde wiring é permutação 0..25)
   - “Desfaça” o deslocamento:
     - `saida = (mapped - position + ring) mod 26`
   - A saída do rotor atual vira a entrada do próximo rotor à esquerda.
5. **Refletor**:
   - Aplique a permutação do refletor (B ou C). Ele mapeia de volta para 0..25.
6. **Caminho de volta (da esquerda para a direita através dos rotores)**:
   - Percorra os mesmos rotores, agora ao contrário, usando a fiação **invertida**:
     - `shifted = (entrada + position - ring) mod 26`
     - `mapped = inverseWiring[shifted]`
     - `saida = (mapped - position + ring) mod 26`
7. **Plugboard novamente** (se houver).
8. **Converta 0..25 para letra A..Z** e concatene ao resultado.
9. **Simetria**: com as mesmas configurações iniciais, aplicar o algoritmo novamente no texto cifrado recupera o texto original.

### Valores padrão úteis

- **Rotores clássicos**:
  - I   wiring: EKMFLGDQVZNTOWYHXUSPAIBRCJ, notch: Q
  - II  wiring: AJDKSIRUXBLHWTMCQGZNPYFVOE, notch: E
  - III wiring: BDFHJLCPRTXVZNYEIWGAKMUSQO, notch: V
  - IV  wiring: ESOVPZJAYQUIRHXLNFTGKDCMWB, notch: J
  - V   wiring: VZBRGITYUPSDNHLXAWMJQOFECK, notch: Z

- **Refletores**:
  - B: YRUHQSLDPXNGOKMIEBFZCWVJAT
  - C: FVPJIAOYEDRZXWGCTKUQSBNMHL

### Observações

- Este simulador inclui **ring setting (Ringstellung)** e a mecânica de **double-step**.
- A API é **simétrica** (não há endpoint separado para decrypt).
- Você pode colar outros rotores/reflectores nos campos “wiring” do frontend, desde que tenham 26 letras A..Z.

Bom uso! :)
