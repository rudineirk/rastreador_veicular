package sociesc.com.rastreadorveicular;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends Activity {
    public Intent openMainActivity;
    private UserLogin AuthTask = null;

    private EditText User;
    private EditText Password;
    private View ProgressView;
    private View LoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        User = (EditText) findViewById(R.id.nome);

        Password = (EditText) findViewById(R.id.senha);
        Password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        /*Instancia botão, prepara para leitura do click*/
        Button SignInButton = (Button) findViewById(R.id.sign_in_button);
        SignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        LoginFormView = findViewById(R.id.login_form);
        ProgressView = findViewById(R.id.login_progress);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            LoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            LoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    LoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            ProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            LoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void attemptLogin() {
        if (AuthTask != null) {
            return;
        }

        // Reset errors.
        User.setError(null);
        Password.setError(null);

        // Store values at the time of the login attempt.
        String usuario = User.getText().toString();
        String senha = Password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(usuario)) {
            User.setError(getString(R.string.error_field_required));
            focusView = User;
            cancel = true;
        }

        if (TextUtils.isEmpty(senha)) {
            Password.setError(getString(R.string.error_field_required));
            focusView = Password;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            AuthTask = new UserLogin(usuario, senha);
            AuthTask.execute();
        }
    }

    public void login(String user){
        openMainActivity = new Intent(this, MainActivity.class);
        openMainActivity.putExtra("USER", user);
        startActivity(openMainActivity);

    }

    private class UserLogin extends AsyncTask<Void, Void, Boolean> {

        private final String user;
        private final String password;

        UserLogin(String user, String password) {
            this.user = user;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DataUser user_data = new DataUser();
            user_data.name = this.user;
            user_data.password = this.password;
            return new ApiUser().login(user_data);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            AuthTask = null;
            showProgress(false);

            if (success) {
                login(user);

            } else {
                Password.setError(getString(R.string.error_incorrect_password));
                Password.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            AuthTask = null;
            showProgress(false);
        }
    }
}



