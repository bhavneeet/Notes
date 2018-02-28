package othello.com.example.bhavneetsingh.notes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;
import android.widget.GridLayout;


public class MyDatabase extends SQLiteOpenHelper {
    public static MyDatabase openHelper;
    public static final String KEY_CONTEXT="content",KEY_ID="post_id",KEY_SMILEY="smiley",KEY_TABLE="post_table",KEY_DATE="current";

    private MyDatabase(Context context)
    {
        super(context,"post_db",null,1);
    }
    public  static MyDatabase getInstance(Context context)
    {
        if(openHelper==null)
        {
            openHelper=new MyDatabase(context.getApplicationContext());
        }
        return openHelper;
    }
    public void onCreate(SQLiteDatabase db) {
        String post_query="create table "+KEY_TABLE+
                "("+KEY_CONTEXT+" text,"
                +KEY_SMILEY+" integer ,"
                +KEY_ID+" integer primary key autoincrement,"
                +KEY_DATE+" datetime,"
                +User.USER_ID+" varchar(40) not null,"
                +" foreign key("+User.USER_ID+") references "+User.TABLE+"("+User.USER_ID+") on delete cascade"
                +");";
        String comment_query="create table " + Comment.TABLE_NAME+ "("
                +Comment.COMMENT_COLUMN+" text,"
                +Comment.POST_ID+" integer ,"
                +Comment.DATE_TIME+ " datetime,"
                +" foreign key("+Comment.POST_ID+") references "+Comment.TABLE_NAME+"("+KEY_ID+") on delete cascade"
                + ")";
        String user_query="create table "+User.TABLE+
                "("+User.NAME+" varchar(20),"
                +User.PASSWORD+" varchar(20),"
                +User.USER_ID+" varchar(40) primary key"
                +");";
        String graph_query="create table "+ Graph.TABLE+"("
                +Graph.SOURCE+" varchar(40),"
                +Graph.TARGET+" varchar(40),"
                +"foreign key("+Graph.SOURCE+") references "+User.TABLE+"("+User.USER_ID+") on delete cascade,"
                +"foreign key("+Graph.TARGET+") references "+User.TABLE+"("+User.USER_ID+") on delete cascade,"
                +" PRIMARY KEY("+Graph.SOURCE+","+Graph.TARGET+")"
                +");";
        db.execSQL(post_query);
        db.execSQL(comment_query);
        db.execSQL(user_query);
        db.execSQL(graph_query);
    }
    @
            Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    static class Comment{
        public static final String TABLE_NAME="comments",COMMENT_COLUMN="comment",POST_ID="post_id",DATE_TIME="date",COMMENT_ID="comment_id";
        private String comment;
        private long expense_id;
        private long comment_id;
        public Comment(long expense_id) {
            this.expense_id = expense_id;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public long getComment_id() {
            return comment_id;
        }

        public void setComment_id(long comment_id) {
            this.comment_id = comment_id;
        }

        public long getExpense_id() {
            return expense_id;
        }

        public void setExpense_id(long expense_id) {
            this.expense_id = expense_id;
        }
    }
    static class User{
        public static final String NAME="NAME",PASSWORD="PASSWORD",USER_ID="USER_ID",TABLE="USER";
        private String name,password,user_id;

        public User(String user_id, String name, String password) {
            this.name = name;
            this.password = password;
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
    static class Graph{
     public static final String SOURCE="SOURCE",TARGET="TARGET",TABLE="GRAPH";
     private String source,target;

        public Graph(String source) {
            this.source = source;
            this.target=target;
        }

        public Graph(String source, String target) {
            this.source = source;
            this.target = target;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }
    }
}
