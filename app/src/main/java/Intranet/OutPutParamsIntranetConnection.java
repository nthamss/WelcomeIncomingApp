package Intranet;




import java.util.Observable;

import Calendar.Calendars;


public class OutPutParamsIntranetConnection extends Observable{

    private Exception exception = null;
    private Calendars calendars;
    private String icsString;

    public void setChanged(){
        super.setChanged();
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.setChanged();
        this.exception = exception;
    }

    public Calendars getCalendars() {
        return calendars;
    }

    public void setCalendars(Calendars calendars) {
        this.setChanged();
        this.calendars = calendars;
    }

    public String getIcsString() {
        return icsString;
    }

    public void setIcsString(String icsString) {
        this.setChanged();
        this.icsString = icsString;
    }
}
