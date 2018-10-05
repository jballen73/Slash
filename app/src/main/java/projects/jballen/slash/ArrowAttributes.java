package projects.jballen.slash;

import android.graphics.Color;

public class ArrowAttributes {
    private FlingType arrowDirection;
    private FlingType correctDirection;
    private GameService.ArrowType arrowType;
    private int color;
    public ArrowAttributes(FlingType direction, GameService.ArrowType type, boolean isColorblind) {
        arrowDirection = direction;
        arrowType = type;
        switch(arrowType) {
            case REGULAR:
                correctDirection = arrowDirection;
                color = Color.BLUE;
                break;
            case REVERSE:
                correctDirection = FlingType.getOpposite(arrowDirection);
                color = Color.GREEN;
                break;
            case NOT:
                correctDirection = FlingType.NONE;
                color = (isColorblind) ? Color.MAGENTA : Color.RED;
                break;
        }

    }
    public FlingType getArrowDirection() {
        return arrowDirection;
    }

    public FlingType getCorrectDirection() {
        return correctDirection;
    }

    public GameService.ArrowType getArrowType() {
        return arrowType;
    }

    public int getColor() {
        return color;
    }
}
