package ir.amjadrad.asshanbevarzesh.object;

import java.util.Date;

public class User {

    // 0 for boy , 1 for man , 2 for woman
    private int id, sex_type, age, weight, height, actiivty_type, birth_y, birth_m, birth_d;
    private String name, phone, email, activity , qr_code;
    private boolean sex = false;// false for woman

    private int my_money , shambe_money;

    public int getMy_money() {
        return my_money;
    }

    public void setMy_money(int my_money) {
        this.my_money = my_money;
    }

    public int getShambe_money() {
        return shambe_money;
    }

    public void setShambe_money(int shambe_mony) {
        this.shambe_money = shambe_mony;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public double getBmr() {
        return bmr;
    }

    public void setBmr(double bmr) {
        this.bmr = bmr;
    }

    public int getBirth_y() {
        return birth_y;
    }

    public void setBirth_y(int birth_y) {
        this.birth_y = birth_y;
    }

    public int getBirth_m() {
        return birth_m;
    }

    public void setBirth_m(int birth_m) {
        this.birth_m = birth_m;
    }

    public int getBirth_d() {
        return birth_d;
    }

    public void setBirth_d(int birth_d) {
        this.birth_d = birth_d;
    }

    private double bmr;


    public int getActiivty_type() {
        return actiivty_type;
    }

    public void setActiivty_type(int actiivty_type) {
        this.actiivty_type = actiivty_type;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public double getBMR() throws Exception {
        double bmr = 0;
        if (sex) {
            //woman
            bmr = (10 * weight) + (6.25 * height) - (5 * getAge()) - 161;
        } else {
            //man
            bmr = (10 * weight) + (6.25 * height) - (5 * getAge()) + 5;
        }

        return bmr;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSex_type() throws Exception {

        if (!sex) {
            return 2;
        } else {
            if (getAge() < 18) {
                return 0;
            } else {
                return 1;
            }
        }

    }

    public void setSex_type(int sex_type) {
        this.sex_type = sex_type;
    }

    public int getAge() throws Exception {

//        int first_slash = getBirth_date().indexOf("/");
//        int year_b = Integer.parseInt(getBirth_date().substring(0, first_slash));
//        int month_b = Integer.parseInt(getBirth_date().substring(first_slash + 1, getBirth_date().substring(first_slash + 1).indexOf("/")));

//        Log.i(">>>>>>>>>", "y= " + year_b + " , m=" + month_b);

        int month_b = birth_m;
        int year_b = birth_y;


        if (month_b < 10) {
            year_b += 621;
        } else {
            year_b += 622;
        }

        Date date = new Date();
        return (date.getYear() + 1900) - year_b;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
