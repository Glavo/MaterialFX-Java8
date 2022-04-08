package org.glavo.materialfx.adapter.skin;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.scene.control.IndexedCell;
import javafx.scene.layout.Region;

public class VirtualFlowWrapper<T extends IndexedCell<?>> {
    private final VirtualFlow<T> flow;

    public VirtualFlowWrapper(VirtualFlow<T> flow) {
        this.flow = flow;
    }

    public Region getVirtualFlow() {
        return flow;
    }
}
