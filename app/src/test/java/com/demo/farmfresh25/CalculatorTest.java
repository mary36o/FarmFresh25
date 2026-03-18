package com.demo.farmfresh25;

import static org.junit.Assert.*;

import org.junit.Test;

public class CalculatorTest {

    @Test
    public void add() {
        Calculator Calculator= new Calculator();
        int result =Calculator.add(5,5);
                assertEquals(10, result);
    }

    @Test
    public void mutiply() {
        Calculator Calculator= new Calculator();
        int result =Calculator.mutiply(5,5);
        assertEquals(25, result);
    }

    @Test
    public void fullName() {
        Calculator Calculator= new Calculator();
        String result =Calculator.fullName("John","Doe");
        assertEquals("JohnDoe", result);

    }
}

