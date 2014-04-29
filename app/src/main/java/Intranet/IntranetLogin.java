package Intranet;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by juanan on 21/04/14.
 */
public class IntranetLogin extends AsyncTask<String, Void, InputParamsIntranetConnection> {

    private final String URL = "https://www.upv.es/exp/aute_intranet";
    private List<String> cookieList;
    private InputParamsIntranetConnection inputParamsIntranetConnection;
    private Observer observer;

    public IntranetLogin(Observer observer){
        this.cookieList = new ArrayList<String>();
        this.observer = observer;
    }

    @Override
    protected void onPreExecute(){

        this.inputParamsIntranetConnection = new InputParamsIntranetConnection();
        this.inputParamsIntranetConnection.addObserver(this.observer);

        this.cookieList.add("UPV_IDIOMA=es");
        this.cookieList.add("T=foto1");
    }

    @Override
    protected InputParamsIntranetConnection doInBackground(String... params) {

        Log.d(((Object) this).getClass().getName(), "Conectando con: " + params[0] + ", " + params[1]);

        String urlPath = "https://www.upv.es/exp/aute_intranet";
        String data = "id=c&estilo=500&vista=&param=&cua=miupv&dni=" + params[0]
                + "&clau=" + params[1];

        try {

            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory
                    .getDefault();

            java.net.URL url = new URL(urlPath);
            HttpsURLConnection request = (HttpsURLConnection) url.openConnection();

            request.setSSLSocketFactory(sslsocketfactory);

            request.setUseCaches(false);
            request.setRequestMethod("POST");

            request.setFollowRedirects(false);
            request.setInstanceFollowRedirects(false);

            request.setRequestProperty("User-Agent","Mozilla/5.0");
            request.setRequestProperty("Host", "www.upv.es");

            for (String cookie : this.cookieList) {
                request.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }

            request.setRequestProperty("Referer", "http://www.upv.es");
            request.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            request.setRequestProperty("Content-Length", Integer.toString(data.length()));

            request.setDoOutput(true);
            request.setDoInput(true);

            DataOutputStream wr = new DataOutputStream(request.getOutputStream());
            wr.writeBytes(data);
            wr.flush();
            wr.close();

            inputParamsIntranetConnection.setCodeResponse(request.getResponseCode());

            /*
                debug
             */

            /*
            if(outPutParams.getException()==null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Log.d( ((Object)this).getClass().getName(), response.toString());
            }else{
                BufferedReader in = new BufferedReader(new InputStreamReader(request.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Log.d( ((Object)this).getClass().getName(), response.toString());
            }
            */

            if(request.getHeaderFields().get("Set-Cookie")!=null){

                //tenemos la cookie! pero responden 302
                this.inputParamsIntranetConnection.setCodeResponse(200);

                cookieList.addAll(request.getHeaderFields().get("Set-Cookie"));

            }else if(this.inputParamsIntranetConnection.getCodeResponse()==302){

                //al poner un usuario y contranya no validos deberia responder con http 401 pero responde con 302 que te manda a un recurso que responde 200
                //una forma de comprobar si el login es correcto es ver si tenemos la cookie de session

                this.inputParamsIntranetConnection.setCodeResponse(401);
            }

            //content debug...
            Log.d( ((Object)this).getClass().getName(), "Code response: " + this.inputParamsIntranetConnection.getCodeResponse());

            for(String item : this.cookieList)
                Log.d( ((Object)this).getClass().getName(), "Cookie: " + item);
            //end content debug


        } catch (Exception e) {
            inputParamsIntranetConnection.setException(e);
        }

        return inputParamsIntranetConnection;
    }

    @Override
    protected void onPostExecute(InputParamsIntranetConnection InputParamsIntranetConnection){
        InputParamsIntranetConnection.setCookieList(this.cookieList);
        InputParamsIntranetConnection.notifyObservers("login");
    }

}
