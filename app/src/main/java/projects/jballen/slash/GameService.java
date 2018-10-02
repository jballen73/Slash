package projects.jballen.slash;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameService extends Service {
    private final LocalBinder binder = new LocalBinder();
    private GameCallbackInterface callbackInterface;
    private Timer timer;
    private int barAmount;
    private FlingType currentArrowDirection;
    private boolean timerRunning = false;
    private final Random gen = new Random();
    public GameService() {
    }
    public void startGame() {
        currentArrowDirection = FlingType.RIGHT;
        callbackInterface.setArrow(currentArrowDirection.ordinal());
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
            if (fling != FlingType.NONE) {
                if (fling == currentArrowDirection) {
                    barAmount = Math.min(barAmount + 10, 100);
                    callbackInterface.updateProgressBar(barAmount);
                    int newDirection = gen.nextInt(8);
                    currentArrowDirection = FlingType.getTypeFromIndex(newDirection);
                    callbackInterface.setArrow(newDirection);
                } else {
                    barAmount = Math.max(barAmount - 5, 0);
                    callbackInterface.updateProgressBar(barAmount);
                }
            }
        }
    }
    TimerTask gameTask = new TimerTask() {
        @Override
        public void run() {
            barAmount -= 1;
            callbackInterface.updateProgressBar(barAmount);
        }
    };
}
