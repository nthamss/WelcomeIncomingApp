package Intranet;

import android.util.Log;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.net.ssl.HttpsURLConnection;

import Calendar.Agenda;
import Calendar.Calendars;


public class IntranetConnection implements Observer {

    private String user;
    private String pass;
    private List<String> cookieList;

    private OutPutParamsIntranetConnection outPutParamsIntranetConnection;

    public IntranetConnection(String user, String pass, Observer observer) {
        this.user = user;
        this.pass = pass;
        outPutParamsIntranetConnection = new OutPutParamsIntranetConnection();
        outPutParamsIntranetConnection.addObserver(observer);
        CookieHandler.setDefault(new CookieManager());
    }

    public void connect(){

        Log.d(((Object) this).getClass().getName(), " -> call connect");

        if(this.outPutParamsIntranetConnection.getException()!=null)
            return ;

        IntranetLogin login = new IntranetLogin(this);
        login.execute(this.user,this.pass);
    }

    public void getCalendars(){

        Log.d(((Object) this).getClass().getName(), " -> call calendars");

        if(this.outPutParamsIntranetConnection.getException()!=null)
            return ;

        IntranetCalendars calendars = new IntranetCalendars(this);
        calendars.execute(this.cookieList);
    }

    public void getICS(Agenda agenda){

        Log.d(((Object) this).getClass().getName(), " -> call ics!");

        if(this.outPutParamsIntranetConnection.getException()!=null)
            return ;


        IntranetICS Ics = new IntranetICS(this, agenda);
        Ics.execute(this.cookieList);
    }

    private Exception processCode(int i) {

        Exception returnException;

        switch (i) {
            case HttpsURLConnection.HTTP_FORBIDDEN:
                returnException = new Exception("Acceso prohibido");
                break;

            case HttpsURLConnection.HTTP_UNAUTHORIZED:
                returnException = new Exception("Usuario o contraseña erroneos");
                break;

            case HttpsURLConnection.HTTP_NOT_FOUND:
                returnException = new Exception("Recurso no encontrado");
                break;

            case HttpsURLConnection.HTTP_CLIENT_TIMEOUT:
                returnException = new Exception("Tiempo de espera agotado");
                break;

            case HttpsURLConnection.HTTP_BAD_REQUEST:
                returnException =  new Exception("Petición incorrecta");
                break;

            case HttpsURLConnection.HTTP_MOVED_PERM:
            case HttpsURLConnection.HTTP_MOVED_TEMP:
                returnException =  new Exception("Han respondido una redirección");
                break;

            case HttpsURLConnection.HTTP_OK:
            case HttpsURLConnection.HTTP_ACCEPTED:
                returnException = null;
                break;

            default:
                returnException =  new Exception("Error desconocido");
        }

        return returnException;
    }

    @Override
    public void update(Observable observable, Object data) {

        InputParamsIntranetConnection inputParamsIntranetConnection = (InputParamsIntranetConnection) observable;

        if (inputParamsIntranetConnection.getException() != null || this.processCode(inputParamsIntranetConnection.getCodeResponse())!=null) {
            //error....

            this.outPutParamsIntranetConnection.setException(this.processCode(inputParamsIntranetConnection.getCodeResponse()));
            Log.d(((Object) this).getClass().getName(), "Update -> error -> " + this.processCode(inputParamsIntranetConnection.getCodeResponse()).getMessage());
            Log.d(((Object) this).getClass().getName(), "Exception: ", inputParamsIntranetConnection.getException());
            return;
        }

        this.cookieList = inputParamsIntranetConnection.getCookieList();

        if(data.equals("login")) {
            //login...

                Log.d(((Object) this).getClass().getName(), "Update -> Login -> connected");
                this.outPutParamsIntranetConnection.setChanged();
                this.outPutParamsIntranetConnection.notifyObservers("login");

        }else if (data.equals("calendars")){
            //calendars...

                Log.d( ((Object)this).getClass().getName(), "Update -> Calendars -> ok");

                this.outPutParamsIntranetConnection.setCalendars(new Calendars(inputParamsIntranetConnection.getResult()));
                this.outPutParamsIntranetConnection.notifyObservers("calendars");

        }else if (data.equals("ics")){
            //ics...

            Log.d( ((Object)this).getClass().getName(), "Update -> ics -> ok");

            this.outPutParamsIntranetConnection.setIcsString(inputParamsIntranetConnection.getResult());
            this.outPutParamsIntranetConnection.notifyObservers("ics");

        }else{

            Log.w( ((Object)this).getClass().getName(), "Yo no tengo que salir!");
        }


    }
}
