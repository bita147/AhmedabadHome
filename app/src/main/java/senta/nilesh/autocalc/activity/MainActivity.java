package senta.nilesh.autocalc.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import senta.nilesh.autocalc.R;
import senta.nilesh.autocalc.controls.MultiSpinner;
import senta.nilesh.autocalc.dto.ItemDTO;
import senta.nilesh.autocalc.dto.NotificationDTO;
import senta.nilesh.autocalc.fragments.DailyViewFragment;
import senta.nilesh.autocalc.listeners.TransactionInsertListener;
import senta.nilesh.autocalc.transporter.ServicesAPI;
import senta.nilesh.autocalc.utils.AppPref;

public class MainActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {
    //    private NavigationView navigationView;
    private FragmentManager fm;
    private FloatingActionButton fabAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fm = getSupportFragmentManager();

        fabAddItem = (FloatingActionButton) findViewById(R.id.fab);
        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        setTitle("" + AppPref.get(this).getUserProfileDTO().getUserName());

        DailyViewFragment fragment = new DailyViewFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.ll_container, fragment).commit();

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                setupHeader();
//            }
//        };
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        // Select First Item and load Fragment
//        navigationView.getMenu().getItem(0).setChecked(true);
//        onNavigationItemSelected(navigationView.getMenu().getItem(0));

    }

//      private void setupHeader() {
//        UserProfileDTO dto = AppPref.get(this).getUserProfileDTO();
//        if (dto != null) {
//            TextView tvUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_username);
//            TextView tvShortnameRound = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_short_name);
//            TextView tvEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_email);
//
//            if (!TextUtils.isEmpty(dto.getFirstName()) && !TextUtils.isEmpty(dto.getLastName())) {
//                tvUserName.setText(dto.getFirstName() + " " + dto.getLastName());
//                tvShortnameRound.setText(String.valueOf(dto.getFirstName().charAt(0) + dto.getLastName().charAt(0)).toUpperCase());
//            } else {
//                tvUserName.setText(dto.getUserName());
//                tvShortnameRound.setText(String.valueOf(dto.getUserName().charAt(0)).toUpperCase());
//            }
//            if (!TextUtils.isEmpty(dto.getEmail())) {
//                tvEmail.setText(dto.getEmail());
//            }
//        }
//    }
    Dialog dialog;

    private void showInputDialog() {
        dialog =  new Dialog(this, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_buy_thing);

        final TextView tvDate = (TextView) dialog.findViewById(R.id.tv_date);
        final EditText etDescription = (EditText) dialog.findViewById(R.id.et_item_description);
        final EditText etAmt = (EditText) dialog.findViewById(R.id.et_item_amount);
        final MultiSpinner spUsers = (MultiSpinner) dialog.findViewById(R.id.sp_users);
        final Spinner spPayby = (Spinner) dialog.findViewById(R.id.sp_payby);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_dialog_ok);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_dialog_cancel);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy EEE hh:mm a");
        tvDate.setText(dateFormat.format(new Date(System.currentTimeMillis())));

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = etDescription.getText().toString().trim();
                String amt = etAmt.getText().toString().trim();

                if (!TextUtils.isEmpty(desc) && !TextUtils.isEmpty(amt) && !spUsers.getSelectedItem().toString().equals(getString(R.string.no_selection))) {
                    final ItemDTO dto = new ItemDTO();
                    dto.setAmt(Integer.parseInt(amt));
                    dto.setItemDesc(desc);
                    dto.setBuyDate(tvDate.getText().toString());
                    dto.setPayBy(spPayby.getSelectedItem().toString());
                    dto.setUserName(AppPref.get(MainActivity.this).getUserProfileDTO().getUserName());
                    dto.setUsersIncluded(spUsers.getSelectedItem().toString());

                    final ProgressDialog pd = ProgressDialog.show(MainActivity.this, null, "Please wait...");
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

    //    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            if (!navigationView.getMenu().getItem(0).isChecked()) {
//                navigationView.getMenu().getItem(0).setChecked(true);
//                onNavigationItemSelected(navigationView.getMenu().getItem(0));
//            } else {
//                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                super.onBackPressed();
//            }
//        }
//    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//        FragmentTransaction frgTransaction = getSupportFragmentManager().beginTransaction();
//        frgTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//
//        // Remove all Fragment
//        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//
//        if (id == R.id.nav_daily_view) {
//            DailyViewFragment fragment = new DailyViewFragment();
//            frgTransaction.add(R.id.ll_container, fragment).addToBackStack(null).commit();
//        } else if (id == R.id.nav_month_view) {
//            MonthViewFragment fragment = new MonthViewFragment();
//            frgTransaction.add(R.id.ll_container, fragment).addToBackStack(null).commit();
//        }
//        else if (id == R.id.nav_profile) {
//            selectFirstMenu();
//            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
//        } else if (id == R.id.nav_setting) {
//            selectFirstMenu();
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

//    private void clearMenuSelection() {
//        for (int i = 0; i < navigationView.getMenu().size(); i++)
//            navigationView.getMenu().getItem(i).setChecked(false);
//    }
//
//    private void selectFirstMenu() {
//        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        if (!navigationView.getMenu().getItem(0).isChecked()) {
//            navigationView.getMenu().getItem(0).setChecked(true);
//            onNavigationItemSelected(navigationView.getMenu().getItem(0));
//        }
//    }
}