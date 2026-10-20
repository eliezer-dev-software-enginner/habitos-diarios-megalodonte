package megalodonte.app.habitos_diarios;

import megalodonte.android.bridge.MegalodonteActivity;
import megalodonte.application.AndroidRouter;
import megalodonte.application.MegalodonteApp;
import megalodonte.base.theme.ThemeManager;
import megalodonte.theme.DefaultTheme;

public final class MainActivity extends MegalodonteActivity {
    private AndroidRouter router;
    @Override protected void main() {
        ThemeManager.setTheme(new DefaultTheme());
        HabitStore store = new HabitStore(getPreferences(0));
        MegalodonteApp.run(context -> {
            router = new AndroidRouter(context)
                    .route("/habits", () -> new HomeScreen(store, router))
                    .route("/new", () -> new NewHabitScreen(store, router));
            router.start("/habits");
        });
    }
    @Override public void onBackPressed() {
        if (router == null || !router.back()) super.onBackPressed();
    }
}
