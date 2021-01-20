package ax.ha.it.starter.utilities;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

public class TreeViewUtility {
    private final TreeView<SourceUtility> fileTreeView;
    private TreeItem<SourceUtility> treeItem;

    public TreeViewUtility(TreeView<SourceUtility> fileTreeView, TreeItem<SourceUtility> treeItem) {
        this.fileTreeView = fileTreeView;
        this.treeItem = treeItem;
        fileTreeView.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEventHandle);
        setRootItem();
        fileTreeView.setEditable(false);
    }

    EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> { // TODO: Implement code for mouseEventhandler
        System.out.println(treeItem.getValue());
    }; // TODO: In this method we want to get the code that is

    public void setRootItem() {
        fileTreeView.setRoot(treeItem);
        fileTreeView.setShowRoot(false);
    }

    public void addTreeItem(SourceUtility item) {
        treeItem.getChildren().add(new TreeItem<>(item));
    }

    public void addTreeItemWithValue(SourceUtility item) {
        treeItem.getChildren().add(new TreeItem<>(item));
    }
}
