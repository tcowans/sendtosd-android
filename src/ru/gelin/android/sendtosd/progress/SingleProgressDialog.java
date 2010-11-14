package ru.gelin.android.sendtosd.progress;

import ru.gelin.android.sendtosd.R;
import android.app.Activity;
import android.view.ViewGroup;

public class SingleProgressDialog extends ProgressDialog {

    protected SingleProgressDialog(Activity activity) {
        super(activity);
        setView(getLayoutInflater().inflate(R.layout.progress_dialog, 
                (ViewGroup)findViewById(R.id.progress_dialog_root)));
    }

}
