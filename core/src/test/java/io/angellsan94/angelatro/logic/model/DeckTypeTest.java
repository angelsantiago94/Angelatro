package io.angellsan94.angelatro.logic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests unitarios para la clase {@link DeckType}.
 * 
 * @since 1.0
 */
class DeckTypeTest {

    @Nested
    @DisplayName("Pruebas de valores iniciales")
    class InitialValuesTests {

        @Test
        @DisplayName("STANDARD debe tener saldo inicial de 4, bonusChips de 0 y bonusMult de 0")
        void testStandardValues() {
            DeckType deckType = DeckType.STANDARD;
            
            assertEquals(4, deckType.getInitialMoney(), "Saldo inicial de STANDARD debe ser 4");
            assertEquals(0, deckType.getBonusChips(), "Bonus chips de STANDARD debe ser 0");
            assertEquals(0, deckType.getBonusMult(), "Bonus mult de STANDARD debe ser 0");
        }

        @Test
        @DisplayName("WEALTHY debe tener saldo inicial de 8, bonusChips de 0 y bonusMult de 0")
        void testWealthyValues() {
            DeckType deckType = DeckType.WEALTHY;
            
            assertEquals(8, deckType.getInitialMoney(), "Saldo inicial de WEALTHY debe ser 8");
            assertEquals(0, deckType.getBonusChips(), "Bonus chips de WEALTHY debe ser 0");
            assertEquals(0, deckType.getBonusMult(), "Bonus mult de WEALTHY debe ser 0");
        }

        @Test
        @DisplayName("POWERED debe tener saldo inicial de 4, bonusChips de 10 y bonusMult de 0")
        void testPoweredValues() {
            DeckType deckType = DeckType.POWERED;
            
            assertEquals(4, deckType.getInitialMoney(), "Saldo inicial de POWERED debe ser 4");
            assertEquals(10, deckType.getBonusChips(), "Bonus chips de POWERED debe ser 10");
            assertEquals(0, deckType.getBonusMult(), "Bonus mult de POWERED debe ser 0");
        }

        @Test
        @DisplayName("MULTIBASE debe tener saldo inicial de 4, bonusChips de 0 y bonusMult de 2")
        void testMultiBaseValues() {
            DeckType deckType = DeckType.MULTIBASE;
            
            assertEquals(4, deckType.getInitialMoney(), "Saldo inicial de MULTIBASE debe ser 4");
            assertEquals(0, deckType.getBonusChips(), "Bonus chips de MULTIBASE debe ser 0");
            assertEquals(2, deckType.getBonusMult(), "Bonus mult de MULTIBASE debe ser 2");
        }
    }

    @Nested
    @DisplayName("Pruebas del método getId")
    class GetIdTests {

        @Test
        @DisplayName("STANDARD debe retornar 'STANDARD' con getId")
        void testStandardGetId() {
            assertEquals("STANDARD", DeckType.STANDARD.getId(), "STANDARD debe retornar 'STANDARD'");
        }

        @Test
        @DisplayName("WEALTHY debe retornar 'WEALTHY' con getId")
        void testWealthyGetId() {
            assertEquals("WEALTHY", DeckType.WEALTHY.getId(), "WEALTHY debe retornar 'WEALTHY'");
        }

        @Test
        @DisplayName("POWERED debe retornar 'POWERED' con getId")
        void testPoweredGetId() {
            assertEquals("POWERED", DeckType.POWERED.getId(), "POWERED debe retornar 'POWERED'");
        }

        @Test
        @DisplayName("MULTIBASE debe retornar 'MULTIBASE' con getId")
        void testMultiBaseGetId() {
            assertEquals("MULTIBASE", DeckType.MULTIBASE.getId(), "MULTIBASE debe retornar 'MULTIBASE'");
        }
    }

    @Nested
    @DisplayName("Pruebas del método fromId")
    class FromIdTests {

        @Test
        @DisplayName("fromId con 'STANDARD' debe retornar DeckType.STANDARD")
        void testFromIdStandard() {
            DeckType result = DeckType.fromId("STANDARD");
            assertEquals(DeckType.STANDARD, result, "fromId('STANDARD') debe retornar STANDARD");
        }

        @Test
        @DisplayName("fromId con 'wealthy' (minúsculas) debe retornar DeckType.WEALTHY")
        void testFromIdWealthyLowerCase() {
            DeckType result = DeckType.fromId("wealthy");
            assertEquals(DeckType.WEALTHY, result, "fromId('wealthy') debe retornar WEALTHY");
        }

        @Test
        @DisplayName("fromId con 'WeAlThY' (mezcla) debe retornar DeckType.WEALTHY")
        void testFromIdWealthyMixedCase() {
            DeckType result = DeckType.fromId("WeAlThY");
            assertEquals(DeckType.WEALTHY, result, "fromId('WeAlThY') debe retornar WEALTHY");
        }

        @Test
        @DisplayName("fromId con 'POWERED' debe retornar DeckType.POWERED")
        void testFromIdPowered() {
            DeckType result = DeckType.fromId("POWERED");
            assertEquals(DeckType.POWERED, result, "fromId('POWERED') debe retornar POWERED");
        }

        @Test
        @DisplayName("fromId con 'multibase' (minúsculas) debe retornar DeckType.MULTIBASE")
        void testFromIdMultiBaseLowerCase() {
            DeckType result = DeckType.fromId("multibase");
            assertEquals(DeckType.MULTIBASE, result, "fromId('multibase') debe retornar MULTIBASE");
        }

