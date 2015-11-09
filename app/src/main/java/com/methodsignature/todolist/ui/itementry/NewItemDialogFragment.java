package com.methodsignature.todolist.ui.itementry;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.methodsignature.todolist.R;

/**
 * Created by randallmitchell on 11/4/15.
 */
public class NewItemDialogFragment extends DialogFragment {

    private EditText editText;
    private View positiveButton;
    private View negativeButton;

    private Listener listener;

    public interface Listener {
        void onSubmit(String userInput);
        void onCancelOrDismiss();
    }

    public NewItemDialogFragment() {

    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_item_dialog, container);

        editText = (EditText) view.findViewById(R.id.new_item_dialog_text_input);

        positiveButton = view.findViewById(R.id.new_item_dialog_positive_button);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmitClicked();
            }
        });

        negativeButton = view.findViewById(R.id.new_item_dialog_negative_button);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCancelOrDismiss();
            }
        });

        getDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                handleCancelOrDismiss();
            }
        });

        getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handleCancelOrDismiss();
            }
        });

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    private void handleSubmitClicked() {
        if (listener != null) {
            listener.onSubmit(editText.getText().toString());
        }
    }

    private void handleCancelOrDismiss() {
        if (listener != null) {
            listener.onCancelOrDismiss();
        }
    }
}
