## Fase 4 — Gestión de la ronda

**Paquete:** `core/io/angellsan94/angelatro/logic/jokers`

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


