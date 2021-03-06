package senta.nilesh.autocalc.controls;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.databinding.ObservableArrayList;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import senta.nilesh.autocalc.R;
import senta.nilesh.autocalc.dto.UserProfileDTO;

public class MultiSpinner extends AppCompatSpinner {

    private String[] entries;
    private boolean[] selected;
    private MultiSpinnerListener listener;
    private Context context;

    public void setMultiSpinnerEntries(ObservableArrayList<String> list, ObservableArrayList<UserProfileDTO> users) {
        entries = list.toArray(new String[list.size()]);
        selected = new boolean[entries.length]; // false-filled by default

        for (int i=0; i<entries.length; i++) {
            for (UserProfileDTO user : users){
                if (user.getUserName().equals(entries[i]) && !user.isGuest()  ){
                    selected[i] = true;
                }
            }
        }
//
//        for (int i=0; i<entries.length; i++) {
//            selected[i] = true;
//        }
        StringBuilder spinnerBuffer = new StringBuilder();
        for (int j = 0; j < entries.length; j++) {
            if (selected[j]) {
                spinnerBuffer.append(entries[j]);
                spinnerBuffer.append(", ");
            }
        }
        // Remove trailing comma
        if (spinnerBuffer.length() > 2) {
            spinnerBuffer.setLength(spinnerBuffer.length() - 2);
        }

        // display new text
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.item_spinner,
                new String[] { (!TextUtils.isEmpty(spinnerBuffer.toString())) ?spinnerBuffer.toString() : context.getString(R.string.no_selection) });
        setAdapter(adapter);

        if (listener != null) {
            listener.onItemsSelected(selected);
        }
    }

    public MultiSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context  =context;
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiSpinner);
//        entries = a.getTextArray(R.styleable.MultiSpinner_android_entries);
//        if (entries != null) {
//            selected = new boolean[entries.length]; // false-filled by default
//            for (int i=0; i<entries.length; i++) {
//
//                selected[i] = true;
//            }
//            StringBuilder spinnerBuffer = new StringBuilder();
//            for (int i = 0; i < entries.length; i++) {
//                if (selected[i]) {
//                    spinnerBuffer.append(entries[i]);
//                    spinnerBuffer.append(", ");
//                }
//            }
//            // Remove trailing comma
//            if (spinnerBuffer.length() > 2) {
//                spinnerBuffer.setLength(spinnerBuffer.length() - 2);
//            }

            // display new text
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
//                    R.layout.item_spinner,
//                    new String[] { (!TextUtils.isEmpty(spinnerBuffer.toString())) ?spinnerBuffer.toString() : context.getString(R.string.no_selection) });
//            setAdapter(adapter);
//
//            if (listener != null) {
//                listener.onItemsSelected(selected);
//            }
//        }
//        a.recycle();
    }

    private OnMultiChoiceClickListener mOnMultiChoiceClickListener = new OnMultiChoiceClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            selected[which] = isChecked;
        }
    };

    private DialogInterface.OnClickListener mOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // build new spinner text & delimiter management
            StringBuilder spinnerBuffer = new StringBuilder();
            for (int i = 0; i < entries.length; i++) {
                if (selected[i]) {
                    spinnerBuffer.append(entries[i]);
                    spinnerBuffer.append(", ");
                }
            }
            // Remove trailing comma
            if (spinnerBuffer.length() > 2) {
                spinnerBuffer.setLength(spinnerBuffer.length() - 2);
            }

            // display new text
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    R.layout.item_spinner,
                    new String[] { (!TextUtils.isEmpty(spinnerBuffer.toString())) ?spinnerBuffer.toString() : context.getString(R.string.no_selection) });
            setAdapter(adapter);

            if (listener != null) {
                listener.onItemsSelected(selected);
            }

            // hide dialog
            dialog.dismiss();
        }
    };

    @Override
    public boolean performClick() {
        new  AlertDialog.Builder(getContext(), R.style.Dialog)
                .setMultiChoiceItems(entries, selected, mOnMultiChoiceClickListener)
                .setPositiveButton(android.R.string.ok, mOnClickListener)
                .setCancelable(true)
                .show();
        return true;
    }

    public void setMultiSpinnerListener(MultiSpinnerListener listener) {
        this.listener = listener;
    }

    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }
}