package name.glonki.teamwork.data;

import com.google.gson.annotations.SerializedName;

import name.glonki.rx.ArrayContainer;

/**
 * Created by Glonki on 14.10.2017.
 */

public class Projects implements ArrayContainer<Project> {

    @SerializedName("STATUS")
    private final String status;
    private final Project[] projects;

    public Projects(String status, Project[] projects) {
        this.status = status;
        this.projects = projects;
    }

    public String getStatus() {
        return status;
    }

    public Project[] getProjects() {
        return projects;
    }

    @Override
    public Project[] getArray() {
        return getProjects();
    }

}
