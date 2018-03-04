package othello.com.example.bhavneetsingh.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    TextView signin;
    TextView register;
    MyDatabase mydb;
    private boolean sign;
    public final static String LOGIN="login",NULL="null",LOGOUT="logout";
    private  SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       sharedPreferences=getSharedPreferences(LOGIN,MODE_PRIVATE);
        String userid=sharedPreferences.getString(LOGIN,NULL);
        Intent intent=getIntent();
        boolean logout=false;
        if(intent!=null)
         logout=intent.getBooleanExtra(LOGOUT,false);
        if(logout)
        {
            userid=NULL;
            sharedPreferences.edit().remove(LOGIN);
            sharedPreferences.edit().commit();
        }
        setContentView(R.layout.activity_login);
        signin=(TextView) findViewById(R.id.sign_in);
        signin.setOnClickListener(this);
        register=(TextView) findViewById(R.id.register1);
        register.setOnClickListener(this);
        sign=true;
        mydb=MyDatabase.getInstance(this);
    }
    public void alreadyLogin()
    {
        sharedPreferences=getSharedPreferences(LOGIN,MODE_PRIVATE);
        String userid=sharedPreferences.getString(LOGIN,NULL);
        Intent intent=getIntent();
        if(!userid.equals(NULL))
        {
            Intent intent1=new Intent(this,MainActivity.class);
            intent1.putExtra(MyDatabase.User.USER_ID,userid);
            startActivity(intent1);
        }

    }
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

        if(view.getId()==signin.getId())
        {
           if(!sign)
           {
               changeLayout();
               sign=true;
           }
           else
           {
               Bundle bundle=bundle();
               if(bundle==null)
                   return;
               MyDatabase.User check=DBManager.containsUser(mydb,bundle.getString(MyDatabase.User.USER_ID));
               if(check==null)
               {
                   Toast.makeText(this, "No User Id exists", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   String password =check.getPassword();
                   if(!bundle.getString(MyDatabase.User.PASSWORD).equals(password))
                   {
                       Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                   }
                   else
                   {
                   startMain(bundle);
                   }
               }
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
                    startMain(bundle);
                }
                else
                {
                    Toast.makeText(this, "Email id exists", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
    public Bundle bundle()
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
        if(!sub.equals("blackhole.co.in"))
        {
            throw new PasswordException("Domain name must be blackhole.co.in");
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
}
class PasswordException extends RuntimeException{
    PasswordException(String msg)
    {
        super(msg);
    }
}