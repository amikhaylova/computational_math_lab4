package Controllers;

import Approximators.*;
import Utils.Point;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ApproximationController {
    @FXML
    private Button solveBut;

    @FXML
    private LineChart<?, ?> lineChart;

    @FXML
    private RadioButton linear;

    @FXML
    private ToggleGroup group1;

    @FXML
    private RadioButton quadratic;

    @FXML
    private RadioButton pow;

    @FXML
    private RadioButton log;

    @FXML
    private TextArea answerTextArea;

    @FXML
    private TableView<Point> pointsTable;

    @FXML
    private TableColumn<?, ?> xCol;

    @FXML
    private TableColumn<?, ?> yCol;

    @FXML
    private TextArea xTextArea;

    @FXML
    private TextArea yTextArea;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private Button test1;

    @FXML
    private Button test2;

    @FXML
    private Button test3;

    ObservableList<Point> points = FXCollections.observableArrayList();

    @FXML
    void initialize() {

        answerTextArea.setEditable(false);

        xCol.setCellValueFactory(new PropertyValueFactory<>("x"));
        yCol.setCellValueFactory(new PropertyValueFactory<>("y"));

        linear.setSelected(true);

        points.addListener(new ListChangeListener<Point>() {
            @Override
            public void onChanged(Change<? extends Point> c) {
                pointsTable.setItems(points);
            }
        });

        test1.setOnAction(event -> {
            xTextArea.setText("1.2 2.9 4.1 5.5 6.7 7.8 9.2 10.3");
            yTextArea.setText("7.4 9.5 11.1 12.9 14.6 17.3 18.2 20.7");
        });

        test2.setOnAction(event -> {
            xTextArea.setText("1.1 2.3 3.7 4.5 5.4 6.8 7.5");
            yTextArea.setText("2.73 5.12 7.74 8.91 10.59 12.75 13.43");
        });

        test3.setOnAction(event -> {
            xTextArea.setText("1.1 2.3 3.7 4.5 5.4 6.8 7.5");
            yTextArea.setText("3.5 4.1 5.2 6.9 8.3 14.8 21.2");
        });

        solveBut.setOnAction(event -> {

            String[] stringX = xTextArea.getText().trim().replaceAll(" +", " ").split(" ");
            String[] stringY = yTextArea.getText().trim().replaceAll(" +", " ").split(" ");

            try {
                if (stringX.length == stringY.length) {
                    for (int i = 0; i < stringX.length; i++) {
                        Double.parseDouble(stringX[i]);
                        Double.parseDouble(stringY[i]);
                    }
                } else {
                    showAlert("Данные введены некорректно", "Количество введенных x и y должны совпадать.");
                }

                if (stringX.length == stringY.length) {
                    points.clear();
                    for (int i = 0; i < stringX.length; i++) {
                        points.add(new Point(Double.parseDouble(stringX[i]), Double.parseDouble(stringY[i])));
                    }

                    Approximator a = getApproximator(points);

                    a.calculateCoefs();
                    if (Double.isNaN(a.getA()) || Double.isNaN(a.getB()) || Double.isNaN(a.getC())) {
                        showAlert("Не удалось вычислить коэффициенты", "1я аппроксимация: система не имеет решений или имеет бесконечное множество решений.");
                    }
                    String firstAppr = a.getStringApproximation();
                    int index = a.getIndexOfMaxDeviation();

                    String excluded = points.get(index).toString();

                    ObservableList<Point> newPoints = FXCollections.observableArrayList(points);
                    newPoints.remove(index);

                    Approximator b = getApproximator(newPoints);


                    b.calculateCoefs();
                    if (Double.isNaN(b.getA()) || Double.isNaN(b.getB()) || Double.isNaN(b.getC())) {
                        showAlert("Не удалось вычислить коэффициенты", "2я аппроксимация: система не имеет решений или имеет бесконечное множество решений.");
                    }
                    String secondAppr = b.getStringApproximation();

                    drawPlots(a, b);
                    showAnswer(firstAppr, excluded, secondAppr);
                }

            } catch (Exception e) {
                showAlert("Данные введены некорректно", "Данные должны быть введены в виде чисел. В качестве разделителя используйте точку.");
            }


        });
    }

    private void showAnswer(String first, String second, String third) {
        answerTextArea.setWrapText(true);
        String ans = "Аппроксимирующая функция до удаления точки с наибольшим отклонением: \n" +
                first + "\n \nКоординаты удаленной точки: " +
                second + "\n \nАппроксимирующая функция после удаления точки с наибольшим отклонением: \n" +
                third;
        answerTextArea.setText(ans);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void drawPlots(Approximator a, Approximator b) {

        xAxis.setAutoRanging(true);

        if (!lineChart.getData().isEmpty()) {
            while (!lineChart.getData().isEmpty()) {
                lineChart.getData().remove(/*(lineChart.getData().size()-1)*/0);
            }

        }

        XYChart.Series series0 = new XYChart.Series();
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        ObservableList<XYChart.Data> datas0 = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datas1 = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datas2 = FXCollections.observableArrayList();

        List<Point> arrayList = new ArrayList<>(points);

        Point min = Collections.min(arrayList, Comparator.comparingDouble(Point::getX));
        Point max = Collections.max(arrayList, Comparator.comparingDouble(Point::getX));

        double down = min.getX() - 3.0;
        double up = max.getX() + 3.0;

        Collections.sort(arrayList);

        if (down > 10) {
            xAxis.setAutoRanging(false);
            xAxis.setLowerBound(down);
            xAxis.setUpperBound(up);
            xAxis.setTickUnit((down) / arrayList.size());
        }

        RadioButton selectedRadioButton = (RadioButton) group1.getSelectedToggle();
        if (selectedRadioButton == log || selectedRadioButton == pow) {
            if (down <= 0) {
                down = 0.1;
            }
            if (down >= up) {
                up = down + 5;
            }
        }

        if (!(Double.isNaN(a.getA()) || Double.isNaN(a.getB()) || Double.isNaN(a.getC()))) {
            try {
                for (double i = down; i < up; i = i + 0.1) {
                    datas0.add(new XYChart.Data(i, a.getApprFunctionValue(i)));
                }
            } catch (Exception e) {
                showAlert("Невозможно отобразить график", "Одно из значений не попадает в область определения функции.");
            }
            series0.setData(datas0);
        } else {
            series0.setData(FXCollections.observableArrayList());
        }
        series0.setName("1я аппроксимация");
        lineChart.getData().add(series0);

        if (!(Double.isNaN(b.getA()) || Double.isNaN(b.getB()) || Double.isNaN(b.getC()))) {
            try {

                for (double i = down; i < up; i = i + 0.1) {
                    datas1.add(new XYChart.Data(i, b.getApprFunctionValue(i)));
                }
            } catch (Exception e) {
                showAlert("Невозможно отобразить график", "Одно из значений не попадает в область определения функции.");
            }
            series1.setData(datas1);

        } else {
            series1.setData(FXCollections.observableArrayList());
        }
        series1.setName("2я аппроксимация");
        lineChart.getData().add(series1);

        for (int i = 0; i < arrayList.size(); i++) {
            datas2.add(new XYChart.Data(arrayList.get(i).getX(), arrayList.get(i).getY()));
        }
        series2.setData(datas2);

        series2.setName("исходные точки");
        lineChart.setCreateSymbols(true);
        lineChart.getData().add(series2);
    }

    private Approximator getApproximator(ObservableList<Point> points) {
        RadioButton selectedRadioButton = (RadioButton) group1.getSelectedToggle();
        if (selectedRadioButton == linear) {
            return new LinearApproximator(points);
        } else if (selectedRadioButton == quadratic) {
            return new QuadraticApproximator(points);
        } else if (selectedRadioButton == pow) {
            return new PowApproximator(points);
        } else if (selectedRadioButton == log) {
            return new LogApproximator(points);
        } else {
            return new ExponentialApproximator(points);
        }
    }
}
