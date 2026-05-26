# Angelatro – Especificaciones del Proyecto (v3)

> **Cambios respecto a v2:** sistema de partida infinita (sin condición de victoria); fórmula de `targetScore` exponencial; eliminada pantalla de Victoria; `stats.json` actualizado con `bestRound` como métrica estrella.  
> Los bloques sin marca están iguales a v2.

---

## 1. Alcance y Visión

- **Proyecto:** Clon de juego roguelike de construcción de mazos (estilo **Balatro**).
- **Plataformas:** Windows, Linux, Android (mínimo Android 8.0 / API 26). `[NUEVO]`
- **Entorno de desarrollo:** Java 17, LibGDX 1.13.x, Gradle 8.x. `[NUEVO]`
- **Estilo Visual:** 2D minimalista, alegre y funcional (colores planos, botones grandes, tipografía legible).
- **Metodologías:** TDD estricto (tests unitarios JUnit 5 antes de implementar), SOLID, KISS.
- **Público objetivo:** Jugadores que buscan partidas rápidas con progresión estratégica.

---

## 2. Menú Principal y Gestión de Sesión

- **Continuar Partida:** Carga el último estado de `GameSession` guardado en `savegame.json` (almacenamiento local). Solo visible si existe el archivo.
- **Empezar Partida:** Permite seleccionar un `DeckType` (mazo inicial) con estadísticas base, y crea una nueva instancia de juego.
- **Colección:** Pantalla para visualizar los Jokers y Mazos desbloqueados permanentemente (ver sección 2.1). `[AMPLIADO]`
- **Estadísticas:** Pantalla que muestra las métricas de `stats.json` (partidas jugadas, **mejor ronda alcanzada**, puntuación máxima en una ronda).
- **Hard Reset:** Botón con confirmación ("¿Seguro?") que borra todos los archivos de guardado (`savegame.json`, `unlocks.json`, `stats.json`) y reinicia la meta-progresión.

### 2.1. Pantalla de Colección `[NUEVO]`

Muestra dos pestañas:
- **Jokers:** cuadrícula de todos los jokers del juego. Los bloqueados se muestran en silueta con un candado. Al pulsar uno desbloqueado se muestra su nombre, descripción, rareza y precio.
- **Mazos:** lista de mazos disponibles con su ventaja. Los bloqueados muestran la condición de desbloqueo.

---

## 3. Reglas de Juego y Economía (núcleo)

### 3.1. Mazos iniciales (DeckType)

Cada mazo concede una ventaja única al empezar la partida. Ejemplos iniciales:

| DeckType      | Dinero inicial | Chips base | Mult base | Bonus especial |
|---------------|----------------|------------|-----------|----------------|
| Estándar      | 4              | 0          | 0         | Ninguno        |
| Acaudalado    | 8              | 0          | 0         | -              |
| Potenciado    | 2              | 10         | 0         | -              |
| Multibase     | 2              | 0          | 2         | -              |

*Los mazos adicionales se desbloquean mediante logros (meta-progresión).*

### 3.2. Valor en chips de las cartas individuales `[NUEVO]`

Cada carta del mazo contribuye con un valor fijo de chips cuando participa en una mano evaluada. Esto se suma **antes** de aplicar el multiplicador.

| Rango          | Chips aportados |
|----------------|-----------------|
| As (A)         | 11              |
| Rey (K)        | 10              |
| Reina (Q)      | 10              |
| Jota (J)       | 10              |
| 10             | 10              |
| 9              | 9               |
| 8              | 8               |
| 7              | 7               |
| 6              | 6               |
| 5              | 5               |
| 4              | 4               |
| 3              | 3               |
| 2              | 2               |

> **Importante:** solo las cartas que forman parte de la mano ganadora contribuyen con sus chips (no todas las cartas del `PlayArea`). Por ejemplo, en un Trío de 7 dentro de un `PlayArea` de 5 cartas, solo las tres 7 aportan chips individuales.

### 3.3. Gestión de manos y áreas

