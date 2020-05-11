package control;

import DB.DbConnection;
import com.jfoenix.controls.*;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.UserTM;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class UserManage_Control {
    public JFXPasswordField password;
    public JFXCheckBox checkBox;
    public JFXTextField textField;
    
    public JFXTextField enterName;
    public JFXTextField enterMobile;
    public JFXTextField enterEmail;
    public JFXTextField enterUserName;

    public JFXButton addNew;
    public JFXButton home;
    public JFXButton save;

    public JFXComboBox selectUserRole;

    public JFXTextField searchUser;
    public TableView<UserTM> table;
    public JFXComboBox selectWorkingPlace;
    public AnchorPane root;

    Button delet;
    static ArrayList<UserTM> userArray=new ArrayList<>();

    public void initialize(){


        table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("userName"));
        table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("role"));
        table.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("delet"));

        randonPassword();

        setUserRole();

        selectWorkingPlace.setVisible(false);

        loadTable();

        deletOnAction();

        searchTable();

        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                password.clear();
                password.setText(newValue);
                System.out.println(password.getText());
            }
        });


        ArrayList<String> userDetail = Login_Control.userDetail;
        if (userDetail.get(0).equals("P.S.T.F Member") || userDetail.get(0).contains("-C") || userDetail.get(0).contains("-H")){
            addNew.setDisable(true);
            return;
        }

    }

    public void loadTable(){

        save.setDisable(true);
        Platform.runLater(()->{
            addNew.requestFocus();
        });

        save.setDisable(true);

        try {
            Statement statement = DbConnection.getInstance().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT *FROM users");

            ObservableList<UserTM> items = table.getItems();

            while (resultSet.next()){

                String user = resultSet.getString(6);
                delet = new Button();
                delet.setText("Delet "+user+"");
                delet.setMaxWidth(100);

                delet.setDisable(true);

                System.out.println(resultSet.getString(6)+" "+resultSet.getString(1)+" "+resultSet.getString(5));
                items.add(new UserTM(resultSet.getString(6),resultSet.getString(1),resultSet.getString(5),delet));
                userArray.add(new UserTM(resultSet.getString(6),resultSet.getString(1),resultSet.getString(5),delet));
                table.refresh();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletOnAction(){
        if (Login_Control.userDetail.get(0).equals("P.S.T.F Member")){
            return;
        }else {
            table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserTM>() {
                @Override
                public void changed(ObservableValue<? extends UserTM> observable, UserTM oldValue, UserTM newValue) {
                    if (newValue == null) {
                        return;
                    }

                    for (UserTM butn : table.getItems()) {
                        butn.getDelet().setDisable(true);
                        if (butn.getDelet().getText().equals(newValue.getDelet().getText())) {
                            butn.getDelet().setDisable(false);


                            String user = butn.getDelet().getText().replace("Delet ", "");

                            butn.getDelet().setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {

                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are u sure toDelet This?", ButtonType.YES, ButtonType.NO);
                                    Optional<ButtonType> buttonType = alert.showAndWait();
                                    if (buttonType.get() == ButtonType.YES) {
                                        try {
                                            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM users WHERE userName=?");
                                            preparedStatement.setObject(1, user);
                                            preparedStatement.executeUpdate();
                                            //loadTable();

                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }

                                        table.getSelectionModel().clearSelection();
                                        table.getItems().clear();
                                        userArray.clear();
                                        //loadTable();
                                    }
                                    table.getSelectionModel().clearSelection();
                                    table.getItems().clear();
                                    userArray.clear();
                                    searchUser.setText("");
                                    loadTable();
                                }
                            });

                        }

                    }
                }
            });
        }
    }

    public void setUserRole(){
        ObservableList userRoleObList = selectUserRole.getItems();
        userRoleObList.add("Admin");
        userRoleObList.add("P.S.T.F Member");
        userRoleObList.add("Hospital IT");
        userRoleObList.add("Center IT");


        selectUserRole.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                System.out.println(newValue);
                if (newValue==null){
                    return;
                }
                selectWorkingPlace.setVisible(false);
                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(10), save);

                TranslateTransition translateTransition1 = new TranslateTransition(Duration.millis(10), searchUser);

                TranslateTransition translateTransition2 = new TranslateTransition(Duration.millis(10), table);


                if (newValue.equals("Hospital IT") || newValue.equals("Center IT")){
                    selectWorkingPlace.setVisible(true);
                    if (newValue.equals("Hospital IT")) {
                        selectWorkingPlace.promptTextProperty().setValue("Hospital");

                        try {
                            Statement statement = DbConnection.getInstance().getConnection().createStatement();
                            ResultSet resultSet = statement.executeQuery("SELECT id,name FROM hospitalManage");

                            ObservableList items = selectWorkingPlace.getItems();
                            items.clear();
                            while (resultSet.next()){

                                items.add(resultSet.getString(2)+"-"+resultSet.getString(1));

                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else {
                        selectWorkingPlace.promptTextProperty().setValue("center");

                        try {
                            Statement statement = DbConnection.getInstance().getConnection().createStatement();
                            ResultSet resultSet = statement.executeQuery("SELECT id,name FROM qurantionCenterManage");

                            ObservableList items = selectWorkingPlace.getItems();
                            items.clear();
                            while (resultSet.next()){

                                items.add(resultSet.getString(2)+"-"+resultSet.getString(1));

                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }


                    translateTransition.setToX(0);
                    translateTransition.setToY(70);
                    translateTransition.play();

                    translateTransition1.setToX(0);
                    translateTransition1.setToY(70);
                    translateTransition1.play();

                    translateTransition2.setToX(0);
                    translateTransition2.setToY(70);
                    translateTransition2.play();
                }
                translateTransition.setToX(0);
                translateTransition.setToY(0);
                translateTransition.play();

                translateTransition1.setToX(0);
                translateTransition1.setToY(0);
                translateTransition1.play();

                translateTransition2.setToX(0);
                translateTransition2.setToY(0);
                translateTransition2.play();

            }
        });




    }

    public void randonPassword(){
        Platform.runLater(()->{
         String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
         String CHAR_UPPER = CHAR_LOWER.toUpperCase();
         String NUMBER = "0123456789";
         String OTHER_CHAR = "!@#$%&*()_+-=[]?";

         String pwd = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
         Random random = new Random();
         char[] pass = new char[8];

         for (int i=0;i<8;i++){
             pass[i]=pwd.charAt(random.nextInt(pwd.length()));
         }



            String s = new String(pass);
            password.setText(s);
        });


    }

    public void checkBoxOnAction(ActionEvent actionEvent) {
        if (checkBox.isSelected()){
            textField.setText(password.getText());
            textField.setVisible(true);
            password.setVisible(false);
        }else {
            password.setText(textField.getText());
            password.setVisible(true);
            textField.setVisible(false);
        }
    }

    public void addNewOnAction(ActionEvent actionEvent) {
        save.setDisable(false);
        randonPassword();
        enterName.requestFocus();

    }

    public void saveOnAction(ActionEvent actionEvent) throws IOException, NoSuchAlgorithmException {
        saveDb();
    }

    public void saveDb() throws IOException, NoSuchAlgorithmException {
        if (enterName.getText().equals("") || enterMobile.getText().equals("")  || enterEmail.getText().equals("") || enterUserName.getText().equals("") || selectUserRole.getValue()==null){
            new Alert(Alert.AlertType.ERROR,"pleas fill all fields", ButtonType.OK).show();
        }else {

            saveProcess();


        }
    }

    public void setDataBaseAndTable_withIT() throws NoSuchAlgorithmException {
        String name = enterName.getText();
        String mobile = enterMobile.getText();
        String email = enterEmail.getText();
        String userName = enterUserName.getText();
        String passWord = password.getText();
        String role = selectWorkingPlace.getValue().toString();


        /*Start decrept users password*/
        String password = passWord;

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++)
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

        System.out.println("Digest(in hex format):: " + sb.toString());
        /*end decrept users password*/

        try {
            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO users VALUES (?,?,?,?,?,?)");
            preparedStatement.setObject(1,name);
            preparedStatement.setObject(2,mobile);
            preparedStatement.setObject(3,email);
            preparedStatement.setObject(4,sb.toString());
            preparedStatement.setObject(5,role);
            preparedStatement.setObject(6,userName);
            preparedStatement.executeUpdate();



        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setDataBaseAndTable() throws NoSuchAlgorithmException {
        String name = enterName.getText();
        String mobile = enterMobile.getText();
        String email = enterEmail.getText();
        String userName = enterUserName.getText();
        String passWord = password.getText();
        String role = selectUserRole.getValue().toString();



        /*Start decrept users password*/
        String password = passWord;

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++)
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

        System.out.println("Digest(in hex format):: " + sb.toString());
        /*end decrept users password*/


        try {
            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO users VALUES (?,?,?,?,?,?)");
            preparedStatement.setObject(1,name);
            preparedStatement.setObject(2,mobile);
            preparedStatement.setObject(3,email);
            preparedStatement.setObject(4,sb.toString());
            preparedStatement.setObject(5,role);
            preparedStatement.setObject(6,userName);
            preparedStatement.executeUpdate();



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveProcess() throws IOException, NoSuchAlgorithmException {

        if (enterName.getText().matches("[a-zA-Z]{3}[ a-zA-Z]*") && enterUserName.getText().matches("[a-zA-Z]{3}[ a-zA-Z]*")){

            if (enterMobile.getText().matches("[0-9]{3}-[0-9]{7}")){

                if (enterEmail.getText().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")){

                    if (selectUserRole.getValue().equals("Hospital IT") || selectUserRole.getValue().equals("Center IT")){

                        if (selectWorkingPlace.getValue()==null){
                            new Alert(Alert.AlertType.ERROR,"pleas fill all fields", ButtonType.OK).show();
                        }else {
                            String value = selectWorkingPlace.getValue().toString();
                            setDataBaseAndTable_withIT();

                            ObservableList<UserTM> items = table.getItems();

                            String user = enterUserName.getText();
                            String name = enterName.getText();
                            Button delet = new Button("Delet");

                            items.add(new UserTM(
                                    user , name , value , delet
                            ));

                            //new Alert(Alert.AlertType.ERROR,"IT Wama", ButtonType.OK).show();

                        }
                    }else {
                        String value = selectUserRole.getValue().toString();
                        setDataBaseAndTable();

                        ObservableList<UserTM> items = table.getItems();

                        String user = enterUserName.getText();
                        String name = enterName.getText();
                        Button delet = new Button("Delet");

                        items.add(new UserTM(
                                user , name , value , delet
                        ));

                        //new Alert(Alert.AlertType.ERROR,"IT nowena", ButtonType.OK).show();
                    }


                    new Alert(Alert.AlertType.CONFIRMATION,"Success to Aded..",ButtonType.OK).show();

                    Parent root= FXMLLoader.load(this.getClass().getResource("/view/UserManage.fxml"));
                    Stage stage=(Stage) (this.root.getScene().getWindow());
                    Scene scene=new Scene(root);
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.show();

                    return;
                }else {
                    new Alert(Alert.AlertType.ERROR,"pleas fill valid Email Address", ButtonType.OK).show();
                }

            }else {
                new Alert(Alert.AlertType.ERROR,"pleas fill valid phone number using 'xxx-xxxxxxx' that method", ButtonType.OK).show();
            }
        }else {
            new Alert(Alert.AlertType.ERROR,"pleas fill the user name and your name grater 3 charactors", ButtonType.OK).show();
        }
    }

    public void searchTable(){
        searchUser.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                table.getItems().clear();

                for (UserTM searchResult:userArray) {
                    if (searchResult.getName().contains(newValue) ||searchResult.getUserName().contains(newValue)
                            ||searchResult.getRole().contains(newValue)){
                        table.getItems().add(searchResult);
                    }
                }
            }
        });
    }

    public void homeOnAction(ActionEvent actionEvent) throws IOException {
        Parent root= FXMLLoader.load(this.getClass().getResource("/view/DashBoard.fxml"));
        Stage stage=(Stage) (this.root.getScene().getWindow());
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void transitionBack(){
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(10), save);

        TranslateTransition translateTransition1 = new TranslateTransition(Duration.millis(10), searchUser);

        TranslateTransition translateTransition2 = new TranslateTransition(Duration.millis(10), table);

        translateTransition.setToX(0);
        translateTransition.setToY(0);
        translateTransition.play();

        translateTransition1.setToX(0);
        translateTransition1.setToY(0);
        translateTransition1.play();

        translateTransition2.setToX(0);
        translateTransition2.setToY(0);
        translateTransition2.play();
    }

    public void encreptValueCheck() throws NoSuchAlgorithmException {
        String password = "Kuf)cGLp";

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++)
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

        System.out.println("Digest(in hex format):: " + sb.toString());

    }

    public void clearBoth(){
        enterName.clear();
        enterMobile.clear();
        enterEmail.clear();
        enterUserName.clear();

//        password.setText("");
//        textField.setText("");
        textField.getTextFormatter();
        password.getTextFormatter();
        checkBox.selectedProperty().setValue(false);
        selectUserRole.getSelectionModel().clearSelection();
        selectWorkingPlace.getSelectionModel().clearSelection();
    }




}
