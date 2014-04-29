package Intranet;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Observer;

import Calendar.Agenda;


public class IntranetICS extends AsyncTask<List<String>, Void, InputParamsIntranetConnection> {

    private Agenda agenda;
    private InputParamsIntranetConnection inputParamsIntranetConnection;
    private Observer observer;

    public IntranetICS(Observer observer, Agenda agenda){
        this.agenda=agenda;
        this.observer = observer;
    }

    @Override
    protected void onPreExecute(){
        this.inputParamsIntranetConnection = new InputParamsIntranetConnection();
        this.inputParamsIntranetConnection.addObserver(this.observer);
    }

    @Override
    protected InputParamsIntranetConnection doInBackground(List<String>... params) {

        try {

            URL url = new URL(this.agenda.getUrl());
            HttpURLConnection request = (HttpURLConnection) url.openConnection();


            request.setUseCaches(false);
            request.setRequestMethod("GET");

            request.setInstanceFollowRedirects(true);

            request.setRequestProperty("User-Agent","Mozilla/5.0");
            request.setRequestProperty("Host", "www.upv.es");

            for (String cookie : params[0]) {
                request.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }

            request.setRequestProperty("Referer", "http://www.upv.es");

            request.setDoOutput(true);
            request.setDoInput(true);

            inputParamsIntranetConnection.setCodeResponse(request.getResponseCode());

            if(request.getHeaderFields().get("Set-Cookie")!=null){
                params[0].addAll(request.getHeaderFields().get("Set-Cookie"));
            }

            inputParamsIntranetConnection.setCookieList(params[0]);


            BufferedReader in =
                    new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringWriter sw = new StringWriter();
            char[] buffer = new char[1024 * 4];
            int n = 0;
            while (-1 != (n = in.read(buffer))) {
                sw.write(buffer, 0, n);

            }
            this.inputParamsIntranetConnection.setResult(sw.toString());

            Log.d( ((Object)this).getClass().getName(), "-> " + sw.toString());

            //content debug...
            Log.d( ((Object)this).getClass().getName(), "Code response: " + this.inputParamsIntranetConnection.getCodeResponse());

        } catch (Exception e) {
            inputParamsIntranetConnection.setException(e);
        }

        return inputParamsIntranetConnection;
    }

    @Override
    protected void onPostExecute(InputParamsIntranetConnection inputParamsIntranetConnection){
        inputParamsIntranetConnection.notifyObservers("ics");
    }

}