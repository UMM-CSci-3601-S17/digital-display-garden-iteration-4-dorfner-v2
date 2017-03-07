package umm3601.environments;

import org.junit.Rule;
import org.junit.Test;

import org.junit.contrib.java.lang.system.ProvideSystemProperty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnvironmentSpec {

    @Rule
    public final ProvideSystemProperty myPropertyHasMyValue
            = new ProvideSystemProperty("MyProperty", "MyValue");

    @Test
    public void doNothing() {
        assertTrue(true);
        assertEquals("MyValue", System.getProperty("MyProperty"));
    }
}
