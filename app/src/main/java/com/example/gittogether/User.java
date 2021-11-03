package com.example.gittogether;

public class User {

    public String firstName, lastName, email, hobby1, hobby2, hobby3, address, userID;

    public User() {
    }

    public User(String firstName, String lastName, String email, String hobby1, String hobby2, String hobby3, String address){
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.hobby1=hobby1;
        this.hobby2=hobby2;
        this.hobby3=hobby3;
        this.address=address;

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getHobby1() {
        return hobby1;
    }

    public String getHobby2() {
        return hobby2;
    }

    public String getHobby3() {
        return hobby3;
    }

    public String getAddress() {
        return address;
    }

    public String getuserID() {
        return userID;
    }
}
