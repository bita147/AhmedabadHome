package senta.nilesh.autocalc.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import senta.nilesh.autocalc.R;
import senta.nilesh.autocalc.controls.MultiSpinner;
import senta.nilesh.autocalc.dto.ItemDTO;
import senta.nilesh.autocalc.dto.NotificationDTO;
import senta.nilesh.autocalc.dto.UserProfileDTO;
import senta.nilesh.autocalc.dto.VersionDTO;
import senta.nilesh.autocalc.dto.WaterBottleDTO;
import senta.nilesh.autocalc.fragments.DailyViewFragment;
import senta.nilesh.autocalc.fragments.MonthViewFragment;
import senta.nilesh.autocalc.fragments.WaterBottleFragment;
import senta.nilesh.autocalc.listeners.TransactionInsertListener;
import senta.nilesh.autocalc.listeners.VersionInfoRetrived;
import senta.nilesh.autocalc.transporter.ServicesAPI;
import senta.nilesh.autocalc.utils.AppPref;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private FragmentManager fm;
    private FloatingActionButton fabAddItem;

    @Override
    protected void onStart() {
        super.onStart();
        ServicesAPI.getUserList(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fm = getSupportFragmentManager();

        checkUpdateVersion();
        fabAddItem = (FloatingActionButton) findViewById(R.id.fab);
        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WaterBottleFragment wbFrg = (WaterBottleFragment) getSupportFragmentManager().findFragmentByTag("WATER_FRG");
                if (wbFrg != null && wbFrg.isAdded() && wbFrg.isVisible())
                    showWaterBottleInputDialog();
                else
                    showInputDialog();
            }
        });

        setTitle("" + AppPref.get(this).getUserProfileDTO().getUserName());

