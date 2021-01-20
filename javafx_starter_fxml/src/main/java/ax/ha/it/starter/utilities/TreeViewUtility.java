package ax.ha.it.starter.utilities;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
public class TreeViewUtility {
    private final TreeView<SourceUtility> fileTreeView;
    private TreeItem<SourceUtility> treeItem;

    public TreeViewUtility(TreeView<SourceUtility> fileTreeView, TreeItem<SourceUtility> treeItem) {

    public TreeViewUtility(TreeView<String> fileTreeView, TreeItem<String> treeItem) {
        this.fileTreeView = fileTreeView;
        this.treeItem = treeItem;
        fileTreeView.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEventHandle);
        setRootItem();
        fileTreeView.setEditable(false);
    }

    EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> { // TODO: Implement code for mouseEventHandler
        System.out.println(treeItem.getValue());
    };

    /**
     * sets the Root for the file
     */
    public void setRootItem() {
        fileTreeView.setRoot(treeItem);
        fileTreeView.setShowRoot(false);
    }

    public void addTreeItem(SourceUtility item) {
        treeItem.getChildren().add(new TreeItem<>(item));
    /**
     * adds a child to the tree
     * @param itemName
     */
    public void addTreeItem(String itemName) {
        treeItem.getChildren().add(new TreeItem<>(itemName));
    }

    public void addTreeItemWithValue(SourceUtility item) {
        treeItem.getChildren().add(new TreeItem<>(item));
    /**
     * Adds a a child with the text from the codeArea to the tree
     * @param itemName
     * @param value
     */
    public void addTreeItemWithValue(String itemName, String value) {
        treeItem.getChildren().add(new TreeItem<>(itemName));
        treeItem.setValue(value);
    }
}
