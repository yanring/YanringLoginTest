package com.example.yanring.login;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanring.login.Database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final String mLastLoginUserName = "Last_Login_User_Name";
    private SQLiteDatabase mSqLiteDatabase;
    private AutoCompleteTextView mAutoCompleteTextView;
    private EditText mEditText;
    private SharedPreferences.Editor mEditor;
    private int mLoginStatue = 0;
    private String mUserNameString;
    private Button mButton;
    private String mLastLogin = "无";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

        mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.email);
        mEditText = (EditText) findViewById(R.id.password);
        mButton = (Button) findViewById(R.id.email_sign_in_button);
        mButton.setOnClickListener(this);
        mTextView = (TextView) findViewById(R.id.textView);
        setData();
        Log.i("LoginActivity","mLoginStatue "+mLoginStatue+"LastLoginUserName"+mLastLogin);
        if (mLoginStatue ==0){
            setTitle("未登录");
            mButton.setText("Login/Register");
        }else if(mLoginStatue ==1) {
            mButton.setText("Quit");
            setTitle(mLastLogin+"已登录");
        }
        final DatabaseHelper databaseHelper = new DatabaseHelper(this);
        mSqLiteDatabase = databaseHelper.getWritableDatabase();

    }

    private void setData() {
        SharedPreferences sharedPreference = getSharedPreferences("Preference_name", Context.MODE_PRIVATE);
        mLastLogin = sharedPreference.getString(mLastLoginUserName,"五");
        mLoginStatue = sharedPreference.getInt("Login_Statue",0);
        mTextView.setText("上次登录 : "+mLastLogin);
    }

    @Override
    public void onClick(View view) {
        Log.i("LoginActivity","OK2");
        switch (view.getId()){
            case R.id.email_sign_in_button:{
                if (mLoginStatue == 0){
                    mUserNameString = mAutoCompleteTextView.getText().toString();
                    String Password = mEditText.getText().toString();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("username",mUserNameString);
                    contentValues.put("password",Password);
                    Log.i("LoginActivity","OK");
                    setTitle(mUserNameString+"已登录");
                    mButton.setText("Quit");

                    //saveData2Preference();
                    mLoginStatue = 1;//设置状态为已登录
                    //如果存在则不添加
                    String[] selectionArgs = { mUserNameString };
                    Cursor cursor = mSqLiteDatabase.query(DatabaseHelper.Database_Name,null,"username=?",selectionArgs,null,null,null);
                    //int index = cursor.getColumnIndex(mUserNameString);
                    if(cursor.getCount()==0){
                        long rowNumber  = mSqLiteDatabase.insert(DatabaseHelper.Database_Name,null,contentValues);
                        Log.i("LoginActivity","index = ");
                        if (rowNumber != -1){
                            Toast.makeText(LoginActivity.this,"ok!",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivity.this,"此账户已存在,已直接登录",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    setTitle("未登陆");
                    mLoginStatue = 0;//设置状态为未登录
                    mButton.setText("LOGIN/REGISTER");
                    saveData2Preference();
                    mLastLogin = mUserNameString;
                    mTextView.setText("上次登录 : "+mLastLogin);
                    mEditText.setText("");

                }

            }
        }
    }
    private void saveData2Preference() {
        //系统会自动帮我们创建一个XML文件,名字是"preference_name",

        SharedPreferences sharedPreference =  LoginActivity.this.getSharedPreferences("Preference_name", Context.MODE_PRIVATE);
        //SharedPreferences sharedPreference = getSharedPreferences("Preference_name", Context.MODE_PRIVATE);

        mEditor = sharedPreference.edit();

        mEditor.putInt("Login_Statue",mLoginStatue);
        mEditor.putString(mLastLoginUserName,mLastLogin);
       // Log.i("LoginActivity","saveData2Preference mLoginStatue"+mLoginStatue);

        mEditor.apply();

        Log.i("LoginActivity","saveData2Preference mLoginStatue"+sharedPreference.getString(mLastLoginUserName,"sd"));
        //Log.i("LoginActivity","saveData2Preference mLoginStatue"+sharedPreference.getString(mLastLoginUserName,"sd"));

    }

    @Override
    protected void onStop() {
        super.onStop();
        mSqLiteDatabase.close();

    }
}
