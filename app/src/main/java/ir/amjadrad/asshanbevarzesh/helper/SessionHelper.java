package ir.amjadrad.asshanbevarzesh.helper;

import android.content.Context;
import android.content.SharedPreferences;

import ir.amjadrad.asshanbevarzesh.object.User;


public class SessionHelper {

    private SharedPreferences sharedPreferences;
    private String name = "mypref";
    private SharedPreferences.Editor editor;


    public SessionHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public void setLogin(boolean login , String phone){
        editor.putBoolean("login" , login);
        editor.putString("phone" , phone);
        editor.commit();
    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean("login" , false);
    }


    public void saveUser(User user) {

        try {

            editor.putString("email" , user.getEmail());
            editor.putString("name" , user.getName());
            editor.putString("qr_code" , user.getQr_code());
            editor.putBoolean("sex" , user.isSex());
            editor.putString("phone",user.getPhone());
            editor.putInt("id",user.getId());
            editor.putInt("bitrh_d",user.getBirth_d());
            editor.putInt("bitrh_m",user.getBirth_m());
            editor.putInt("bitrh_y",user.getBirth_y());
            editor.putInt("height",user.getHeight());
            editor.putInt("weight",user.getWeight());
            editor.putInt("my_money",user.getMy_money());
            editor.putInt("shambe_money",user.getShambe_money());
            editor.putLong("bmr", (long) user.getBMR());

        } catch (Exception e) {
            e.printStackTrace();
        }

        editor.commit();
    }

    public User getUser() {
        User user = new User();

        user.setPhone(sharedPreferences.getString("phone", ""));
        user.setEmail(sharedPreferences.getString("email", ""));
        user.setName(sharedPreferences.getString("name", ""));
        user.setQr_code(sharedPreferences.getString("qr_code", ""));
        user.setSex(sharedPreferences.getBoolean("sex", false));
        user.setHeight(sharedPreferences.getInt("height", -1));
        user.setWeight(sharedPreferences.getInt("weight", -1));
        user.setBirth_d(sharedPreferences.getInt("birth_d", -1));
        user.setBirth_m(sharedPreferences.getInt("birth_m", -1));
        user.setBirth_y(sharedPreferences.getInt("birth_y", -1));
        user.setId(sharedPreferences.getInt("id", -1));
        user.setMy_money(sharedPreferences.getInt("my_money", 0));
        user.setShambe_money(sharedPreferences.getInt("shambe_money", 0));
        user.setBmr(sharedPreferences.getLong("bmr", 0));
        return user;
    }


}
