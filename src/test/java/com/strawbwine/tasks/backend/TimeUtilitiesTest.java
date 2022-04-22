package com.strawbwine.tasks.backend;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilitiesTest {
    @Test
    void addTwoNumbers() {
        assertEquals(2, 1+1);
    }

    @Test
    void returnCorrectOutput() {
        assertEquals(Duration.ofMinutes(210), TimeUtilities.fromHoursToDuration(3.50));
    }

    @Test
    void negativeNumberOfHours() {
        assertEquals(Duration.ofMinutes(-210), TimeUtilities.fromHoursToDuration(-3.50));
    }
}