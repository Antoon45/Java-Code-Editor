package ax.ha.it.starter.utilities;

import java.io.File;
import java.io.IOException;

public class TerminalUtility {

    final static String errorMessage = "Can't launch terminal";
    private static final String OS = System.getProperty("os.name").toLowerCase();

    /**
     * @return : true if current OS is Windows
     */
    public static boolean isWindows() {
        return OS.contains("win");
    }

    private static void openWindowsTerminal() {
        if (isWindows()) {
            try {
                Runtime.getRuntime().exec("cmd.exe /c start", null);
            } catch (IOException e) {
                System.out.println(errorMessage);
            }
        } else {
            System.out.println(errorMessage);
        }
    }
}
