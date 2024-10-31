package ru.tyatyushkin.telegram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class MainTest {

    @Test
    public void testMain() {
        // This is a simple test to check if the main method runs without exceptions
        Main.main(new String[]{});
        assertEquals(1, 1, "This is a placeholder test");
    }

}
