package utils;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;

public class ChatItem {
    private final StringProperty fromAndTo;
    private final StringProperty message;


    public ChatItem(String fromAndTo, String message) {
        this.fromAndTo = new SimpleStringProperty(fromAndTo);
        this.message = new SimpleStringProperty(message);
    }

    public ChatItem(){
        this(null, null);
    }


    public static Callback<ChatItem, Observable[]> extractor() {
        return param -> new Observable[]{param.fromAndTo, param.message};
    }

    public String getFromAndTo() {
        return fromAndTo.get();
    }

    public StringProperty fromAndToProperty() {
        return fromAndTo;
    }

    public void setFromAndTo(String fromAndTo) {
        this.fromAndTo.set(fromAndTo);
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }
}
