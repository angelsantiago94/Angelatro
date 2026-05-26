# Angelatro – Hoja de Ruta de Desarrollo

> Marca cada tarea con `[x]` al completarla.  
> Cada fase debe tener todos sus tests en verde antes de pasar a la siguiente.

---

## Fase 0 — Configuración del proyecto

- [x] Generar proyecto con `gdx-liftoff`: módulos `core`, `desktop`, `android`
- [x] Configurar Java 17 en `build.gradle`
- [x] Establecer `minSdkVersion 26` y `targetSdkVersion 34` en el módulo Android
- [x] Añadir dependencia Gson al módulo `core` (`com.google.code.gson:gson:2.10.1`)
- [x] Añadir dependencia JUnit 5 al módulo `core` (solo `testImplementation`)
- [x] Verificar que el proyecto compila y lanza la ventana de escritorio vacía
- [x] Verificar que un test JUnit vacío pasa sin entorno gráfico
- [x] Inicializar repositorio Git y hacer el primer commit
- [x] Crear `README.md` con instrucciones básicas de compilación

---

## Fase 1 — Modelo de cartas y configuración de mazos

**Paquete:** `io.angellsan94.angelatro.logic.model`

### DeckType

`DeckType` es un enum que encapsula la configuración inicial de una partida. Es la única fuente de verdad para los valores de arranque; ninguna otra clase debe tener esos números hardcodeados.

Cada constante del enum expone:
- `getInitialMoney()` → saldo inicial de `Wallet`
- `getBonusChips()` → chips extra que `ScoreEngine` suma a cada mano durante toda la partida
- `getBonusMult()` → mult extra que `ScoreEngine` suma a cada mano durante toda la partida
- `getId()` → identificador de cadena para persistencia en JSON (ej. `"STANDARD"`)

Clases que interactúan con `DeckType`:
- **`GameSession`**: recibe el `DeckType` elegido en el menú y lo pasa a `Wallet` y `ScoreEngine` al iniciar partida.
- **`Wallet`**: llama a `deckType.getInitialMoney()` en su constructor para fijar el saldo inicial.
- **`ScoreEngine`**: recibe `bonusChips` y `bonusMult` del `DeckType` activo y los suma en cada cálculo, **antes** de aplicar los jokers.
- **`SaveManager`**: guarda el `id` del `DeckType` en `savegame.json` para reconstruir la sesión al cargar.
- **`UnlockService`**: gestiona qué `DeckType`s están disponibles para seleccionar (los bloqueados no aparecen en el menú de selección).

```
Flujo de selección:
MenuSelecciónMazo → elige DeckType
    → GameSession.startNewGame(deckType)
        → new Wallet(deckType.getInitialMoney())
        → ScoreEngine.setBonuses(deckType.getBonusChips(), deckType.getBonusMult())
        → SaveManager guarda deckType.getId() en savegame.json
```

Tareas:
- [x] Crear enum `DeckType` con las 4 constantes iniciales (STANDARD, WEALTHY, POWERED, MULTIBASE)
  - [x] Test: `DeckType.STANDARD.getInitialMoney()` → 4
  - [x] Test: `DeckType.WEALTHY.getInitialMoney()` → 8
  - [x] Test: `DeckType.POWERED.getBonusChips()` → 10
  - [x] Test: `DeckType.MULTIBASE.getBonusMult()` → 2
  - [x] Test: `DeckType.STANDARD.getBonusChips()` → 0 y `getBonusMult()` → 0
  - [x] Test: todos los `DeckType` tienen un `getId()` único y no nulo
  - [x] Test: `DeckType.fromId("STANDARD")` devuelve `DeckType.STANDARD` (necesario para deserializar)
  - [x] Test: `DeckType.fromId("id_inexistente")` lanza excepción o devuelve `Optional.empty()`

### Modelo de cartas

