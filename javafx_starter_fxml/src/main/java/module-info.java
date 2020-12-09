module ax.ha.it.startermodule {
    requires javafx.controls;
    requires javafx.fxml;

    // FXML uses reflection to access controller methods etc
    // => Must open up our package for this to work
    opens ax.ha.it.starter to javafx.fxml;
    exports ax.ha.it.starter;
}