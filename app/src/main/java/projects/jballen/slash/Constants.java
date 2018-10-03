package projects.jballen.slash;

class Constants {
    static final int MINIMUM_VELOCITY = 2500;
    static final double STRAIGHT_DEADBAND = .30;
    static final double DIAGONAL_DEADBAND = .6;
    static final float BASE_ARROW_WIDTH = 70f;
    static final float BASE_ARROW_HEIGHT = 500f;
    static final float TOP_ARROW_EXTENSION = 30;
    static final float TOP_ARROW_HEIGHT = 80;
    static final float DOWN_ROTATION = 0;
    static final float DOWN_LEFT_ROTATION = 45;
    static final float LEFT_ROTATION = 90;
    static final float UP_LEFT_ROTATION = 135;
    static final float UP_ROTATION = 180;
    static final float UP_RIGHT_ROTATION = 225;
    static final float RIGHT_ROTATION = 270;
    static final float DOWN_RIGHT_ROTATION = 315;
    static final int STARTING_MAX_RED_SCORE = 10;
    static final int SUCCESS_INCREASE = 10;
    static final int REGULAR_FAILURE_DECREASE = -5;
    static final String LOG_STRING_DIRECTION = "DetectedFlingDirection";
}
