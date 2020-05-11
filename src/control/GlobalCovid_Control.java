package control;

import DB.DbConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class GlobalCovid_Control {
    public Label dateLable;
    public Label conformeLable;
    public Label recorverLable;
    public Label deadLable;

    public JFXDatePicker datePicler;
    public JFXTextField conformedText;
    public JFXTextField recorverText;
    public JFXTextField deadText;

    public JFXButton updateButton;
    public JFXButton homeButton;
    public AnchorPane root;


    public void initialize(){

        ArrayList<String> userDetail = Login_Control.userDetail;
        if (userDetail.get(0).equals("P.S.T.F Member") || userDetail.get(0).contains("-C") || userDetail.get(0).contains("-H")){
            updateButton.setDisable(true);
            return;
        }

        LocalDate today = LocalDate.now();
        datePicler.setValue(today);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        System.out.println(simpleDateFormat.format(date));


        displayDetails();



    }

    public void displayDetails(){
        LocalDate today = LocalDate.now();
        int totalConformCases = 0;
        int totalRecorverCases = 0;
        int totalDeadCases = 0;
        try {
            Statement statement = DbConnection.getInstance().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT *FROM globalcovid");

            while (resultSet.next()){
                totalConformCases +=resultSet.getInt(2);
                totalRecorverCases +=resultSet.getInt(3);
                totalDeadCases +=resultSet.getInt(4);

            }
            System.out.println(totalConformCases+" "+totalDeadCases);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        dateLable.setText(today.minusDays(1)+"");
        conformeLable.setText(totalConformCases+"");
        recorverLable.setText(totalRecorverCases+"");
        deadLable.setText(totalDeadCases+"");
    }

    public void dateClick(ActionEvent actionEvent) {

        LocalDate startDate = LocalDate.of(2020, 05, 01);
        LocalDate today = LocalDate.now();

        if (datePicler.getValue().isBefore(startDate)){
            new Alert(Alert.AlertType.ERROR,"you can select 2020/05/01 TO Today cant select less 05/01", ButtonType.OK).show();
            datePicler.setValue(today);
        }else if (datePicler.getValue().isAfter(today)){
            new Alert(Alert.AlertType.ERROR,"you can select 2020/05/01 TO Today ,cant select over today", ButtonType.OK).show();
            datePicler.setValue(today);
        }
    }

    public void updateButtonOnAction(ActionEvent actionEvent) {
        LocalDate date = datePicler.getValue();
        String conformedCases = conformedText.getText();
        String recorverCases = recorverText.getText();
        String deadCadses = deadText.getText();
        if ((conformedCases.isEmpty() || recorverCases.isEmpty() || deadCadses.isEmpty()) && date.equals(LocalDate.now())){
            new Alert(Alert.AlertType.ERROR,"You can update today details after 22:30 and Pleas enter all fields and try again",ButtonType.OK).show();

        }else if ((conformedCases.isEmpty() || recorverCases.isEmpty() || deadCadses.isEmpty()) && date.isBefore(LocalDate.now())){
            new Alert(Alert.AlertType.ERROR,"Pleas enter all fields and try again",ButtonType.OK).show();
        }else {

            if (conformedCases.matches("[0-9]*") && recorverCases.matches("[0-9]*") && deadCadses.matches("[0-9]*")) {

                LocalDate today = LocalDate.now();

                datePicler.setValue(today);

                try {
                    Statement statement = DbConnection.getInstance().getConnection().createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT covidDate From globalcovid");

                    while (resultSet.next()) {
                        if (date.isEqual(resultSet.getDate(1).toLocalDate())) {
                            System.out.println(resultSet.getDate(1));
                            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement("UPDATE globalcovid SET conformed=?,recover=?,dead=? WHERE covidDate=?");
                            preparedStatement.setObject(1, conformedCases);
                            preparedStatement.setObject(2, recorverCases);
                            preparedStatement.setObject(3, deadCadses);
                            preparedStatement.setObject(4, date);
                            preparedStatement.executeUpdate();

                            displayDetails();
                            conformedText.clear();
                            recorverText.clear();
                            deadText.clear();
                            return;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                Date dates = new Date();


                try {
                    if (simpleDateFormat.parse(simpleDateFormat.format(dates)).after(simpleDateFormat.parse("22:30")) && simpleDateFormat.parse(simpleDateFormat.format(dates)).before(simpleDateFormat.parse("23:59"))) {
                        System.out.println("in if condition");
                        try {
                            PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO globalcovid VALUES (?,?,?,?)");
                            preparedStatement.setObject(1, date);
                            preparedStatement.setObject(2, conformedCases);
                            preparedStatement.setObject(3, recorverCases);
                            preparedStatement.setObject(4, deadCadses);
                            preparedStatement.executeUpdate();

                            displayDetails();
                            conformedText.clear();
                            recorverText.clear();
                            deadText.clear();
                            return;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    } else {
                        new Alert(Alert.AlertType.ERROR, "You can only update today details after 22:30 and befor 23:59", ButtonType.OK).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }



            }else {
                new Alert(Alert.AlertType.ERROR,"Pleas enter Numiric values to the fields",ButtonType.OK).show();
            }
        }


    }

    public void homeButtonOnAction(ActionEvent actionEvent) {
        try {
            Parent root= FXMLLoader.load(this.getClass().getResource("/view/DashBoard.fxml"));
            Stage stage=(Stage)(this.root.getScene().getWindow());
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
