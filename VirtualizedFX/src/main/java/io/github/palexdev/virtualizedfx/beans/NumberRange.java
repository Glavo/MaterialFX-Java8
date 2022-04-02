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

package io.github.palexdev.virtualizedfx.beans;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Simple bean to represent a range of values from min to max.
 *
 * @param <T> The type of Number to represent
 */
public class NumberRange<T extends Number> {
    //================================================================================
    // Properties
    //================================================================================
    private final T min;
    private final T max;

    //================================================================================
    // Constructors
    //================================================================================
    public NumberRange(T min, T max) {
        this.min = min;
        this.max = max;
    }

    //================================================================================
    // Methods
    //================================================================================

    /**
     * @return the upper bound
     */
    public T getMin() {
        return min;
    }

    /**
     * @return the lower bound
     */
    public T getMax() {
        return max;
    }

    /**
     * Expands a range on integers to a List of integers.
     */
    public static List<Integer> expandRange(NumberRange<Integer> range) {
        return IntStream.rangeClosed(range.getMin(), range.getMax()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Expands a range of integers to a Set of integers.
     */
    public static Set<Integer> expandRangeToSet(NumberRange<Integer> range) {
        return IntStream.rangeClosed(range.getMin(), range.getMax()).collect(HashSet::new, HashSet::add, HashSet::addAll);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberRange<?> that = (NumberRange<?>) o;
        return Objects.equals(min, that.min) && Objects.equals(max, that.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }

    @Override
    public String toString() {
        return "Min[" + min + "], Max[" + max + "]";
    }
    //================================================================================
    // Static Methods
    //================================================================================

    /**
     * Returns a new instance of NumberRange with the given min and max bounds.
     */
    public static <T extends Number> NumberRange<T> of(T min, T max) {
        return new NumberRange<>(min, max);
    }

    /**
     * Returns a new instance of NumberRange with the given val as both min and max bounds.
     */
    public static <T extends Number> NumberRange<T> of(T val) {
        return new NumberRange<>(val, val);
    }

    /**
     * Checks if the given value is contained in the given range (bounds are included).
     */
    public static boolean inRangeOf(double val, NumberRange<Double> range) {
        return Math.max(range.getMin(), val) == Math.min(val, range.getMax());
    }

    /**
     * Checks if the given value is contained in the given range (bounds are included).
     */
    public static boolean inRangeOf(float val, NumberRange<Float> range) {
        return Math.max(range.getMin(), val) == Math.min(val, range.getMax());
    }

    /**
     * Checks if the given value is contained in the given range (bounds are included).
     */
    public static boolean inRangeOf(int val, NumberRange<Integer> range) {
        return Math.max(range.getMin(), val) == Math.min(val, range.getMax());
    }

    /**
     * Checks if the given value is contained in the given range (bounds are included).
     */
    public static boolean inRangeOf(long val, NumberRange<Long> range) {
        return Math.max(range.getMin(), val) == Math.min(val, range.getMax());
    }

    /**
     * Checks if the given value is contained in any of the given ranges (bounds are included).
     */
    public static boolean inRangeOf(double val, List<NumberRange<Double>> ranges) {
        return ranges.stream().anyMatch(range -> inRangeOf(val, range));
    }

    /**
     * Checks if the given value is contained in any of the given ranges (bounds are included).
     */
    public static boolean inRangeOf(float val, List<NumberRange<Float>> ranges) {
        return ranges.stream().anyMatch(range -> inRangeOf(val, range));
    }

    /**
     * Checks if the given value is contained in any of the given ranges (bounds are included).
     */
    public static boolean inRangeOf(int val, List<NumberRange<Integer>> ranges) {
        return ranges.stream().anyMatch(range -> inRangeOf(val, range));
    }

    /**
     * Checks if the given value is contained in any of the given ranges (bounds are included).
     */
    public static boolean inRangeOf(long val, List<NumberRange<Long>> ranges) {
        return ranges.stream().anyMatch(range -> inRangeOf(val, range));
    }
}