- **`PlayerHand`**: contiene hasta **8 cartas**. Permite seleccionar/deseleccionar cartas para jugar o descartar.
- **`PlayArea`**: contiene las cartas que se van a evaluar en una mano. Límite estricto de **5 cartas**.
- **Flujo de jugar una mano:**
  1. Consumir una mano (disminuir contador de manos).
  2. Evaluar las cartas del `PlayArea` usando `HandEvaluator` y `ScoreEngine`.
  3. Sumar la puntuación al marcador.
  4. Mover las cartas del `PlayArea` al mazo de descarte.
  5. Robar cartas del mazo hasta tener 8 en `PlayerHand`, o hasta que el mazo se vacíe.
  6. **El mazo de descarte nunca se recicla.** Si el mazo se agota, el jugador solo puede operar con las cartas que le queden en mano.
  7. Comprobar condición de derrota por cartas (ver sección 3.4).
- **Flujo de descartar:**
  1. Consumir un descarte.
  2. Eliminar las cartas seleccionadas de `PlayerHand` y añadirlas al mazo de descarte.
  3. Robar del mazo el mismo número de cartas descartadas (o menos si el mazo no tiene suficientes).
  4. Comprobar condición de derrota por cartas (ver sección 3.4).

### 3.4. Ciclo de ronda, puntuación objetivo y fin de partida

- Cada ronda comienza con **3 manos** y **3 descartes**.
- La partida es **infinita**: no existe una ronda final. El objetivo es sobrevivir el mayor número de rondas posible.
- **Puntuación objetivo (`targetScore`)** se calcula al inicio de cada ronda con escala exponencial:
  ```
  targetScore = round(300 * 1.6^roundNumber)
  ```
  donde `roundNumber` empieza en 0.

  | Ronda (índice) | targetScore aproximado |
  |----------------|------------------------|
  | 0              | 300                    |
  | 1              | 480                    |
  | 2              | 768                    |
  | 3              | 1.229                  |
  | 5              | 3.145                  |
  | 8              | 12.885                 |
  | 10             | 32.972                 |
  | 15             | ~268.000               |

- Si `scoreActual >= targetScore` antes de agotar manos → se **gana la ronda** y se avanza a la tienda.
- **Condiciones de derrota (Game Over):**
  1. Se agotan las manos sin alcanzar el `targetScore`.
  2. Aún quedan manos disponibles pero `PlayerHand` está vacía **y** el mazo también está vacío — el jugador no puede formar ninguna mano más.

> La condición 2 se evalúa tras cada robo (después de jugar una mano o descartar). En la práctica ocurre cuando el jugador ha descartado agresivamente y el mazo se ha agotado antes de alcanzar el objetivo.

> **Decisión de diseño:** la escala exponencial asegura que incluso con jokers muy potentes la curva siempre supera al jugador tarde o temprano, manteniendo la tensión indefinidamente. Un buen run puede durar 12-15 rondas; uno excepcional, 20+.

#### Pantalla de Game Over

Se muestra cuando el jugador no alcanza el `targetScore`. Incluye:
- **Ronda alcanzada** (métrica estrella, destacada visualmente).
- Si es récord personal (`ronda > bestRound`): mensaje de nuevo récord.
- Puntuación de la última ronda fallida y puntuación acumulada de la partida.
- Botón "Menú principal".
- Se actualiza `stats.json` con las estadísticas de la partida y se evalúan desbloqueos.

### 3.5. Economía (Wallet)

- **Ganancias al superar ronda:**
  ```
  ganancia = (manosRestantes * 2) + (descartesRestantes * 1) + interés
  ```
- **Interés:**
  - Por cada 5 monedas ahorradas **al inicio del cálculo**, se añade 1 moneda.
  - Máximo 5 monedas de interés por ronda.
  - Ejemplo: 22 monedas → interés = `floor(22/5)` = 4.
  - El interés se calcula **antes** de sumar el bono por manos/descartes restantes.
- **Venta de Jokers:** se recupera la mitad del precio de compra (redondeado hacia abajo). El joker se elimina de `JokerManager`.
- **Límite de Jokers activos:** máximo **6** en todo momento.

### 3.6. Tienda (Shop)

Aparece inmediatamente después de ganar una ronda.

