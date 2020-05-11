package control;

import DB.DbConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.CenterTM;
import util.HospitalTM;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

public class ManageCenter_Control {
    public JFXButton home;
    public JFXButton addNew;


    public TitledPane qurantionInformationAccordion;
    public JFXTextField centerId;
    public JFXTextField centerName;
    public JFXTextField centerCity;
    public JFXComboBox centerDistrict;
    public JFXTextField centerCapasity;
    public JFXTextField director;
    public JFXTextField directorContact;
    public JFXTextField centerContact1;
    public JFXTextField centerContact2;
    public JFXTextField fax;
    public JFXTextField email;
    public JFXButton saveButton;
    public JFXButton deletButton;
    
    public JFXTextField searchCenterDetails;
    public TableView<CenterTM> table;
    
    static ArrayList<CenterTM> centerDetailArray=new ArrayList<>();
    public AnchorPane root;

    public void initialize(){


        table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("centerId"));
        table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("centerName"));
        table.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("centerCity"));
        table.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("centerDistrict"));
        table.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("director"));

        loadTableValue();

        loadDistrict();

        selectTableValueAndLoadFelds();

        setDesableButens();

        searchCenterDetailsKey();

        centerId.setDisable(true);

        ArrayList<String> userDetail = Login_Control.userDetail;
        if (userDetail.get(0).equals("P.S.T.F Member") || userDetail.get(0).contains("-C") || userDetail.get(0).contains("-H")){
            addNew.setDisable(true);
            saveButton.setDisable(true);
            deletButton.setDisable(true);
            return;
        }
    }

    public void loadTableValue(){
        try {
            Statement statement = DbConnection.getInstance().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT *FROM qurantionCenterManage");

            ObservableList<CenterTM> items = table.getItems();
            while (resultSet.next()){
                CenterTM centerTM = new CenterTM(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(6));

                items.add(centerTM);
                centerDetailArray.add(centerTM);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDistrict(){

        ObservableList items = centerDistrict.getItems();
        ObservableList<Object> objects = FXCollections.observableArrayList(
                "Ampara","Anuradhapura","Badulla","Batticaloa","Colombo",
                "Galle","Gampaha","Hambantota","Jaffna","Kalutara",
                "Kandy","Kegalle","Kilinochchi","Kurunegala","Mannar",
                "Matale","Matara","Moneragala","Mullaitivu","Nuwara Eliya",
                "Polonnaruwa","Puttalam","Ratnapura","Trincomalee","Vavuniya"

        );
        for (int x=0;x<25;x++){
            items.add(objects.get(x));
        }

    }

    public void selectTableValueAndLoadFelds(){

        ArrayList<String> userDetail = Login_Control.userDetail;
        if (userDetail.get(0).equals("P.S.T.F Member") || userDetail.get(0).contains("-C") || userDetail.get(0).contains("-H")){
            return;
        }else {

            table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CenterTM>() {
                @Override
                public void changed(ObservableValue<? extends CenterTM> observable, CenterTM oldValue, CenterTM newValue) {

                    if (newValue == null) {
                        return;
                    }
                    String centerID = table.getSelectionModel().getSelectedItem().getCenterId();
                    centerId.setText(centerID);
                    try {
                        Statement statement = DbConnection.getInstance().getConnection().createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT *FROM qurantionCenterManage WHERE id='" + centerID + "' ");

                        ObservableList distretObList = centerDistrict.getItems();
                        while (resultSet.next()) {
                            System.out.println(newValue);

                            for (int x = 0; x < distretObList.size(); x++) {
                                if (distretObList.get(x).equals(resultSet.getString(4))) {
                                    centerDistrict.getSelectionModel().select(distretObList.get(x));
                                }
                            }
                            centerName.setText(resultSet.getString(2));
                            centerCity.setText(resultSet.getString(3));
                            centerCapasity.setText(resultSet.getString(5));
                            director.setText(resultSet.getString(6));
                            directorContact.setText(resultSet.getString(7));
                            centerContact1.setText(resultSet.getString(8));
                            centerContact2.setText(resultSet.getString(9));
                            fax.setText(resultSet.getString(10));
                            email.setText(resultSet.getString(11));

                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    saveButton.setDisable(true);
                    deletButton.setDisable(false);

                }
            });
        }
    }

    public void setDesableButens(){
        saveButton.setDisable(true);
        deletButton.setDisable(true);
    }

    public void searchCenterDetailsKey(){
        searchCenterDetails.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println(newValue);
                ObservableList<CenterTM> items = table.getItems();
                items.clear();

                for (CenterTM centerSearch:centerDetailArray) {
                    if (centerSearch.getCenterId().contains(newValue) || centerSearch.getCenterName().contains(newValue) ||
                            centerSearch.getCenterCity().contains(newValue) || centerSearch.getCenterDistrict().contains(newValue) || centerSearch.getDirector().contains(newValue)){

                        items.add(centerSearch);

                    }
                }
            }
        });
    }

    public void homeOnAction(ActionEvent actionEvent) throws IOException {
        Parent root= FXMLLoader.load(this.getClass().getResource("/view/DashBoard.fxml"));
        Stage stage=(Stage)(this.root.getScene().getWindow());
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void saveButtonOnAction(ActionEvent actionEvent) {
        String id = centerId.getText();
        String name = centerName.getText();
        String city = centerCity.getText();
        Object district = centerDistrict.getValue();
        String capasity = centerCapasity.getText();
        String centerDirector = director.getText();
        String directorContactNo = directorContact.getText();
        String centerContact_1 = centerContact1.getText();
        String centerContact_2 = centerContact2.getText();
        String centerFax = fax.getText();
        String centerEmail = email.getText();

        if (id.equals("")  || name.equals("") || city.equals("") || district==null
                || capasity.equals("") ||centerDirector.equals("") || directorContactNo.equals("")
                || centerContact_1.equals("") || centerContact_2.equals("") || centerFax.equals("") || centerEmail.equals("")){
            new Alert(Alert.AlertType.ERROR,"Pleas Fill All Fielda And TryAgain", ButtonType.OK).show();

        }else if (name.matches("[a-zA-Z]{3}[a-zA-Z]*") && city.matches("[a-zA-Z]{3}[a-zA-Z]*") && centerDirector.matches("[a-zA-Z]{3}[a-zA-Z]*")) {

            if (capasity.matches("[0-9]{1,5}")) {

                if (directorContactNo.matches("[0-9]{3}-[0-9]{7}") && centerContact_1.matches("[0-9]{3}-[0-9]{7}")
                        && centerContact_2.matches("[0-9]{3}-[0-9]{7}") && centerFax.matches("[0-9]{3}-[0-9]{7}")) {

                    if (centerEmail.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
                        System.out.println("compleat");
                        ObservableList<CenterTM> hospitalTable = table.getItems();
                        hospitalTable.add(new CenterTM(id, name, city, district + "", centerDirector));

                        try {
                            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO qurantionCenterManage VALUES (?,?,?,?,?,?,?,?,?,?,?)");
                            preparedStatement.setObject(1, id);
                            preparedStatement.setObject(2, name);
                            preparedStatement.setObject(3, city);
                            preparedStatement.setObject(4, district);
                            preparedStatement.setObject(5, capasity);
                            preparedStatement.setObject(6, centerDirector);
                            preparedStatement.setObject(7, directorContactNo);
                            preparedStatement.setObject(8, centerContact_1);
                            preparedStatement.setObject(9, centerContact_2);
                            preparedStatement.setObject(10, centerFax);
                            preparedStatement.setObject(11, centerEmail);
                            preparedStatement.executeUpdate();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        clearFieldsValue();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "pleas check Your Email and Try Again", ButtonType.OK).show();
                    }

                } else {
                    new Alert(Alert.AlertType.ERROR, "Pleas enter Contact number, FAX  using 'xxx-xxxxxxx' method And TryAgain", ButtonType.OK).show();
                }

            } else {
                new Alert(Alert.AlertType.ERROR, "Pleas enter Correct Center Capasity And TryAgain", ButtonType.OK).show();

            }
        }else {
            new Alert(Alert.AlertType.ERROR, "Pleas enter name,city,director charactors value more than 3 And TryAgain", ButtonType.OK).show();
        }

    }

    public void deletButtonOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are u sure to Delet?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get()==ButtonType.YES){
            try {
                PreparedStatement deletCentermanage = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM qurantionCenterManage WHERE id=?");
                deletCentermanage.setObject(1,centerId.getText());
                deletCentermanage.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {

        }

        table.getItems().clear();
        loadTableValue();
    }

    public void addNewOnAction(ActionEvent actionEvent) {
        clearFieldsValue();
        saveButton.setDisable(false);
        deletButton.setDisable(true);
        table.getSelectionModel().clearSelection();

        idAutoGenerate();
        centerName.requestFocus();
    }

    public void clearFieldsValue(){
        centerId.setText("");
        centerName.setText("");
        centerCity.setText("");
        centerDistrict.getSelectionModel().clearSelection();
        centerCapasity.setText("");
        director.setText("");
        directorContact.setText("");
        centerContact1.setText("");
        centerContact2.setText("");
        fax.setText("");
        email.setText("");
    }

    public void idAutoGenerate(){
        int maxId=0;

        for (CenterTM loop:table.getItems()) {
            int idNumber = Integer.parseInt(loop.getCenterId().replace("C", ""));
            if (maxId<idNumber){
                maxId=idNumber;
            }
        }
        maxId++;

        String id="";
        if (maxId<10){
            id="C00"+maxId;
        }else if (maxId<100){
            id="C0"+maxId;
        }else {
            id="C"+maxId;
        }

        centerId.setText(id);
    }
}
