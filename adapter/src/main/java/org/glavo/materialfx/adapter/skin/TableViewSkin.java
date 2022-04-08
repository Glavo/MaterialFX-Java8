package org.glavo.materialfx.adapter.skin;

import javafx.scene.control.TableView;
import javafx.scene.control.skin.VirtualFlow;

public class TableViewSkin<T> extends javafx.scene.control.skin.TableViewSkin<T> {
    public TableViewSkin(TableView<T> control) {
        super(control);
    }

    protected VirtualFlowWrapper<?> lookupVirtualFlow(TableView<?> tableView) {
        return new VirtualFlowWrapper<>((VirtualFlow<?>) tableView.lookup(".virtual-flow"));
    }
}
