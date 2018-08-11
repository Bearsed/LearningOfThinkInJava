package GcAlgorithm;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * Created by chenyang on 2017/2/2.
 *
 *
 *
 */
public class TraceCanReliveObj {
    public static TraceCanReliveObj obj;
    static ReferenceQueue<TraceCanReliveObj> phantomQueue=null;
    public static class CheckRefQueue extends Thread{
        @Override
        public void run() {
            while (true){
                if(phantomQueue!=null){
                    PhantomReference<TraceCanReliveObj> objt=null;
                    try {
                        objt=(PhantomReference<TraceCanReliveObj>)phantomQueue.remove();
                    }catch (InterruptedException e){
                        e.fillInStackTrace();
                    }
                    if(objt!=null){
                        System.out.println("TraceCanReliveObj is delete");
                    }
                }
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("CanReliveObj finalize called");
        obj=this;
    }

    @Override
    public String toString() {
        return "I am CanReliveObj";
    }

    public static void main(String[] args) throws InterruptedException{
        Thread t=new CheckRefQueue();
        t.setDaemon(true);
        t.start();
        phantomQueue=new ReferenceQueue<TraceCanReliveObj>();
        obj=new TraceCanReliveObj();
        PhantomReference<TraceCanReliveObj> phantomRef=new PhantomReference<TraceCanReliveObj>(obj,phantomQueue);
        obj=null;
        System.gc();
        Thread.sleep(1000);
        if(obj==null){
            System.out.println("obj 是null");
        }else {
            System.out.println("obj 可用");
        }
        System.out.println("第二次gc");
        obj=null;
        System.gc();
        Thread.sleep(1000);
        if(obj==null){
            System.out.println("obj是null");
        }else {
            System.out.println("obj 可用");
        }
    }
}
