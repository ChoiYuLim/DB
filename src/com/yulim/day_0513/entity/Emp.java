package com.yulim.day_0513.entity;

public class Emp {
    private String ename;
    private int empno;
    private String job;
    private String sal;
    private String comm;

    public Emp(String ename, int empno, String job, String sal, String comm) {
        super();
        this.ename = ename;
        this.empno = empno;
        this.job = job;
        this.sal = sal;
        this.comm = comm;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getSal() {
        return sal;
    }

    public void setSal(String sal) {
        this.sal = sal;
    }

    public String getComm() {
        if(comm==null) return "0";
        return comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }

    public int getEmpno() {
        return empno;
    }

    public void setEmpno(int empno) {
        this.empno = empno;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

}
