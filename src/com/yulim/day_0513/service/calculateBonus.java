package com.yulim.day_0513.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.yulim.day_0513.entity.Emp;
import com.yulim.day_0513.util.JDBCUtil;

public class calculateBonus {
    public static void main(String[] args) throws SQLException {

        // Oracle JDBC 드라이버 로드
        Connection conn = JDBCUtil.getConnection();
        Statement stmt = conn.createStatement();

        List<Emp> empInfo = new ArrayList<Emp>();

        // 코드 실행 전 시간 측정
        long startTime = System.currentTimeMillis();

        // EMP 테이블 데이터 중 ENAME, EMPNO, KOB, SAL, COMM 가져오기
        ResultSet rs = stmt.executeQuery("SELECT ENAME,EMPNO,JOB,SAL,COMM FROM EMP");

        // empInfo에 넣기
        while (rs.next()) {
            empInfo.add(new Emp(rs.getString("ENAME"), Integer.parseInt(rs.getString("EMPNO")),
                    rs.getString("JOB"), rs.getString("SAL"), rs.getString("COMM")));
        }

        // CUSTOMER 테이블 데이터 중 ACCOUNT_MGR 가져오기
        ResultSet rs2 = stmt.executeQuery("SELECT ACCOUNT_MGR FROM CUSTOMER");

        Map<String, Integer> customerCntMap = new HashMap<>();

        while (rs2.next()) {
            String mgr = rs2.getString("ACCOUNT_MGR");
            // Map에서 해당 값의 개수를 가져옴
            Integer count = customerCntMap.get(mgr);

            if (count == null) {
                // 값이 Map에 없으면 개수를 1로 설정
                customerCntMap.put(mgr, 1);
            } else {
                // 값이 이미 Map에 있으면 개수를 1 증가시킴
                customerCntMap.put(mgr, count + 1);
            }
        }

        // 값이 100000보다 큰 경우 키 값을 저장하는 리스트
        List<String> higerKey = new ArrayList<>();
        // 값이 100000보다 작은 경우 키 값을 저장하는 리스트
        List<String> lowerKey = new ArrayList<>();

        // Map을 순회하면서 값이 100000보다 큰 경우, 작은 경우 각각 키 값을 저장
        for (Map.Entry<String, Integer> entry : customerCntMap.entrySet()) {
            if (entry.getValue() > 100000) {
                higerKey.add(entry.getKey());
            } else {
                lowerKey.add(entry.getKey());
            }
        }

        for (Emp e : empInfo) {
            // 기본 보너스 0
            int bonus = 0;
            // 100000보다 크면서, Job이 두개가 아닌 경우 보너스 2000
            if (higerKey.contains(e.getEmpno() + "") && e.getJob() != "Analyist"
                    && e.getJob() != "President") {
                bonus = 2000;
            }
            // 100000보다 작으면서, Job이 두개가 아닌 경우 보너스 1000
            else if (lowerKey.contains(e.getEmpno() + "") && e.getJob() != "Analyist"
                    && e.getJob() != "President") {
                bonus = 1000;
            }
            // Bonus 테이블에 삽입
            int result = stmt.executeUpdate("INSERT INTO BONUS(ENAME,JOB,SAL,COMM) VALUES('"
                    + e.getEname() + "','" + e.getJob() + "','" + e.getSal() + "','"
                    + Integer.toString(Integer.parseInt(e.getComm()) + bonus) + "')");
            if (result > 0) {
                // 성공한 경우(적용된 행의 갯수가 0개보다 크면)
                System.out.println("성공");
            } else {
                // 실패한 경우(작용된 행의 갯수가 0이면)
                System.out.println("실패");
            }
        }

        long endTime = System.currentTimeMillis(); // 코드 실행 후 시간 측정
        double time = (endTime - startTime) / 1000.0;
        System.out.println("걸린 시간 : " + time + "초");
    }
}
