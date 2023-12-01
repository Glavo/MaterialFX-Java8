package org.glavo.materialfx.adapter.skin;

public final class Utils {
    public static <V> V invoke(Lambda<V> callable) {
        try {
            return callable.invoke();
        } catch (Throwable t) {
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new Error(t);
            }
        }
    }

    public static double invoke(DoubleLambda callable) {
        try {
            return callable.invoke();
        } catch (Throwable t) {
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new Error(t);
            }
        }
    }

    public interface Lambda<V> {
        V invoke() throws Throwable;
    }

    public interface DoubleLambda {
        double invoke() throws Throwable;
    }
}
