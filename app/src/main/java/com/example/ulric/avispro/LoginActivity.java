package com.example.ulric.avispro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private final String DUMMY_LOGIN = "Bruce";
    private final String DUMMY_PASSWORD = "iambatman";
    Button btnLogin, btnRegister;
    EditText user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnRegister = findViewById(R.id.boton_login);
        btnRegister = findViewById(R.id.boton_register);

        user = findViewById(R.id.label_usuario);
        pass = findViewById(R.id.label_contrasena);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usr = user.getText().toString();
                String pas = pass.getText().toString();
                if (usr.isEmpty() || pas.isEmpty()){
                    Toast.makeText(v.getContext(), R.string.empty_login, Toast.LENGTH_LONG).show();
                } else if (!usr.equals(DUMMY_LOGIN) || pas.equals(DUMMY_PASSWORD)) {
                    Toast.makeText(v.getContext(), R.string.wrong_login, Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(v.getContext(), ListActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
}
