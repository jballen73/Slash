package projects.jballen.slash;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
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
    private Timer fadeOutTimer;
    private TimerTask fadeOutTimerTask;
    private boolean isFading;
    private int alpha;
    private int arrowTypeNum;
    private boolean gameRunning = false;
    public GameService() {
    }
    public void startGame() {
        isColorblind = callbackInterface.isColorblind();
        arrowTypeNum = 4;
        currentArrow = makeNewArrow();
        callbackInterface.setFirstArrow(currentArrow);
        barAmount = 100;
        score = 0;
        alpha = 255;
        maxRedScore = STARTING_MAX_RED_SCORE;
        callbackInterface.updateScore(score);
        timer = new Timer();
        fadeOutTimer = new Timer();
        currentGameTask = createNewGameTask();
        isFading = false;
        gameRunning = true;
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
        gameRunning = false;
        stopFadeOut();
        fadeOutTimer.cancel();
        fadeOutTimer.purge();
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
                    callbackInterface.setBorder(Color.YELLOW);
                    currentArrow = makeNewArrow();
                    callbackInterface.setArrow(currentArrow);
                    callbackInterface.playSuccessSound(currentArrow.getArrowType());
                } else {
                    barAmount = Math.max(barAmount + REGULAR_FAILURE_DECREASE, 0);
                    callbackInterface.updateProgressBar(barAmount);
                    callbackInterface.setBorder(Color.BLACK);
                    currentArrow = makeNewArrow();
                    callbackInterface.setArrow(currentArrow);
                    callbackInterface.playFailureSound();
                }
            } else if (fling != FlingType.NONE) {
                redCount = 0;
                barAmount = Math.max(barAmount + REGULAR_FAILURE_DECREASE, 0);
                callbackInterface.updateProgressBar(barAmount);
                callbackInterface.setBorder(Color.BLACK);
                ArrowAttributes newArrow = makeNewArrow();
                currentArrow = newArrow;
                callbackInterface.setArrow(newArrow);
                callbackInterface.playFailureSound();
            }
        }
    }
    private ArrowAttributes makeNewArrow() {
        alpha = 255;
        int newDirection = gen.nextInt(8);
        Log.d("CURRENT_RANDOM_BOUND", Integer.toString(arrowTypeNum));
        int type = gen.nextInt(arrowTypeNum);
        ArrowAttributes newArrow;
        if (type < 4) {
            newArrow = new ArrowAttributes(FlingType.getTypeFromIndex(newDirection),
                    ArrowType.REGULAR, isColorblind);
        } else if (type < 6) {
            newArrow = new ArrowAttributes(FlingType.getTypeFromIndex(newDirection),
                    ArrowType.REVERSE, isColorblind);
        } else {
            if (barAmount > maxRedScore + 5) {
                newArrow = new ArrowAttributes(FlingType.getTypeFromIndex(newDirection),
                        ArrowType.NOT, isColorblind);
            } else {
                newArrow = new ArrowAttributes(FlingType.getTypeFromIndex(newDirection),
                        ArrowType.REGULAR, isColorblind);
            }
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
                        callbackInterface.setBorder(Color.YELLOW);
                        currentArrow = makeNewArrow();
                        callbackInterface.setArrow(currentArrow);
                        redCount = 0;
                        barAmount = Math.min(barAmount + maxRedScore + SUCCESS_INCREASE, 100);
                        increaseScore();
                        callbackInterface.playSuccessSound(currentArrow.getArrowType());
                    }
                } else {
                    barAmount -= 1;
                }
                callbackInterface.updateProgressBar(barAmount);
            }

        };
    }
    TimerTask createNewFadeOutTask() {
        return new TimerTask() {
            @Override
            public void run() {
                alpha -= 5;
                callbackInterface.updateAlpha(alpha);
            }
        };
    }
    public void fadeOut() {
        if (!gameRunning) {return;}
        fadeOutTimerTask = createNewFadeOutTask();
        fadeOutTimer.schedule(fadeOutTimerTask, 0, 1);
        isFading = true;
    }
    public void stopFadeOut() {
        if (isFading) {
            fadeOutTimerTask.cancel();
            isFading = false;
        }
    }
    private void increaseScore() {
        score++;
        callbackInterface.updateScore(score);
        if (score > 0 && score % 10 == 0 && currentPeriod > 60) {
            currentPeriod -= 5;
            reStartTimer();
            if (arrowTypeNum < 7) {
                arrowTypeNum++;
            }
        }
    }

    public enum ArrowType {
        REGULAR,
        REVERSE,
        NOT
    }
}
