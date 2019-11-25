package com.example.cta_t4.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cta_t4.R;
import com.example.cta_t4.RestClient;
import com.example.cta_t4.Validator;
import com.example.cta_t4.entity.Credential;
import com.example.cta_t4.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final User user = new User(null,null,null,null,null,null,null,null,null,null,null,null);
        final EditText login_username = findViewById(R.id.login_username);
        final EditText login_password = findViewById(R.id.login_password);
        final Button login_btn = findViewById(R.id.btn_login);
        final EditText s_firstName = findViewById(R.id.first_name);
        final EditText s_surname = findViewById(R.id.surname);
        final EditText s_username = findViewById(R.id.username);
        final EditText s_pass = findViewById(R.id.password);
        final EditText s_email = findViewById(R.id.email);
        final TextView s_dob = findViewById(R.id.dob);
        Button dob_btn = findViewById(R.id.btn_dob);
        final RadioGroup sexRadionGroup = findViewById(R.id.radioSex);
        List<Integer> levels = new ArrayList<>();
        levels.add(1);
        levels.add(2);
        levels.add(3);
        levels.add(4);
        levels.add(5);
        Spinner level_of_activity = findViewById(R.id.level_spinner);
        ArrayAdapter<Integer> levelSpinner = new ArrayAdapter<>(LoginActivity.this,android.R.layout.simple_spinner_item, levels);
        levelSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level_of_activity.setAdapter(levelSpinner);
        final EditText s_height = findViewById(R.id.height);
        final EditText s_weight = findViewById(R.id.weight);
        final EditText s_address = findViewById(R.id.address);
        final EditText s_postcode = findViewById(R.id.postcode);
        final EditText s_step = findViewById(R.id.step);
        Button sign_btn = findViewById(R.id.btn_register);

        dob_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                DatePickerDialog dpd = new DatePickerDialog(LoginActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int m, int dayOfMonth) {
                        s_dob.setText(Integer.toString(y) + "-" + Integer.toString(m + 1) + "-" + Integer.toString(dayOfMonth));
                    }
                },year,month,day);
                dpd.show();
            }
        });

        level_of_activity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int select_level = Integer.parseInt(parent.getItemAtPosition(position).toString());
                user.setLevelOfActivity(select_level);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int genderId = sexRadionGroup.getCheckedRadioButtonId();
                RadioButton genderRadiobtn = findViewById(genderId);
                SignUpAsyncTask signUpAsyncTask = new SignUpAsyncTask();

                    if(s_firstName.getText().toString().isEmpty()){
                        s_firstName.setError("First name is null!");
                        s_firstName.requestFocus();
                    }
                    else if(s_surname.getText().toString().isEmpty()){
                        s_surname.setError("Surname is null!");
                        s_surname.requestFocus();
                    }
                    else if(s_username.getText().toString().isEmpty()){
                        s_username.setError("Username is null or exist!");
                        s_username.requestFocus();
                    }
                    else if(s_pass.getText().toString().isEmpty()) {
                        s_pass.setError("Password is null!");
                        s_pass.requestFocus();
                    }
                    else if(!Validator.checkEmail(s_email.getText().toString())){
                        s_email.setError("Email format is wrong!");
                        s_email.requestFocus();
                    }
                    else if(s_dob.getText().toString().isEmpty()){
                        s_dob.setText("Date of birth is null!");
                        s_dob.requestFocus();
                    }
                    else if(s_height.getText().toString().isEmpty() || !Validator.checkDouble(s_height.getText().toString())){
                        s_height.setError("Your Weight is null or is not an Double type!");
                        s_height.requestFocus();
                    }
                    else if(s_weight.getText().toString().isEmpty() || !Validator.checkDouble(s_weight.getText().toString())){
                        s_weight.setError("Your Height is null or is not an Double type!");
                        s_weight.requestFocus();
                    }
                    else if(s_address.getText().toString().isEmpty()){
                        s_address.setError("Address is null!");
                        s_address.requestFocus();
                    }
                    else if(s_postcode.getText().toString().isEmpty()){
                        s_postcode.setError("Postcode is null!");
                        s_postcode.requestFocus();
                    }
                    else if(s_step.getText().toString().isEmpty() || !Validator.checkNumeric(s_step.getText().toString())){
                        s_step.setError("Steps per mile is null or is not an Integer type!");
                        s_step.requestFocus();
                    }
                    else {
                        signUpAsyncTask.execute(s_firstName.getText().toString(),
                                s_surname.getText().toString(), s_pass.getText().toString(), s_email.getText().toString(), s_dob.getText().toString(),
                                s_height.getText().toString(), s_weight.getText().toString(), genderRadiobtn.getText().toString(),
                                s_address.getText().toString(), s_postcode.getText().toString(), user.getLevelOfActivity().toString(),
                                s_step.getText().toString(), s_username.getText().toString());
                    }

            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
                String username = login_username.getText().toString();
                String pass = login_password.getText().toString();
                loginAsyncTask.execute(username,pass);

            }
        });

    }

    private class SignUpAsyncTask extends AsyncTask<String, Void, String>
        {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            String firstName = params[0];
            String surname = params[1];
            String password = params[2];
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            String passHash = sb.toString();
            String email = params[3];
            Date dob = null;
            try {
                dob = new SimpleDateFormat("yyyy-MM-dd").parse(params[4]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Double height = Double.parseDouble(params[5]);
            Double weight = Double.parseDouble(params[6]);
            String gender = params[7];
            String address = params[8];
            Integer postcode = Integer.parseInt(params[9]);
            Integer levelOfActivity = Integer.parseInt(params[10]);
            Integer stepsPerMile = Integer.parseInt(params[11]);
            String username = params[12];
            Date date = new Date();
            User user = new User(firstName,surname,email,dob,height,weight,gender,address,postcode,levelOfActivity,stepsPerMile);
            Integer user_Id = RestClient.createUser(user);
            User userId = new User(user_Id);
            Credential credential = new Credential(username,passHash,date,userId);
            RestClient.createCredential(credential);
            return "Sign up successfully";
        }
        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
        }
    }

    private class LoginAsyncTask extends AsyncTask<String,Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            String credentialString = RestClient.getCredentialByUsername(strings[0]);
            if(!credentialString.equals("")) {
                JSONArray credentialJsonArray = new JSONArray();
                JSONObject credentialJson = new JSONObject();
                User user = new User();
                try {
                    credentialJsonArray = new JSONArray(credentialString);
                    credentialJson = credentialJsonArray.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String pass = new String();
                String log_pass = strings[1];
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                byte[] hashInBytes = md.digest(log_pass.getBytes(StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                for (byte b : hashInBytes) {
                    sb.append(String.format("%02x", b));
                }
                String passHash = sb.toString();
                try {
                    pass = credentialJson.get("passwordHash").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (pass != null && pass.equals(passHash)) {
                    String userString = null;
                    Integer userId = null;
                    String name = null;
                    String surname = null;
                    String email = null;
                    Date dob = null;
                    Double height = null;
                    Double weight = null;
                    String gender = null;
                    String address = null;
                    Integer postcode = null;
                    Integer level = null;
                    Integer step = null;
                    try {
                        userString = credentialJson.get("userId").toString();
                        JSONObject userJson = new JSONObject(userString);
                        userId = Integer.parseInt(userJson.get("userId").toString());
                        name = userJson.get("name").toString();
                        surname = userJson.get("surname").toString();
                        email = userJson.get("email").toString();
                        dob = null;
                        try {
                            dob = new SimpleDateFormat("yyyy-MM-dd").parse(userJson.get("dob").toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        height = Double.parseDouble(userJson.get("height").toString());
                        weight = Double.parseDouble(userJson.get("weight").toString());
                        gender = userJson.get("gender").toString();
                        address = userJson.get("address").toString();
                        postcode = Integer.parseInt(userJson.get("postcode").toString());
                        level = Integer.parseInt(userJson.get("levelOfActivity").toString());
                        step = Integer.parseInt(userJson.get("stepsPerMile").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    user.setUserId(userId);
                    user.setName(name);
                    user.setSurname(surname);
                    user.setEmail(email);
                    user.setDob(dob);
                    user.setHeight(height);
                    user.setWeight(weight);
                    user.setGender(gender);
                    user.setAddress(address);
                    user.setPostcode(postcode);
                    user.setLevelOfActivity(level);
                    user.setStepsPerMile(step);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("login_user", user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return "Login successful!";
                } else if (pass == null) {
                    return "Please enter your password";
                } else {
                    return "The password is incorrect";
                }
            }
            else
                return "The user doesn't exist";
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
        }
    }
}


