package xyz.demj.baseadapter;

/**
 * Created by demj on 2016/10/15.
 */

public class A {
    int mInt;

    public A(int pI) {
        mInt = pI;
    }

    @Override
    public String toString() {
        return "" + mInt;
    }

    public static class B extends A {

        public B(int pI) {
            super(pI);
        }
    }
}
