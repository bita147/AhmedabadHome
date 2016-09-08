package senta.nilesh.autocalc.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import senta.nilesh.autocalc.R;
import senta.nilesh.autocalc.dto.UserProfileDTO;
import senta.nilesh.autocalc.transporter.ServicesAPI;
import senta.nilesh.autocalc.utils.AppPref;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private Button btnRegister;
    private EditText etUserName;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (AppPref.get(this).getUserProfileDTO() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        etUserName = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO){
                    checkFirrebaseLogin();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                checkFirrebaseLogin();
                break;
            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

    private void checkFirrebaseLogin() {
        String uName, pass;
        uName = etUserName.getText().toString().trim();
        pass = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(uName) || TextUtils.isEmpty(pass)) {
            Snackbar.make(findViewById(R.id.snackbar_position), "Fields can't empty!", Snackbar.LENGTH_SHORT).show();
        } else {
            UserProfileDTO profile = new UserProfileDTO();
            profile.setUserName(uName);
            profile.setPassword(pass);
            final ProgressDialog pd = ProgressDialog.show(LoginActivity.this, null, "Please wait...");
            ServicesAPI.doLogin(LoginActivity.this, profile, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if (firebaseError != null) {
                        Snackbar.make(findViewById(R.id.snackbar_position), "" + firebaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
                    } else {
                        AppPref prefs = AppPref.get(LoginActivity.this);
                        UserProfileDTO profile = prefs.getUserProfileDTO();
                        profile.setRegisterToken(prefs.getRegistrationToken());
                        prefs.saveLoginDTO(profile);
                        ServicesAPI.updateRegistrationToken(profile, new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        });
                    }
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                }
            });
        }
    }
}
