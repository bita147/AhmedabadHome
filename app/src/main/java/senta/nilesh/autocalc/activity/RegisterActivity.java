package senta.nilesh.autocalc.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import senta.nilesh.autocalc.R;
import senta.nilesh.autocalc.dto.UserProfileDTO;
import senta.nilesh.autocalc.transporter.ServicesAPI;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText etUname, etPass, etConfPass;
    private boolean isGuest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        String uName = handleDynamicLink();


        btnRegister = (Button) findViewById(R.id.btn_register);
        etUname = (EditText) findViewById(R.id.et_username);
        etPass = (EditText) findViewById(R.id.et_password);
        etConfPass = (EditText) findViewById(R.id.et_confirm_password);

        if (!TextUtils.isEmpty(uName)){
            etUname.setText(uName);
            etUname.setEnabled(false);
            isGuest = true;
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = etUname.getText().toString().trim(), pass = etPass.getText().toString().trim(), confPass = etConfPass.getText().toString().trim();
                if (TextUtils.isEmpty(uname) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(confPass)) {
                    Snackbar.make(findViewById(R.id.snackbar_position), "Fields can't empty", Snackbar.LENGTH_SHORT).show();
                } else if (!pass.equals(confPass)) {
                    Snackbar.make(findViewById(R.id.snackbar_position), "Password not match", Snackbar.LENGTH_SHORT).show();
                } else {
                    firebaseRegister(uname, pass,isGuest);
                }
            }
        });
    }

    private String handleDynamicLink() {
        try {
            Intent intent = getIntent();
            Uri data = intent.getData();
            return data.getQueryParameter("uname");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    private void firebaseRegister(final String uname, final String pass, boolean isGuest) {
        UserProfileDTO dto = new UserProfileDTO(uname, pass, null, null, null, null,isGuest,  null);
        final ProgressDialog pd = ProgressDialog.show(RegisterActivity.this, null, "Please wait...");
        ServicesAPI.doRegister(dto, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    if (firebaseError.getCode() == 2002) {
                        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                        i.putExtra("UNAME",uname);
                        i.putExtra("PASS",pass);
                        startActivity(i);
                        finish();
                    }else
                        Snackbar.make(findViewById(R.id.snackbar_position), "" + firebaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }
}
