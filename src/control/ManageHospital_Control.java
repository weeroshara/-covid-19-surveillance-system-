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
import util.HospitalTM;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class ManageHospital_Control {
    public JFXButton home;
    public JFXButton addNew;

    public TitledPane hospitalInformationAccordion;
    public JFXTextField hospitalId;
    public JFXTextField hospitalName;
    public JFXTextField hospitalCity;
    public JFXComboBox hospitalDestric;
    public JFXTextField hospitalCapacity;
    public JFXTextField director;
    public JFXTextField directorContact;
    public JFXTextField hospitalContact1;
    public JFXTextField hospitalContact2;
    public JFXTextField fax;
    public JFXTextField email;
    public JFXButton saveButton;
    public JFXButton deletButton;

    public JFXTextField searchHospitalDetails;
    public TableView<HospitalTM> table;

    static ArrayList<HospitalTM> tableValueArray=new ArrayList<>();
    public AnchorPane root;

    public void initialize(){


        table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("hospitalId"));
        table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("hospitalName"));
        table.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("hospitalCity"));
        table.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("hospitalDestric"));
        table.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("director"));


        loadTableValue();

        loadDistrict();

        selectTableValueAndLoadFelds();

        setDesableButens();

        searchHospitalDetailsKey();

        hospitalId.setDisable(true);

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
            ResultSet resultSet = statement.executeQuery("SELECT *FROM hospitalmanage");

            ObservableList<HospitalTM> items = table.getItems();
            while (resultSet.next()){
                HospitalTM hospitalTM = new HospitalTM(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(6));

                items.add(hospitalTM);
                tableValueArray.add(hospitalTM);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDistrict(){

        ObservableList items = hospitalDestric.getItems();
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

            table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HospitalTM>() {
                @Override
                public void changed(ObservableValue<? extends HospitalTM> observable, HospitalTM oldValue, HospitalTM newValue) {

                    if (newValue == null) {
                        return;
                    }
                    String hospitalID = table.getSelectionModel().getSelectedItem().getHospitalId();
                    hospitalId.setText(hospitalID);
                    try {
                        Statement statement = DbConnection.getInstance().getConnection().createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT *FROM hospitalmanage WHERE id='" + hospitalID + "' ");

                        ObservableList distretObList = hospitalDestric.getItems();
                        while (resultSet.next()) {
                            System.out.println(newValue);

                            for (int x = 0; x < distretObList.size(); x++) {
                                if (distretObList.get(x).equals(resultSet.getString(4))) {
                                    hospitalDestric.getSelectionModel().select(distretObList.get(x));
                                }
                            }
                            hospitalName.setText(resultSet.getString(2));
                            hospitalCity.setText(resultSet.getString(3));
                            hospitalCapacity.setText(resultSet.getString(5));
                            director.setText(resultSet.getString(6));
                            directorContact.setText(resultSet.getString(7));
                            hospitalContact1.setText(resultSet.getString(8));
                            hospitalContact2.setText(resultSet.getString(9));
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

    public void addNewOnAction(ActionEvent actionEvent) {
        clearFieldsValue();
        saveButton.setDisable(false);
        deletButton.setDisable(true);
        table.getSelectionModel().clearSelection();

        idAutoGenerate();
        hospitalName.requestFocus();

    }

    public void saveButtonOnAction(ActionEvent actionEvent) {
        String id = hospitalId.getText();
        String name = hospitalName.getText();
        String city = hospitalCity.getText();
        Object district = hospitalDestric.getValue();
        String capasity = hospitalCapacity.getText();
        String hospitalDirector = director.getText();
        String directorContactNo = directorContact.getText();
        String hospitalContact_1 = hospitalContact1.getText();
        String hospitalContact_2 = hospitalContact2.getText();
        String hospitalFax = fax.getText();
        String hospitalEmail = email.getText();

        if (id.equals("")  || name.equals("") || city.equals("") || district==null
                || capasity.equals("") ||hospitalDirector.equals("") || directorContactNo.equals("")
                || hospitalContact_1.equals("") || hospitalContact_2.equals("") || hospitalFax.equals("") || hospitalEmail.equals("")){
            new Alert(Alert.AlertType.ERROR,"Pleas Fill All Fielda And TryAgain",ButtonType.OK).show();

        }else if (name.matches("[a-zA-Z]{3}[a-zA-Z]*") && city.matches("[a-zA-Z]{3}[a-zA-Z]*") && hospitalDirector.matches("[a-zA-Z]{3}[a-zA-Z]*")) {

            if (capasity.matches("[0-9]{1,5}")) {

                if (directorContactNo.matches("[0-9]{3}-[0-9]{7}") && hospitalContact_1.matches("[0-9]{3}-[0-9]{7}")
                        && hospitalContact_2.matches("[0-9]{3}-[0-9]{7}") && hospitalFax.matches("[0-9]{3}-[0-9]{7}")) {

                    if (hospitalEmail.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
                        System.out.println("compleat");
                        ObservableList<HospitalTM> hospitalTable = table.getItems();
                        hospitalTable.add(new HospitalTM(id, name, city, district + "", hospitalDirector));

                        try {
                            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO hospitalmanage VALUES (?,?,?,?,?,?,?,?,?,?,?)");
                            preparedStatement.setObject(1, id);
                            preparedStatement.setObject(2, name);
                            preparedStatement.setObject(3, city);
                            preparedStatement.setObject(4, district);
                            preparedStatement.setObject(5, capasity);
                            preparedStatement.setObject(6, hospitalDirector);
                            preparedStatement.setObject(7, directorContactNo);
                            preparedStatement.setObject(8, hospitalContact_1);
                            preparedStatement.setObject(9, hospitalContact_2);
                            preparedStatement.setObject(10, hospitalFax);
                            preparedStatement.setObject(11, hospitalEmail);
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
                new Alert(Alert.AlertType.ERROR, "Pleas enter Correct hospital Capasity And TryAgain", ButtonType.OK).show();

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
                PreparedStatement deletHospitalmanage = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM hospitalmanage WHERE id=?");
                deletHospitalmanage.setObject(1,hospitalId.getText());
                deletHospitalmanage.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {

        }

        table.getItems().clear();
        loadTableValue();

    }

    public void clearFieldsValue(){
        hospitalId.setText("");
        hospitalName.setText("");
        hospitalCity.setText("");
        hospitalDestric.getSelectionModel().clearSelection();
        hospitalCapacity.setText("");
        director.setText("");
        directorContact.setText("");
        hospitalContact1.setText("");
        hospitalContact2.setText("");
        fax.setText("");
        email.setText("");
    }

    public void setDesableButens(){
        saveButton.setDisable(true);
        deletButton.setDisable(true);
    }

    public void searchHospitalDetailsKey(){
        searchHospitalDetails.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println(newValue);
                ObservableList<HospitalTM> items = table.getItems();
                items.clear();

                for (HospitalTM hospitalSearch:tableValueArray) {
                    if (hospitalSearch.getHospitalId().contains(newValue) || hospitalSearch.getHospitalName().contains(newValue) ||
                            hospitalSearch.getHospitalCity().contains(newValue) || hospitalSearch.getHospitalDestric().contains(newValue) || hospitalSearch.getDirector().contains(newValue)){

                        items.add(hospitalSearch);

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

    public void idAutoGenerate(){
        int maxId=0;

        for (HospitalTM loop:table.getItems()) {
            int idNumber = Integer.parseInt(loop.getHospitalId().replace("H", ""));
            if (maxId<idNumber){
                maxId=idNumber;
            }
        }
        maxId++;

        String id="";
        if (maxId<10){
            id="H00"+maxId;
        }else if (maxId<100){
            id="H0"+maxId;
        }else {
            id="H"+maxId;
        }

        hospitalId.setText(id);
    }

}
