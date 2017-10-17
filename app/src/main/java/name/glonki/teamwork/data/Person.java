package name.glonki.teamwork.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import name.glonki.teamwork.interfaces.Identifiable;
import name.glonki.teamwork.interfaces.Named;

/**
 * Created by Glonki on 13.10.2017.
 */

public class Person implements Named, Identifiable {

    public final static int EVERYONE = -1;

    private final int id;
    @SerializedName("first-name")
    private final String firstName;
    @SerializedName("last-name")
    private final String lastName;
    @SerializedName("user-name")
    private final String userName;

    public Person(int id, String firstName, String lastName, String userName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
    }

    public static Person getEveryone(String everyone) {
        return new Person(EVERYONE, everyone, "", "");
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        StringBuilder builder = new StringBuilder();
        if(firstName != null && firstName.length() > 0) {
            builder.append(firstName);
        }
        if(builder.length() == 0 && userName != null && userName.length() > 0) {
            builder.append(userName);
        }
        if(lastName != null && lastName.length() > 0) {
            boolean empty = builder.length() == 0;
            if(!empty) {
                builder.append(" ");
            }
            builder.append(lastName);
        }
        return builder.toString();
    }

    public static String getPartiesIds(List<Person> persons, List<Integer> second) {
        StringBuilder builder = new StringBuilder();
        int l = second.size();
        for(int i=0; i<l; i++) {
            Person current = persons.get(second.get(i));
            builder.append(current.getId());
            if(i < l-1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }
}
