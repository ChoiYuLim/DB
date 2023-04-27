package com.yulim.day_0408;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class JavaFX extends Application {

    private ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@192.168.119.119:1521:dink";
        String user = "scott";
        String password = "tiger";

        try {
            Connection conn = null;
            conn = DriverManager.getConnection(url, user, password);

            Class.forName(driver);
            System.out.println("jdbc driver 로딩 성공");
            DriverManager.getConnection(url, user, password);
            System.out.println("오라클 연결 성공");

            // 3. SQL 쿼리를 실행합니다.
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT *\r\n"
                    + "FROM (\r\n"
                    + "        SELECT E.*, DEPT.DNAME, DEPT.LOC\r\n"
                    + "        FROM\r\n"
                    + "            (SELECT EMP_LARGE.*,SALGRADE.GRADE\r\n"
                    + "             FROM EMP_LARGE,SALGRADE \r\n"
                    + "             WHERE EMP_LARGE.SAL BETWEEN SALGRADE.LOSAL AND SALGRADE.HISAL) E\r\n"
                    + "             JOIN DEPT ON E.DEPTNO = DEPT.DEPTNO ) A\r\n"
                    + "WHERE ROWNUM <= 10\r\n"
                    + "ORDER BY SAL DESC, HIREDATE DESC");

            // 4. 쿼리 결과를 처리합니다.
            while (rs.next()) {
                data.add(new XYChart.Data<String, Number>(rs.getString("ENAME"), rs.getInt("SAL")));
            }

        } catch (ClassNotFoundException e) {
            System.out.println("jdbc driver 로딩 실패");
        } catch (SQLException e) {
            System.out.println("오라클 연결 실패");
        }

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.getData().add(new XYChart.Series<>("SAL", data));

        Scene scene = new Scene(chart, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

