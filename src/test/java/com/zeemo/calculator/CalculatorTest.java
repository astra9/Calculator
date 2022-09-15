package com.zeemo.calculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {
    private Calculator calculator = new Calculator();

    @Test
    public void testSum(){
        assertEquals(5, calculator.sum(2,3));
    }

    @Test
    public void testSubtraction(){
        assertEquals(5, calculator.subtract(8,3));
    }
}
