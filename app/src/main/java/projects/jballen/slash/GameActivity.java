package projects.jballen.slash;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GameCallbackInterface{
    private GestureDetectorCompat mDetector;
    private GameArrow gameArrow;
    private GameService gameService;
    private boolean isBound = false;
    private boolean colorblindMode;
    private ProgressBar gameTimerBar;
    private Button startButton;
    private TextView gameScore;
    public static final String FINAL_GAME_SCORE = "endOfGameScore";

    private MediaPlayer highSuccessMediaPlayer;
    private MediaPlayer midSuccessMediaPlayer;
    private MediaPlayer lowSuccessMediaPlayer;
    private MediaPlayer failureMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mDetector = new GestureDetectorCompat(this, this);
        gameArrow = findViewById(R.id.gameArrow);
        gameTimerBar = findViewById(R.id.gameTimerBar);
        gameTimerBar.setVisibility(View.INVISIBLE);
        startButton = findViewById(R.id.gameStartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });
        gameScore = findViewById(R.id.scoreDisplay);
        colorblindMode = getIntent().getBooleanExtra(WelcomeActivity.COLORBLIND_MODE, false);
        highSuccessMediaPlayer = MediaPlayer.create(this, R.raw.slash_success_high);
        midSuccessMediaPlayer = MediaPlayer.create(this, R.raw.slash_success_mid);
        lowSuccessMediaPlayer = MediaPlayer.create(this, R.raw.slash_success_low);
        failureMediaPlayer = MediaPlayer.create(this, R.raw.slash_failure);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent gameIntent = new Intent(this, GameService.class);
        bindService(gameIntent, gameServiceConnection, BIND_AUTO_CREATE);


    }
    @Override
    protected void onStop() {
        super.onStop();
        highSuccessMediaPlayer.release();
        midSuccessMediaPlayer.release();
        lowSuccessMediaPlayer.release();
        failureMediaPlayer.release();
        highSuccessMediaPlayer = null;
        midSuccessMediaPlayer = null;
        lowSuccessMediaPlayer = null;
        failureMediaPlayer = null;
        unbindService(gameServiceConnection);
        isBound = false;
    }
    @Override
    public void updateProgressBar(int newValue) {
        gameTimerBar.setProgress(newValue);
        if (newValue == 0) {
            gameService.stopGame();
        }
    }
    @Override
    public void goToGameOver(int finalScore) {
        Intent goToGameOverIntent = new Intent(this, GameOverActivity.class);
        goToGameOverIntent.putExtra(FINAL_GAME_SCORE, finalScore);
        startActivity(goToGameOverIntent);
    }
    @Override
    public boolean isColorblind() {
        return colorblindMode;
    }
    @Override
    public void playSuccessSound(GameService.ArrowType newType) {
        switch(newType) {
            case REGULAR:
                midSuccessMediaPlayer.start();
                break;
            case REVERSE:
                highSuccessMediaPlayer.start();
                break;
            case NOT:
                lowSuccessMediaPlayer.start();
                break;
        }
    }
    @Override
    public void playFailureSound() {
        failureMediaPlayer.start();
    }
    private ServiceConnection gameServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            GameService.LocalBinder binder = (GameService.LocalBinder) iBinder;
            gameService = binder.getService();
            isBound = true;
            gameService.setCallbackInterface(GameActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };
    private void startGame() {
        startButton.setVisibility(View.GONE);
        gameTimerBar.setVisibility(View.VISIBLE);
        gameArrow.setVisibility(View.VISIBLE);
        gameScore.setVisibility(View.VISIBLE);
        gameService.startGame();
    }
    @Override
    public void updateScore(final int newValue) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameScore.setText(getString(R.string.game_score, newValue));
            }
        });
    }
    @Override
    public void setArrow(final ArrowAttributes attributes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameArrow.setDirection(attributes.getArrowDirection().ordinal());
                gameArrow.setColor(attributes.getColor());
                gameArrow.invalidate();
            }
        });

    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mDetector.onTouchEvent(motionEvent)) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        FlingType flingType = (FlingInterpreter.getFlingType(v, v1));
        gameService.handleFling(flingType);
        return true;
    }
}
