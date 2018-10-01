package projects.jballen.slash;

import android.app.Activity;

import static projects.jballen.slash.Constants.DOWN_LEFT_ROTATION;
import static projects.jballen.slash.Constants.DOWN_RIGHT_ROTATION;
import static projects.jballen.slash.Constants.DOWN_ROTATION;
import static projects.jballen.slash.Constants.LEFT_ROTATION;
import static projects.jballen.slash.Constants.RIGHT_ROTATION;
import static projects.jballen.slash.Constants.UP_LEFT_ROTATION;
import static projects.jballen.slash.Constants.UP_RIGHT_ROTATION;
import static projects.jballen.slash.Constants.UP_ROTATION;

public enum FlingType {
    RIGHT(R.string.direction_right, RIGHT_ROTATION),
    UP_RIGHT(R.string.direction_up_right, UP_RIGHT_ROTATION),
    UP(R.string.direction_up, UP_ROTATION),
    UP_LEFT(R.string.direction_up_left, UP_LEFT_ROTATION),
    LEFT(R.string.direction_left, LEFT_ROTATION),
    DOWN_LEFT(R.string.direction_down_left, DOWN_LEFT_ROTATION),
    DOWN(R.string.direction_down, DOWN_ROTATION),
    DOWN_RIGHT(R.string.direction_down_right, DOWN_RIGHT_ROTATION),
    NONE(R.string.direction_none, 0);
    private int flingTextId;
    private float rotateAngle;
    FlingType(int flingTextId, float rotateAngle) {
        this.flingTextId = flingTextId;
        this.rotateAngle = rotateAngle;
    }
    public int getFlingTextId() {return flingTextId;}
    public float getRotateAngle() {return rotateAngle;}
    public static FlingType getTypeFromIndex(int index) {return values()[index];}
}
