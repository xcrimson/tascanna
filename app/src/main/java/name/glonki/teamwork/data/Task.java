package name.glonki.teamwork.data;

import com.google.gson.annotations.SerializedName;

import name.glonki.teamwork.util.DateUtil;

/**
 * Created by Glonki on 13.10.2017.
 */

public class Task {

    private String content;
    private String description;
    @SerializedName("start-date")
    private String startDate;
    @SerializedName("due-date")
    private String dueDate;
    @SerializedName("responsible-party-id")
    private String responsibleParty;
    private String priority;

    public Task(String content, String description, String startDate, String dueDate,
                String responsibleParty, String priority) {
        this.content = content;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.responsibleParty = responsibleParty;
        this.priority = priority;
    }

    public static Task newTaskWeekFromToday() {
        return new Task("", "", DateUtil.today(), DateUtil.oneWeekLater(),
                Integer.toString(Person.EVERYONE), "medium");
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setResponsibleParty(String responsibleParty) {
        this.responsibleParty = responsibleParty;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getResponsibleParty() {
        return responsibleParty;
    }

    public String getPriority() {
        return priority;
    }

}
