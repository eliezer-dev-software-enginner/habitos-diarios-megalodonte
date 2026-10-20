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

public final class NewHabitScreen implements ScreenComponent {
    private final HabitStore store;
    private final AndroidRouter router;
    private final State<String> name = State.of("");
    private final State<String> message = State.of("");

    public NewHabitScreen(HabitStore store, AndroidRouter router) {
        this.store = store;
        this.router = router;
    }

    @Override public Component render() {
        return new Scroll(new Column(new ColumnProps().paddingAll(24).spacingOf(14).fillWidth()).children(
                new Text("Novo hábito", new TextProps().fontSize(28)),
                new Input(name, new InputProps().placeHolder("Nome do hábito")),
                new Button("Salvar hábito").onClick(this::save),
                new Button("Voltar").onClick(router::back),
                new Text(message)));
    }

    private void save() {
        String value = name.get().trim();
        if (value.isEmpty()) { message.set("Digite um hábito."); return; }
        store.add(value);
        router.back();
    }
}
