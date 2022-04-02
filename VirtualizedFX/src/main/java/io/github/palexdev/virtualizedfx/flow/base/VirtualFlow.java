/*
 * Copyright (C) 2021 Parisi Alessandro
 * This file is part of VirtualizedFX (https://github.com/palexdev/VirtualizedFX).
 *
 * VirtualizedFX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VirtualizedFX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with VirtualizedFX.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.palexdev.virtualizedfx.flow.base;

import io.github.palexdev.virtualizedfx.cell.Cell;
import io.github.palexdev.virtualizedfx.flow.base.OrientationHelper.HorizontalHelper;
import io.github.palexdev.virtualizedfx.flow.base.OrientationHelper.VerticalHelper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Region;

import java.util.function.Function;

/**
 * Public API for every Virtual Flow
 *
 * @param <T> the type of objects to represent
 * @param <C> the type of Cell to use
 */
public interface VirtualFlow<T, C extends Cell<T>> {

    /**
     * @return the items list
     */
    ObservableList<T> getItems();

    /**
     * Replaces the items list with the given one.
     */
    void setItems(ObservableList<T> items);

    /**
     * @return the function used to build a Cell from an object of type T
     */
    Function<T, C> getCellFactory();

    /**
     * Property for the cell factory function.
     */
    ObjectProperty<Function<T, C>> cellFactoryProperty();

    /**
     * Sets the function used to build a Cell from an object of type T.
     */
    void setCellFactory(Function<T, C> cellFactory);

    /**
     * @return the width of the cells
     */
    double getCellWidth();

    /**
     * @return the height of the cells
     */
    double getCellHeight();

    /**
     * @return the instance of the horizontal scroll bar, null if the virtual flow
     * doesn't have it
     */
    ScrollBar getHBar();

    /**
     * @return the instance of the vertical scroll bar, null if the virtual flow
     * doesn't have it
     */
    ScrollBar getVBar();

    boolean isFitToWidth();

    /**
     * Specifies whether the cells should take all the space
     * available on the x axis.
     * <p>
     * This is used by the {@link VerticalHelper}.
     */
    BooleanProperty fitToWidthProperty();

    void setFitToWidth(boolean fitToWidth);

    boolean isFitToHeight();

    /**
     * Specifies whether the cells should take all the space
     * available on the y axis.
     * <p>
     * This is used by the {@link HorizontalHelper}.
     */
    BooleanProperty fitToHeightProperty();

    void setFitToHeight(boolean fitToHeight);

    /**
     * @return the horizontal scroll bar's value
     */
    double getHorizontalPosition();

    /**
     * Property for the horizontal scroll bar's value.
     */
    DoubleProperty horizontalPositionProperty();

    /**
     * Sets the horizontal scroll bar's value
     */
    void setHorizontalPosition(double hValue);

    /**
     * @return the vertical scroll bar's value
     */
    double getVerticalPosition();

    /**
     * Property for the vertical scroll bar's value.
     */
    DoubleProperty verticalPositionProperty();

    /**
     * Sets the vertical scroll bar's value
     */
    void setVerticalPosition(double vValue);

    /**
     * @return the instance of the virtual flow
     */
    Region getVirtualFlow();
}