- [x] Crear excepción `DeckEmptyException` en el paquete io.angellsan94.angelatro.expections
- [x] Crear enum `Suit` (HEARTS, DIAMONDS, CLUBS, SPADES)
- [x] Crear enum `Rank` con valor en chips por rango (2=2 … As=11)
  - [x] Test: `Rank.ACE.getChips()` devuelve 11
  - [x] Test: `Rank.KING.getChips()` devuelve 10
  - [x] Test: `Rank.TWO.getChips()` devuelve 2
- [x] Crear clase `Card(Rank, Suit)`
  - [x] Test: `card.getRankChips()` delega en `Rank`
  - [x] Test: dos cartas con mismo rango y palo son iguales (`equals`/`hashCode`)
- [x] Crear clase `Deck`
  - [x] Test: un mazo nuevo tiene exactamente 52 cartas
  - [x] Test: el mazo contiene exactamente 4 cartas de cada rango
  - [x] Test: `shuffle()` no pierde ni duplica cartas
  - [x] Test: `draw()` reduce el tamaño en 1
  - [x] Test: `draw()` sobre mazo vacío lanza `DeckEmptyException`
  - [x] Test: `isEmpty()` es true cuando no quedan cartas


---

## Fase 2 — Evaluador de manos

**Paquete:** `core/io/angellsan94/angelatrologic/game`

- [x] Crear enum `HandType` con los 10 tipos (Carta Alta … Escalera Real)
- [x] Crear clase `HandEvaluator`
  - **Carta Alta**
    - [x] Test: 5 cartas sin combinación → CARTA_ALTA
  - **Pareja**
    - [x] Test: exactamente dos cartas del mismo rango → PAREJA
  - **Doble Pareja**
    - [x] Test: dos pares distintos → DOBLE_PAREJA
  - **Trío**
    - [x] Test: tres cartas del mismo rango → TRIO
  - **Escalera**
    - [x] Test: 5 rangos consecutivos de distintos palos → ESCALERA
    - [x] Test: escalera con As bajo (A-2-3-4-5) → ESCALERA
    - [x] Test: escalera con As alto (10-J-Q-K-A) se evalúa después como Escalera Real si mismo palo
  - **Color**
    - [x] Test: 5 cartas del mismo palo sin escalera → COLOR
  - **Full House**
    - [x] Test: trío + pareja → FULL_HOUSE
  - **Póker**
    - [x] Test: cuatro cartas del mismo rango → POKER
  - **Escalera de Color**
    - [x] Test: 5 consecutivos del mismo palo (no 10-J-Q-K-A) → ESCALERA_DE_COLOR
  - **Escalera Real**
    - [x] Test: 10-J-Q-K-A del mismo palo → ESCALERA_REAL
  - **Prioridad**
    - [x] Test: Full House no se confunde con Trío
    - [x] Test: Escalera Real tiene prioridad sobre Escalera de Color
    - [x] Test: Color tiene prioridad sobre Escalera si no son consecutivos
- [x] `evaluate()` devuelve también las `scoringCards` (cartas que forman la mano)
  - [x] Test: en una Pareja de 7 dentro de 5 cartas, `scoringCards` contiene solo las dos 7

---

## Fase 3 — Motor de puntuación y niveles de mano

**Paquete:** `core/logic/game`

- [ ] Crear clase `HandLevelManager`
  - [ ] Test: nivel inicial de cualquier mano es 0
  - [ ] Test: `getChips(PAREJA)` nivel 0 → 20
  - [ ] Test: `getChips(PAREJA)` nivel 3 → 50
  - [ ] Test: `getMult(PAREJA)` nivel 3 → 5
  - [ ] Test: `getChips(CARTA_ALTA)` nivel 0 → 5
  - [ ] Test: `getMult(CARTA_ALTA)` nivel 1 → 1 (piso de 0.5)
  - [ ] Test: `getMult(CARTA_ALTA)` nivel 2 → 2
  - [ ] Test: `upgrade(handType)` incrementa el nivel en 1
- [ ] Crear clase `HandEvaluationContext`
  - Campos: `handType`, `playedCards`, `scoringCards`, `levelManager`, `globalStats`, `wallet`
