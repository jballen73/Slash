package projects.jballen.slash;

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
    public static FlingType getRightAdjacent(FlingType flingType) {
        int index = flingType.ordinal() - 1;
        return (index == -1) ? DOWN_RIGHT : getTypeFromIndex(index);
    }
    public static FlingType getLeftAdjacent(FlingType flingType) {
        return getTypeFromIndex((flingType.ordinal() + 1) % 8);
    }
    public static FlingType getOpposite(FlingType flingType){
        switch(flingType) {
            case DOWN:
                return UP;
            case DOWN_LEFT:
                return UP_RIGHT;
            case LEFT:
                return RIGHT;
            case UP_LEFT:
                return DOWN_RIGHT;
            case UP:
                return DOWN;
            case UP_RIGHT:
                return DOWN_LEFT;
            case RIGHT:
                return LEFT;
            case DOWN_RIGHT:
                return UP_LEFT;
            default:
                return NONE;
        }
    }
}
