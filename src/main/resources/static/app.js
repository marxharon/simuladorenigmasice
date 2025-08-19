async function runEnigma(){
  const reflMap = {
    B: "YRUHQSLDPXNGOKMIEBFZCWVJAT",
    C: "FVPJIAOYEDRZXWGCTKUQSBNMHL"
  };
  const request = {
    text: document.getElementById('inputText').value,
    rotors: [
      // Left to right (as on the machine): R3, R2, R1
      {
        wiring: document.getElementById('wiring3').value.toUpperCase(),
        position: document.getElementById('pos3').value,
        ringSetting: parseInt(document.getElementById('ring3').value || '1', 10),
        notch: document.getElementById('notch3').value.toUpperCase()
      },
      {
        wiring: document.getElementById('wiring2').value.toUpperCase(),
        position: document.getElementById('pos2').value,
        ringSetting: parseInt(document.getElementById('ring2').value || '1', 10),
        notch: document.getElementById('notch2').value.toUpperCase()
      },
      {
        wiring: document.getElementById('wiring1').value.toUpperCase(),
        position: document.getElementById('pos1').value,
        ringSetting: parseInt(document.getElementById('ring1').value || '1', 10),
        notch: document.getElementById('notch1').value.toUpperCase()
      }
    ],
    reflector: {
      wiring: reflMap[document.getElementById('reflector').value],
      name: document.getElementById('reflector').value
    },
    plugboard: document.getElementById('plugboard').value
  };

  const status = document.getElementById('status');
  status.textContent = 'Processando...';
  try{
    const resp = await fetch('/api/enigma/encode', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(request)
    });
    const data = await resp.json();
    document.getElementById('outputText').value = data.result || '';
    status.textContent = 'OK';
  }catch(e){
    console.error(e);
    status.textContent = 'Erro ao processar';
  }
}

document.getElementById('btnEncrypt').addEventListener('click', runEnigma);
