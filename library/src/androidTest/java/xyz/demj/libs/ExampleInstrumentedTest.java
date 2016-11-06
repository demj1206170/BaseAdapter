package xyz.demj.libs;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import xyz.demj.libs.camrecyclerviewadapter.BaseRecyclerViewAdapter;
import xyz.demj.libs.camrecyclerviewadapter.MultiSelectionRecyclerViewAdapter;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("xyz.demj.library.test", appContext.getPackageName());
    }

    static class A {
        int mInt;

        A(int index) {
            mInt = index;
        }

        @Override
        public String toString() {
            return "index: " + mInt;
        }
    }

    static class M extends A {

        M(int index) {
            super(index);
        }
    }

    public void test() {
        BaseRecyclerViewAdapter<A> baseAdapter = new MultiSelectionRecyclerViewAdapter<>(null);
    }
}
