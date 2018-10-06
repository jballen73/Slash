package projects.jballen.slash;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static projects.jballen.slash.Constants.LOG_STRING_DIRECTION;
import static projects.jballen.slash.Constants.REGULAR_FAILURE_DECREASE;
import static projects.jballen.slash.Constants.STARTING_MAX_RED_SCORE;
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
    private TimerTask currentGameTask;
    private int currentPeriod = 100;
    private int score;
    private int maxRedScore;
    private boolean isColorblind;
    public GameService() {
    }
    public void startGame() {
        isColorblind = callbackInterface.isColorblind();
        currentArrow = makeNewArrow();
        callbackInterface.setArrow(currentArrow);
        barAmount = 100;
        score = 0;
        maxRedScore = STARTING_MAX_RED_SCORE;
        callbackInterface.updateScore(score);
        timer = new Timer();
        currentGameTask = createNewGameTask();
        startTimer();
    }
    public void startTimer() {
        timer.schedule(currentGameTask, 0, 100);
        timerRunning = true;
    }
    public void reStartTimer() {
        currentGameTask.cancel();
        currentGameTask = createNewGameTask();
        timer.schedule(currentGameTask, 0 , currentPeriod);
    }
    public void stopGame() {
        stopTimer();
        callbackInterface.goToGameOver(score);
    }
    private void stopTimer() {
        timer.cancel();
        timer.purge();
        currentGameTask.cancel();
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
        Log.d(LOG_STRING_DIRECTION, fling.name());
        if (timerRunning) {
            if (currentArrow.getArrowType() != ArrowType.NOT && fling != FlingType.NONE) {
                if (fling == currentArrow.getCorrectDirection()) {
                    barAmount = Math.min(barAmount + SUCCESS_INCREASE, 100);
                    increaseScore();
                    callbackInterface.updateProgressBar(barAmount);
                    ArrowAttributes newArrow = makeNewArrow();
                    currentArrow = newArrow;
                    callbackInterface.setArrow(newArrow);
                } else {
                    barAmount = Math.max(barAmount + REGULAR_FAILURE_DECREASE, 0);
                    callbackInterface.updateProgressBar(barAmount);
                }
            } else if (fling != FlingType.NONE) {
                redCount = 0;
                barAmount = Math.max(barAmount + REGULAR_FAILURE_DECREASE, 0);
                callbackInterface.updateProgressBar(barAmount);
                ArrowAttributes newArrow = makeNewArrow();
                currentArrow = newArrow;
                callbackInterface.setArrow(newArrow);
            }
        }
    }
    private ArrowAttributes makeNewArrow() {
        int newDirection = gen.nextInt(8);
        int type = gen.nextInt(7);
        ArrowAttributes newArrow;
        if (type < 4) {
            newArrow = new ArrowAttributes(FlingType.getTypeFromIndex(newDirection),
                    ArrowType.REGULAR, isColorblind);
        } else if (type < 6) {
            newArrow = new ArrowAttributes(FlingType.getTypeFromIndex(newDirection),
                    ArrowType.REVERSE, isColorblind);
        } else {
            newArrow = new ArrowAttributes(FlingType.getTypeFromIndex(newDirection),
                    ArrowType.NOT, isColorblind);
        }
        return newArrow;
    }
    TimerTask createNewGameTask() {
        return new TimerTask() {
            @Override
            public void run() {
                if (currentArrow.getArrowType() == ArrowType.NOT) {
                    barAmount = Math.max(barAmount - 1, 0);
                    redCount++;
                    if (redCount >= maxRedScore) {
                        currentArrow = makeNewArrow();
                        callbackInterface.setArrow(currentArrow);
                        redCount = 0;
                        barAmount = Math.min(barAmount + maxRedScore + SUCCESS_INCREASE, 100);
                        increaseScore();
                    }
                } else {
                    barAmount -= 1;
                }
                callbackInterface.updateProgressBar(barAmount);
            }

        };
    }
    private void increaseScore() {
        score++;
        callbackInterface.updateScore(score);
        if (score > 0 && score % 10 == 0 && currentPeriod > 60) {
            currentPeriod -= 5;
            reStartTimer();
        }
    }

    public enum ArrowType {
        REGULAR,
        REVERSE,
        NOT;
    }
}
