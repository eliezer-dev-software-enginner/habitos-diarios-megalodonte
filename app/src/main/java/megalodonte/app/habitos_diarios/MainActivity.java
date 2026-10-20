package megalodonte.app.habitos_diarios;

import megalodonte.android.bridge.MegalodonteActivity;
import megalodonte.application.MegalodonteApp;
import megalodonte.base.theme.ThemeManager;
import megalodonte.theme.DefaultTheme;

public final class MainActivity extends MegalodonteActivity {
    @Override protected void main() {
        ThemeManager.setTheme(new DefaultTheme());
        MegalodonteApp.run(context -> context.useView(new HomeScreen(getPreferences(0))));
    }
}
