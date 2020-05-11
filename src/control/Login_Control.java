package control;

import DB.DbConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Login_Control {

    public AnchorPane root;

    static ArrayList<String> userDetail = new ArrayList<>();
    public JFXTextField enterUserName;
    public JFXPasswordField enterPassword;
    public JFXTextField passwordText;
    public CheckBox showPassword;
    public JFXButton logionButton;


    public void logionButtonOnAction(ActionEvent actionEvent) throws NoSuchAlgorithmException {


        checkPassword();

    }

    public void showPasswordOnAction(ActionEvent actionEvent) {
        if (showPassword.isSelected()) {
            passwordText.setText(enterPassword.getText());
            passwordText.setVisible(true);
            enterPassword.setVisible(false);
            enterPassword.clear();

            passwordText.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    enterPassword.clear();
                    enterPassword.setText(newValue);
                    System.out.println(enterPassword.getText());
                }
            });

        } else {
            enterPassword.setText(passwordText.getText());
            enterPassword.setVisible(true);
            passwordText.setVisible(false);
//            passwordText.clear();
//            passwordText.setText(enterPassword.getText());
        }
    }

    public void checkPassword() throws NoSuchAlgorithmException {

        userDetail.clear();
        String user = enterUserName.getText();
        String password = enterPassword.getText();

        /*Start decrept users password*/

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++)
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

        System.out.println("Digest(in hex format):: " + sb.toString());
        /*end decrept users password*/

        try {
            Statement statement = DbConnection.getInstance().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT *FROM users ");


            while (resultSet.next()) {
                System.out.println(resultSet.getString(4));
                if (sb.toString().equals(resultSet.getString(4)) && user.equals(resultSet.getString(6))) {
                    System.out.println("is true");
                    userDetail.add(resultSet.getString(5));

                    new Alert(Alert.AlertType.INFORMATION,"superrr...",ButtonType.OK).show();

                    Parent root = FXMLLoader.load(this.getClass().getResource("/view/DashBoard.fxml"));
                    Stage stage = (Stage) (this.root.getScene().getWindow());
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.show();

                    return;
                }
            }
            new Alert(Alert.AlertType.INFORMATION,"pleas enter correct user name and password",ButtonType.OK).show();
            enterPassword.clear();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}



