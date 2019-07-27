package com.sebastiaan.xenopelthis.ui.templates.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sebastiaan.xenopelthis.R;

public abstract class OverrideDialog<T, U> {
    private TextView textExists, textRelations;
    private ViewStub conflictItem;
    private Button cancelButton, overrideButton;

    private Dialog dialog;

    private Activity parent;

    public OverrideDialog(Activity activity) { parent = activity; }

    public void showDialog(U conflicting, long conflictID, OverrideListener onOverrideListener) {
        dialog = new Dialog(parent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_override);

        findGlobalViews(dialog);
        prepareList(conflictID);

        setupButtons(dialog, onOverrideListener);

        inflateViews(conflicting, conflictID);
        setTextViews(conflicting);
    }

    abstract void prepareList(long conflictID);
    abstract void inflateViews(U conflict, long conflictID);
    abstract void setTextViews(U conflict);

    private void findGlobalViews(Dialog dialog) {
        textExists = dialog.findViewById(R.id.dialog_conflict_text);
        textRelations = dialog.findViewById(R.id.dialog_conflict_text_relations);
        conflictItem = dialog.findViewById(R.id.dialog_conflict_item);
        cancelButton = dialog.findViewById(R.id.dialog_conflict_cancel);
        overrideButton = dialog.findViewById(R.id.dialog_conflict_override);
    }

    private void setupButtons(Dialog dialog, OverrideListener onOverrideListener) {
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        overrideButton.setOnClickListener(v -> {
            dialog.dismiss();
            onOverrideListener.onOverride();
        });
    }

}
