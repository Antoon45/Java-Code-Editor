package ax.ha.it.starter.utilities;

import java.io.File;

public class SourceUtility {

    private File sourceFile;

    public SourceUtility(File source) {
        this.sourceFile = source;
    }

    public String getName() {
        return sourceFile.getName();
    }

    public String getPath() {
        return sourceFile.getPath();
    }

    public File getFile() {
        return sourceFile;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SourceUtility){
            SourceUtility source = (SourceUtility)obj;
            return source.getPath().equals(getPath());
        }
        return false;
    }
}
