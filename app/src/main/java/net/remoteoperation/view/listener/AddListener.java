package net.remoteoperation.view.listener;

import android.view.View;

import net.remoteoperation.view.MainView;

/**
 * Created by nathav63 on 7/3/15.
 */
public class AddListener implements View.OnClickListener {

    MainView mainView;

    public AddListener(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onClick(View v) {
        mainView.add();
    }
}
