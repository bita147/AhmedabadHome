package senta.nilesh.autocalc.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import senta.nilesh.autocalc.R;
import senta.nilesh.autocalc.adapters.BottleAdapter;
import senta.nilesh.autocalc.dto.WaterBottleDTO;
import senta.nilesh.autocalc.listeners.BottleListOperationListener;
import senta.nilesh.autocalc.listeners.FirebaseWaterBottleItemListChangeListener;
import senta.nilesh.autocalc.transporter.ServicesAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaterBottleFragment extends Fragment implements FirebaseWaterBottleItemListChangeListener, BottleListOperationListener {


    private ObservableArrayList<WaterBottleDTO> rcvList = new ObservableArrayList<>();
    private BottleAdapter adp;
    private View empty;
    private ImageView ivEmpty;
    private TextView tvEmpty;
    private Context context;

    public WaterBottleFragment() {
        // Required empty public constructor
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
        ((AppCompatActivity) context).setTitle("Water Bottles");
        View root = inflater.inflate(R.layout.fragment_water_view, container, false);
        RecyclerView rcvDailyList = (RecyclerView) root.findViewById(R.id.rcv_bottle_list);

        adp = new BottleAdapter(getActivity(), rcvList, WaterBottleFragment.this);
        empty = root.findViewById(R.id.ll_empty_view);
        ivEmpty = (ImageView) root.findViewById(R.id.iv_empty);
        tvEmpty = (TextView) root.findViewById(R.id.tv_empty);

        rcvDailyList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvDailyList.setAdapter(adp);

        loadDetails();

        return root;

    }

    private void loadDetails() {
        ServicesAPI.getAllWaterBottlesRecords(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bottle, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_total_bottle) {
            int totalBottle = 0;
            for (WaterBottleDTO dto : rcvList) {
                totalBottle += dto.getBottleCount();
            }
            showDialog(totalBottle);
        }
        return super.onOptionsItemSelected(item);
    }

    Dialog dialog;

    private void showDialog(final int totalBottle) {
        dialog = new Dialog(context, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_water_bottle_count);

        final EditText etRate = (EditText) dialog.findViewById(R.id.et_rate);
        final TextView tvTotalPay = (TextView) dialog.findViewById(R.id.tv_total_amt);
        final TextView tvTotalBottle = (TextView) dialog.findViewById(R.id.tv_water_bottle_count);
        tvTotalBottle.setText(String.valueOf(totalBottle));
        final Button btnOk = (Button) dialog.findViewById(R.id.btn_dialog_ok);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
        etRate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnOk.performClick();
                    return true;
                }
                return false;
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amt = etRate.getText().toString().trim();
                if (!TextUtils.isEmpty(amt)) {
                    int totalPay = totalBottle * Integer.parseInt(amt);
                    tvTotalPay.setText(String.valueOf(totalPay));
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


    @Override
    public void onItemChange(ObservableArrayList<WaterBottleDTO> itemList) {
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
    public void onDeleteItemClick(final WaterBottleDTO item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Dialog);
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete this item?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ServicesAPI.removeWaterNodebyKey(item.getKey());
            }
        });
        builder.setNegativeButton("CANCEL", null);
        builder.create().show();
    }
}
