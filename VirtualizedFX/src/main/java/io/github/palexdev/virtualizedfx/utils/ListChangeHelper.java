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

package io.github.palexdev.virtualizedfx.utils;

import io.github.palexdev.virtualizedfx.beans.NumberRange;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Helper class to process changes in a list of items of type T, it also needs a range of indexes to
 * whether the changed values are within range.
 * <p></p>
 * Uses three beans to compute the change: {@link Change}, {@link ChangeBean}, {@link ReplaceBean}.
 */
public class ListChangeHelper {

    /**
     * Processes the given change for the given range of indexes.
     * @return a new instance of {@link Change} containing all the changes
     */
    public static <T> Change processChange(ListChangeListener.Change<? extends T> change, NumberRange<Integer> indexes) {
        Set<ChangeBean> added = new HashSet<>();
        Set<ChangeBean> removed = new HashSet<>();
        Set<ReplaceBean> replaced = new HashSet<>();

        int removeFrom = -1;
        int removeTo = -1;
        Set<Integer> removedAccumulator = new HashSet<>();

        int removedSize = 0;
        while (change.next()) {
            if (change.wasReplaced()) {
                NumberRange<Integer> range = NumberRange.of(change.getFrom(), change.getTo() - 1);
                Set<Integer> changed = NumberRange.expandRangeToSet(range).stream().filter(i -> NumberRange.inRangeOf(i, indexes)).collect(Collectors.toSet());

                removeFrom = change.getTo();
                removeTo = NumberUtils.clamp(change.getRemovedSize() - 1, 0, indexes.getMax());
                removedAccumulator.addAll(NumberRange.expandRangeToSet(NumberRange.of(removeFrom, removeTo)));

                removedAccumulator.removeAll(changed);
                replaced.add(new ReplaceBean(range, changed, removedAccumulator));
                continue;
            }
            if (change.wasAdded()) {
                NumberRange<Integer> range = NumberRange.of(change.getFrom(), change.getTo() - 1);
                added.add(new ChangeBean(range, NumberRange.expandRangeToSet(range)));
                continue;
            }
            if (change.wasRemoved()) {
                NumberRange<Integer> range = computeRemovedIndexes(change, removedSize);
                if (removeFrom == -1) removeFrom = range.getMin();
                removeTo = range.getMax();
                removedAccumulator.addAll(NumberRange.expandRangeToSet(range));
                removedSize += change.getRemovedSize();
            }
        }
        removed.add(new ChangeBean(NumberRange.of(removeFrom, removeTo), removedAccumulator));
        return new Change(added, removed, replaced);
    }

    /**
     * Helper method to correctly compute the index of one or multiple removal
     * changes.
     */
    private static <T> NumberRange<Integer> computeRemovedIndexes(ListChangeListener.Change<? extends T> change, int toOffset) {
        int size = change.getList().size();
        if (size == 0) {
            return NumberRange.of(0, change.getRemovedSize() - 1);
        }

        int from = change.getTo() + toOffset;
        int to = change.getFrom() + (change.getRemovedSize() - 1) + toOffset;
        return NumberRange.of(from, to);
    }

    /**
     * Bean to contain two Sets of added and removed {@link ChangeBean}s and one of {@link ReplaceBean}s.
     * <p>
     * This can also process the addition, removal and replacement with {@link #processAddition(TriConsumer)},
     * {@link #processRemoval(TriConsumer)}, {@link #processReplacement(BiConsumer)}.
     * <p></p>
     * When processing the changes the order matters!! You must first process replacements, then additions and then removals!!
     */
    public static class Change {
        //================================================================================
        // Properties
        //================================================================================
        private boolean wasReplacement;
        private final Set<ChangeBean> added;
        private final Set<ChangeBean> removed;
        private final Set<ReplaceBean> replaced;

        //================================================================================
        // Constructors
        //================================================================================
        private Change(Set<ChangeBean> added, Set<ChangeBean> removed, Set<ReplaceBean> replaced) {
            this.added = added;
            this.removed = removed;
            this.replaced = replaced;
        }

