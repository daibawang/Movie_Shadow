package com.sgcy.shadow.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sgcy.shadow.R;

public class Information_Activity extends AppCompatActivity {

    public static int MODE = MODE_PRIVATE;    //访问模式
    public static final String PREFERENCE_NAME = "test1"; //保存文件的名称
    private EditText name;
    private EditText email;
    private Button submit;
    private Button clear;
    private TextView show;
    private Button look;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_);

        name=(EditText) findViewById(R.id.name);
        email=(EditText) findViewById(R.id.email);
        submit=(Button) findViewById(R.id.submit);
        look=(Button) findViewById(R.id.look);
        clear=(Button) findViewById(R.id.clear);
        show=(TextView) findViewById(R.id.show);

        //获取对象
        final SharedPreferences shared=getSharedPreferences(PREFERENCE_NAME, MODE);

        //为查看按钮添加事件监听,查看文件中的内容
        look.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View arg0) {
                String nameLook=shared.getString("name", "暂无"); //key-value
                String emailLook=shared.getString("email","暂无");

                show.setText("姓名："+nameLook+"\n"+"邮箱："+emailLook+"\n");
                clearEdit();    //  清空编辑框的内容
            }
        });

        //为提交按钮添加事件响应，将编辑框的内容保存到文件中
        submit.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                SharedPreferences.Editor eidtor=shared.edit();    //声明Editor对象
                if(name.getText()!=null && email.getText()!=null)
                {
                    if(!"".equals(name.getText().toString()) && !"".equals(email.getText().toString()))
                    {
                        eidtor.putString("name", name.getText().toString());
                        eidtor.putString("email", email.getText().toString());
                        eidtor.commit();    //编辑完成，保存
                        showToast("成功添加信息！");
                        look.performClick();    //调用查看按钮的效果
                    }
                }
            }

        });

        //为清空按钮添加事件监听，清除文件中保存的内容
        clear.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                SharedPreferences.Editor eidtor=shared.edit();    //声明Editor对象
                eidtor.clear();
                eidtor.commit();
                showToast("信息清除成功！！！");
                look.performClick();    //调用查看按钮的效果
            }
        });
    }

    //清空编辑框的内容
    private void clearEdit()
    {
        name.setText("");
        email.setText("");
    }

    private void showToast(String string) {
        Toast toast = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