- Se generan **2 jokers aleatorios** del pool de jokers desbloqueados según `UnlockService`.
- **Pool y repeticiones `[NUEVO]`:** un joker ya activo en el `JokerManager` del jugador **puede** aparecer en la tienda (acumular duplicados no está soportado inicialmente; si el jugador ya tiene ese joker, simplemente no puede comprarlo). Para la versión inicial, no se filtra el pool — si sale uno que ya tienes, no se puede comprar.
- Cada joker tiene: Nombre, descripción, precio, rareza, efecto.
- El jugador puede:
  - **Comprar** un joker (si tiene saldo suficiente y no excede el límite de 6).
  - **Vender** un joker activo.
  - **Pasar** a la siguiente ronda sin comprar.
- La tienda ofrece además **2 mejoras de nivel para un tipo de mano** (ver sección 3.7). Precio fijo de 6 monedas cada una.
- Al salir de la tienda:
  - Se incrementa `roundNumber` en 1.
  - Se reinician los contadores de manos y descartes a 3.
  - Se repone el mazo (nuevo mazo completo barajado; se descartan las cartas que quedaban en mano).
  - Se guarda automáticamente la partida.

### 3.7. Sistema de niveles de mano (HandLevelManager)

Cada `HandType` tiene un nivel que empieza en 0. Al comprar una mejora, sube 1.

**Fórmula:**
- **Chips base** = `chipsInicial + (incrementoChips * nivel)`
- **Mult base** = `multInicial + (incrementoMult * nivel)`

**Tabla de parámetros:**

| HandType          | chipsInicial | multInicial | incrementoChips | incrementoMult |
|-------------------|--------------|-------------|-----------------|----------------|
| Carta Alta        | 5            | 1           | 10              | 1              |
| Pareja            | 20           | 2           | 15              | 1              |
| Doble Pareja      | 30           | 2           | 20              | 1              |
| Trío              | 40           | 3           | 20              | 2              |
| Escalera          | 30           | 4           | 30              | 3              |
| Color             | 35           | 4           | 30              | 3              |
| Full House        | 40           | 4           | 25              | 2              |
| Póker             | 60           | 7           | 30              | 3              |
| Escalera de Color | 100          | 8           | 40              | 4              |
| Escalera Real     | 100          | 8           | 40              | 4              | `[NUEVO]`
| Repoker           | 120          | 12          | 35              | 3              |
| Full de color     | 140          | 14          | 40              | 4              |
| 5 de color        | 160          | 16          | 50              | 3              |


> **Escalera Real `[NUEVO]`:** secuencia 10-J-Q-K-A del mismo palo. Se evalúa **antes** que la Escalera de Color en `HandEvaluator`. Mismos valores que Escalera de Color (se diferencia solo por nombre y logros).

**Ejemplo:** Pareja nivel 3 → Chips = 20 + 15×3 = 65, Mult = 2 + 1×3 = 5 → base = 325 (antes de chips individuales y jokers).

### 3.8. Orden de cálculo de puntuación en ScoreEngine `[NUEVO]`

El orden exacto importa. Se aplica en este orden:

1. Chips base de la mano (`HandLevelManager.getChips(handType)`).
2. Suma de chips individuales de las cartas que forman la mano ganadora (ver tabla 3.2).
3. Aplicar `modifyChips` de cada Joker activo (en orden de posición en `JokerManager`).
4. Mult base de la mano (`HandLevelManager.getMult(handType)`).
5. Aplicar `modifyMult` de cada Joker activo (en orden de posición).
6. Puntuación final = `totalChips * totalMult`.

```java
public int calculateTotalScore(HandEvaluationContext ctx, List<Joker> activeJokers) {
    // 1. Chips base de la mano
    int chips = levelManager.getChips(ctx.getHandType());

    // 2. Chips individuales de las cartas ganadoras
    for (Card card : ctx.getScoringCards()) { // solo las cartas que puntúan
        chips += card.getRankChips();
        chips += card.getChipBonus(); // bonus extra (jokers de carta, futuro)
    }

    // 3. Modificadores de chips de jokers
    for (Joker joker : activeJokers) {
        chips = joker.getEffect().modifyChips(chips, ctx);
    }

    // 4-5. Mult base y modificadores de mult de jokers
    int mult = levelManager.getMult(ctx.getHandType());
    for (Joker joker : activeJokers) {
        mult = joker.getEffect().modifyMult(mult, ctx);
    }

    return chips * mult;
}
```

> `ctx.getScoringCards()` devuelve solo las cartas que forman la combinación ganadora, no todas las del `PlayArea`.

