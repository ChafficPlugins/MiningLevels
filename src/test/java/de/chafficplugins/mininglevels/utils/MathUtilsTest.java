package de.chafficplugins.mininglevels.utils;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {

    @RepeatedTest(50)
    void randomDouble_shouldReturnValueInRange() {
        double result = MathUtils.randomDouble(0, 100);
        assertTrue(result >= 0 && result < 100,
                "Expected value in [0, 100), got: " + result);
    }

    @RepeatedTest(50)
    void randomDouble_shouldReturnValueInNegativeRange() {
        double result = MathUtils.randomDouble(-50, 50);
        assertTrue(result >= -50 && result < 50,
                "Expected value in [-50, 50), got: " + result);
    }

    @Test
    void randomDouble_sameMinMax_shouldReturnMin() {
        double result = MathUtils.randomDouble(5, 5);
        assertEquals(5.0, result, 0.001);
    }

    @RepeatedTest(20)
    void randomDouble_smallRange_shouldReturnValueInRange() {
        double result = MathUtils.randomDouble(1, 3);
        assertTrue(result >= 1 && result < 3,
                "Expected value in [1, 3), got: " + result);
    }

    @RepeatedTest(50)
    void randomDouble_zeroToOne_shouldReturnValueInRange() {
        double result = MathUtils.randomDouble(0, 1);
        assertTrue(result >= 0 && result < 1,
                "Expected value in [0, 1), got: " + result);
    }

    @RepeatedTest(30)
    void randomDouble_largeRange_shouldReturnValueInRange() {
        double result = MathUtils.randomDouble(-1_000_000, 1_000_000);
        assertTrue(result >= -1_000_000 && result < 1_000_000,
                "Expected value in [-1000000, 1000000), got: " + result);
    }

    @RepeatedTest(30)
    void randomDouble_verySmallRange_shouldReturnValueInRange() {
        double result = MathUtils.randomDouble(0.5, 0.5001);
        assertTrue(result >= 0.5 && result < 0.5001,
                "Expected value in [0.5, 0.5001), got: " + result);
    }

    @RepeatedTest(30)
    void randomDouble_negativeRange_shouldReturnValueInRange() {
        double result = MathUtils.randomDouble(-100, -50);
        assertTrue(result >= -100 && result < -50,
                "Expected value in [-100, -50), got: " + result);
    }

    @Test
    void randomDouble_zeroRange_atZero_shouldReturnZero() {
        double result = MathUtils.randomDouble(0, 0);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    void randomDouble_zeroRange_atNegative_shouldReturnThatValue() {
        double result = MathUtils.randomDouble(-7.5, -7.5);
        assertEquals(-7.5, result, 0.001);
    }

    @Test
    void randomDouble_producesDistinctValues() {
        // Over 100 calls, we should see at least 2 distinct values
        double first = MathUtils.randomDouble(0, 1000);
        boolean foundDifferent = false;
        for (int i = 0; i < 100; i++) {
            double next = MathUtils.randomDouble(0, 1000);
            if (Math.abs(next - first) > 0.001) {
                foundDifferent = true;
                break;
            }
        }
        assertTrue(foundDifferent, "Expected distinct values over 100 calls");
    }

    @RepeatedTest(30)
    void randomDouble_integerBounds_shouldReturnValueInRange() {
        double result = MathUtils.randomDouble(10, 20);
        assertTrue(result >= 10 && result < 20,
                "Expected value in [10, 20), got: " + result);
    }

    @RepeatedTest(20)
    void randomDouble_fractionalBounds_shouldReturnValueInRange() {
        double result = MathUtils.randomDouble(1.5, 2.5);
        assertTrue(result >= 1.5 && result < 2.5,
                "Expected value in [1.5, 2.5), got: " + result);
    }
}
