package jp.techacademy.inoue.yuuta.test4;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;
import java.util.Timer;
import java.util.TimerTask;




    public class MainActivity extends AppCompatActivity {

        Cursor cursor = null;

        private static final int PERMISSIONS_REQUEST_CODE = 100;
        Timer mTimer;
        TextView mTimerText;
        double mTimerSec = 0.0;

        Handler mHandler = new Handler();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Android 6.0以降の場合
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // パーミッションの許可状態を確認する
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // 許可されている
                    getContentsInfo();
                } else {
                    // 許可されていないので許可ダイアログを表示する
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
                }
                // Android 5系以下の場合
            } else {
                getContentsInfo();
            }

            Button button = (Button) findViewById(R.id.button);
            Button button2 = (Button) findViewById(R.id.button2);
            Button button3 = (Button) findViewById(R.id.button3);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( cursor == null )
                    {
                        return;
                    }
                    if (cursor.moveToNext()) {
                        // indexからIDを取得し、そのIDから画像のURIを取得する
                        int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                        Long id = cursor.getLong(fieldIndex);
                        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        imageView.setImageURI(imageUri);
                    } else {
                        cursor.moveToFirst();
                    }
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( cursor == null )
                    {
                        return;
                    }
                    if (cursor.moveToPrevious()) {
                        // indexからIDを取得し、そのIDから画像のURIを取得する
                        int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                        Long id = cursor.getLong(fieldIndex);
                        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        imageView.setImageURI(imageUri);
                    } else {
                        cursor.moveToLast();
                    }
                }
            });

            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTimer == null) {
                        ((Button)v).setText("停止");
                        mTimer = new Timer();
                        mTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                mTimerSec += 0.1;

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if( cursor == null )
                                        {
                                            return;
                                        }
                                        if (cursor.moveToNext()) {
                                            // indexからIDを取得し、そのIDから画像のURIを取得する
                                            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                                            Long id = cursor.getLong(fieldIndex);
                                            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                                            ImageView imageView = (ImageView) findViewById(R.id.imageView);
                                            imageView.setImageURI(imageUri);
                                        } else {
                                            cursor.moveToFirst();
                                        }
                                    }
                                });
                            }
                        }, 0, 2000);
                    }
                    else if (mTimer != null) {
                        ((Button)v).setText("再生");
                        Button button = (Button) findViewById(R.id.button);
                        Button button2 = (Button) findViewById(R.id.button2);
                        button.setEnabled(true);
                        button2.setEnabled(true);
                        mTimer.cancel();
                        mTimer = null;
                    }
                }
            });
        }
        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case PERMISSIONS_REQUEST_CODE:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        getContentsInfo();
                    }
                    break;
                default:
                    break;
            }
        }

        private void getContentsInfo() {

            // 画像の情報を取得する
            ContentResolver resolver = getContentResolver();
            cursor = resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                    null, // 項目(null = 全項目)
                    null, // フィルタ条件(null = フィルタなし)
                    null, // フィルタ用パラメータ
                    null // ソート (null ソートなし)
            );


            if (cursor.moveToFirst()) {
                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = cursor.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                imageVIew.setImageURI(imageUri);

            }
            //cursor.close();
        }


}



