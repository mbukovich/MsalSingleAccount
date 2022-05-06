package com.example.msalsingleaccount;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.ILoggerCallback;
import com.microsoft.identity.client.IMultipleAccountPublicClientApplication;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.Logger;
import com.microsoft.identity.client.Prompt;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

public class MainActivity extends AppCompatActivity {

    TextView mTextView;
    ISingleAccountPublicClientApplication mMsalClient;

    String[] mScopes = {"User.Read"};

    private StringBuilder mLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogs = new StringBuilder();
        Logger.getInstance().setExternalLogger(new ILoggerCallback()
        {
            @Override
            public void log(String tag, Logger.LogLevel logLevel, String message, boolean containsPII)
            {
                mLogs.append(message).append('\n');
            }
        });

        mTextView = findViewById(R.id.text_view_name);

        PublicClientApplication.createSingleAccountPublicClientApplication(this,
                R.raw.msal_config,
                new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        mMsalClient = application;
                        login();
                    }

                    @Override
                    public void onError(MsalException exception) {
                        mTextView.setText(exception.getMessage());
                    }
                });
    }

    private void login() {
        mMsalClient.signIn(this, "", mScopes, getAuthCallback());
        // mMsalClient.acquireToken(this, mScopes, getAuthCallback());
    }

    private AuthenticationCallback getAuthCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onCancel() {
                mTextView.setText("User cancelled Authentication.");
            }

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                mTextView.setText(authenticationResult.getAccount().getUsername());
            }

            @Override
            public void onError(MsalException exception) {
                mTextView.setText(exception.getMessage());
            }
        };
    }
}