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
}
