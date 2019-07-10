package app.GUI;

import app.AppContext;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Innokentiy on 27.06.2019.
 */
public class GUIService implements Runnable {
    private AppContext context;
    private CountDownLatch countDownLatch;
    public GUIService(AppContext context, int count){
        this.context=context;
        countDownLatch = new CountDownLatch(count);

    }
    @Override
    public void run() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        context.getMainFrame().refreshTree();
    }
    public void decrLatch(){
        countDownLatch.countDown();
    }
}
