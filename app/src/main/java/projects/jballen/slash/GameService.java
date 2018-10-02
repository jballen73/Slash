package projects.jballen.slash;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class GameService extends Service {
    private final LocalBinder binder = new LocalBinder();
    private GameCallbackInterface callbackInterface;
    private Timer timer;
    private int barAmount;
    public GameService() {
    }
    public void startTimer() {
        timer = new Timer();
        barAmount = 100;
        timer.schedule(gameTask, 0, 100);
    }
    public void stopTimer() {
        timer.cancel();
        gameTask.cancel();
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
    TimerTask gameTask = new TimerTask() {
        @Override
        public void run() {
            barAmount -= 1;
            callbackInterface.updateProgressBar(barAmount);
        }
    };
}
