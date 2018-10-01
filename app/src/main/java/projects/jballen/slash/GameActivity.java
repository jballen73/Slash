package projects.jballen.slash;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{
    private GestureDetectorCompat mDetector;
    private TextView testTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        testTextView = findViewById(R.id.gameTestText);
        mDetector = new GestureDetectorCompat(this, this);
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
        testTextView.setText(getString(FlingInterpreter.getFlingType(v, v1).getFlingTextId()));
        return true;
    }
}