        @Test
        @DisplayName("fromId con identificador null debe lanzar IllegalArgumentException")
        void testFromIdNullThrowsException() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DeckType.fromId(null),
                "fromId(null) debe lanzar IllegalArgumentException"
            );
            assertTrue(exception.getMessage().contains("null"), "El mensaje debe indicar que el identificador no puede ser null");
        }

        @Test
        @DisplayName("fromId con identificador inexistente debe lanzar IllegalArgumentException")
        void testFromIdNonExistentThrowsException() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DeckType.fromId("INVALID_ID"),
                "fromId('INVALID_ID') debe lanzar IllegalArgumentException"
            );
            assertTrue(exception.getMessage().contains("No existe un DeckType"), 
                "El mensaje debe indicar que no existe el DeckType");
        }
    }

    @Nested
    @DisplayName("Pruebas del método fromIdOptional")
    class FromIdOptionalTests {

        @Test
        @DisplayName("fromIdOptional con 'STANDARD' debe retornar Optional con STANDARD")
        void testFromIdOptionalStandard() {
            Optional<DeckType> result = DeckType.fromIdOptional("STANDARD");
            assertTrue(result.isPresent(), "fromIdOptional('STANDARD') debe retornar un Optional presente");
            assertEquals(DeckType.STANDARD, result.get(), "El Optional debe contener STANDARD");
        }

        @Test
        @DisplayName("fromIdOptional con 'wealthy' (minúsculas) debe retornar Optional con WEALTHY")
        void testFromIdOptionalWealthyLowerCase() {
            Optional<DeckType> result = DeckType.fromIdOptional("wealthy");
            assertTrue(result.isPresent(), "fromIdOptional('wealthy') debe retornar un Optional presente");
            assertEquals(DeckType.WEALTHY, result.get(), "El Optional debe contener WEALTHY");
        }

        @Test
        @DisplayName("fromIdOptional con identificador null debe retornar Optional.empty()")
        void testFromIdOptionalNullReturnsEmpty() {
            Optional<DeckType> result = DeckType.fromIdOptional(null);
            assertTrue(result.isEmpty(), "fromIdOptional(null) debe retornar Optional.empty()");
        }

        @Test
        @DisplayName("fromIdOptional con identificador inexistente debe retornar Optional.empty()")
        void testFromIdOptionalNonExistentReturnsEmpty() {
            Optional<DeckType> result = DeckType.fromIdOptional("INVALID_ID");
            assertTrue(result.isEmpty(), "fromIdOptional('INVALID_ID') debe retornar Optional.empty()");
        }

        @Test
        @DisplayName("fromIdOptional con cadena vacía debe retornar Optional.empty()")
        void testFromIdOptionalEmptyStringReturnsEmpty() {
            Optional<DeckType> result = DeckType.fromIdOptional("");
            assertTrue(result.isEmpty(), "fromIdOptional('') debe retornar Optional.empty()");
        }
    }

    @Nested
    @DisplayName("Pruebas de inmutabilidad")
    class ImmutabilityTests {

        @Test
        @DisplayName("STANDARD debe ser inmutable - getInitialMoney debe retornar constante")
        void testStandardIsImmutable() {
            DeckType standard = DeckType.STANDARD;
            
            // Intento de modificar (debería fallar en compilación, pero verificamos el valor)
            int initialMoney = standard.getInitialMoney();
            assertEquals(4, initialMoney, "getInitialMoney debe retornar valor constante");
            
            int bonusChips = standard.getBonusChips();
            assertEquals(0, bonusChips, "getBonusChips debe retornar valor constante");
            
            int bonusMult = standard.getBonusMult();
            assertEquals(0, bonusMult, "getBonusMult debe retornar valor constante");
            
            assertEquals("STANDARD", standard.getId(), "getId debe retornar valor constante");
        }
    }

    @Nested
    @DisplayName("Pruebas de cobertura completa de valores")
    class FullCoverageTests {

        @Test
        @DisplayName("fromId debe encontrar todos los DeckType existentes")
        void testFromIdFindsAllDeckTypes() {
            // Verificar que todos los valores están disponibles
            DeckType standard = DeckType.fromId("STANDARD");
            DeckType wealthy = DeckType.fromId("WEALTHY");
            DeckType powered = DeckType.fromId("POWERED");
            DeckType multiBase = DeckType.fromId("MULTIBASE");
            
            // Verificar que los valores obtenidos son los correctos
            assertEquals(DeckType.STANDARD, standard);
            assertEquals(DeckType.WEALTHY, wealthy);
            assertEquals(DeckType.POWERED, powered);
            assertEquals(DeckType.MULTIBASE, multiBase);
        }

        @Test
        @DisplayName("fromIdOptional debe encontrar todos los DeckType existentes")
        void testFromIdOptionalFindsAllDeckTypes() {
            Optional<DeckType> standardOpt = DeckType.fromIdOptional("STANDARD");
            Optional<DeckType> wealthyOpt = DeckType.fromIdOptional("WEALTHY");
            Optional<DeckType> poweredOpt = DeckType.fromIdOptional("POWERED");
            Optional<DeckType> multiBaseOpt = DeckType.fromIdOptional("MULTIBASE");
            
            assertTrue(standardOpt.isPresent());
            assertTrue(wealthyOpt.isPresent());
            assertTrue(poweredOpt.isPresent());
            assertTrue(multiBaseOpt.isPresent());
        }
    }
}