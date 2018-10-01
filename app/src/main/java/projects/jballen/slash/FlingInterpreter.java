package projects.jballen.slash;

import static projects.jballen.slash.Constants.DIAGONAL_DEADBAND;
import static projects.jballen.slash.Constants.STRAIGHT_DEADBAND;
import static projects.jballen.slash.Constants.MINIMUM_VELOCITY;

public class FlingInterpreter {
    public static FlingType getFlingType(float x_velocity, float y_velocity) {
        if (Math.abs(x_velocity) > MINIMUM_VELOCITY) {
            if (x_velocity > 0) {
                if (Math.abs(x_velocity) * STRAIGHT_DEADBAND > Math.abs(y_velocity)) {
                    return FlingType.RIGHT;
                } else if (Math.abs(x_velocity) * DIAGONAL_DEADBAND < Math.abs(y_velocity)) {
                    if (y_velocity < 0) {
                        return FlingType.UP_RIGHT;
                    } else {
                        return FlingType.DOWN_RIGHT;
                    }
                }
            } else {
                if (Math.abs(x_velocity) * STRAIGHT_DEADBAND > Math.abs(y_velocity)) {
                    return FlingType.LEFT;
                } else if (Math.abs(x_velocity) * DIAGONAL_DEADBAND < Math.abs(y_velocity)) {
                    if (y_velocity < 0) {
                        return FlingType.UP_LEFT;
                    } else {
                        return FlingType.DOWN_LEFT;
                    }
                }
            }
        } else if (Math.abs(y_velocity) > MINIMUM_VELOCITY) {
            if (Math.abs(y_velocity) * STRAIGHT_DEADBAND > Math.abs(x_velocity)) {
                if (y_velocity < 0) {
                    return FlingType.UP;
                } else {
                    return FlingType.DOWN;
                }
            }

        }
        return FlingType.NONE;
    }
}
