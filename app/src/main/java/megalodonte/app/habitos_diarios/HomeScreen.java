package megalodonte.app.habitos_diarios;

import megalodonte.application.AndroidRouter;
import megalodonte.base.components.Component;
import megalodonte.base.components.ScreenComponent;
import megalodonte.base.state.State;
import megalodonte.components.Button;
import megalodonte.components.Text;
import megalodonte.components.Scroll;
import megalodonte.components.inputs.Input;
import megalodonte.components.layout_components.Column;
import megalodonte.props.ColumnProps;
import megalodonte.props.InputProps;
import megalodonte.props.TextProps;

public final class HomeScreen implements ScreenComponent {
    private final HabitStore store;
    private final AndroidRouter router;
    private final State<String> number = State.of("");
    private final State<String> listing;
    private final State<String> message = State.of("");

    public HomeScreen(HabitStore store, AndroidRouter router) {
        this.store = store;
        this.router = router;
        listing = State.of(store.summary());
    }

    @Override public Component render() {
        return new Scroll(new Column(new ColumnProps().paddingAll(24).spacingOf(14).fillWidth()).children(
                new Text("Hábitos Diários", new TextProps().fontSize(28)),
                new Text("Marque o que concluiu hoje. Amanhã os hábitos recomeçam."),
                new Button("Novo hábito").onClick(() -> router.navigate("/new")),
                new Text(listing),
                new Input(number, new InputProps().placeHolder("Número do hábito")),
                new Button("Marcar / desmarcar").onClick(() -> change(false)),
                new Button("Remover hábito").onClick(() -> change(true)),
                new Text(message)));
    }

    private void change(boolean remove) {
        int index;
        try { index = Integer.parseInt(number.get().trim()) - 1; }
        catch (NumberFormatException error) { message.set("Informe o número do hábito."); return; }
        if (index < 0 || index >= store.count()) { message.set("Hábito não encontrado."); return; }
        if (remove) store.remove(index); else store.toggle(index);
        listing.set(store.summary());
        message.set(remove ? "Hábito removido." : "Hábito atualizado.");
    }
}
