package ax.ha.it.starter.utilities;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

public class TreeViewUtility {
    public TreeView<String> fileTreeView;
    public TreeItem<String> treeItem;

    public TreeViewUtility(TreeView<String> fileTreeView, TreeItem<String> treeItem) {
        this.fileTreeView = fileTreeView;
        this.treeItem = treeItem;
        fileTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);
        setRootItem();
    }

    EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
        System.out.print(treeItem.getChildren());
        System.out.println(treeItem.getValue());
    };


    public void setRootItem() {
        fileTreeView.setRoot(treeItem);
        fileTreeView.setShowRoot(false);
    }

    public void addTreeItem(String itemName) {
        treeItem.getChildren().add(new TreeItem<>(itemName));
    }

    public void addTreeItemWithValue(String itemName, String value) {
        treeItem.getChildren().add(new TreeItem<>(itemName));
        treeItem.setValue(value);
    }
}
