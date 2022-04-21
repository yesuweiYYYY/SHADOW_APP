package data_struct;

public class localtion {
    public String username;
    public double x;
    public double y;

    public localtion(){};
    public localtion(String usrname, double x,double y){
        this.username=usrname;
        this.x=x;
        this.y=y;
    }

    @Override
    public String toString(){
        return username+",x"+String.valueOf(x)+",y"+String.valueOf(y);
    }
}
