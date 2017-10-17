package name.glonki.teamwork.data;

import name.glonki.teamwork.interfaces.Identifiable;
import name.glonki.teamwork.interfaces.Named;

/**
 * Created by Glonki on 14.10.2017.
 */

public class TaskList implements Named, Identifiable {

    private final String name;
    private final int id;

    public TaskList(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

}
