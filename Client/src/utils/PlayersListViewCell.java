package utils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * The type Players list view cell.
 */
public class PlayersListViewCell extends ListCell<LeaderBoardItem> {
    @FXML
    private Label usernameLabel;

    @FXML
    private Label rankLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private GridPane gridPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label estMort;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(LeaderBoardItem leaderBoardItem, boolean empty) {
        super.updateItem(leaderBoardItem, empty);

        if (empty || leaderBoardItem == null){
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null){
                mLLoader = new FXMLLoader(getClass().getResource("/views/leaderBoardListCell.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e1){
                    e1.printStackTrace();
                }
            }
            usernameLabel.setText(leaderBoardItem.getUsername());
            rankLabel.setText(leaderBoardItem.getRank());
            scoreLabel.setText(String.valueOf(leaderBoardItem.getScore()));
            if(leaderBoardItem.isAlive()){
                estMort.setVisible(false);
                usernameLabel.setDisable(false);
                rankLabel.setDisable(false);
                scoreLabel.setDisable(false);
            } else {
                estMort.setVisible(true);
                usernameLabel.setDisable(false);
                rankLabel.setDisable(true);
                scoreLabel.setDisable(true);
            }

            setText(null);
            setGraphic(anchorPane);
        }
    }
}
