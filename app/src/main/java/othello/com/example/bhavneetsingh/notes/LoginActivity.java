package othello.com.example.bhavneetsingh.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,LoginInterface{

    TextView signin;
    TextView register;
    MyDatabase mydb;
    private boolean sign;
    public final static String LOGIN="login",NULL="null",LOGOUT="logout";
    private  SharedPreferences sharedPreferences;
    private Bundle current_bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        alreadyLogin();
        init();

    }
    public void init()
    {
        //getting buttons from layout
        signin=(TextView) findViewById(R.id.sign_in);
        signin.setOnClickListener(this);
        register=(TextView) findViewById(R.id.register1);
        register.setOnClickListener(this);
        //setting current layout to singin layout
        sign=true;
        //Local database
        mydb=MyDatabase.getInstance(this);
    }
    //Checking for already login
    public void alreadyLogin()
    {
        sharedPreferences=getSharedPreferences(LOGIN,MODE_PRIVATE);
        String userid=sharedPreferences.getString(LOGIN,NULL);
        Intent intent=getIntent();
        if(!userid.equals(NULL))
        {
            Intent intent1=new Intent(this,MainActivity.class);
            intent1.putExtra(MyDatabase.User.USER_ID,userid);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent1);
        }
    }
    //chanhging layout
    public void changeLayout()
    {
        if(sign)
        {
            setContentView(R.layout.registrer_layout);
            signin=(TextView) findViewById(R.id.sign_in1);
            signin.setOnClickListener(this);
            register=(TextView) findViewById(R.id.register);
            register.setOnClickListener(this);
        }
        else
        {
            setContentView(R.layout.activity_login);
            signin=(TextView) findViewById(R.id.sign_in);
            signin.setOnClickListener(this);
            register=(TextView) findViewById(R.id.register1);
            register.setOnClickListener(this);

        }
    }
    public void onClick(View view)
    {
        //Sigining in
        if(view.getId()==signin.getId())
        {
            if(!sign)
            {
                changeLayout();
                sign=true;
            }
            else
            {
                Bundle bundle=signInbundle();
                if(bundle==null)
                    return;
                checkLogin(bundle);
            }
        }
        //regiseration
        else if(view.getId()==register.getId())
        {
            if(sign)
            {
                changeLayout();
                sign=false;
            }
            else
            {
                Bundle bundle=registerBundle();
                if(bundle==null)
                {
                    return;
                }
                MyDatabase.User user=new MyDatabase.User(bundle.getString(MyDatabase.User.USER_ID),bundle.getString(MyDatabase.User.NAME),bundle.getString(MyDatabase.User.PASSWORD));
                boolean check=DBManager.addUser(mydb,user);
                if(check)
                {
                    Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show();
                  register(bundle);
                }
                else
                {
                    Toast.makeText(this, "Email id exists", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public boolean createAccount()
    {
        return false;
    }
    public boolean checkSignin()
    {
        return false;
    }
    //start main activity
    public void startMain(Bundle bundle)
    {

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(LOGIN,bundle.getString(MyDatabase.User.USER_ID));
        editor.commit();
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        startActivity(intent);
    }
    public Bundle signInbundle()
    {
        Bundle bundle=new Bundle();
        EditText userid=(EditText)findViewById(R.id.username);
        EditText password=(EditText)findViewById(R.id.user_password);
        if(userid.getText().toString().length()==0||password.getText().toString().length()==0)
            return null;
        bundle.putString(MyDatabase.User.USER_ID,userid.getText().toString());
        bundle.putString(MyDatabase.User.PASSWORD,password.getText().toString());
        return bundle;
    }
    public Bundle registerBundle()
    {
        Bundle bundle=new Bundle();

        try{
            //get username
            EditText userid=(EditText)findViewById(R.id.new_username);
            //get password
            EditText password=(EditText)findViewById(R.id.new_user_password);
            //check for empty
            if(userid.getText().toString().length()==0)
            {
                throw new PasswordException("UserName must not be empty");
            }
            //check password
            checkPassword(password.getText().toString());
            //confirm password
            EditText confirm=(EditText)findViewById(R.id.confirm_user_password);
            if(!confirm.getText().toString().equals(password.getText().toString()))
            {
                throw new PasswordException("Password must be same");
            }
            //get email id
            EditText id=(EditText)findViewById(R.id.userid);
            //check email id
            checkID(id.getText().toString());
            bundle.putString(MyDatabase.User.NAME,userid.getText().toString());
            bundle.putString(MyDatabase.User.PASSWORD,password.getText().toString());
            bundle.putString(MyDatabase.User.USER_ID,id.getText().toString());
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
        return bundle;
    }
    //Checking USer id
    public boolean checkID(String id)
    {
        if(id.length()==0)
        {
            throw new PasswordException("ID should not be empty");
        }
        int ind=id.indexOf('@');
        if(ind==-1)
        {
            throw new PasswordException("USer id must have domain name with @");
        }
        String sub=id.substring(ind+1);
        String s1=id.substring(0,ind);
        if(s1.length()==0)
        {
            throw new PasswordException("Empty email id");
        }
        if(sub.length()==0)
        {
            throw new PasswordException("USer id must have domain name");
        }
        if(!sub.equals("gmail.com"))
        {
            throw new PasswordException("Domain name must be gmail.com");
        }
        return true;
    }
    //Checking Password
    public boolean checkPassword(String password)
    {
        if(password.length()==0)
        {
            throw new PasswordException("Empty password");
        }
        if(password.length()<5)
        {
            throw new PasswordException("Password Length must be min 5 chars");
        }

        return true;
    }
    public void checkLogin(Bundle bundle)
    {


        ProgressBar loginBar=findViewById(R.id.loginbar);
        if(loginBar!=null)
        {
            signin=findViewById(R.id.sign_in);
            signin.setVisibility(View.INVISIBLE);
            loginBar.setVisibility(View.VISIBLE);
        }

        String urlLink="https://triads.herokuapp.com/user?id="+bundle.getString(MyDatabase.User.USER_ID)+"&password="+bundle.getString(MyDatabase.User.PASSWORD);
        Log.d("url",urlLink);
        LoginNetworking networking=new LoginNetworking(this);
        networking.execute("login",urlLink);
    }
    //Registering into dataabase
    public void register(Bundle bundle)
    {
        ProgressBar registerBar=findViewById(R.id.registerbar);
        if(registerBar!=null)
        {
            register=findViewById(R.id.register);
            register.setVisibility(View.INVISIBLE);
            registerBar.setVisibility(View.VISIBLE);
        }
        String urlLink="https://triads.herokuapp.com/register?id="+bundle.getString(MyDatabase.User.USER_ID)+"&name="+bundle.getString(MyDatabase.User.NAME)+"&password="+bundle.getString(MyDatabase.User.PASSWORD);

        Log.d("url",urlLink);
        LoginNetworking networking=new LoginNetworking(this);
        networking.execute("register",urlLink);
    }
/**
 * When result from url is obtained
 */
    @Override
    public void onPostExecute(Bundle result) {
        boolean check=result.getBoolean("result");

        ProgressBar loginBar=findViewById(R.id.loginbar);
        ProgressBar registerBar=findViewById(R.id.registerbar);
        if(loginBar!=null)
        {
            signin=findViewById(R.id.sign_in);
            signin.setVisibility(View.VISIBLE);
            loginBar.setVisibility(View.GONE);
        }
        else if(registerBar!=null)
        {
            register=findViewById(R.id.register);
            register.setVisibility(View.VISIBLE);
            registerBar.setVisibility(View.GONE);
        }
        if(!check)
      {
          Toast.makeText(this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
          return;
      }
      DBManager.addUser(mydb,new MyDatabase.User(result.getString(MyDatabase.User.USER_ID),result.getString(MyDatabase.User.NAME) , result.getString(MyDatabase.User.PASSWORD)));
      startMain(result);
    }
}
class PasswordException extends RuntimeException{
    PasswordException(String msg)
    {
        super(msg);
    }
}
class LoginNetworking extends AsyncTask<String,Void,Bundle> {

    private LoginInterface loginInterface;
    public LoginNetworking( LoginInterface loginInterface) {
     this.loginInterface=loginInterface;
    }

    @Override
    protected Bundle doInBackground(String... strings) {
        String request=strings[0],url=strings[1];
        Bundle ans=new Bundle();
        try {
            URL url1 = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNext()) {
                stringBuilder.append(scanner.next());
            }
            String result = stringBuilder.toString();
            Log.d("Login",result);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray=jsonObject.getJSONArray("rows");
            JSONObject object=jsonArray.getJSONObject(0);
            if(jsonArray.length()==0)
            {
                ans.putBoolean("result",false);
            }
            else
            {
                ans.putBoolean("result",true);
                ans.putString(MyDatabase.User.NAME,object.getString("name"));
                ans.putString(MyDatabase.User.USER_ID,object.getString("user_id"));
                ans.putString(MyDatabase.User.PASSWORD,object.getString("password"));
            }
        }
        catch (Exception e)
        {

        }
        return ans;
        }
    protected void onPostExecute(Bundle result)
    {
        super.onPostExecute(result);
        loginInterface.onPostExecute(result);
    }

}
interface LoginInterface{
     void onPostExecute(Bundle result);
}