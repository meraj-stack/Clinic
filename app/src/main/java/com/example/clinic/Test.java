package com.example.clinic;

public class Test {
    String id;

    String testname;
    String testdescription;

    public Test(String testname, String testdescription){
        this.testname=testname;
        this.testdescription=testdescription;
    }

    public Test(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public String getTestdescription() {
        return testdescription;
    }

    public void setTestdescription(String testdescription) {
        this.testdescription = testdescription;
    }
}
