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

    EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
        System.out.println(treeItem.getValue());
    };

    /**
     * sets the Root for the file
     */
    public void setRootItem() {
        fileTreeView.setRoot(treeItem);
        fileTreeView.setShowRoot(false);
    }
    /**
     * adds a child to the tree
     * @param itemName
     */
    public void addTreeItem(SourceUtility itemName) {
        treeItem.getChildren().add(new TreeItem<SourceUtility>(itemName));
    }

    /**
     * Adds a a child with the text from the codeArea to the tree
     * @param itemName
     */
    public void addTreeItemWithValue(SourceUtility itemName) {
        treeItem.getChildren().add(new TreeItem<>(itemName));
    }
}
