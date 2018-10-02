package projects.jballen.slash;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

public class GameActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GameCallbackInterface{
    private GestureDetectorCompat mDetector;
    private GameArrow gameArrow;
    private GameService gameService;
    private boolean isBound = false;
    private ProgressBar gameTimerBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mDetector = new GestureDetectorCompat(this, this);
        gameArrow = findViewById(R.id.gameArrow);
        gameTimerBar = findViewById(R.id.gameTimerBar);
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
        unbindService(gameServiceConnection);
        isBound = false;
    }
    @Override
    public void updateProgressBar(int newValue) {
        gameTimerBar.setProgress(newValue);
        if (newValue == 0) {
            gameService.stopTimer();
        }
    }
    private ServiceConnection gameServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            GameService.LocalBinder binder = (GameService.LocalBinder) iBinder;
            gameService = binder.getService();
            isBound = true;
            gameService.setCallbackInterface(GameActivity.this);
            gameService.startTimer();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };
    @Override
    public void setArrow(int direction) {

    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mDetector.onTouchEvent(motionEvent)) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }
    private int i = 0;
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
        if (flingType == FlingType.NONE) {
            gameArrow.setVisibility(View.INVISIBLE);
        } else {
            gameArrow.setVisibility(View.VISIBLE);
            gameArrow.setDirection(flingType.ordinal());
            gameArrow.invalidate();
        }

        return true;
    }
}
