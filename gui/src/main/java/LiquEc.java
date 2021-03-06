/*
 * Open Source Software published under the GNU Licence, Version 2.0.
 */

import io.github.liquec.gui.main.CoreGuiModule;
import io.github.liquec.gui.main.LiquEcGuiExecutable;
import io.github.liquec.gui.main.LiveGuiModule;
import io.github.liquec.gui.main.StandardEventSourceModule;

/**
 * This class is used instead of LiquEcGuiExecutable to avoid showing the package name in the JavaFX menu.
 * This problem will be solve in a better way in a future version.
 */
public class LiquEc extends LiquEcGuiExecutable {
    public static void main(final String... args) {
        runApp(
            args,
            a -> launch(a), // This should remain as a lambda, to keep the short name for the quit menu item
            new CoreGuiModule(), new LiveGuiModule(), new StandardEventSourceModule(args));
    }
}
