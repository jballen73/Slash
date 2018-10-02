package projects.jballen.slash;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static projects.jballen.slash.Constants.MAX_RED_SCORE;
import static projects.jballen.slash.Constants.RED_FAILURE_DECREASE;
import static projects.jballen.slash.Constants.REGULAR_FAILURE_DECREASE;
import static projects.jballen.slash.Constants.SUCCESS_INCREASE;

public class GameService extends Service {
    private final LocalBinder binder = new LocalBinder();
    private GameCallbackInterface callbackInterface;
    private Timer timer;
    private int barAmount;
    private ArrowAttributes currentArrow;
    private int redCount = 0;
    private boolean timerRunning = false;
    private final Random gen = new Random();
    public GameService() {
    }
    public void startGame() {
        currentArrow = new ArrowAttributes(FlingType.RIGHT, ArrowType.REGULAR);
        callbackInterface.setArrow(currentArrow);
        startTimer();
    }
    public void startTimer() {
        timer = new Timer();
        barAmount = 100;
        timer.schedule(gameTask, 0, 100);
        timerRunning = true;
    }
    public void stopTimer() {
        timer.cancel();
        gameTask.cancel();
        timerRunning = false;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public class LocalBinder extends Binder {
        public GameService getService() {
            return GameService.this;
        }
    }
    public void setCallbackInterface(GameCallbackInterface callbackInterface) {
        this.callbackInterface = callbackInterface;
    }
    public void handleFling(FlingType fling) {
        if (timerRunning) {
            if (currentArrow.getArrowType() != ArrowType.NOT && fling != FlingType.NONE) {
                if (fling == currentArrow.getCorrectDirection()) {
                    barAmount = Math.min(barAmount + SUCCESS_INCREASE, 100);
                    callbackInterface.updateProgressBar(barAmount);
                    ArrowAttributes newArrow = makeNewArrow();
                    currentArrow = newArrow;
                    callbackInterface.setArrow(newArrow);
                } else {
                    barAmount = Math.max(barAmount + REGULAR_FAILURE_DECREASE, 0);
                    callbackInterface.updateProgressBar(barAmount);
                }
            } else if (fling != FlingType.NONE) {
                barAmount = Math.max(barAmount + RED_FAILURE_DECREASE, 0);
                callbackInterface.updateProgressBar(barAmount);
            }
        }
    }
    private ArrowAttributes makeNewArrow() {
        int newDirection = gen.nextInt(8);
        int type = gen.nextInt(7);
        ArrowAttributes newArrow;
        if (type < 4) {
            newArrow = new ArrowAttributes(FlingType.getTypeFromIndex(newDirection),
                    ArrowType.REGULAR);
        } else if (type < 6) {
            newArrow = new ArrowAttributes(FlingType.getTypeFromIndex(newDirection),
                    ArrowType.REVERSE);
        } else {
            newArrow = new ArrowAttributes(FlingType.getTypeFromIndex(newDirection),
                    ArrowType.NOT);
        }
        return newArrow;
    }
    TimerTask gameTask = new TimerTask() {
        @Override
        public void run() {
            if (currentArrow.getArrowType() == ArrowType.NOT) {
                barAmount = Math.min(barAmount + 1, 100);
                redCount++;
                if (redCount >= MAX_RED_SCORE) {
                    currentArrow = makeNewArrow();
                    callbackInterface.setArrow(currentArrow);
                    redCount = 0;
                }
            } else {
                barAmount -= 1;
            }
            callbackInterface.updateProgressBar(barAmount);
        }
    };
    public enum ArrowType {
        REGULAR,
        REVERSE,
        NOT;
    }
}
