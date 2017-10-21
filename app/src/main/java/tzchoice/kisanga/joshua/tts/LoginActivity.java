package tzchoice.kisanga.joshua.tts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import tzchoice.kisanga.joshua.tts.Helper.SQLiteHandler;
import tzchoice.kisanga.joshua.tts.Helper.SessionManager;
import tzchoice.kisanga.joshua.tts.Pojo.User;

import static tzchoice.kisanga.joshua.tts.R.id.username;

public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private EditText inputUsername,inputPincode;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = (EditText) findViewById(username);
        inputPincode = (EditText) findViewById(R.id.pin_code);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        inputPincode.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if(inputUsername.getText().length() > 0 && inputPincode.getText().length() >0){
                                String username = inputUsername.getText().toString().trim();
                                int pinCode = Integer.parseInt(inputPincode.getText().toString().trim());

                                checkLogin(username,pinCode);

                            }else{
                                Toast.makeText(LoginActivity.this, "fill username/pin code ", Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {


                if(inputUsername.getText().length() > 0 && inputPincode.getText().length() >0){
                    String username = inputUsername.getText().toString().trim();
                    int pinCode = Integer.parseInt(inputPincode.getText().toString().trim());

                    checkLogin(username,pinCode);

                }else{
                    Toast.makeText(LoginActivity.this, "fill username/pin code ", Toast.LENGTH_SHORT).show();
                }


            }

        });


    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String username, final int pinCode) {
        pDialog.setMessage("logging in ...");
        showDialog();

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        User user = new User();
        user.setUsername(username);
        user.setPin_code(pinCode);


    try {

     Call<User> call = service.postLogin(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {


                if(response.isSuccess()) {

                    User userObj = response.body();
                    db.addUser(userObj.getUsername(), userObj.getPin_code(), userObj.getCheckpoint_id(), userObj.getCheckpoint(), userObj.getUser_id());

                    session.setLogin(true);
                    hideDialog();
                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "Welcome : " + username, Toast.LENGTH_SHORT).show();
                    finish();
                }else{

                    Toast.makeText(LoginActivity.this, "Invalid email and/or password", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(LoginActivity.this, "No internet, make sure you are connected to internet", Toast.LENGTH_SHORT).show();
                hideDialog();

            }
        });
        }catch (Exception e){
        Toast.makeText(LoginActivity.this, "Server Error. check internet or try again", Toast.LENGTH_SHORT).show();
        hideDialog();
        }

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
