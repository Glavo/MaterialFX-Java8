package org.glavo.materialfx.adapter.skin;

import com.sun.javafx.tk.TKStage;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Window;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

@SuppressWarnings("deprecation")
public class SnapUtils {
    private SnapUtils() {
    }

    private static final double EPSILON = 1e-14;

    private static double scaledCeil(double value, double scale) {
        return Math.ceil(value * scale - EPSILON) / scale;
    }

    private static double scaledRound(double value, double scale) {
        return Math.round(value * scale) / scale;
    }

    private static double snapSizeX(Region region, double value, boolean snapToPixel) {
        return snapToPixel ? scaledCeil(value, getSnapScale(region)) : value;
    }

    private static double snapSizeY(Region region, double value, boolean snapToPixel) {
        return snapToPixel ? scaledCeil(value, getSnapScale(region)) : value;
    }

    private static double snapPositionX(Region region, double value, boolean snapToPixel) {
        return snapToPixel ? scaledRound(value, getSnapScale(region)) : value;
    }

    private static double snapPositionY(Region region, double value, boolean snapToPixel) {
        return snapToPixel ? scaledRound(value, getSnapScale(region)) : value;
    }

    private static final MethodHandle IMPL_GETPEER_HANDLE = Utils.invoke(() -> MethodHandles.publicLookup()
            .findVirtual(
                    Window.class,
                    "impl_getPeer",
                    MethodType.methodType(TKStage.class)
            ));

    private static final MethodHandle GET_RENDER_SCALE_HANDLE = Utils.invoke(() -> MethodHandles.publicLookup()
            .findVirtual(
                    TKStage.class,
                    "getRenderScale",
                    MethodType.methodType(float.class)
            ));

    private static double getSnapScale(Region region) {
        Scene scene = region.getScene();
        if (scene == null) return 1.0;
        Window window = scene.getWindow();
        if (window == null) return 1.0;
        return Utils.invoke(() -> {
            TKStage peer = (TKStage) IMPL_GETPEER_HANDLE.invoke(window);
            if (peer == null) return 1.0D;
            return ((Float) GET_RENDER_SCALE_HANDLE.invoke(peer)).doubleValue();
        });
    }

    private static double snapSpaceX(Region region, double value, boolean snapToPixel) {
        return snapToPixel ? scaledRound(value, getSnapScale(region)) : value;
    }

    private static double snapSpaceY(Region region, double value, boolean snapToPixel) {
        return snapToPixel ? scaledRound(value, getSnapScale(region)) : value;
    }


    public static double snapSizeX(Region region, double value) {
        return snapSizeX(region, value, region.isSnapToPixel());
    }

    public static double snapSizeY(Region region, double value) {
        return snapSizeY(region, value, region.isSnapToPixel());
    }

    public static double snapPositionX(Region region, double value) {
        return snapPositionX(region, value, region.isSnapToPixel());
    }

    public static double snapPositionY(Region region, double value) {
        return snapPositionY(region, value, region.isSnapToPixel());
    }

    public static double snapSpaceX(Region region, double value) {
        return snapSpaceX(region, value, region.isSnapToPixel());
    }

    public static double snapSpaceY(Region region, double value) {
        return snapSpaceY(region, value, region.isSnapToPixel());
    }
}
