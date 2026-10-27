/* ============================================================
   El Azar · Prototipo navegable
   Lógica mínima para recorrer todas las pantallas y probar los
   flujos principales sin backend.
   ============================================================ */

(() => {
  'use strict';

  const $ = (selector, root = document) => root.querySelector(selector);
  const $$ = (selector, root = document) => Array.from(root.querySelectorAll(selector));

  const state = {
    user: 'usuario.G',
    balance: 1250,
    diceCount: 3,
    rolls: 4,
    snakesPosition: 12,
    snakesRound: 5,
    rpsWins: 2,
    rpsLosses: 1,
    rpsRound: 5,
    shooterScore: 120,
    shooterAmmo: 6,
    aimDistance: 0
  };

  const money = (value) =>
    '$ ' + Number(value).toLocaleString('en-US', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });

  /* ================= Pantallas ================= */
  const screens = $$('.screen').map((screen) => screen.id);

  function syncHash(id, replace = true) {
    const hash = '#' + id;
    if (location.hash === hash) return;
    if (replace) history.replaceState(null, '', hash);
    else location.hash = hash;
  }

  function openScreen(id) {
    const target = document.getElementById(id);
    if (!target) return;

    $$('.screen').forEach((screen) => {
      const active = screen === target;
      screen.classList.toggle('active', active);
      screen.setAttribute('aria-hidden', active ? 'false' : 'true');
    });

    target.scrollTop = 0;
    closeDrawer();
    closeOptions();
    markDrawerItem(id);
    syncHash(id);
  }

  function openRoute(id) {
    if (id === 'drawer') {
      openScreen('dashboard');
      openDrawer();
    } else if (id === 'options') {
      openScreen('dashboard');
      openOptions();
    } else if (screens.includes(id)) {
      openScreen(id);
    }
  }

  function markDrawerItem(id) {
    $$('.drawer-item[data-go]').forEach((item) => {
      item.classList.toggle('active', item.dataset.go === id);
    });
  }

  function goBack() {
    const current = $('.screen.active');
    openScreen(current?.dataset.back || 'dashboard');
  }

  /* ================= Menú lateral y opciones ================= */
  const currentScreenId = () => $('.screen.active')?.id || 'login';

  function openDrawer() {
    $('#drawer').classList.add('open');
    $('#drawer').setAttribute('aria-hidden', 'false');
    $('#drawerBackdrop').classList.add('show');
    syncHash('drawer');
  }

  function closeDrawer() {
    $('#drawer').classList.remove('open');
    $('#drawer').setAttribute('aria-hidden', 'true');
    $('#drawerBackdrop').classList.remove('show');
    if (location.hash === '#drawer') syncHash(currentScreenId());
  }

  function openOptions() {
    $('#optionsMenu').classList.add('show');
    $('#optionsMenu').setAttribute('aria-hidden', 'false');
    $('#optionsBackdrop').classList.add('show');
    syncHash('options');
  }

  function closeOptions() {
    $('#optionsMenu').classList.remove('show');
    $('#optionsMenu').setAttribute('aria-hidden', 'true');
    $('#optionsBackdrop').classList.remove('show');
    if (location.hash === '#options') syncHash(currentScreenId());
  }

  /* ================= Diálogos y avisos ================= */
  const dialogs = {
    privacy: {
      title: 'Protección de datos',
      body: 'En El Azar resguardamos tu información con cifrado en tránsito y en reposo. Nunca compartimos tus datos de pago con otros jugadores y puedes solicitar la eliminación de tu cuenta en cualquier momento.'
    },
    responsible: {
      title: 'Juego responsable',
      body: 'Juega solo por diversión. Define límites de tiempo y de dinero antes de empezar, y utiliza las herramientas de autoexclusión si lo necesitas. Solo para mayores de 18 años.'
    },
    help: {
      title: 'Ayuda',
      body: 'Soporte: soporte@elazar.mx\nLunes a domingo, 9:00 a 21:00 h.\n\nTambién puedes consultar las preguntas frecuentes desde el menú lateral de la app Android.'
    },
    info: {
      title: 'Acerca de El Azar',
      body: 'El Azar es una mesa de juegos de azar multijugador en tiempo real con salas de apuesta. Versión 1.1 del prototipo navegable.\n\nProyecto académico de Interfaces de Usuario, Layouts, Estilos, Temas y Menús.'
    },
    profile: {
      title: 'Mi perfil',
      body: `@${state.user}\nJugador verificado\n\nNombre: Jugador de El Azar\nCorreo: usuario@elazar.mx\nSaldo: ${money(state.balance)}\n\nEn el APK puedes editar estos datos desde “Mi perfil”. En este prototipo web se muestra el resumen.`
    },
    history: {
      title: 'Historial de partidas',
      body: `Tus últimas partidas:\n\n• Ruleta · Hoy, 10:22 · +$ 140.00\n• Piedra, papel o tijera · Hoy, 09:48 · +$ 80.00\n• Dados M · Ayer, 21:40 · +$ 120.00\n• Serpientes y Escaleras · Hace 2 días · +$ 50.00\n• Disparos · Hace 2 días · −$ 30.00\n• Mano de Póker · Hace 3 días · −$ 20.00`
    },
    forgot: {
      title: 'Recuperar contraseña',
      body: 'Escribe tu correo en el inicio de sesión y te enviaremos un enlace para restablecer tu contraseña. En este prototipo el envío es simulado.'
    }
  };

  function openDialog(key) {
    const data = dialogs[key] || dialogs.info;
    $('#dialogTitle').textContent = data.title;
    $('#dialogBody').textContent = data.body;
    $('#dialogBackdrop').classList.add('show');
    closeDrawer();
    closeOptions();
  }

  function closeDialog() {
    $('#dialogBackdrop').classList.remove('show');
  }

  let toastTimer;
  function showToast(message) {
    const toast = $('#toast');
    toast.textContent = message;
    toast.classList.add('show');
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => toast.classList.remove('show'), 2400);
  }

  /* ================= Datos compartidos ================= */
  function updateSharedData() {
    $$('.js-balance').forEach((node) => { node.textContent = money(state.balance); });
    $$('.js-user').forEach((node) => { node.textContent = state.user; });
  }

  /* ================= Dados ================= */
  const PIP_PATTERNS = {
    1: [4],
    2: [0, 8],
    3: [0, 4, 8],
    4: [0, 2, 6, 8],
    5: [0, 2, 4, 6, 8],
    6: [0, 2, 3, 5, 6, 8]
  };

  function renderDie(element, face, animate = false) {
    if (!element) return;
    if (!element.querySelector('.pip')) {
      for (let i = 0; i < 9; i += 1) {
        const pip = document.createElement('span');
        pip.className = 'pip';
        element.appendChild(pip);
      }
    }
    const active = new Set(PIP_PATTERNS[face] || []);
    $$('.pip', element).forEach((pip, index) => {
      pip.classList.toggle('on', active.has(index));
    });
    element.dataset.face = String(face);
    if (animate) {
      element.classList.remove('spin');
      void element.offsetWidth;
      element.classList.add('spin');
    }
  }

  function updateDiceBet() {
    $('#diceBet').textContent = `Apuesta total: ${money(state.diceCount * 10)}`;
  }

  function setDiceCount(count) {
    state.diceCount = count;
    $$('[data-dice-count]').forEach((chip) => {
      chip.classList.toggle('active', Number(chip.dataset.diceCount) === count);
    });
    $('#die2').style.display = count >= 2 ? 'grid' : 'none';
    $('#die3').style.display = count >= 3 ? 'grid' : 'none';
    updateDiceBet();
  }

  function rollDice() {
    const values = Array.from({ length: state.diceCount }, () => 1 + Math.floor(Math.random() * 6));
    const dice = [$('#die1'), $('#die2'), $('#die3')];
    values.forEach((value, index) => renderDie(dice[index], value, true));
    $('#diceEquation').textContent = values.join(' + ');
    $('#diceTotal').textContent = String(values.reduce((sum, value) => sum + value, 0));
    state.rolls += 1;
    $('#diceRolls').textContent = `Lanzamientos: ${state.rolls}`;

    const total = values.reduce((sum, value) => sum + value, 0);
    const stake = state.diceCount * 10;
    if (total > state.diceCount * 3.5) {
      state.balance += stake;
      showToast(`¡Ganaste la ronda! +${money(stake)}`);
    } else {
      state.balance = Math.max(0, state.balance - stake);
      showToast(`Esta vez perdiste tu apuesta: −${money(stake)}`);
    }
    updateSharedData();
  }

  /* ================= Serpientes y Escaleras ================= */
  const snakes = { 17: 6, 24: 11, 33: 21, 36: 28 };
  const ladders = { 3: 12, 8: 15, 19: 27, 30: 34 };

  function numberAt(gridRow, col) {
    const rowFromBottom = 5 - gridRow;
    const realCol = rowFromBottom % 2 === 1 ? 5 - col : col;
    return rowFromBottom * 6 + realCol + 1;
  }

  function buildSnakesBoard() {
    const board = $('#snakesBoard');
    if (!board || board.childElementCount) return;
    for (let row = 0; row < 6; row += 1) {
      for (let col = 0; col < 6; col += 1) {
        const number = numberAt(row, col);
        const cell = document.createElement('div');
        cell.className = 'cell';
        cell.dataset.number = String(number);
        if (snakes[number]) {
          cell.classList.add('snake');
          cell.textContent = `🐍 ${number}`;
        } else if (ladders[number]) {
          cell.classList.add('ladder');
          cell.textContent = `🪜 ${number}`;
        } else {
          cell.textContent = String(number);
        }
        board.appendChild(cell);
      }
    }
    paintSnakes();
  }

  function paintSnakes() {
    $$('#snakesBoard .cell').forEach((cell) => {
      const number = Number(cell.dataset.number);
      cell.classList.toggle('player', number === state.snakesPosition);
      if (number === state.snakesPosition) {
        cell.textContent = `★ ${number}`;
      } else if (snakes[number]) {
        cell.textContent = `🐍 ${number}`;
      } else if (ladders[number]) {
        cell.textContent = `🪜 ${number}`;
      } else {
        cell.textContent = String(number);
      }
    });
    $('#snakesPosition').textContent = `Casilla ${state.snakesPosition}`;
  }

  function rollSnakes() {
    const value = 1 + Math.floor(Math.random() * 6);
    renderDie($('#snakesDie'), value, true);
    let target = Math.min(36, state.snakesPosition + value);
    let message = `Avanzas ${value} casillas.`;

    if (snakes[target]) {
      target = snakes[target];
      message = `¡Serpiente! bajas a la casilla ${target} 🐍`;
    } else if (ladders[target]) {
      target = ladders[target];
      message = `¡Escalera! subes a la casilla ${target} 🪜`;
    }

    state.snakesPosition = target;
    state.snakesRound += 1;
    $('#snakesRound').textContent = `Ronda ${state.snakesRound}`;
    $('#snakesMove').textContent = `${message} · Mi posición: casilla ${target}`;
    paintSnakes();

    if (target >= 36) {
      showToast('¡Ganaste la partida! 🏆');
      state.snakesPosition = 1;
      setTimeout(paintSnakes, 700);
    }
  }

  /* ================= Piedra, papel o tijera ================= */
  const gestureAssets = {
    rock: 'assets/ic_gesture_rock.png',
    paper: 'assets/ic_gesture_paper.png',
    scissors: 'assets/ic_gesture_scissors.png'
  };

  function currentGesture() {
    return $('#rpsGestures input[name="gesture"]:checked')?.value || null;
  }

  function currentRpsBet() {
    return Number($('#rpsBets input[name="bet"]:checked')?.value || 10);
  }

  function updateRpsBet() {
    $('#rpsBet').textContent = `Apuesta: ${money(currentRpsBet())}`;
  }

  function previewGesture() {
    const gesture = currentGesture();
    if (gesture) $('#rpsPlayerMove').src = gestureAssets[gesture];
  }

  function playRps() {
    const player = currentGesture();
    if (!player) {
      showToast('Primero selecciona una jugada.');
      return;
    }

    const options = ['rock', 'paper', 'scissors'];
    const opponent = options[Math.floor(Math.random() * options.length)];
    $('#rpsPlayerMove').src = gestureAssets[player];
    $('#rpsOpponentMove').src = gestureAssets[opponent];

    const draw = player === opponent;
    const win =
      (player === 'rock' && opponent === 'scissors') ||
      (player === 'paper' && opponent === 'rock') ||
      (player === 'scissors' && opponent === 'paper');

    const stake = currentRpsBet() * ($('#rpsAll').checked ? 2 : 1);
    const tiebreak = $('.tiebreak');
    tiebreak.style.visibility = draw ? 'visible' : 'hidden';

    if (draw) {
      $('#rpsResult').textContent = 'Empate, se repite la ronda.';
      $('#rpsResult').style.color = 'var(--orange-light)';
    } else if (win) {
      state.rpsWins += 1;
      state.balance += stake;
      $('#rpsResult').textContent = `¡Ganaste! +${money(stake)}`;
      $('#rpsResult').style.color = 'var(--green)';
    } else {
      state.rpsLosses += 1;
      state.balance = Math.max(0, state.balance - stake);
      $('#rpsResult').textContent = `Perdiste la ronda: −${money(stake)}`;
      $('#rpsResult').style.color = 'var(--orange-light)';
    }

    $('#rpsSub').textContent = $('#rpsAll').checked
      ? 'Todo o nada: apuesta duplicada'
      : 'Apuesta normal';
    $('#rpsScore').textContent = `${state.rpsWins} - ${state.rpsLosses}`;
    state.rpsRound += 1;
    $('#rpsRound').textContent = `Ronda ${state.rpsRound}`;
    updateSharedData();
  }

  /* ================= Disparos ================= */
  function aimAt(event) {
    const target = $('#shooterTarget');
    const rect = target.getBoundingClientRect();
    const x = Math.min(Math.max(event.clientX - rect.left, 0), rect.width);
    const y = Math.min(Math.max(event.clientY - rect.top, 0), rect.height);
    const crosshair = $('#crosshair');
    crosshair.style.left = `${x}px`;
    crosshair.style.top = `${y}px`;
    crosshair.style.transform = 'translate(-50%, -50%)';
    const dx = x - rect.width / 2;
    const dy = y - rect.height / 2;
    state.aimDistance = Math.hypot(dx, dy) / (rect.width / 2);
  }

  function fire() {
    if (state.shooterAmmo <= 0) {
      state.shooterAmmo = 6;
      $('#shooterAmmo').textContent = '6';
      showToast('Municiones recargadas.');
      return;
    }

    state.shooterAmmo -= 1;
    if (state.aimDistance <= 0.25) {
      state.shooterScore += 20;
      showToast('¡Acierto! +20 puntos 🎯');
    } else if (state.aimDistance <= 0.55) {
      state.shooterScore += 10;
      showToast('¡Acierto! +10 puntos 🎯');
    } else {
      showToast('Fallaste, sigue intentando.');
    }
    $('#shooterScore').textContent = String(state.shooterScore);
    $('#shooterAmmo').textContent = String(state.shooterAmmo);
  }

  /* ================= Pago ================= */
  function setupPayment() {
    const number = $('#payCard');
    const expiry = $('#payExpiry');
    const holder = $('#payHolder');

    holder.addEventListener('input', () => {
      $('#ccHolder').textContent = holder.value.trim().toUpperCase() || 'NOMBRE DEL TITULAR';
    });

    number.addEventListener('input', () => {
      const digits = number.value.replace(/\D/g, '').slice(0, 16);
      number.value = digits.replace(/(.{4})/g, '$1 ').trim();
      const padded = digits.padEnd(16, '•');
      $('#ccNumber').textContent = padded.match(/.{1,4}/g).join(' ');
    });

    expiry.addEventListener('input', () => {
      const digits = expiry.value.replace(/\D/g, '').slice(0, 4);
      expiry.value = digits.length > 2 ? `${digits.slice(0, 2)}/${digits.slice(2)}` : digits;
      $('#ccExpiry').textContent = expiry.value || 'MM/AA';
    });

    $('#ccHolder').textContent = 'NOMBRE DEL TITULAR';
  }

  function confirmPayment() {
    const holder = $('#payHolder').value.trim();
    const digits = $('#payCard').value.replace(/\D/g, '');
    const expiry = $('#payExpiry').value;
    const cvv = $('#payCvv').value;

    if (!holder) return showToast('Escribe el nombre del titular.');
    if (digits.length !== 16) return showToast('El número de tarjeta debe tener 16 dígitos.');
    if (expiry.length !== 5) return showToast('La fecha debe tener el formato MM/AA.');
    if (cvv.length !== 3) return showToast('El CVV debe tener 3 dígitos.');

    showToast(`Tarjeta ****${digits.slice(-4)} registrada correctamente.`);
    openScreen('dashboard');
  }

  /* ================= Tienda ================= */
  function buy(name, price) {
    if (state.balance < price) {
      showToast('Saldo insuficiente para este artículo.');
      return;
    }
    state.balance -= price;
    updateSharedData();
    showToast(`Compraste ${name} 🎉`);
  }

  /* ================= Login / registro ================= */
  function tryLogin(button) {
    const user = $('#loginUser').value.trim();
    const password = $('#loginPassword').value;
    if (!user || !password) {
      showToast('Escribe tu usuario y tu contraseña.');
      return false;
    }
    if (password.length < 4) {
      showToast('Usuario o contraseña incorrectos.');
      return false;
    }
    state.user = user.replace(/^@/, '');
    updateSharedData();
    showToast(`Sesión iniciada como @${state.user} 🎲`);
    return true;
  }

  function registerFromWeb() {
    const inputs = $$('#register .field input');
    const username = inputs[2]?.value.trim().replace(/^@/, '');
    if (username) {
      state.user = username;
      updateSharedData();
    }
    showToast('¡Cuenta creada! Ya puedes jugar.');
  }

  /* ================= Eventos ================= */
  document.addEventListener('click', (event) => {
    const go = event.target.closest('[data-go]');
    if (go) {
      event.preventDefault();
      const target = go.dataset.go;
      const active = $('.screen.active')?.id;

      if (active === 'login' && target === 'dashboard' && go.classList.contains('btn-primary')) {
        if (!tryLogin(go)) return;
      }
      if (active === 'register' && target === 'dashboard') {
        registerFromWeb();
      }
      openScreen(target);
      return;
    }

    const dialogTrigger = event.target.closest('[data-dialog]');
    if (dialogTrigger) {
      event.preventDefault();
      openDialog(dialogTrigger.dataset.dialog);
      return;
    }

    const filter = event.target.closest('[data-filter]');
    if (filter) {
      $$('[data-filter]').forEach((chip) => chip.classList.toggle('active', chip === filter));
      const key = filter.dataset.filter;
      $$('.game-card').forEach((card) => {
        const tags = (card.dataset.tags || '').split(' ').filter(Boolean);
        card.style.display = key === 'all' || tags.includes(key) ? '' : 'none';
      });
      return;
    }

    const diceChip = event.target.closest('[data-dice-count]');
    if (diceChip) {
      setDiceCount(Number(diceChip.dataset.diceCount));
      return;
    }

    const skin = event.target.closest('[data-skin]');
    if (skin) {
      $$('[data-skin]').forEach((chip) => chip.classList.toggle('active', chip === skin));
      const filters = {
        classic: 'none',
        neon: 'hue-rotate(150deg) saturate(1.6)',
        wood: 'sepia(.7) saturate(1.3)',
        gold: 'sepia(.35) saturate(1.8) brightness(1.08)'
      };
      const image = $('#shooterTarget > img');
      if (image) image.style.filter = filters[skin.dataset.skin] || 'none';
      return;
    }

    const action = event.target.closest('[data-action]');
    if (!action) return;
    event.preventDefault();

    switch (action.dataset.action) {
      case 'back':
        goBack();
        break;
      case 'drawer':
        openDrawer();
        break;
      case 'close-drawer':
        closeDrawer();
        break;
      case 'options':
        openOptions();
        break;
      case 'close-options':
        closeOptions();
        break;
      case 'toast':
        showToast(action.dataset.message || 'Acción del prototipo');
        break;
      case 'roll-dice':
        rollDice();
        break;
      case 'roll-snakes':
        rollSnakes();
        break;
      case 'expand-snakes':
        $('.board-frame').classList.toggle('expanded');
        $('.players-title').style.display = $('.board-frame').classList.contains('expanded') ? 'none' : '';
        $('.players-row').style.display = $('.board-frame').classList.contains('expanded') ? 'none' : '';
        action.textContent = $('.board-frame').classList.contains('expanded') ? 'Contraer mapa' : 'Expandir mapa';
        break;
      case 'play-rps':
        playRps();
        break;
      case 'fire':
        fire();
        break;
      case 'confirm-payment':
        confirmPayment();
        break;
      case 'buy':
        buy(action.dataset.name, Number(action.dataset.price));
        break;
      case 'close-dialog':
        closeDialog();
        break;
      case 'share':
        shareApp();
        break;
      case 'logout':
        closeDrawer();
        closeOptions();
        openScreen('login');
        showToast('Sesión cerrada.');
        break;
      default:
        break;
    }
  });

  $('#dialogBackdrop').addEventListener('click', (event) => {
    if (event.target === $('#dialogBackdrop')) closeDialog();
  });

  $('#shooterTarget').addEventListener('click', aimAt);

  $('#rpsGestures').addEventListener('change', previewGesture);
  $('#rpsBets').addEventListener('change', updateRpsBet);
  $('#rpsAll').addEventListener('change', () => {
    $('#rpsSub').textContent = $('#rpsAll').checked
      ? 'Todo o nada: apuesta duplicada'
      : 'Apuesta normal';
  });

  function shareApp() {
    const text = '¡Únete a El Azar y juega conmigo! 🎲';
    if (navigator.share) {
      navigator.share({ title: 'El Azar', text }).catch(() => {});
      return;
    }
    if (navigator.clipboard?.writeText) {
      navigator.clipboard.writeText(text)
        .then(() => showToast('Enlace copiado al portapapeles.'))
        .catch(() => showToast(text));
    } else {
      showToast(text);
    }
  }

  /* ================= Arranque ================= */
  function init() {
    updateSharedData();
    updateDiceBet();
    updateRpsBet();
    renderDie($('#die1'), 5);
    renderDie($('#die2'), 2);
    renderDie($('#die3'), 6);
    renderDie($('#snakesDie'), 4);
    buildSnakesBoard();
    setupPayment();
    setDiceCount(3);

    const initial = location.hash.replace('#', '');
    if (initial === 'drawer' || initial === 'options' || screens.includes(initial)) {
      openRoute(initial);
    } else {
      openScreen('login');
    }

    const now = new Date();
    const time = now.toLocaleTimeString('es-MX', { hour: '2-digit', minute: '2-digit', hour12: false });
    $('.status-time').textContent = time;
  }

  window.addEventListener('hashchange', () => {
    const id = location.hash.replace('#', '');
    if (id === 'drawer' || id === 'options' || screens.includes(id)) openRoute(id);
  });

  init();
})();