- [ ] Crear clase `ScoreEngine` (sin jokers aún)
  - [ ] Test: Carta Alta con As, DeckType STANDARD → chips = 5 + 11 = 16, mult = 1 → score = 16
  - [ ] Test: Carta Alta con As, DeckType POWERED (bonusChips=10) → chips = 5 + 11 + 10 = 26, mult = 1 → score = 26
  - [ ] Test: Pareja de Reyes nivel 0, DeckType MULTIBASE (bonusMult=2) → chips = 20+10+10 = 40, mult = 2+2 = 4 → score = 160
  - [ ] Test: Pareja de Reyes nivel 3, DeckType STANDARD → chips = 50+10+10 = 70, mult = 5 → score = 350
  - [ ] Test: los bonos de `DeckType` se aplican **antes** que los jokers (orden: base + bonusDeck + jokers)
  - [ ] Test: solo las `scoringCards` aportan chips individuales, no todas las del PlayArea
  - [ ] Test: `InvalidPlayAreaSizeException` si `playedCards` está vacía
- [ ] Crear excepción `InvalidPlayAreaSizeException`

---

## Fase 4 — Gestión de la ronda

**Paquete:** `core/logic/game`

- [ ] Crear clase `PlayerHand`
  - [ ] Test: capacidad máxima de 8 cartas
  - [ ] Test: `select(card)` marca la carta como seleccionada
  - [ ] Test: `deselect(card)` la desmarca
  - [ ] Test: `getSelected()` devuelve solo las marcadas
  - [ ] Test: `lanza HandLimitExceededException` si se intentan añadir más de 5 cartas al `PlayArea`
- [ ] Crear clase `RoundManager`
  - [ ] Test: al iniciar ronda, manos = 3 y descartes = 3
  - [ ] Test: `playHand()` reduce manos en 1
  - [ ] Test: `discard()` reduce descartes en 1
  - [ ] Test: `playHand()` con 0 manos lanza excepción
  - [ ] Test: `discard()` con 0 descartes lanza excepción
  - [ ] Test: `targetScore` ronda 0 → 300
  - [ ] Test: `targetScore` ronda 1 → 480 (round(300 * 1.6^1))
  - [ ] Test: `targetScore` ronda 3 → 1229 (round(300 * 1.6^3))
  - [ ] Test: `isRoundWon()` true si score >= targetScore
  - [ ] **Condición de derrota 1:** `isGameOver()` true si manos = 0 y score < targetScore
  - [ ] **Condición de derrota 2:** `isGameOver()` true si mano vacía Y mazo vacío (con manos restantes)
  - [ ] Test: reponer mazo al inicio de cada ronda (52 cartas barajadas)
  - [ ] Test: al robar tras jugar, las cartas del `PlayArea` van al descarte (no vuelven al mazo)
  - [ ] Test: al agotar el mazo, no se recicla el descarte

---

## Fase 5 — Economía

**Paquete:** `core/logic/economy`

- [ ] Crear clase `Wallet`
  - [ ] Test: `new Wallet(DeckType.STANDARD)` → saldo inicial 4
  - [ ] Test: `new Wallet(DeckType.WEALTHY)` → saldo inicial 8
  - [ ] Test: `spend(amount)` reduce el saldo
  - [ ] Test: `spend()` con saldo insuficiente lanza `NotEnoughMoneyException`
  - [ ] Test: `earn(amount)` aumenta el saldo
  - [ ] Test: `getAmount()` devuelve el saldo actual
- [ ] Crear clase `EconomyCalculator` (calcula ganancias al final de ronda)
  - [ ] Test: 2 manos restantes + 1 descarte restante + 10 monedas → ganancia = 2*2 + 1*1 + floor(10/5) = 7
  - [ ] Test: interés máximo de 5 monedas (25+ monedas guardadas)
  - [ ] Test: el interés se aplica sobre el saldo **antes** de sumar el bono de manos/descartes
