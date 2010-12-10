import java.util.concurrent.SynchronousQueue;

/**
 * User: joeo
 * Date: 12/2/10
 * Time: 6:07 AM
 * <p/>
 * Copyright
 */
public class synctest {
    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<String> queue=new SynchronousQueue<String>();
        queue.put("foo");
    }
}
