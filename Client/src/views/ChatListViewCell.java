package views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import utils.ChatItem;

import java.io.IOException;

/**
 * The type Players list view cell.
 */
public class ChatListViewCell extends ListCell<ChatItem> {
    @FXML
    private Label fromAndToLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private GridPane gridPane;

    @FXML
    private AnchorPane anchorPane;


    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(ChatItem chatItem, boolean empty) {
        super.updateItem(chatItem, empty);

        if (empty || chatItem == null){
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null){
                mLLoader = new FXMLLoader(getClass().getResource("/views/chatListCell.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e1){
                    e1.printStackTrace();
                }
            }
            System.out.println("Le loader = " + mLLoader);
            fromAndToLabel.setText(chatItem.getFromAndTo());
            messageLabel.setText(chatItem.getMessage());
            System.out.println("On affecte les valeurs = " + chatItem.getFromAndTo() + " et " + chatItem.getMessage());


            anchorPane.setFocusTraversable(false);
            anchorPane.setMouseTransparent(true);
            setText(null);
            setGraphic(anchorPane);
        }
    }

}
