package com.example.cta_t4.Fragment;

import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cta_t4.Activity.LoginActivity;
import com.example.cta_t4.Activity.MainActivity;
import com.example.cta_t4.R;
import com.example.cta_t4.Validator;
import com.example.cta_t4.entity.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class HomeFragment extends Fragment {
    View vMain;

    Button login_btn;
    Button signout_btn;
    EditText cal_goal;
    Button set_cal_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_main, container, false);
        signout_btn = vMain.findViewById(R.id.signout_btn);
        cal_goal = vMain.findViewById(R.id.cal_goal);
        set_cal_btn = vMain.findViewById(R.id.add_goal_btn);
        final User login_user = ((MainActivity)getActivity()).getLogin_user();
        Async async = new Async();
        if (login_user != null) {
            String firstName = login_user.getName();
            async.execute(new String[] {firstName});
        }
        else{
            async.execute("");
        }
        login_btn = vMain.findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        set_cal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cal_string = cal_goal.getText().toString();
                if(login_user != null && Validator.checkDouble(cal_string)){
                    Double calorie = Double.parseDouble(cal_string);
                    Integer userId = login_user.getUserId();
                    ((MainActivity)getActivity()).getCal_map().put(userId,calorie);
                }
                else if (login_user == null) {
                    Toast.makeText((MainActivity)getActivity(),"Please Login first!",Toast.LENGTH_SHORT).show();
                }
                else if (!Validator.checkDouble(cal_string)){
                    cal_goal.setError("Calorie goal must be a number!");
                    cal_goal.requestFocus();
                }
            }
        });
        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setLogin_user(null);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return vMain;
    }

    private class Async extends AsyncTask<String,Void,String>{
        TextView welcome = vMain.findViewById(R.id.welcome);
        TextView datetime = vMain.findViewById(R.id.current_datetime);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        @Override
        protected String doInBackground(String... strings) {
            String firstName = strings[0];
            return firstName;
        }

        @Override
        protected void onPostExecute(String firstName) {
            welcome.setText("Welcome " + firstName + "!");
            datetime.setText("Time now: " + dateFormat.format(date));
        }
    }
}
