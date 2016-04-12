package com.duchuyctlk.units;

import android.support.test.runner.AndroidJUnit4;

import com.duchuyctlk.Constant;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ConstantTest {
    @Test
    public void testPrivateConstructor() {
        try {
            Constructor<Constant> constructor = Constant.class.getDeclaredConstructor(new Class[0]);
            constructor.setAccessible(true);
            constructor.newInstance(new Object[0]); //should throws AssertionError here
            assertTrue("This line is never reached!", false);
        } catch (Throwable e) {
            assertTrue("This line is reached!", true);
        }
    }
}