- [ ] Crear excepción `NotEnoughMoneyException`

---

## Fase 6 — Jokers

**Paquete:** `core/logic/jokers`

- [ ] Crear interfaz `JokerEffect`
- [ ] Crear clase `Joker` (id, name, description, price, rarity, effect)
- [ ] Crear enum `Rarity` (COMMON, UNCOMMON, RARE) con probabilidades y precios
- [ ] Crear clase `JokerManager`
  - [ ] Test: límite de 6 jokers activos
  - [ ] Test: `add()` con 6 jokers lanza `JokerLimitExceededException`
  - [ ] Test: `sell(joker)` elimina el joker y devuelve `floor(precio / 2)` a la wallet
  - [ ] Test: `sell()` de un joker no activo lanza excepción
- [ ] Implementar joker **J001 – Matador** (`PairChipsEffect`)
  - [ ] Test: +30 chips si la mano es PAREJA
  - [ ] Test: 0 chips extra si la mano no es PAREJA
- [ ] Implementar joker **J002 – Corazón Ardiente** (`HeartMultEffect`)
  - [ ] Test: +2 mult por cada carta de HEARTS en `scoringCards`
  - [ ] Test: 0 cartas de HEARTS → mult sin cambio
- [ ] Implementar joker **J003 – Memorioso** (`CumulativeMultEffect`)
  - [ ] Test: +1 mult por cada vez que se ha jugado esa mano en la partida
  - [ ] Test: primera vez que se juega → +0 mult adicional (si es la primera)
- [ ] Implementar joker **J004 – Escalador** (`StairChipsEffect`)
  - [ ] Test: +15 chips si la mano es ESCALERA o superior
  - [ ] Test: PAREJA no activa el efecto
- [ ] Integrar jokers en `ScoreEngine`
  - [ ] Test: Pareja nivel 0 con J001 activo → chips = 20 + 20 + 20 + 30 = 90, mult = 2 → 180
  - [ ] Test: el orden de aplicación es chips primero, luego mult
  - [ ] Test: con 0 jokers activos el resultado es el mismo que sin jokers
- [ ] Crear excepción `JokerLimitExceededException`

---

## Fase 7 — Generador de tienda

**Paquete:** `core/logic/economy`

- [ ] Crear clase `ShopGenerator`
  - [ ] Test: genera exactamente 2 jokers por visita
  - [ ] Test: los jokers generados pertenecen al pool desbloqueado
  - [ ] Test: la distribución de rareza respeta las probabilidades (test estadístico con N grande)
  - [ ] Test: genera exactamente 2 mejoras de mano aleatorias
  - [ ] Test: el precio de una mejora de mano es 6 monedas

---

## Fase 8 — Persistencia

**Paquete:** `core/logic/persistence`

- [ ] Crear clase `SaveManager`
  - [ ] Test: `save(gameSession)` escribe un JSON válido que incluye el `id` del `DeckType` activo
  - [ ] Test: `load()` reconstruye la sesión exactamente igual (roundNumber, deckType, wallet, jokers activos, niveles de mano)
  - [ ] Test: `load()` usa `DeckType.fromId()` para reconstruir el mazo — no hardcodea valores
  - [ ] Test: `load()` cuando no existe archivo devuelve `Optional.empty()`
  - [ ] Test: `delete()` elimina el archivo de guardado
- [ ] Crear clase `StatsManager`
  - [ ] Test: `updateAfterGame()` incrementa `gamesPlayed`
  - [ ] Test: `updateAfterGame()` actualiza `bestRound` solo si la ronda actual es mayor
  - [ ] Test: `updateAfterGame()` acumula `roundsCompleted`
  - [ ] Test: `updateAfterGame()` actualiza `bestScore` solo si es mayor
  - [ ] Test: `updateAfterGame()` suma a `handsPlayedByType`
