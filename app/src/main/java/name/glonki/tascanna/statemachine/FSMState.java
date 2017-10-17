package name.glonki.tascanna.statemachine;

/**
 * Created by Glonki on 15.10.2017.
 */

public class FSMState {

    private final String name;

    public FSMState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "State: ".concat(name);
    }

}