//        DailyViewFragment fragment = new DailyViewFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.ll_container, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.snackbar_position);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setupHeader();
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Select First Item and load Fragment
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        if (AppPref.get(this).getUserProfileDTO().isGuest()) {
            navigationView.getMenu().getItem(1).setVisible(false);
        }
    }

    private void checkUpdateVersion() {
        AppPref.get(MainActivity.this).saveVersionCode(MainActivity.this);
        ServicesAPI.getVersionUpadteInfo(new VersionInfoRetrived() {
            @Override
            public void onVersionInfoRetrived(VersionDTO version) {
                if (version != null && version.getVersion() > AppPref.get(MainActivity.this).getVersionCode()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Update");
                    builder.setMessage("App Update available press download button to download it");
                    builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            appDownloadAndInstall();
                        }
                    });
                    if (version.isIsOldAppContinue()) {
                        builder.setNegativeButton("Cancel", null);
                    } else
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            }
        });
    }

    private void appDownloadAndInstall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else
            ServicesAPI.getAllFiles(this);
    }

    // For Android M Permision
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission(final Context context, final String permission) {

        if (shouldShowRequestPermissionRationale(permission)) {
            Snackbar.make(findViewById(R.id.snackbar_position), "App need permission", Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{permission}, 0);
                }
            }).show();
        } else {
            Snackbar.make(findViewById(R.id.snackbar_position), "Permission not Granted", Snackbar.LENGTH_SHORT).show();
            requestPermissions(new String[]{permission}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    switch (permissions[i]) {
                        case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                            ServicesAPI.getAllFiles(this);
                            break;
                    }
                }
            }
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setupHeader() {
        UserProfileDTO dto = AppPref.get(this).getUserProfileDTO();
        if (dto != null) {
            TextView tvUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_username);
            TextView tvShortnameRound = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_short_name);
            TextView tvEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_email);

            if (!TextUtils.isEmpty(dto.getFirstName()) && !TextUtils.isEmpty(dto.getLastName())) {
                tvUserName.setText(dto.getFirstName() + " " + dto.getLastName());
                tvShortnameRound.setText(String.valueOf(dto.getFirstName().charAt(0) + dto.getLastName().charAt(0)).toUpperCase());
            } else {
                tvUserName.setText(dto.getUserName());
                tvShortnameRound.setText(String.valueOf(dto.getUserName().charAt(0)).toUpperCase());
            }
            if (!TextUtils.isEmpty(dto.getEmail())) {
                tvEmail.setText(dto.getEmail());
            }
        }
    }

    Dialog dialog;

    private void showInputDialog() {
        dialog = new Dialog(this, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_buy_thing);

        final TextView tvDate = (TextView) dialog.findViewById(R.id.tv_date);
        final EditText etDescription = (EditText) dialog.findViewById(R.id.et_item_description);
        final EditText etAmt = (EditText) dialog.findViewById(R.id.et_item_amount);
        final MultiSpinner spUsers = (MultiSpinner) dialog.findViewById(R.id.sp_users);
        final Spinner spPayby = (Spinner) dialog.findViewById(R.id.sp_payby);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_dialog_ok);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_dialog_cancel);

        spPayby.setAdapter(new ArrayAdapter<String>(this, R.layout.item_spinner, AppPref.get(this).getUserList()));
        spUsers.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, AppPref.get(this).getUserList()));
        spUsers.setMultiSpinnerEntries(AppPref.get(this).getUserList(), AppPref.get(this).getUserProfileList());

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

    int waterBottle = 1;

    private void showWaterBottleInputDialog() {
        dialog = new Dialog(this, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_water_bottle);

        final TextView tvDate = (TextView) dialog.findViewById(R.id.tv_date);
        final ImageView ivIncrease = (ImageView) dialog.findViewById(R.id.iv_increase);
        final ImageView ivDecrease = (ImageView) dialog.findViewById(R.id.iv_decrease);
        final TextView tvWaterBottleCount = (TextView) dialog.findViewById(R.id.tv_water_bottle_count);

        Button btnOk = (Button) dialog.findViewById(R.id.btn_dialog_ok);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_dialog_cancel);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy EEE hh:mm a");
        tvDate.setText(dateFormat.format(new Date(System.currentTimeMillis())));

        ivIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (waterBottle > 9)
                    return;
                waterBottle++;
                tvWaterBottleCount.setText(String.valueOf(waterBottle));
            }
        });
        ivDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (waterBottle < 2)
                    return;
                waterBottle--;
                tvWaterBottleCount.setText(String.valueOf(waterBottle));
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaterBottleDTO dto = new WaterBottleDTO();
                dto.setBottleCount(waterBottle);
                dto.setBuyDate(tvDate.getText().toString());
                dto.setUserName(AppPref.get(MainActivity.this).getUserProfileDTO().getUserName());

                final ProgressDialog pd = ProgressDialog.show(MainActivity.this, null, "Please wait...");
                ServicesAPI.insertWaterBottle(dto, new TransactionInsertListener() {
                    @Override
                    public void onTransactionCompleted() {
                        waterBottle = 1;
                        if (pd != null && pd.isShowing())
                            pd.dismiss();
                        dialog.dismiss();
                    }
                });
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.snackbar_position);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!navigationView.getMenu().getItem(0).isChecked()) {
                navigationView.getMenu().getItem(0).setChecked(true);
                onNavigationItemSelected(navigationView.getMenu().getItem(0));
            } else {
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction frgTransaction = getSupportFragmentManager().beginTransaction();
        frgTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        // Remove all Fragment
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (id == R.id.nav_daily_view) {
            DailyViewFragment fragment = new DailyViewFragment();
            frgTransaction.add(R.id.ll_container, fragment, "DAILY_FRG").addToBackStack(null).commit();
        } else if (id == R.id.nav_month_view) {
            MonthViewFragment fragment = new MonthViewFragment();
            frgTransaction.add(R.id.ll_container, fragment, "MONTH_FRG").addToBackStack(null).commit();
        } else if (id == R.id.nav_water_bottle) {
            WaterBottleFragment fragment = new WaterBottleFragment();
            frgTransaction.add(R.id.ll_container, fragment, "WATER_FRG").addToBackStack(null).commit();
        } else if (id == R.id.nav_profile) {
            selectFirstMenu();
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        } else if (id == R.id.nav_setting) {
            selectFirstMenu();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.snackbar_position);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clearMenuSelection() {
        for (int i = 0; i < navigationView.getMenu().size(); i++)
            navigationView.getMenu().getItem(i).setChecked(false);
    }

    private void selectFirstMenu() {
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (!navigationView.getMenu().getItem(0).isChecked()) {
            navigationView.getMenu().getItem(0).setChecked(true);
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }
    }
}
