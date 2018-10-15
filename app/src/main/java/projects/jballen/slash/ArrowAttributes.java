package projects.jballen.slash;

import android.graphics.Color;

import java.util.HashSet;

public class ArrowAttributes {
    private FlingType arrowDirection;
    private HashSet<FlingType> correctDirections = new HashSet<>();
    private GameService.ArrowType arrowType;
    private int color;
    public ArrowAttributes(FlingType direction, GameService.ArrowType type, boolean isColorblind) {
        arrowDirection = direction;
        arrowType = type;
        switch(arrowType) {
            case REGULAR:
                correctDirections.add(arrowDirection);
                correctDirections.add(FlingType.getLeftAdjacent(arrowDirection));
                correctDirections.add(FlingType.getRightAdjacent(arrowDirection));
                color = Color.BLUE;
                break;
            case REVERSE:
                FlingType opposite = FlingType.getOpposite(arrowDirection);
                correctDirections.add(opposite);
                correctDirections.add(FlingType.getLeftAdjacent(opposite));
                correctDirections.add(FlingType.getRightAdjacent(opposite));
                color = Color.GREEN;
                break;
            case NOT:
                correctDirections.add(FlingType.NONE);
                color = (isColorblind) ? Color.MAGENTA : Color.RED;
                break;
        }

    }
    public FlingType getArrowDirection() {
        return arrowDirection;
    }

    public HashSet<FlingType> getCorrectDirections() {
        return correctDirections;
    }

    public GameService.ArrowType getArrowType() {
        return arrowType;
    }

    public int getColor() {
        return color;
    }
}
