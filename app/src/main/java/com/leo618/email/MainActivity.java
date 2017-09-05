package com.leo618.email;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.leo618.mail.MailManager;

public class MainActivity extends AppCompatActivity {
    private EditText senderName;
    private EditText senderPassword;
    private EditText recName;
    private EditText subject;
    private EditText body;
    private Button   send;

    private void assignViews() {
        senderName = (EditText) findViewById(R.id.senderName);
        senderPassword = (EditText) findViewById(R.id.senderPassword);
        recName = (EditText) findViewById(R.id.recName);
        subject = (EditText) findViewById(R.id.subject);
        body = (EditText) findViewById(R.id.body);
        send = (Button) findViewById(R.id.send);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senderNameVal     = senderName.getText().toString();
                String senderPasswordVal = senderPassword.getText().toString();
                String recNameVal        = recName.getText().toString();
                String subjectVal        = subject.getText().toString();
                String bodyVal           = body.getText().toString();

                if (TextUtils.isEmpty(senderNameVal) || TextUtils.isEmpty(senderPasswordVal)
                        || TextUtils.isEmpty(recNameVal) || TextUtils.isEmpty(subjectVal) || TextUtils.isEmpty(bodyVal)){
                    Toast.makeText(MainActivity.this, "填写信息" , Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean sendResult = MailManager.config(senderNameVal, senderPasswordVal, recNameVal).sendMail(subjectVal, bodyVal);
                Toast.makeText(MainActivity.this, "sendResult:" + sendResult, Toast.LENGTH_SHORT).show();
                subject.setText("");
                body.setText("");
            }
        });
    }
}
