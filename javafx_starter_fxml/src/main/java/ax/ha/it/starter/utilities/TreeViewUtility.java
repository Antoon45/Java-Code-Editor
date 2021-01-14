package ax.ha.it.starter.utilities;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class TreeViewUtility {
    public TreeView<String> fileTreeView;
    public TreeItem<String> treeItem;

    public TreeViewUtility(TreeView<String> fileTreeView, TreeItem<String> treeItem) {
        this.fileTreeView = fileTreeView;
        this.treeItem = treeItem;
        setRootItem();
    }

    public void setRootItem() {
        fileTreeView.setRoot(treeItem);
        fileTreeView.setShowRoot(false);
    }

    public void addTreeItem(String itemName) {
        treeItem.getChildren().add(new TreeItem<>(itemName));
    }
}