### 3.9. Jokers: diseño extensible con patrón Strategy

Los Jokers modifican la puntuación según el contexto de la mano.

#### Sistema de rareza `[NUEVO]`

| Rareza       | Probabilidad en tienda | Precio |
|--------------|------------------------|--------|
| Común        | 70%                    | 4 monedas |
| Poco común   | 25%                    | 6 monedas |
| Raro         | 5%                     | 8 monedas |

#### Interfaz base

```java
public interface JokerEffect {
    int modifyChips(int currentChips, HandEvaluationContext context);
    int modifyMult(int currentMult, HandEvaluationContext context);
}

public class HandEvaluationContext {
    private HandType handType;
    private List<Card> playedCards;    // todas las del PlayArea
    private List<Card> scoringCards;   // solo las que forman la mano [NUEVO]
    private HandLevelManager levelManager;
    private GameStats globalStats;
    // getters...
}
```

#### Lista inicial de Jokers (mínimo viable) `[NUEVO]`

| ID | Nombre | Rareza | Precio | Efecto |
|----|--------|--------|--------|--------|
| J001 | Matador | Común | 4 | +30 chips si la mano es Pareja |
| J002 | Corazón Ardiente | Común | 4 | Cada carta de Corazones aporta +2 mult |
| J003 | Memorioso | Poco común | 6 | +1 mult por cada vez que has jugado esta mano en la partida |
| J004 | Escalador | Común | 4 | +15 chips si la mano es Escalera o superior |
| J005 | Avaricioso | Poco común | 6 | +1 mult por cada 5 monedas que tengas al jugar la mano |
| J006 | Figuras | Común | 4 | +4 chips por cada carta de figura (J, Q, K) en la mano |
| J007 | As en la Manga | Poco común | 6 | +20 chips si la mano contiene al menos un As |
| J008 | Fullero | Raro | 8 | +4 mult si la mano es Full House o Póker |

#### Ejemplos de implementación

```java
// J001 - Matador
public class PairChipsEffect implements JokerEffect {
    public int modifyChips(int chips, HandEvaluationContext ctx) {
        return ctx.getHandType() == HandType.PAIR ? chips + 30 : chips;
    }
    public int modifyMult(int mult, HandEvaluationContext ctx) { return mult; }
}

// J002 - Corazón Ardiente
public class HeartMultEffect implements JokerEffect {
    public int modifyMult(int mult, HandEvaluationContext ctx) {
        long hearts = ctx.getScoringCards().stream()
                        .filter(c -> c.getSuit() == Suit.HEARTS).count();
        return mult + (int) hearts * 2;
    }
    public int modifyChips(int chips, HandEvaluationContext ctx) { return chips; }
}

// J005 - Avaricioso
public class WealthMultEffect implements JokerEffect {
    public int modifyMult(int mult, HandEvaluationContext ctx) {
        return mult + (ctx.getWallet().getAmount() / 5);
    }
    public int modifyChips(int chips, HandEvaluationContext ctx) { return chips; }
}
```

> **Nota:** `HandEvaluationContext` debe incluir referencia a `Wallet` para el joker J005. `[NUEVO]`

#### Limitaciones iniciales (KISS)

- Implementa solo J001–J004 primero. El resto se añade tras validar el motor.
- No implementes efectos que dependan de la posición o de otros Jokers (extensión futura).

---

## 4. Meta-progresión (persistente entre partidas)

Los datos se guardan en `unlocks.json` y `stats.json`. Incluye:

**`stats.json`:**
- `gamesPlayed`: partidas iniciadas.
- `bestRound`: **métrica estrella** — ronda más alta alcanzada en cualquier partida (índice base 0; se muestra como "Ronda N+1" en la UI).
- `roundsCompleted`: total de rondas superadas acumuladas en todas las partidas.
- `maxJokersHeld`: máximo de jokers activos alcanzado alguna vez.
- `bestScore`: mejor puntuación en una sola ronda.
- `totalScore`: suma acumulada de todas las puntuaciones.
- `handsPlayedByType`: mapa `{HandType → int}` con cuántas veces se jugó cada tipo de mano.

**`unlocks.json`:**
- `unlockedJokers`: lista de IDs de jokers desbloqueados.
- `unlockedDecks`: lista de IDs de mazos desbloqueados.
- `completedAchievements`: lista de IDs de logros ya procesados (para no evaluarlos dos veces). `[NUEVO]`

