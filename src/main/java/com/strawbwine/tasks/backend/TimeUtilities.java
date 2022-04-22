package com.strawbwine.tasks.backend;

import java.time.Duration;

public class TimeUtilities {
    public static Duration fromHoursToDuration(double hours) {
        long minutes = (long)(hours*60);
        return Duration.ofMinutes(minutes);
    }
}
