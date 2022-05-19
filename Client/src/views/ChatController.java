package views;

import Apps.GameApp;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import utils.ChatItem;
import utils.ChatListViewCell;
import utils.LeaderBoardItem;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private ListView<ChatItem> chatListView;

    private GameApp gameApp;
    private SortedList<LeaderBoardItem> sortedList;


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
        this.gameApp = app;

        sortedList = new SortedList<>(app.getLeaderBoardItems());
        chatListView.setCellFactory(playersListView -> new ChatListViewCell());
        chatListView.setItems(app.getChatItems());

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatListView.setMouseTransparent( true );
        chatListView.setFocusTraversable( false );

    }

}
