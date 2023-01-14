package com.example.dunno.Model;

public class patient {
    public String name , email="lolo" , password="psps", doctor_name ;

    public patient (){
    }

    public patient(String email, String name, String password, String doctor_name) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.doctor_name = doctor_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }
}
