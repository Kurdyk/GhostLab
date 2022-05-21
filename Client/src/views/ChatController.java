package views;

import Apps.GameApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import utils.ChatItem;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private ListView<ChatItem> chatListView;

    @FXML
    private Button envoyerButton;

    @FXML
    private TextArea messageArea;

    private GameApp gameApp;


    /**
     * Instantiates a new Leader board controller.
     */
    public ChatController() {
    }

    /**
     * Set game app.
     *
     * @param app the app
     */
    public void setGameApp(GameApp app){
        System.out.println("GameApp knocked on CharController's door");
        this.gameApp = app;

        chatListView.setCellFactory(chatListView -> new ChatListViewCell());
        chatListView.setItems(app.getChatItems());
        chatListView.refresh();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ChatController initialized !");
        System.out.println("ChatListView is now := " + this.chatListView);
        this.messageArea.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.length() > 200 && !t1.startsWith("/p:")){
                messageArea.setText(t1.substring(0, 200));
            } else if (t1.startsWith("/p:")){
                messageArea.setText(t1.substring(0, 212));
            }
        });
//        chatListView.setMouseTransparent( true );
//        chatListView.setFocusTraversable( false );

    }

    @FXML
    private void handleSendMessage(){
        String message = this.messageArea.getText();
        this.messageArea.setText("");
        if (message.startsWith("/p:")){
            String dest = message.substring(3, 11);
            System.out.println("Destinataire : " + dest);
            this.gameApp.getPendingMessages().add(new ChatItem("Moi, à " + dest, message.substring(12)));
            this.gameApp.getConnectionHandler().getWriter().send("SEND? ")
                    .send(dest)
                    .send(" ")
                    .send(message.substring(13))
                    .end();
        }
        else {
            this.gameApp.getConnectionHandler().getWriter()
                    .send("MALL? ")
                    .send(message)
                    .end();
        }
    }

}
