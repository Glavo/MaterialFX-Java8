package org.glavo.materialfx.adapter.skin;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.scene.control.TableView;

public class TableViewSkin<T> extends com.sun.javafx.scene.control.skin.TableViewSkin<T> {
    public TableViewSkin(TableView<T> control) {
        super(control);
    }

    protected VirtualFlowWrapper<?> lookupVirtualFlow(TableView<?> tableView) {
        return new VirtualFlowWrapper<>((VirtualFlow<?>) tableView.lookup(".virtual-flow"));
    }
}