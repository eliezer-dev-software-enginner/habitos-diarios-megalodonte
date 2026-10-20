package megalodonte.app.habitos_diarios;

import android.content.SharedPreferences;
import java.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
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
    private final SharedPreferences storage;
    private final State<String> name = State.of("");
    private final State<String> number = State.of("");
    private final State<String> listing = State.of("");
    private final State<String> message = State.of("");
    private JSONArray habits;

    public HomeScreen(SharedPreferences storage) {
        this.storage = storage;
        try { habits = new JSONArray(storage.getString("habits", "[]")); }
        catch (JSONException error) { throw new IllegalStateException(error); }
        String today = LocalDate.now().toString();
        if (!today.equals(storage.getString("day", ""))) {
            try { for (int i = 0; i < habits.length(); i++) habits.optJSONObject(i).put("done", false); }
            catch (JSONException error) { throw new IllegalStateException(error); }
            storage.edit().putString("day", today).putString("habits", habits.toString()).apply();
        }
        refresh("");
    }

    @Override public Component render() {
        return new Scroll(new Column(new ColumnProps().paddingAll(24).spacingOf(14).fillWidth()).children(
                new Text("Hábitos Diários", new TextProps().fontSize(28)),
                new Text("Marque o que concluiu hoje. Amanhã os hábitos recomeçam."),
                new Input(name, new InputProps().placeHolder("Novo hábito")),
                new Button("Adicionar hábito").onClick(this::add),
                new Input(number, new InputProps().placeHolder("Número do hábito")),
                new Button("Marcar / desmarcar").onClick(() -> change(false)),
                new Button("Remover hábito").onClick(() -> change(true)),
                new Text(message), new Text(listing)));
    }

    private void add() {
        String value = name.get().trim();
        if (value.isEmpty()) { refresh("Digite um hábito."); return; }
        try { habits.put(new JSONObject().put("name", value).put("done", false)); }
        catch (JSONException error) { throw new IllegalStateException(error); }
        name.set(""); save("Hábito adicionado.");
    }

    private void change(boolean remove) {
        int index;
        try { index = Integer.parseInt(number.get().trim()) - 1; }
        catch (NumberFormatException error) { refresh("Informe o número do hábito."); return; }
        if (index < 0 || index >= habits.length()) { refresh("Hábito não encontrado."); return; }
        if (remove) habits.remove(index);
        else {
            try {
                JSONObject item = habits.getJSONObject(index);
                item.put("done", !item.optBoolean("done"));
            } catch (JSONException error) { throw new IllegalStateException(error); }
        }
        save(remove ? "Hábito removido." : "Hábito atualizado.");
    }

    private void save(String status) {
        storage.edit().putString("habits", habits.toString()).apply();
        refresh(status);
    }

    private void refresh(String status) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < habits.length(); i++) {
            JSONObject item = habits.optJSONObject(i);
            text.append(i + 1).append(". ").append(item.optBoolean("done") ? "☑ " : "☐ ")
                    .append(item.optString("name")).append('\n');
        }
        listing.set(text.isEmpty() ? "Nenhum hábito ainda." : text.toString());
        message.set(status);
    }
}
