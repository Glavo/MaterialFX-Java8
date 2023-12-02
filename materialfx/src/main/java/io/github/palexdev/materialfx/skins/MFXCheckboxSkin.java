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

package io.github.palexdev.materialfx.skins;

import io.github.palexdev.materialfx.beans.PositionBean;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.effects.ripple.MFXCircleRippleGenerator;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.skins.base.MFXLabeledSkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * This is the implementation of the {@code Skin} associated with every {@link MFXCheckbox}.
 */
public class MFXCheckboxSkin extends MFXLabeledSkinBase<MFXCheckbox> {
	//================================================================================
	// Properties
	//================================================================================

	private final StackPane rippleContainer;
	private final MFXCircleRippleGenerator rippleGenerator;

	//================================================================================
	// Constructors
	//================================================================================
	public MFXCheckboxSkin(MFXCheckbox checkbox) {
		super(checkbox);

		MFXFontIcon mark = new MFXFontIcon();
		mark.getStyleClass().add("mark");
		MFXIconWrapper box = new MFXIconWrapper(mark, -1);
		box.getStyleClass().add("box");

		rippleContainer = new StackPane();
		rippleGenerator = new MFXCircleRippleGenerator(rippleContainer);
		rippleGenerator.setManaged(false);
		rippleGenerator.setAnimateBackground(false);
		rippleGenerator.setCheckBounds(false);
		rippleGenerator.setClipSupplier(() -> null);
		rippleGenerator.setRipplePositionFunction(event -> {
			PositionBean position = new PositionBean();
			position.setX(Math.min(event.getX(), rippleContainer.getWidth()));
			position.setY(Math.min(event.getY(), rippleContainer.getHeight()));
			return position;
		});

		rippleContainer.getChildren().addAll(rippleGenerator, box);
		rippleContainer.getStyleClass().add("ripple-container");

		updateAlignment();
		initContainer();

		getChildren().setAll(topContainer);
		addListeners();
	}

	//================================================================================
	// Overridden Methods
	//================================================================================
	@Override
	protected void addListeners() {
		super.addListeners();
		MFXCheckbox checkbox = getSkinnable();

		checkbox.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			rippleGenerator.generateRipple(event);
			checkbox.fire();
		});
	}

	@Override
	protected Pane getControlContainer() {
		return rippleContainer;
	}
}
