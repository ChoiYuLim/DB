package com.yulim.day_0513;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class calculateBonus {
    public static void main(String[] args) throws SQLException {

        // 1. Oracle JDBC 드라이버를 로드합니다.
        Connection conn = JDBCUtil.getConnection();

        // 2. 데이터베이스에 연결합니다.
        List<Emp> empInfo = new ArrayList<Emp>();
        List<Customer> customerInfo = new ArrayList<Customer>();

        // 3. SQL 쿼리를 실행합니다.
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT ENAME,EMPNO,JOB,SAL,COMM FROM EMP");

        // 4. 쿼리 결과를 처리합니다.
        while (rs.next()) {
            empInfo.add(new Emp(rs.getString("ENAME"), Integer.parseInt(rs.getString("EMPNO")),
                    rs.getString("JOB"), rs.getString("SAL"), rs.getString("COMM")));
        }

        // 3. SQL 쿼리를 실행합니다.
        Statement stmt2 = conn.createStatement();
        ResultSet rs2 = stmt2.executeQuery("SELECT ACCOUNT_MGR FROM CUSTOMER");
        long startTime = System.currentTimeMillis(); // 코드 실행 전 시간 측정

        // 4. 쿼리 결과를 처리합니다.
        while (rs2.next()) {
            customerInfo.add(new Customer(rs2.getString("ACCOUNT_MGR")));
        }

        Map<String, Integer> valueCountMap = new HashMap<>();

        for (Customer c : customerInfo) {
            // Map에서 해당 값의 개수를 가져옴
            Integer count = valueCountMap.get(c.getAccount_mgr());

            if (count == null) {
                // 값이 Map에 없으면 개수를 1로 설정
                valueCountMap.put(c.getAccount_mgr(), 1);
            } else {
                // 값이 이미 Map에 있으면 개수를 1 증가시킴
                valueCountMap.put(c.getAccount_mgr(), count + 1);
            }
        }

        // 값이 100000보다 큰 경우 키 값을 저장하는 리스트
        List<String> higerKey = new ArrayList<>();
        List<String> lowerKey = new ArrayList<>();
        // Map을 순회하면서 값이 100000보다 큰 경우 키 값을 저장
        for (Map.Entry<String, Integer> entry : valueCountMap.entrySet()) {
            if (entry.getValue() > 100000) {
                higerKey.add(entry.getKey());
            } else {
                lowerKey.add(entry.getKey());
            }
        }

        for (Emp e : empInfo) {
            if (higerKey.contains(e.getEmpno() + "")) {
                if (e.getJob() != "Analyist" && e.getJob() != "President") {
                    Statement stmt3 = conn.createStatement();
                    int result =
                            stmt3.executeUpdate("INSERT INTO BONUS(ENAME,JOB,SAL,COMM) VALUES('"
                                    + e.getEname() + "','" + e.getJob() + "','" + e.getSal() + "','"
                                    + Integer.toString(Integer.parseInt(e.getComm()) + 2000)
                                    + "')");
                    if (result > 0) {
                        // 성공한 경우(적용된 행의 갯수가 0개보다 크면)
                        System.out.println("성공");
                    } else {
                        // 실패한 경우(작용된 행의 갯수가 0이면)
                        System.out.println("실패");
                    }
                }
            } else if (lowerKey.contains(e.getEmpno() + "")) {
                if (e.getJob() != "Analyist" && e.getJob() != "President") {
                    Statement stmt3 = conn.createStatement();
                    int result =
                            stmt3.executeUpdate("INSERT INTO BONUS(ENAME,JOB,SAL,COMM) VALUES('"
                                    + e.getEname() + "','" + e.getJob() + "','" + e.getSal() + "','"
                                    + Integer.toString(Integer.parseInt(e.getComm()) + 1000)
                                    + "')");
                    if (result > 0) {
                        // 성공한 경우(적용된 행의 갯수가 0개보다 크면)
                        System.out.println("성공");
                    } else {
                        // 실패한 경우(작용된 행의 갯수가 0이면)
                        System.out.println("실패");
                    }
                }
            }
        }
        long endTime = System.currentTimeMillis(); // 코드 실행 후 시간 측정
        double time = (endTime - startTime) / 1000.0;
        System.out.println("걸린 시간 : " + time + "초");
    }
}
