package com.leo618.email;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.leo618.mail.MailManager;

public class MainActivity extends AppCompatActivity {
    private EditText subject;
    private EditText body;
    private Button   send;

    @SuppressLint("SetTextI18n")
    private void assignViews() {
        subject = (EditText) findViewById(R.id.subject);
        body = (EditText) findViewById(R.id.body);
        send = (Button) findViewById(R.id.send);
        subject.setText("subject=这里是邮件的主题 标题");
        body.setText("body=这是邮件内容");
    }

    private static final String senderMailName     = "xxx@126.com";
    private static final String senderMailPassword = "this is password";
    private static final String revieverMailName   = "yyy@126.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();

        final String subjectStr = subject.getText().toString();
        final String contentStr = body.getText().toString();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = MailManager.getInstance()
                        .config(senderMailName, senderMailPassword, revieverMailName)
                        .sendMail(subjectStr, contentStr);
                Toast.makeText(MainActivity.this, "send:" + result, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
