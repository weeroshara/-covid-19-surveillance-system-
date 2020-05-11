package control;

import DB.DbConnection;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DashBord_Control {
    public ImageView globalImage;
    public ImageView centerImage;
    public ImageView userImage;
    public ImageView hospitalImage;


    public Label welcomeLable;
    public Label globleLable;
    public Label centerLable;
    public Label hospitalLable;
    public Label userLable;
    public AnchorPane root;
    public JFXButton signOut;


    public void initialize() throws NoSuchAlgorithmException {
        globleLable.setVisible(false);
        centerLable.setVisible(false);
        hospitalLable.setVisible(false);
        userLable.setVisible(false);

        ArrayList<String> userDetail = Login_Control.userDetail;
        System.out.println(userDetail.get(0)+" is role ");

        if (userDetail.get(0).equals("Admin") || userDetail.get(0).equals("P.S.T.F Member")){

            System.out.println("Admin or pstf");
        }else if (userDetail.get(0).contains("-C")){

            System.out.println("center");
            hospitalImage.setDisable(true);
            userImage.setDisable(true);

        }else if (userDetail.get(0).contains("-H")){

            System.out.println("hospital");
            userImage.setDisable(true);
            centerImage.setDisable(true);
        }



    }

    public void globalImageOnAction(MouseEvent mouseEvent) throws IOException {
        Parent root= FXMLLoader.load(this.getClass().getResource("/view/GlobalCovid.fxml"));
        Stage stage=(Stage) (this.root.getScene().getWindow());
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void globalImageOnMouseEnter(MouseEvent mouseEvent) {
        globalImage.setCursor(Cursor.HAND);

        welcomeLable.setVisible(false);
        centerLable.setVisible(false);
        hospitalLable.setVisible(false);
        userLable.setVisible(false);
        globleLable.setVisible(true);

    }

    public void globalImageOnMouseExit(MouseEvent mouseEvent) {
        welcomeLable.setVisible(true);
        globleLable.setVisible(false);
        hospitalLable.setVisible(false);
        centerLable.setVisible(false);
        userLable.setVisible(false);
    }



    public void centerImageOnAction(MouseEvent mouseEvent) throws IOException {
        Parent root= FXMLLoader.load(this.getClass().getResource("/view/ManageCenter.fxml"));
        Stage stage=(Stage) (this.root.getScene().getWindow());
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }

    public void centerImageOnMouseEnter(MouseEvent mouseEvent) {
        centerImage.setCursor(Cursor.HAND);

        welcomeLable.setVisible(false);
        globleLable.setVisible(false);
        hospitalLable.setVisible(false);
        userLable.setVisible(false);
        centerLable.setVisible(true);
    }

    public void centerImageOnMouseExit(MouseEvent mouseEvent) {
        welcomeLable.setVisible(true);
        globleLable.setVisible(false);
        hospitalLable.setVisible(false);
        centerLable.setVisible(false);
        userLable.setVisible(false);
    }




    public void userImageOnAction(MouseEvent mouseEvent) throws IOException {
        Parent root= FXMLLoader.load(this.getClass().getResource("/view/UserManage.fxml"));
        Stage stage=(Stage) (this.root.getScene().getWindow());
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void userImageOnMouseEnter(MouseEvent mouseEvent) {
        userImage.setCursor(Cursor.HAND);

        welcomeLable.setVisible(false);
        globleLable.setVisible(false);
        hospitalLable.setVisible(false);
        centerLable.setVisible(false);
        userLable.setVisible(true);
    }

    public void userImageOnMouseExit(MouseEvent mouseEvent) {
        welcomeLable.setVisible(true);
        globleLable.setVisible(false);
        hospitalLable.setVisible(false);
        centerLable.setVisible(false);
        userLable.setVisible(false);
    }




    public void hospitalImageOnAction(MouseEvent mouseEvent) throws IOException {
        Parent root= FXMLLoader.load(this.getClass().getResource("/view/ManageHospital.fxml"));
        Stage stage=(Stage) (this.root.getScene().getWindow());
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void hospitalImageOnMouseEnter(MouseEvent mouseEvent) {
        hospitalImage.setCursor(Cursor.HAND);

        welcomeLable.setVisible(false);
        globleLable.setVisible(false);
        centerLable.setVisible(false);
        userLable.setVisible(false);
        hospitalLable.setVisible(true);
    }

    public void hospitalImageOnMouseExit(MouseEvent mouseEvent) {
        welcomeLable.setVisible(true);
        globleLable.setVisible(false);
        hospitalLable.setVisible(false);
        centerLable.setVisible(false);
        userLable.setVisible(false);
    }


    public void signOutOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/Login.fxml"));
        Stage stage = (Stage) (this.root.getScene().getWindow());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
