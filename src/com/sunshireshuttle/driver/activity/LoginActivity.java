package com.sunshireshuttle.driver.activity;

import java.io.IOException;
import java.io.InputStream;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sunshireshuttle.driver.BaseActivity;
import com.sunshireshuttle.driver.MySPEdit;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.control.DriverCommonControl;
import com.sunshireshuttle.driver.control.ProgressCallback;
import com.sunshireshuttle.driver.model.IDQueryResponse;
import com.sunshireshuttle.driver.model.LoginResponse;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import net.iaf.framework.util.Version;

/**
 * @author Heisenberg heisenberg.gong@koolpos.com>
 * @ClassName: LoginActivity
 * @Description: 登陆页面
 * @date 2016年8月5日 下午9:49:53
 */

public class LoginActivity extends BaseActivity {

    private View btn_login;
    private EditText et_name, et_pw;
    private MySPEdit edit;
    private TextView tv_version;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;

    private class SurfaceViewLis implements SurfaceHolder.Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                play();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mediaPlayer.stop();
        }

    }

    public void play() throws IllegalArgumentException, SecurityException,
            IllegalStateException, IOException {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        AssetFileDescriptor fd = this.getAssets().openFd("driver_load_final.mp4");
        mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(),
                fd.getLength());
        mediaPlayer.setLooping(true);
        mediaPlayer.setDisplay(surfaceView.getHolder());
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 装载完毕回调
                mediaPlayer.start();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (application.dirverToken != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }
        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        mediaPlayer = new MediaPlayer();
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceViewLis());

        btn_login = findViewById(R.id.btn_login);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText("V " + Version.getVersionName());
        tv_version.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputStream is = getBaseContext().getAssets().open("version_tag.txt");
                    int size = is.available();
                    // Read the entire asset into a local byte buffer.
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    // Convert the buffer into a string.
                    String text = new String(buffer);
                    showDialog(null, text);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        et_name = (EditText) findViewById(R.id.et_name);
        et_pw = (EditText) findViewById(R.id.et_pw);
        edit = MySPEdit.getInstance(application);
        et_name.setText(edit.getName());
        et_pw.setText(edit.getPassword());
        btn_login.setOnClickListener(new OnClickListener() {

            private ProgressCallback<LoginResponse> updateViewAsyncCallback = new ProgressCallback<LoginResponse>(
                    LoginActivity.this) {

                @Override
                protected void showProgress() {
                    setProgressMessage("login...");
                    super.showProgress();
                    btn_login.setEnabled(false);
                }

                @Override
                protected void dismissProgress() {
                    super.dismissProgress();
                    btn_login.setEnabled(true);
                }

                @Override
                public void onPostExecute(LoginResponse response) {
                    super.onPostExecute(response);
                    if (response.isResponseOK()) {
                        edit.setName(et_name.getText().toString());
                        edit.setPassword(et_pw.getText().toString());
                        getDirverInfo(response.getId());
                    }
                }
            };

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_name.getText().toString()) || TextUtils.isEmpty(et_pw.getText().toString())) {
                    return;
                }
                DriverCommonControl control = new DriverCommonControl();
                String username = et_name.getText().toString();
                String driver_code = et_pw.getText().toString();
                control.login(updateViewAsyncCallback, username, driver_code);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void getDirverInfo(final String id) {
        ProgressCallback<IDQueryResponse> updateViewAsyncCallback = new ProgressCallback<IDQueryResponse>(
                LoginActivity.this) {

            @Override
            protected void showProgress() {
                setProgressMessage("get datas...");
                super.showProgress();
                btn_login.setEnabled(false);
            }

            @Override
            protected void dismissProgress() {
                super.dismissProgress();
                btn_login.setEnabled(true);
            }

            @Override
            public void onPostExecute(IDQueryResponse response) {
                super.onPostExecute(response);
                edit.setDirverToken(response.getDriver());
                application.dirverToken = response.getDriver();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        };

        DriverCommonControl control = new DriverCommonControl();
        control.getDirverInfo(updateViewAsyncCallback, id);

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(null)
//                .setUrl(Uri.parse("http://host/path"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        try {
            client.connect();
            AppIndex.AppIndexApi.start(client, getIndexApiAction());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        try {
            AppIndex.AppIndexApi.end(client, getIndexApiAction());
            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