**Logros iniciales:**

| ID | Condición | Recompensa |
|----|-----------|------------|
| REACH_ROUND_5 | `bestRound >= 4` (superar ronda 5) | Joker "Veterano" (+2 mult por ronda superada en la partida actual) |
| REACH_ROUND_10 | `bestRound >= 9` | Mazo "Resistente" (+1 mano por ronda) |
| HAVE_6_JOKERS | `maxJokersHeld >= 6` | Mazo "Jokerómano" (empieza con un joker aleatorio) |
| PLAY_100_HANDS | `sum(handsPlayedByType) >= 100` | Joker "Amuleto" (+10 chips por mano) |
| REACH_ROUND_15 | `bestRound >= 14` | Joker "Leyenda" (efecto complejo, implementar en futuro) |

`UnlockService` evalúa los logros **al final de cada partida** (Game Over) y actualiza los desbloqueos automáticamente.

---

## 5. Arquitectura del Software (SOLID + KISS)

### 5.1. Estructura de paquetes

```
core/
├── logic/              # Lógica de negocio 100% libre de LibGDX (para tests JUnit)
│   ├── model/          # Card, Deck, HandType, Joker, Suit, Rank, etc.
│   ├── game/           # GameSession, RoundManager, ScoreEngine, HandEvaluator,
│   │                   # HandLevelManager, HandEvaluationContext
│   ├── economy/        # Wallet, JokerManager, ShopGenerator
│   ├── jokers/         # JokerEffect y sus implementaciones concretas
│   ├── stats/          # GameStats, AchievementChecker [NUEVO]
│   └── persistence/    # SaveManager, UnlockService (Gson)
├── screens/            # MainMenuScreen, GameScreen, ShopScreen,
│                       # GameOverScreen, CollectionScreen, StatsScreen
├── ui/                 # Componentes reutilizables (CardActor, JokerCard, HUD)
└── utils/              # Helpers para cargar texturas, fuentes, etc.
```

### 5.2. HUD durante la partida `[NUEVO]`

La pantalla de juego (`GameScreen`) debe mostrar siempre visible:

- Puntuación actual / puntuación objetivo (ej. "1.240 / 3.000").
- Manos restantes.
- Descartes restantes.
- Monedas actuales.
- Número de ronda actual (ej. "Ronda 3 / 8").
- Jokers activos (iconos o nombres en barra lateral).

### 5.3. Principios aplicados

- **Single Responsibility:** `ScoreEngine` solo calcula, no guarda. `HandEvaluator` solo identifica la mano.
- **Open/Closed:** nuevos jokers se añaden sin modificar `ScoreEngine` (patrón Strategy).
- **Dependency Inversion:** las pantallas dependen de interfaces (ej. `IGameSessionProvider`).
- **KISS:** fórmulas lineales o cuadráticas simples; no se añaden efectos complejos hasta que lo básico funcione.

### 5.4. Excepciones de dominio

```
NotEnoughMoneyException
JokerLimitExceededException
HandLimitExceededException
DeckEmptyException              (el mazo se ha agotado; no se recicla el descarte)
NotEnoughCardsToDrawException   (se intenta robar pero mazo y mano están vacíos → Game Over)
InvalidPlayAreaSizeException    (si se intenta jugar con 0 cartas)
```

---

## 6. Guía para el novato en LibGDX (conceptos clave)

### 6.1. Configuración del proyecto `[NUEVO]`

Usa el generador oficial de LibGDX (`gdx-liftoff` o `libgdx.io/wiki/project-generation`) con:
- **Java 17**.
- Módulos: `core`, `desktop`, `android`.
- Extensiones: ninguna obligatoria inicialmente (Gson se añade manualmente al `build.gradle`).
- `targetSdkVersion` Android: **34**; `minSdkVersion`: **26**.

### 6.2. Ciclo de vida de una aplicación LibGDX

- `create()`: se llama una vez al inicio. Aquí cargas recursos, creas la primera pantalla.
- `render()`: se llama 60 veces por segundo. Dibuja la pantalla actual y procesa entrada.
- `resize()`, `pause()`, `resume()`, `dispose()`: para manejar cambios de tamaño o pérdida de foco.

