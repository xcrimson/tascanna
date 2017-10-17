package name.glonki.tascanna.viewsetters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import name.glonki.list.ViewSetter;
import name.glonki.tascanna.R;
import name.glonki.teamwork.data.Project;

/**
 * Created by Glonki on 14.10.2017.
 */

public class ProjectViewSetter implements ViewSetter<Project> {

    private final RequestManager picRequestManager;

    public ProjectViewSetter(RequestManager picRequestManager) {
        this.picRequestManager = picRequestManager;
    }

    @Override
    public void setView(View view, Project current, Object previous, Object next) {
        TextView projectName = view.findViewById(R.id.project_name);
        TextView projectDescription = view.findViewById(R.id.project_description);
        TextView letter = view.findViewById(R.id.letter);
        ImageView pic = view.findViewById(R.id.userpic);
        View divider = view.findViewById(R.id.divider);

        projectName.setText(current.getName());
        projectDescription.setText(current.getDescription());
        if(current.hasLogoUri()) {
            picRequestManager.load(current.getLogoUrl())
                    .apply(RequestOptions.circleCropTransform()).into(pic);
            letter.setVisibility(View.GONE);
        } else {
            pic.setImageBitmap(null);
            String name = current.getName();
            letter.setText(notEmpty(name)? name.substring(0,1) : "?");

        }
        divider.setVisibility(next==null? View.GONE : View.VISIBLE);
    }

    private boolean notEmpty(String string) {
        return string != null && string.length() != 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_project;
    }

}
