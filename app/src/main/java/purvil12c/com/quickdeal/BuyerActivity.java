package purvil12c.com.quickdeal;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BuyerActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    TextView textView1;
    TextView textView2;

    private CallbackManager callbackManager;

    private static GoogleApiClient googleApiClient;
    private static final int REQUEST_CODE = 1000;

    ImageView googleImageView;
    LoginButton facebookLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_buyer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Seller");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textView1=(TextView)findViewById(R.id.forgot_tv);
        String text1 = "<font color=#000000>Forgot your </font> <b><font color=#3F51B5>password?</font></b>";
        textView1.setText(Html.fromHtml(text1));

        textView2=(TextView)findViewById(R.id.register_tv);
        String text2 = "<font color=#000000>Don't have an account?</font> <b><font color=#3F51B5> Register now</font></b>";
        textView2.setText(Html.fromHtml(text2));

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),RegisterBuyerActivity.class);
                startActivity(intent);
            }
        });

        //facebookLogin=(LoginButton) findViewById(R.id.facebook);
        googleImageView=(ImageView)findViewById(R.id.google);
        GoogleSignInOptions signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();
        googleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        callbackManager = CallbackManager.Factory.create();

        /*facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }


            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn(){
        Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQUEST_CODE);

    }
    private void signOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Toast.makeText(getApplicationContext(),"Logged Out!",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void handleResults(GoogleSignInResult result){

        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            String email = account.getEmail();
            String name = account.getDisplayName();
            //String img_url = account.getPhotoUrl().toString();
            Toast.makeText(getApplicationContext(),""+email +" "+name,Toast.LENGTH_LONG).show();
            signOut();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResults(result);
        }
    }
}
