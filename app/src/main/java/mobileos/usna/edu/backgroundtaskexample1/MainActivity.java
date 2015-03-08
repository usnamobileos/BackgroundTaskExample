package mobileos.usna.edu.backgroundtaskexample1;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {
    
    private final String LOG_TAG = "PEPIN";
    
    private TextView outputText;

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            outputText.setText("What: " + msg.what);

            Bundle bundle = msg.getData();
            String receivedString = bundle.getString("myID", null);
            if(receivedString != null) {
                outputText.append("\nID: " + receivedString);
            }
            
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputText = (TextView) findViewById(R.id.outputText);
    }

    public void runTask(View v){

        // VERSION 1
        // Update the TextView using a background Thread
//        Thread thread = new Thread(){
//            public void run( ) {
//
//                Log.i(LOG_TAG, "Thread Started");
//
//                try {
//                    for(int i = 0; i < 5; i++){
//                        // This will cause the app to throw a CalledFromWrongThreadException
////                        outputText.setText("Current Count = " + i);
//
//                        // Let the Handler take care of updating the TextView
//                        mHandler.obtainMessage(i).sendToTarget();
//
//                        Thread.sleep(2000); //pause for 2 seconds
//
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                Log.i(LOG_TAG, "Thread Finished");
//
//            }
//        };
//        thread.start();


        // VERSION 2
        // Update the TextView using a Runnable
//        Thread thread = new Thread( new Runnable(){
//            public void run( ) {
//
//                Log.i(LOG_TAG, "Runnable Started");
//
//                try {
//                    for(int i = 0; i < 5; i++){
//
//                        UUID id = UUID.randomUUID();
//
//                        // Let the Handler take care of updating the TextView
//
//                        // this time we'll send something in a bundle, like we did with Intents
//                        Message msg = mHandler.obtainMessage(i);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("myID", id.toString());
//                        msg.setData(bundle);
//
//                        mHandler.sendMessage(msg);
//
//                        Thread.sleep(2000); //pause for 2 seconds
//
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                Log.i(LOG_TAG, "Runnable Finished");
//
//            }
//        });
//        thread.start();


        // VERSION 3
        // Update the TextView using a TimerTask
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask(){
//            public void run( ) {
//                
//                Log.i(LOG_TAG, "TimerTask Started");
//
//                for (int i = 0; i < 5; i++){
//                    try {
//                        mHandler.obtainMessage(i).sendToTarget();
//                        Thread.sleep(2000); //pause for 2 seconds
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                Log.i(LOG_TAG, "TimerTask Finished");
//            }
//        }, 2000); //2 sec delay before calling the run method
        

        // VERSION 4
        // Update the TextView using an AsyncTask
        new MyAsyncTask().execute();   
        
    }    
    

    public class MyAsyncTask extends AsyncTask<String, Integer, Long> {
        
        @Override
        protected void onPreExecute(){
            // do work in UI thread before doInBackground() runs
            outputText.setText("Starting Task...");
        }


        @Override
        protected Long doInBackground(String... params) {
            // do work in background thread

            Long someValue = 1775L;

            int progress = 0;

            while(progress < 100){
                try {
                    Thread.sleep(100); //pause for 100 milliseconds
                    
                    // push updates to UI thread
                    publishProgress(progress);

                    // check to see if the task has been cancelled
                    // using taskName.cancel(bool) from UI thread
                    // cancel(true) = task is stopped before completion
                    // cancel(false) = task is allowed to complete
                    if(isCancelled()){
                        // handle call to cancel
                        progress = 100;
                    }
                    
                    progress++;
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return someValue;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // update UI thread according to progress,
            // e.g. update a ProgressBar display
            outputText.setText("Progress: " + progress[0]);
        }

        @Override
        protected void onPostExecute(Long result) {
            // do work in UI thread after doInBackground() completes
            outputText.setText("Task Result: " + result);
        }
        
        @Override
        protected void onCancelled (Long result){
            // executed on UI thread
            // called instead of onPostExecute() after doInBackground() 
            // is complete if cancel() is called on the AsyncTask
            outputText.setText("Task Cancelled");
        }
    }

}
