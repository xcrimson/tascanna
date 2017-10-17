package name.glonki.tascanna.statemachine;

/**
 * Created by Glonki on 15.10.2017.
 */

public class FSMTrigger {

    private final String name;

    public FSMTrigger(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Trigger: ".concat(name);
    }

}
