package data_struct;

public class User {
    public String username;
    public String password;
    public String sex;
    public String age;
    public String phone;
    public String descrption;
    public String image;

    public User(
            String username,
            String password,
            String sex,
            String age,
            String phone,
            String descrption,
            String image
    ){
        this.username=username;
        this.password=password;
        this.age=age;
        this.sex=sex;
        this.phone=phone;
        this.descrption=descrption;
        this.image=image;
    }

    public void setUsername(String s){
        username=s;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername(){
        return username;
    }
}
