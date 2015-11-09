package com.methodsignature.todolist.ui.itementry;

import android.support.v4.app.FragmentManager;

/**
 * Created by randallmitchell on 11/4/15.
 */
public class DefaultNewItemDialogManager implements NewItemDialogManager {

    private static final String FRAG_TAG = DefaultNewItemDialogManager.class.getName() + "_FragTag";

    private Listener listener;
    private NewItemDialogFragment dialogFragment;

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void showDialog(FragmentManager fragmentManager) {
        if (dialogFragment == null) {
            dialogFragment = new NewItemDialogFragment();
            dialogFragment.setListener(new NewItemDialogFragment.Listener() {
                @Override
                public void onSubmit(String userInput) {
                    handleDialogInput(userInput);
                }

                @Override
                public void onCancelOrDismiss() {
                    handleDialogCancel();
                }
            });
        }

        if (!dialogFragment.isAdded()) {
            dialogFragment.show(fragmentManager, FRAG_TAG);
        }
    }

    @Override
    public void cancelDialog() {
        if (dialogFragment != null && dialogFragment.isAdded()) {
            dialogFragment.dismiss();
            dialogFragment.setListener(null);
        }
        dialogFragment = null;
    }

    private void handleDialogInput(String userInput) {
        cancelDialog();
        if (listener != null) {
            listener.onDialogResult(userInput);
        }
    }

    private void handleDialogCancel() {
        cancelDialog();
        if (listener != null) {
            listener.onDialogCancelled();
        }
    }
}
