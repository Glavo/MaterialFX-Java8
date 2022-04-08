package org.glavo.materialfx.adapter.skin;

import javafx.scene.control.IndexedCell;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.layout.Region;

public class VirtualFlowWrapper<T extends IndexedCell<?>> {
    private final VirtualFlow<T> flow;

    public VirtualFlowWrapper(VirtualFlow<T> flow) {
        this.flow = flow;
    }

    public Region getVirtualFlow() {
        return flow;
    }

    public int getCellCount() {
        return flow.getCellCount();
    }

    public T getCell(int idx) {
        return flow.getCell(idx);
    }
}
