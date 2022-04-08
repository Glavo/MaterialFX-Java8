/*
 * Copyright (C) 2022 Parisi Alessandro
 * This file is part of MaterialFX (https://github.com/palexdev/MaterialFX).
 *
 * MaterialFX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MaterialFX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MaterialFX.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.palexdev.materialfx.utils;

import java.util.List;
import java.util.Random;

public class RandomUtils {
	public static final Random random = new Random(System.currentTimeMillis());

	private RandomUtils() {
	}

	/**
	 * @return a random value from the given array
	 */
	public static <T> T randFromArray(T[] array) {
		int index = random.nextInt(array.length);
		return array[index];
	}

	/**
	 * @return a random value from the given list
	 */
	public static <T> T randFromList(List<T> list) {
		int index = random.nextInt(list.size());
		return list.get(index);
	}

	public static int nextInt(int origin, int bound) {
		int value = random.nextInt();

		return (value % (bound - origin)) + origin;
	}
}
