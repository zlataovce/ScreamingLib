package org.screamingsandals.lib.utils.event;

import java.util.Arrays;
import java.util.List;

public enum EventPriority {
    LOWEST,
    LOW,
    NORMAL,
    HIGH,
    HIGHEST;

    public static List<EventPriority> VALUES = Arrays.asList(values());
}
