package org.glavo.materialfx.adapter.skin;

import javafx.scene.layout.Region;

public class SnapUtils {
    private SnapUtils() {
    }


    public static double snapSizeX(Region region, double value) {
        return region.snapSizeX(value);
    }

    public static double snapSizeY(Region region, double value) {
        return region.snapSizeY(value);
    }

    public static double snapPositionX(Region region, double value) {
        return region.snapPositionX(value);
    }

    public static double snapPositionY(Region region, double value) {
        return region.snapPositionY(value);
    }

    public static double snapSpaceX(Region region, double value) {
        return region.snapSpaceX(value);
    }

    public static double snapSpaceY(Region region, double value) {
        return region.snapSpaceY(value);
    }
}
