package my.edu.utar.foodcourtdrinkorderingapp.Domain;

public class User {
    String Name,Email,PhoneNo;

    public User(){

    }

    public User(String Name, String Email, String PhoneNo) {
        this.Name = Name;
        this.Email = Email;
        this.PhoneNo = PhoneNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }
}