- [ ] Crear clase `UnlockService`
  - [ ] Test: logro `REACH_ROUND_5` se concede cuando `bestRound >= 4`
  - [ ] Test: un logro ya concedido no se concede dos veces
  - [ ] Test: `getUnlockedJokerIds()` incluye los jokers base más los desbloqueados
  - [ ] Test: `getUnlockedDeckIds()` incluye los mazos base más los desbloqueados
  - [ ] Test: un `DeckType` bloqueado no aparece en `getUnlockedDeckIds()`
  - [ ] Test: al desbloquear un mazo por logro, `getUnlockedDeckIds()` lo incluye en la siguiente consulta

---

## Fase 9 — Integración de lógica (GameSession)

**Paquete:** `core/logic/game`

- [ ] Crear clase `GameSession` que orquesta todos los sistemas
  - [ ] Test: `startNewGame(DeckType.WEALTHY)` → `wallet.getAmount()` = 8
  - [ ] Test: `startNewGame(DeckType.POWERED)` → `scoreEngine.getBonusChips()` = 10
  - [ ] Test: `startNewGame(DeckType.MULTIBASE)` → `scoreEngine.getBonusMult()` = 2
  - [ ] Test: `startNewGame()` inicializa mazo completo (52 cartas), manos = 3, descartes = 3, ronda = 0
  - [ ] Test: flujo completo de ronda ganada → tienda → siguiente ronda incrementa `roundNumber`
  - [ ] Test: flujo de derrota por manos agotadas → llama a `StatsManager.updateAfterGame()`
  - [ ] Test: flujo de derrota por cartas agotadas → llama a `StatsManager.updateAfterGame()`
  - [ ] Test: guardar automáticamente al salir de la tienda llama a `SaveManager.save()`
- [ ] Ejecutar suite completa de tests y verificar cobertura > 80% en `logic/`

---

## Fase 10 — Pantallas LibGDX

**Paquete:** `core/screens` y `core/ui`

> A partir de aquí no hay tests JUnit (la UI se verifica manualmente).

### 10.1. Infraestructura base
- [ ] Crear clase principal `AngelatroGame extends Game`
  - [ ] `create()`: configurar `FitViewport(800, 480)`, cargar `BitmapFont`, navegar a `MainMenuScreen`
  - [ ] `dispose()`: liberar recursos globales
- [ ] Crear componente `CardView` (rectángulo + texto con rango y palo en color)

### 10.2. MainMenuScreen
- [ ] Botón "Nueva Partida" → navega a pantalla de selección de mazo
- [ ] Botón "Continuar" → visible solo si existe `savegame.json`; carga sesión y va a `GameScreen`
- [ ] Botón "Colección" → navega a `CollectionScreen`
- [ ] Botón "Estadísticas" → navega a `StatsScreen`
- [ ] Botón "Hard Reset" → diálogo de confirmación; borra archivos y recarga menú

### 10.3. Pantalla de selección de mazo
- [ ] Consultar `UnlockService.getUnlockedDeckIds()` para saber qué mazos mostrar
- [ ] Muestra solo los mazos desbloqueados con nombre y ventaja (los bloqueados no aparecen)
- [ ] Botón "Jugar" con el mazo seleccionado → crea `GameSession` con ese `DeckType` y navega a `GameScreen`

### 10.4. GameScreen
- [ ] HUD superior: puntuación actual / objetivo, ronda, manos restantes, descartes restantes, monedas
- [ ] Mostrar jokers activos (barra lateral o zona inferior)
- [ ] Mostrar `PlayerHand` con `CardView` pulsables (seleccionar / deseleccionar)
- [ ] Botón "Jugar mano" (activo solo si hay ≥1 carta seleccionada)
- [ ] Botón "Descartar" (activo solo si hay ≥1 carta seleccionada y descartes > 0)
- [ ] Mostrar animación de puntuación al jugar (texto flotante con los puntos conseguidos)
- [ ] Detectar victoria de ronda → navegar a `ShopScreen`
- [ ] Detectar derrota → navegar a `GameOverScreen`

