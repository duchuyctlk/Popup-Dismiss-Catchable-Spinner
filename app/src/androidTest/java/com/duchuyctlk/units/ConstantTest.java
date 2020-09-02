package com.duchuyctlk.units;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.duchuyctlk.Constant;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ConstantTest {
    @Test
    @SuppressWarnings("unchecked")
    public void testPrivateConstructor() {
        try {
            Constructor<Constant> constructor = Constant.class.getDeclaredConstructor((Class[]) null);
            constructor.setAccessible(true);
            constructor.newInstance((Object[]) null); //should throws AssertionError here
            assertTrue("This line is never reached!", false);
        } catch (Throwable e) {
            assertTrue("This line is reached!", true);
        }
    }
}
