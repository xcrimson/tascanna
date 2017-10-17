package name.glonki.teamwork;

import io.reactivex.Observable;
import name.glonki.teamwork.data.People;
import name.glonki.teamwork.data.Projects;
import name.glonki.teamwork.data.Task;
import name.glonki.teamwork.data.TaskLists;
import name.glonki.teamwork.data.TaskToAdd;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Glonki on 13.10.2017.
 */

public interface TeamworkAPI {

    @GET("/projects.json")
    Observable<Projects> getProjects();

    @GET("/people.json")
    Observable<People> getPeople();

    @GET("/projects/{project_id}/tasklists.json")
    Observable<TaskLists> getTaskLists(@Path("project_id") int projectId);

    @POST("/tasklists/{id}/tasks.json")
    Observable<Void> addTask(@Path("id") int taskListId, @Body Task task);

    @POST("/tasklists/{id}/tasks.json")
    Call<Void> addTaskSync(@Path("id") int taskListId, @Body TaskToAdd task);

}
