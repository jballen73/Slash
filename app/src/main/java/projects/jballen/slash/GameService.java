package projects.jballen.slash;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class GameService extends Service {
    private final LocalBinder binder = new LocalBinder();
    private GameCallbackInterface callbackInterface;
    public GameService() {
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
}
