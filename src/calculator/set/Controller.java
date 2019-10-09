package calculator.set;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Controller {

    private static ArrayList<String> universe = new ArrayList<>();
    private static ArrayList<ArrayList<Boolean>> bitmaps = new ArrayList<>();

    @FXML
    private TextField uniText, setText;
    @FXML
    private Button uniButton, setButton;
    @FXML
    private ComboBox<String> operator;
    @FXML
    private ComboBox<Integer> fSet, sSet;
    @FXML
    private Label answer, sets;

    @FXML
    private void inUni() {
        String str = uniText.getText();
        if (str.length() == 0)
            return;
        String[] arr = str.split(",");
        Arrays.sort(arr);
        uniButton.setDisable(true);
        uniText.setDisable(true);
        setText.setDisable(false);
        setButton.setDisable(false);
        universe = new ArrayList<>(Arrays.asList(arr));
        StringBuilder mysb = new StringBuilder();
        mysb.append("U = ");
        mysb.append(universe);
        mysb.append("\n");
        sets.setText(mysb.toString());
    }

    private ArrayList<String> intersection(int fin, int sin) {
        if (universe.size() == 0)
            return null;
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < universe.size(); i++)
            if (bitmaps.get(fin).get(i) && bitmaps.get(sin).get(i))
                result.add(universe.get(i));
        return result;
    }

    private ArrayList<String> union(int fin, int sin) {
        if (universe.size() == 0)
            return null;
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < universe.size(); i++)
            if (bitmaps.get(fin).get(i) || bitmaps.get(sin).get(i))
                result.add(universe.get(i));
        return result;
    }

    private ArrayList<String> complement(int fin) {
        if (universe.size() == 0)
            return null;
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < universe.size(); i++)
            if (!bitmaps.get(fin).get(i))
                result.add(universe.get(i));
        return result;
    }

    @FXML
    private void addSet() {
        String str = setText.getText();
        if (str.length() == 0)
            return;
        StringBuilder mysb = new StringBuilder();
        String[] newSet = str.split(",");
        int result;
        ArrayList<Boolean> newBitmap = new ArrayList<>();
        for (int i = 0; i < universe.size(); i++)
            newBitmap.add(false);
        for (int i = 0; i < newSet.length; i++) {
            if (((result = Collections.binarySearch(universe, newSet[i])) >= 0)) {
                newBitmap.set(result, true);
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error !");
                alert.setHeaderText("See the details below");
                alert.setContentText("You have entered a set with elements which doesn't belong to the Universal set.");
                alert.show();
                setText.setText("");
                return;
            }
        }
        bitmaps.add(newBitmap);
        mysb.append(sets.getText());
        mysb.append("S");
        mysb.append(bitmaps.size());
        mysb.append(" = ");
        mysb.append(Arrays.asList(newSet));
        mysb.append("\n");
        sets.setText(mysb.toString());
        fSet.getItems().add(bitmaps.size());
        sSet.getItems().add(bitmaps.size());
        setText.setText("");

    }

    @FXML
    private void calculate() {
        if (fSet.getValue() == null || (sSet.getValue() == null && !operator.getValue().equals("¬")) || operator.getValue() == null)
            return;
        StringBuilder mysb = new StringBuilder();
        mysb.append("Answer = ");
        switch (operator.getValue()) {
            case "∪":
                mysb.append(union(fSet.getValue() - 1, sSet.getValue() - 1));
                break;
            case "∩":
                mysb.append(intersection(fSet.getValue() - 1, sSet.getValue() - 1));
                break;
            case "¬":
                mysb.append(complement(fSet.getValue() - 1));
                break;
        }
        answer.setText(mysb.toString());
    }

    @FXML
    private void exit() {
        Platform.exit();
    }

    @FXML
    private void info() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("How to use");
        alert.setContentText("(1) Enter the universal set in the first text box where elements are separated by comma (  ,  ).\n(2) Enter the sets in the same way in the second text box.\n(3) Choose the sets and the operation via combo boxes.\n(4) Hit calculate.");
        alert.show();
        alert.setWidth(600);

    }

    @FXML
    private void disableSec() {
        if (operator.getValue() == null)
            return;
        if (operator.getValue().equals("¬"))
            sSet.setDisable(true);
        else
            sSet.setDisable(false);
    }

}
