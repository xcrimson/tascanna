package name.glonki.teamwork.data;

import name.glonki.rx.ArrayContainer;

/**
 * Created by Glonki on 14.10.2017.
 */

public class People implements ArrayContainer<Person> {

    private final Person[] people;

    public People(Person[] people) {
        this.people = people;
    }

    public Person[] getPeople() {
        return people;
    }

    @Override
    public Person[] getArray() {
        return getPeople();
    }

}
