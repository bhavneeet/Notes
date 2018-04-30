package othello.com.example.bhavneetsingh.Triads.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import othello.com.example.bhavneetsingh.Triads.Database.DBManager;
import othello.com.example.bhavneetsingh.Triads.Database.MyDatabase;
import othello.com.example.bhavneetsingh.Triads.Networking.*;
import othello.com.example.bhavneetsingh.Triads.R;
import othello.com.example.bhavneetsingh.Triads.User.User;

public class LoginActivity extends AppCompatActivity{

    TextView signin;
    TextView register;
    MyDatabase mydb;
    private boolean sign;
    private static final String EMAIL = "email";
    CallbackManager callbackManager;
    LoginButton loginButton;
    public final static String LOGIN="login",NULL="null",LOGOUT="logout";
    private  SharedPreferences sharedPreferences;
    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        alreadyLogin();
        loginButton = (LoginButton) findViewById(R.id.login_facebook);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFacebook();
            }
        });

    }

    public void logout()
    {
        LoginManager.getInstance().logOut();
    }
    public void loginFacebook(){


        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("user_status"));

        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                     registerUser();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void registerUser()
    {

        Profile profile=Profile.getCurrentProfile();
        User user=new User(profile.getId(),profile.getName(),"12345");
        user.setProfilePictureUrl(profile.getProfilePictureUri(640,640).toString());
        DBManager.registerUser(user, new OnDownloadComplete<User>() {
         @Override
         public void onDownloadComplete(User result) {
             if(result!=null)
             {
                 startMain();
             }
             else
             {
                 Toast.makeText(LoginActivity.this, "Can not Login/Register", Toast.LENGTH_SHORT).show();
             }
         }
     });
    }
    public void alreadyLogin()
    {
        if(AccessToken.getCurrentAccessToken()!=null)
        startMain();
    }
    public void startMain()
    {
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
