package hpi.com.hpifitness.utils;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Georgey on 26-01-2017.
 */

public class CustomeAsyncTask extends AsyncTask<Void, Void, Void> {

    //  private ProgressDialog pd;
    private Context context;
    // private boolean showProgress = true;
    //  private String progressText = "Processing...";

    public CustomeAsyncTask(Context context) {
        this.context = context;
    }

    public CustomeAsyncTask(Context context, boolean showProgress) {
        this.context = context;
        //   this.showProgress = showProgress;
    }

    public CustomeAsyncTask(Context context, String msg) {
        this.context = context;
      /*  if (msg != null) {
            this.progressText = msg;
        }*/
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //    if (showProgress) {
        //      try {
             /*   pd = new ProgressDialog(context);
                pd.setMessage(progressText);
                pd.setCancelable(false);
                pd.show();*/
        //    } catch (Exception ex) {
        //  }
        //}

        preExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            doInBackground();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        postExecute();
       /* if (showProgress && pd.isShowing()) {
            pd.dismiss();
        }*/
    }

    // overridden methods

    public void doInBackground() {

    }

    public void postExecute() {

    }

    public void preExecute() {

    }
}
