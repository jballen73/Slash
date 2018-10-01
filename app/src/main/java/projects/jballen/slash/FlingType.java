package projects.jballen.slash;

import android.app.Activity;

public enum FlingType {
    UP(R.string.direction_up),
    UP_LEFT(R.string.direction_up_left),
    UP_RIGHT(R.string.direction_up_right),
    LEFT(R.string.direction_left),
    RIGHT(R.string.direction_right),
    DOWN_LEFT(R.string.direction_down_left),
    DOWN_RIGHT(R.string.direction_down_right),
    DOWN(R.string.direction_down),
    NONE(R.string.direction_none);
    private int flingTextId;
    FlingType(int flingTextId) {this.flingTextId = flingTextId;}
    public int getFlingTextId() {return flingTextId;}
}
