package itkido.me.androidstudioaudioplayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.palette.graphics.Palette;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mp;
    int totalTime;
    private ImageView fastForward;
    private ImageView rewind;
    private NotificationManagerCompat notificationManager;
    public static final String CHANNEL_2_ID = "channel2";
    FloatingActionButton btnSoundFloating;
    TextView mentorName;
    TextView audioTitle;
    ImageView mentorImage;
    FloatingActionButton floating_back_button;
    Boolean isFABOpen;
    FloatingActionButton btnSpeedFloating;
    FloatingActionButton btnSpeed2Floating;

    RelativeLayout relLayoutAudioView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        volumeBar = findViewById(R.id.volumeBar);
        btnSoundFloating = findViewById(R.id.btnSoundFloating);
        btnSpeedFloating = findViewById(R.id.btnSpeedFloating);
        btnSpeed2Floating = findViewById(R.id.btnSpeed2Floating);
        relLayoutAudioView = findViewById(R.id.relLayoutAudioView);
        isFABOpen=false;
        mentorName = findViewById(R.id.mentorName);
        audioTitle = findViewById(R.id.audioTitle);
        mentorImage = findViewById(R.id.mentorImage);
        floating_back_button = findViewById(R.id.floating_back_button);
        elapsedTimeLabel = findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = findViewById(R.id.remainingTimeLabel);
        playBtn = findViewById(R.id.playBtn);
        rewind = findViewById(R.id.rewind);
        fastForward = findViewById(R.id.fastForward);
        fastForward.setVisibility(View.VISIBLE);
        rewind.setVisibility(View.VISIBLE);
        volumeBar.setVisibility(View.INVISIBLE);


        getSupportActionBar().hide();


        btnSoundFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (volumeBar.getVisibility() == View.VISIBLE){
                    volumeBar.setVisibility(View.INVISIBLE);
                }else {
                    volumeBar.setVisibility(View.VISIBLE);
                }



            }
        });




        notificationManager = NotificationManagerCompat.from(this);
        //mediaSession = new MediaSessionCompat(this, "tag");


        floating_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                finish();
            }
        });


        btnSpeedFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFABOpen == false){
                    if (!mp.isPlaying()) {
                        // Stopping
                        mp.start();
                        playBtn.setBackgroundResource(R.drawable.ic_pause);

                    } else {
                        // Playing
                        float speed = 1.0f;
                        mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));

                        playBtn.setBackgroundResource(R.drawable.ic_play);
                    }

                    showFABMenu();
                }else{

                    if (!mp.isPlaying()) {
                        // Stopping
                        mp.start();
                        playBtn.setBackgroundResource(R.drawable.ic_pause);

                    } else {
                        // Playing
                        float speed = 1.0f;
                        mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));
                        mp.pause();
                        playBtn.setBackgroundResource(R.drawable.ic_play);
                    }

                    closeFABMenu();


                }
            }
        });


        btnSpeed2Floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mp.isPlaying()) {
                    // Stopping
                    mp.start();
                    playBtn.setBackgroundResource(R.drawable.ic_pause);

                } else {
                    // Playing
                    float speed = 2.0f;
                    mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));
                    playBtn.setBackgroundResource(R.drawable.ic_play);
                }
                closeFABMenu();

            }
        });




        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playBtnClick();
            }
        });


        createMediaPlayer();

        createNotificationChannels();


    }

    private void showFABMenu(){
        isFABOpen=true;
        btnSpeed2Floating.animate().translationY(-225);

    }

    private void closeFABMenu(){
        isFABOpen=false;
        btnSpeed2Floating.animate().translationY(0);

    }


    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is Channel 2");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel2);
        }
    }


// If you want to get the bitmap from the url
//    public static Bitmap getBitmapFromURL(String strUrl) {
//        try {
//            URL url = new URL(strUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    //If you want to send notifications
    public void sendNotification(String mentorNameTxt, String mentorTitleTxt) {


        //Bitmap artwork = BitmapFactory.decodeResource(getResources(), R.drawable.);



//        try {
//            WallpaperManager.getInstance(this).setBitmap(artwork, null, true, WallpaperManager.FLAG_LOCK);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
//        PendingIntent pauseIntent = PendingIntent.getBroadcast(this,
//                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
//                .setSmallIcon(R.drawable.ic_launcher_round)
//                .setContentTitle(mentorTitleTxt)
//                .setContentText(mentorNameTxt)
//                .setLargeIcon(artwork)
//                .addAction(R.drawable.ic_previous, "Previous", null)
//                .addAction(R.drawable.ic_stop_black, "Pause", pauseIntent)
//                .addAction(R.drawable.ic_next, "Next", null)
//                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
//                        .setShowActionsInCompactView(0, 1, 2)
//                        .setMediaSession(mediaSession.getSessionToken()))
//                .setPriority(NotificationCompat.PRIORITY_LOW)
//                .setOngoing(false)
//                .build();
//
//
//
//        notificationManager.notify(2, notification);

    }






    public static int manipulateColor(int color, float factor) {
        float[] hsv = new float[3]; Color.colorToHSV(color, hsv); hsv[2] *= factor; return Color.HSVToColor(hsv);
//        int a = Color.alpha(color);
//        int r = Math.round(Color.red(color) * factor);
//        int g = Math.round(Color.green(color) * factor);
//        int b = Math.round(Color.blue(color) * factor);
//        return Color.argb(a, Math.min(r,255), Math.min(g,255), Math.min(b,255));
    }

    private void createMediaPlayer(){



        // Media Player
        mp = MediaPlayer.create(this, Uri.parse("https://firebasestorage.googleapis.com/v0/b/lucasportfolio-9bdb3.appspot.com/o/darth-vader.mp3?alt=media&token=d17eb13b-137a-4c78-b5d0-b8766af684e4"));



            mp.setLooping(true);
            mp.seekTo(0);
            mp.setVolume(0.5f, 0.5f);
            totalTime = mp.getDuration();
            playBtnClick();
            // Position Bar
            positionBar = findViewById(R.id.positionBar);
            positionBar.setMax(totalTime);
            positionBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (fromUser) {
                                mp.seekTo(progress);
                                positionBar.setProgress(progress);

                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    }
            );


            rewind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPosition = mp.getCurrentPosition();

                    mp.seekTo(currentPosition - 10000);
                    positionBar.setProgress(currentPosition);
                }
            });

            fastForward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPosition = mp.getCurrentPosition();

                    mp.seekTo(currentPosition + 10000);
                    positionBar.setProgress(currentPosition);
                }
            });

            // Volume Bar

            volumeBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            float volumeNum = progress / 100f;
                            mp.setVolume(volumeNum, volumeNum);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    }
            );

            // Thread (Update positionBar & timeLabel)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mp != null) {
                        try {
                            Message msg = new Message();
                            msg.what = mp.getCurrentPosition();
                            handler.sendMessage(msg);
                            Thread.sleep(1000);
                            //
                            // sendNotification(mentorName, mentorTitle);
                        } catch (InterruptedException e) {}
                    }
                }
            }).start();



    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            // Update positionBar.
            positionBar.setProgress(currentPosition);

            // Update Labels.
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;


    }

    private void stopPlaying(){
        mp.pause();
        playBtn.setBackgroundResource(R.drawable.ic_play);
    }

    public void playBtnClick() {

        if (!mp.isPlaying()) {
            // Stopping

            mp.start();
            playBtn.setBackgroundResource(R.drawable.ic_pause);

        } else {
            // Playing
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.ic_play);
        }

    }
}