### 10.5. ShopScreen
- [ ] Mostrar 2 jokers con nombre, descripción, rareza y precio
- [ ] Botón "Comprar" por joker (deshabilitado si saldo insuficiente o límite alcanzado)
- [ ] Mostrar jokers activos del jugador con botón "Vender" en cada uno
- [ ] Mostrar 2 mejoras de mano con tipo y precio (6 monedas)
- [ ] Botón "Siguiente ronda" → actualiza estado y navega a `GameScreen`

### 10.6. GameOverScreen
- [ ] Mostrar ronda alcanzada (destacada)
- [ ] Mostrar mensaje de nuevo récord si `ronda > bestRound` previo
- [ ] Mostrar puntuación de la última ronda y acumulada
- [ ] Botón "Menú principal"

### 10.7. CollectionScreen
- [ ] Pestaña "Jokers": cuadrícula con todos los jokers (bloqueados en silueta)
- [ ] Pestaña "Mazos": lista con condición de desbloqueo para los bloqueados

### 10.8. StatsScreen
- [ ] Mostrar: partidas jugadas, mejor ronda, rondas completadas (total), mejor puntuación en una ronda

---

## Fase 11 — Jokers restantes

Una vez la UI funciona, implementar los jokers pendientes sobre la base ya probada:

- [ ] **J005 – Avaricioso:** +1 mult por cada 5 monedas al jugar (requiere `wallet` en contexto)
- [ ] **J006 – Figuras:** +4 chips por cada J, Q o K en `scoringCards`
- [ ] **J007 – As en la Manga:** +20 chips si hay al menos un As en `scoringCards`
- [ ] **J008 – Fullero:** +4 mult si la mano es FULL_HOUSE o POKER
- [ ] Tests unitarios para cada uno antes de implementar

---

## Fase 12 — Meta-progresión y logros

- [ ] Verificar que `UnlockService` evalúa logros correctamente al final de cada partida
- [ ] Verificar que los jokers y mazos desbloqueados aparecen en `CollectionScreen`
- [ ] Verificar que los nuevos jokers desbloqueados entran en el pool de `ShopGenerator`
- [ ] Implementar logro `REACH_ROUND_10` y su recompensa
- [ ] Implementar logro `HAVE_6_JOKERS` y su recompensa
- [ ] Implementar logro `PLAY_100_HANDS` y su recompensa

---

## Fase 13 — Audio

- [ ] Añadir efecto de sonido al jugar una mano
- [ ] Añadir efecto de sonido al comprar en la tienda
- [ ] Añadir efecto de sonido al vender un joker
- [ ] (Opcional) Añadir música de fondo en bucle

---

## Fase 14 — Empaquetado y entrega

- [ ] Probar en escritorio Windows
- [ ] Probar en escritorio Linux
- [ ] Probar en dispositivo Android físico (o emulador API 26+)
- [ ] Verificar que el guardado automático funciona en Android
- [ ] Verificar que `Hard Reset` limpia todos los archivos correctamente
- [ ] Generar JAR de escritorio (`./gradlew desktop:dist`)
- [ ] Generar APK firmado (`./gradlew android:assembleRelease`)
- [ ] Completar `README.md` con instrucciones de compilación para las tres plataformas
- [ ] Crear `CHANGELOG.md` con resumen de funcionalidades implementadas
- [ ] Ejecutar suite completa de tests por última vez y confirmar cobertura > 80%

---

## Backlog (funcionalidades futuras)

Ideas para añadir tras completar la versión base, sin comprometer la estabilidad actual:

- Arrastrar cartas con `DragListener` en lugar de pulsar para seleccionar
- Jokers con efectos que dependen de la posición en `JokerManager`
- Jokers que se activan entre sí (efectos compuestos)
- Efectos xMult (multiplicador multiplicativo, no aditivo)
- Más tipos de mazos iniciales desbloqueables
- Animaciones de transición entre pantallas
- Soporte para múltiples idiomas
- Tabla de puntuaciones local (mejores rondas con fecha)
