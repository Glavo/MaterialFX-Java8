package org.glavo.materialfx.adapter.skin;

import javafx.scene.control.ListView;
import javafx.scene.control.skin.VirtualFlow;

public class ListViewSkin<T> extends javafx.scene.control.skin.ListViewSkin<T> {
    public ListViewSkin(ListView<T> control) {
        super(control);
    }

    protected VirtualFlowWrapper<?> lookupVirtualFlow(ListView<?> listView) {
        return new VirtualFlowWrapper<>((VirtualFlow<?>) listView.lookup(".virtual-flow"));
    }
}