        //================================================================================
        // Methods
        //================================================================================

        /**
         * For each {@link ChangeBean} that is an addition executes the given action.
         * <p>
         * The consumer gives the 'from' index, the 'to' index and the Set of added indexes.
         */
        public void processAddition(TriConsumer<Integer, Integer, Set<Integer>> action) {
            if (added.isEmpty()) return;
            for (ChangeBean changeBean : added) {
                if (changeBean.changed.isEmpty()) continue;
                action.accept(changeBean.gerFrom(), changeBean.getTo(), changeBean.changed);
            }
        }

        /**
         * For each {@link ChangeBean} that is a removal executes the given action.
         * <p>
         * The consumer gives the 'from' index, the 'to' index and the Set of removed indexes.
         */
        public void processRemoval(TriConsumer<Integer, Integer, Set<Integer>> action) {
            if (removed.isEmpty() || wasReplacement) return;
            for (ChangeBean changeBean : removed) {
                if (changeBean.changed.isEmpty()) continue;
                action.accept(changeBean.gerFrom(), changeBean.getTo(), changeBean.changed);
            }
        }

        /**
         * For each {@link ReplaceBean} executes the given action.
         * <p>
         * The consumer gives the Set of changed indexes and the set of removed indexes.
         */
        public void processReplacement(BiConsumer<Set<Integer>, Set<Integer>> action) {
            if (replaced.isEmpty()) return;
            for (ReplaceBean replaceBean : replaced) {
                if (replaceBean.isEmpty()) continue;
                action.accept(replaceBean.changed, replaceBean.removed);
            }
            wasReplacement = true;
        }

        //================================================================================
        // Getters
        //================================================================================

        /**
         * @return the Set of {@link ChangeBean}s that are additions
         */
        public Set<ChangeBean> getAdded() {
            return added;
        }

        /**
         * @return the Set of {@link ChangeBean}s that are removals
         */
        public Set<ChangeBean> getRemoved() {
            return removed;
        }

        /**
         * @return the Set of {@link ReplaceBean}s
         */
        public Set<ReplaceBean> getReplaced() {
            return replaced;
        }
    }

    /**
     * Bean to represent an addition or removal change.
     */
    public static class ChangeBean {
        //================================================================================
        // Properties
        //================================================================================
        private final NumberRange<Integer> range;
        private final Set<Integer> changed;

        //================================================================================
        // Constructor
        //================================================================================
        public ChangeBean(NumberRange<Integer> range, Set<Integer> added) {
            this.range = range;
            this.changed = added;
        }

        //================================================================================
        // Getters
        //================================================================================

        /**
         * @return the index at which the change started
         */
        public int gerFrom() {
            return range.getMin();
        }

        /**
         * @return the index at which the change ended
         */
        public int getTo() {
            return range.getMax();
        }
    }

    /**
     * Bean to represent a replacement change.
     * <p>
     * In JavaFX replacements are a particular type of change because they can
     * signal both replacements and removals at the same time.
     * <p></p>
     * For example if you use {@link ObservableList#setAll(Collection)} the change will carry the new
     * items as a replacement and all the others as a removal.
     */
    public static class ReplaceBean {
        //================================================================================
        // Properties
        //================================================================================
        private final NumberRange<Integer> range;
        private final Set<Integer> changed;
        private final Set<Integer> removed;

        //================================================================================
        // Constructors
        //================================================================================
        public ReplaceBean(NumberRange<Integer> range, Set<Integer> changed, Set<Integer> removed) {
            this.range = range;
            this.changed = changed;
            this.removed = removed;
        }

        //================================================================================
        // Getters
        //================================================================================

        /**
         * @return the index at which the change started
         */
        public int gerFrom() {
            return range.getMin();
        }

        /**
         * @return the index at which the change ended
         */
        public int getTo() {
            return range.getMax();
        }

        /**
         * @return checks whether both 'changed' and 'removed' Sets are empty
         */
        public boolean isEmpty() {
            return changed.isEmpty() && removed.isEmpty();
        }
    }
}
