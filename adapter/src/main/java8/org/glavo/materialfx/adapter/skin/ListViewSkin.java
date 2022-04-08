package org.glavo.materialfx.adapter.skin;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.scene.control.ListView;

public class ListViewSkin<T> extends com.sun.javafx.scene.control.skin.ListViewSkin<T> {
    public ListViewSkin(ListView<T> control) {
        super(control);
    }

    protected VirtualFlowWrapper<?> lookupVirtualFlow(ListView<?> listView) {
        return new VirtualFlowWrapper<>((VirtualFlow<?>) listView.lookup(".virtual-flow"));
    }
}
