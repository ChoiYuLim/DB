package com.yulim.day_0408;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC {

    public static void main(String[] args) {

        // 1. Oracle JDBC 드라이버를 로드합니다.
        Connection conn = null;

        // 2. 데이터베이스에 연결합니다.
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver"); // 로딩하는 코드. 오라클의 JDBC 드라이버 이름 (JDBC
                                                              // 드라이버는 ojdbc11.jar)
            String url = "jdbc:oracle:thin:@192.168.119.119:1521/dink"; // DBMS 연결을 위한 식별 값,
                                                                        // jdbc:oracle:thin:@HOST:PORT:SID
            String user = "scott";
            String passwd = "tiger";
            conn = DriverManager.getConnection(url, user, passwd); // DriverManager를 이용해서 Connection
                                                                   // 생성
            System.out.println(conn);

            // 3, SQL 쿼리를 실행합니다.
            Statement stmt = conn.createStatement();
            System.out.println(stmt);
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
                System.out.println(rs.getString("HIREDATE") + " " + rs.getString("SAL"));
            }

        } catch (ClassNotFoundException e) {
            // 드라이버 로드 중 예외가 발생한 경우 처리합니다.
            e.printStackTrace();
        } catch (SQLException e) {
            // 데이터베이스 연결 및 쿼리 실행 중 예외가 발생한 경우 처리합니다.
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                // 연결 닫기 중 예외가 발생한 경우 처리합니다.
                e.printStackTrace();
            }
        }
    }
}