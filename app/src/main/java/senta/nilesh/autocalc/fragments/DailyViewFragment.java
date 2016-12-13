package senta.nilesh.autocalc.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.SimpleDateFormat;
import java.util.Date;

import senta.nilesh.autocalc.R;
import senta.nilesh.autocalc.activity.ReportActivity;
import senta.nilesh.autocalc.adapters.DailyAdapter;
import senta.nilesh.autocalc.controls.MultiSpinner;
import senta.nilesh.autocalc.dto.ItemDTO;
import senta.nilesh.autocalc.dto.NotificationDTO;
import senta.nilesh.autocalc.dto.UserProfileDTO;
import senta.nilesh.autocalc.listeners.FirebaseItemListChangeListener;
import senta.nilesh.autocalc.listeners.ListOperationListener;
import senta.nilesh.autocalc.listeners.TransactionInsertListener;
import senta.nilesh.autocalc.transporter.ServicesAPI;
import senta.nilesh.autocalc.utils.AppPref;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyViewFragment extends Fragment implements FirebaseItemListChangeListener, ListOperationListener {

    private ObservableArrayList<ItemDTO> rcvList = new ObservableArrayList<>();
    private DailyAdapter adp;
    private View empty;
    private ImageView ivEmpty;
    private TextView tvEmpty;
    private Context context;

    public DailyViewFragment() {

    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) context).setTitle(setupHeader());
        View root = inflater.inflate(R.layout.fragment_daily_view, container, false);
        RecyclerView rcvDailyList = (RecyclerView) root.findViewById(R.id.rcv_daily_list);

        adp = new DailyAdapter(getActivity(), rcvList, DailyViewFragment.this);
        empty = root.findViewById(R.id.ll_empty_view);
        ivEmpty = (ImageView) root.findViewById(R.id.iv_empty);
        tvEmpty = (TextView) root.findViewById(R.id.tv_empty);

        rcvDailyList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvDailyList.setAdapter(adp);

        getRecords();

        updateRegistrationToken();
        return root;
    }

    private String setupHeader() {
        UserProfileDTO dto = AppPref.get(context).getUserProfileDTO();
        if (dto != null) {
            if (!TextUtils.isEmpty(dto.getFirstName()) && !TextUtils.isEmpty(dto.getLastName())) {
                return dto.getFirstName() + " " + dto.getLastName();

            } else {
                return dto.getUserName();
            }
        }
        return "";
    }

    private void updateRegistrationToken() {
        AppPref prefs = AppPref.get(getActivity());
        UserProfileDTO profile = prefs.getUserProfileDTO();
        profile.setRegisterToken(prefs.getRegistrationToken());
        prefs.saveLoginDTO(profile);
        ServicesAPI.updateRegistrationToken(profile, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
            }
        });
    }

    private void getRecords() {
        ServicesAPI.getAllRecords(context, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_report) {
            Intent intent = new Intent(getActivity(), ReportActivity.class);
            intent.putExtra("DATA", rcvList);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemChange(ObservableArrayList<ItemDTO> itemList) {
        if (!isAdded())
            return;
        rcvList.clear();
        rcvList.addAll(itemList);
        adp.notifyDataSetChanged();
        if (rcvList.size() == 0) {
            empty.setVisibility(View.VISIBLE);
            tvEmpty.setText(getString(R.string.no_data_found));
            ivEmpty.setBackgroundResource(R.drawable.sad_smily);
        } else
            empty.setVisibility(View.GONE);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDeleteItemClick(final ItemDTO item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Dialog);
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete this item?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ServicesAPI.removeNodebyKey(item.getKey());
            }
        });
        builder.setNegativeButton("CANCEL", null);
        builder.create().show();
    }


    @Override
    public void onItemClick(View view, final ItemDTO item) {
        final Dialog dialog = new Dialog(getActivity(), R.style.Dialog);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_thing_detail);

        TextView tvInserted = (TextView) dialog.findViewById(R.id.tv_inserted);
        TextView tvUsersIncluded = (TextView) dialog.findViewById(R.id.tv_users_included);
        TextView tvAmount = (TextView) dialog.findViewById(R.id.tv_amount);
        TextView tvPaidBy = (TextView) dialog.findViewById(R.id.tv_paidby);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_dialog_ok);
        Button btnInsertAgain = (Button) dialog.findViewById(R.id.btn_dialog_insert_again);

        tvInserted.setText(item.getUserName());
        tvUsersIncluded.setText(item.getUsersIncluded());
        tvPaidBy.setText(item.getPayBy());
        tvAmount.setText(String.valueOf(item.getAmt()));

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnInsertAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showInputDialog(item);
            }
        });
        dialog.show();

        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });
    }

    Dialog dialog;

    private void showInputDialog(ItemDTO itemDTO) {
        dialog = new Dialog(context, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_buy_thing);

        final TextView tvDate = (TextView) dialog.findViewById(R.id.tv_date);
        final EditText etDescription = (EditText) dialog.findViewById(R.id.et_item_description);
        final EditText etAmt = (EditText) dialog.findViewById(R.id.et_item_amount);
        final MultiSpinner spUsers = (MultiSpinner) dialog.findViewById(R.id.sp_users);
        final Spinner spPayby = (Spinner) dialog.findViewById(R.id.sp_payby);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_dialog_ok);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_dialog_cancel);

        spPayby.setAdapter(new ArrayAdapter<String>(context,R.layout.item_spinner,AppPref.get(context).getUserList()));
        spUsers.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,AppPref.get(context).getUserList()));
        spUsers.setMultiSpinnerEntries(AppPref.get(context).getUserList(),AppPref.get(context).getUserProfileList());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy EEE hh:mm a");
        tvDate.setText(dateFormat.format(new Date(System.currentTimeMillis())));

        etDescription.setText("" + itemDTO.getItemDesc());
        etAmt.setText(""+(int) itemDTO.getAmt());

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = etDescription.getText().toString().trim();
                String amt = etAmt.getText().toString().trim();

                if (!TextUtils.isEmpty(desc) && !TextUtils.isEmpty(amt) && !spUsers.getSelectedItem().toString().equals(getString(R.string.no_selection))) {
                    final ItemDTO dto = new ItemDTO();
                    dto.setAmt(Float.parseFloat(amt));
                    dto.setItemDesc(desc);
                    dto.setBuyDate(tvDate.getText().toString());
                    dto.setPayBy(spPayby.getSelectedItem().toString());
                    dto.setUserName(AppPref.get(context).getUserProfileDTO().getUserName());
                    dto.setUsersIncluded(spUsers.getSelectedItem().toString());

                    final ProgressDialog pd = ProgressDialog.show(context, null, "Please wait...");
                    ServicesAPI.insertTransation(dto, new TransactionInsertListener() {
                        @Override
                        public void onTransactionCompleted() {
                            NotificationDTO n = new NotificationDTO();
                            n.setData(dto);
                            ServicesAPI.sendNotification(n);

                            if (pd != null && pd.isShowing())
                                pd.dismiss();
                            dialog.dismiss();
                        }
                    });
                } else {
                    Snackbar.make(dialog.findViewById(R.id.snackbar_position), "Fields can't empty", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });

    }
}
