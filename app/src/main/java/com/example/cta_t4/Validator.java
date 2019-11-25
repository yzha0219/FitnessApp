package com.example.cta_t4;

import com.example.cta_t4.entity.Credential;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static boolean checkEmail(CharSequence email){
        if (email == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean checkNumeric(String string) {
        if (string == null || "".equals(string)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNumeric = pattern.matcher(string);
        if (isNumeric.matches()) {
            return true;
        }
        return false;
    }

    public static boolean userExist(String name) throws JSONException {
        JSONObject credential = new JSONObject(RestClient.getCredentialByUsername(name));
        if (credential == null)
            return false;
        else
            return true;
    }


    public static boolean checkDouble(String string){
        if (string == null || "".equals(string)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]+[.]{0,1}[0-9]*");
        Matcher isDouble = pattern.matcher(string);
        if (isDouble.matches()) {
            return true;
        }
        return false;
    }

    public static String getUnit(String string){
        Pattern unit_pat = Pattern.compile("\\d+([A-Za-z]+)");
        Matcher ser_unit_mat = unit_pat.matcher(string);
        if(ser_unit_mat.find()) {
            return ser_unit_mat.group(1);
        }
        return "";
    }

    public static Double getAmount(String string){
        Pattern amount_pat = Pattern.compile("(\\d+\\.?\\d*)");
        Matcher ser_amount_mat = amount_pat.matcher(string);
        if(ser_amount_mat.find()) {
            Double amount = Double.parseDouble(ser_amount_mat.group());
            return amount;
        }
        return 0.0;
    }

    public static Long getSecretId(String string){
        Pattern id_pat = Pattern.compile("(\\d+)");
        Matcher id_mat = id_pat.matcher(string);
        if(id_mat.find()) {
            Long id = Long.parseLong(id_mat.group());
            return id;
        }
        return 0L;
    }

    public static String getFoodName(String string){
        Pattern name_pat = Pattern.compile("(\\D+)");
        Matcher name_mat = name_pat.matcher(string);
        if(name_mat.find()) {
            return name_mat.group();
        }
        return "";
    }

    public static Float toFloat(Double value) {
        return value == null ? null : value.floatValue();
    }
}
