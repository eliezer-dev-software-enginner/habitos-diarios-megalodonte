package megalodonte.app.habitos_diarios;

import android.content.SharedPreferences;
import java.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

/** Local persistence and daily completion reset shared by both routes. */
public final class HabitStore {
    private final SharedPreferences storage;
    private final JSONArray habits;

    public HabitStore(SharedPreferences storage) {
        this.storage = storage;
        try { habits = new JSONArray(storage.getString("habits", "[]")); }
        catch (JSONException error) { throw new IllegalStateException(error); }
        String today = LocalDate.now().toString();
        if (!today.equals(storage.getString("day", ""))) {
            try { for (int i = 0; i < habits.length(); i++) habits.getJSONObject(i).put("done", false); }
            catch (JSONException error) { throw new IllegalStateException(error); }
            storage.edit().putString("day", today).putString("habits", habits.toString()).apply();
        }
    }

    public int count() { return habits.length(); }
    public void add(String name) {
        try { habits.put(new JSONObject().put("name", name).put("done", false)); }
        catch (JSONException error) { throw new IllegalStateException(error); }
        save();
    }
    public void toggle(int index) {
        try {
            JSONObject habit = habits.getJSONObject(index);
            habit.put("done", !habit.optBoolean("done"));
        } catch (JSONException error) { throw new IllegalStateException(error); }
        save();
    }
    public void remove(int index) { habits.remove(index); save(); }
    public String summary() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < habits.length(); i++) {
            JSONObject habit = habits.optJSONObject(i);
            text.append(i + 1).append(". ").append(habit.optBoolean("done") ? "☑ " : "☐ ")
                    .append(habit.optString("name")).append('\n');
        }
        return text.isEmpty() ? "Nenhum hábito ainda." : text.toString();
    }
    private void save() { storage.edit().putString("habits", habits.toString()).apply(); }
}