### 6.3. Separación lógica / UI

Las clases de `logic/` **NO** deben importar nada de `com.badlogic.gdx`. Así los tests JUnit se ejecutan en cualquier IDE sin entorno gráfico.

### 6.4. Entrada táctil / ratón

- Para botones, usa `TextButton` de Scene2D con `ClickListener`.
- Para seleccionar cartas, usa `Actor` con `addListener(new ClickListener() {...})`.
- Para arrastrar cartas (futuro), `DragListener` o `InputProcessor` manual.
- En Android, los toques se convierten automáticamente si usas los listeners estándar.

### 6.5. Guardado de archivos

```java
Gdx.files.local("savegame.json")   // funciona en Windows, Linux y Android
```

`SaveManager` serializa/deserializa con Gson. Añade `implementation 'com.google.code.gson:gson:2.10.1'` al `build.gradle` del módulo `core`.

### 6.6. Audio (mínimo viable) `[NUEVO]`

LibGDX soporta audio sin dependencias extra:

```java
// Música de fondo (loop)
Music bgm = Gdx.audio.newMusic(Gdx.files.internal("music/loop.mp3"));
bgm.setLooping(true);
bgm.play();

// Efecto de sonido (carta jugada, compra, etc.)
Sound cardSound = Gdx.audio.newSound(Gdx.files.internal("sfx/card_play.wav"));
cardSound.play();
```

**Para la versión inicial:** incluye al menos un efecto al jugar una mano y uno al comprar en la tienda. La música puede omitirse hasta tener el juego funcional.

### 6.7. Representación visual (mínima)

- Al principio, dibuja las cartas como rectángulos con texto (ej. "AS♥", "KH"). No necesitas texturas.
- Usa `BitmapFont` de LibGDX para texto.
- Para pantallas de tienda, botones con texto ("Comprar", "Vender", "Siguiente ronda").
- Usa `FitViewport` con resolución base **800×480** (landscape) para mantener proporción en todas las plataformas.

---

## 7. Estrategia de desarrollo (TDD estricto)

1. Escribir el test que describe el comportamiento esperado (rojo).
2. Implementar lo mínimo para pasar el test (verde).
3. Refactorizar manteniendo los tests en verde.
4. No escribir código de producción sin un test que falle.

**Orden de implementación sugerido `[NUEVO]`:**

1. `Card`, `Deck`, `Rank`, `Suit` (modelo puro).
2. `HandEvaluator` (identificar manos con tests exhaustivos).
3. `HandLevelManager` + tabla de valores.
4. `ScoreEngine` (con chips individuales, chips base, mult base; sin jokers aún).
5. `RoundManager` (contador de manos/descartes, targetScore, game over).
6. `Wallet` + economía de ronda.
7. Primeros 4 jokers + integración en `ScoreEngine`.
8. `SaveManager` + `UnlockService`.
9. Pantallas LibGDX (MainMenu → GameScreen → ShopScreen → GameOver/Victory).
10. Meta-progresión y logros.

*Consejo:* crea una suite de tests separada por clase. Ejecuta los tests cada pocos minutos durante el desarrollo.

---

## 8. Requisitos no funcionales

- **Rendimiento:** 60 fps en cualquier dispositivo Android de gama media (2019 en adelante).
- **Persistencia:** el guardado automático tras cada ronda no debe interrumpir la fluidez (ejecutar en hilo separado si es necesario, con `CompletableFuture` o similar en la capa de presentación).
- **Código:** comentarios en español (elige uno y mantén consistencia en todo el proyecto).
- **Tamaño de pantalla:** `FitViewport` con resolución base 800×480. `[ACLARADO]`
- **Idioma de la UI:** español. `[NUEVO]`
- **Sin dependencias de terceros salvo:** LibGDX, Gson, JUnit 5. `[NUEVO]`

---

## 9. Entregables finales

- Código fuente completo (repositorio Git con historial de commits).
- Tests unitarios (cobertura > 80% en paquetes `logic/`).
- Archivo `README.md` con instrucciones de compilación y ejecución (desktop y Android).
- Ejecutable JAR para escritorio y APK firmado para Android.
- `CHANGELOG.md` con el resumen de cambios entre versiones (buena práctica para el portfolio). `[NUEVO]`
