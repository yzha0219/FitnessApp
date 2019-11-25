package com.example.cta_t4.Fragment;

import androidx.fragment.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cta_t4.Activity.MainActivity;
import com.example.cta_t4.R;
import com.example.cta_t4.RestClient;
import com.example.cta_t4.SQLite.DailySteps;
import com.example.cta_t4.SQLite.DailyStepsDatabase;
import com.example.cta_t4.entity.Report;
import com.example.cta_t4.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.room.Room;


public class StepsFragment extends Fragment {

    View vSteps;
    DailyStepsDatabase db;
    EditText step_input;
    EditText stepid_edit;
    EditText steps_edit;
    TextView add_info;
    TextView edit_info;
    TextView display_info;
    Button add_btn;
    Button edit_btn;
    Button display_all;
    Button post_netbean;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vSteps = inflater.inflate(R.layout.fragment_steps, container, false);
        db = Room.databaseBuilder(getActivity(),
                DailyStepsDatabase.class, "dailysteps_database")
                .fallbackToDestructiveMigration()
                .build();
        step_input = vSteps.findViewById(R.id.step_input);
        stepid_edit = vSteps.findViewById(R.id.stepid_edit);
        steps_edit = vSteps.findViewById(R.id.steps_edit);
        add_btn = vSteps.findViewById(R.id.add_step_btn);
        edit_btn = vSteps.findViewById(R.id.edit_step_btn);
        add_info = vSteps.findViewById(R.id.input_info);
        edit_info = vSteps.findViewById(R.id.edit_info);
        display_info = vSteps.findViewById(R.id.display_all);
        display_all = vSteps.findViewById(R.id.display_btn);
        post_netbean = vSteps.findViewById(R.id.post_db);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertDatabase insertDatabase = new InsertDatabase();
                insertDatabase.execute();
            }
        });
        display_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadDatabase readDatabase = new ReadDatabase();
                readDatabase.execute();
            }
        });
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDatabase updateDatabase = new UpdateDatabase();
                updateDatabase.execute();
            }
        });
        post_netbean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostToNetbean postToNetbean = new PostToNetbean();
                postToNetbean.execute();
            }
        });
        return vSteps;
    }

    private class InsertDatabase extends AsyncTask<Void, Void, String> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(Void... params) {
            Integer userId = ((MainActivity) getActivity()).getLogin_user().getUserId();
            String time = LocalTime.now().toString();
            try {
                Integer steps = Integer.parseInt(step_input.getText().toString());
                Double cal_goal = ((MainActivity) getActivity()).getCal_map().get(userId);
                DailySteps dailySteps = new DailySteps(userId, time, steps, cal_goal);
                long id = db.dailyStepsDao().insert(dailySteps);
                return "StepID:"+ id +"UserID: " + userId + " Time:" + time + " Steps: " + steps + " Cal Goal: " + cal_goal;
            } catch (Exception e) {
                return "Please input a valid steps";
            }
        }

        @Override
        protected void onPostExecute(String string) {
            add_info.setText(string);
        }
    }

    private class ReadDatabase extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            List<DailySteps> dailyStepsList = db.dailyStepsDao().getAll();
            String allSteps = "";
            for(DailySteps dailySteps : dailyStepsList){
                String stepsString = "StepID:"+ dailySteps.getDailyStepId() +"UserID: " + dailySteps.getUserId() +
                        " Time:" + dailySteps.getTime() + " Steps: " + dailySteps.getSteps() + " Cal Goal: " + dailySteps.getCalorie_goal();
                allSteps += stepsString + "\n";
            }
            return allSteps;
        }

        @Override
        protected void onPostExecute(String string) {
            display_info.setText(string);
        }
    }

    private class UpdateDatabase extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            DailySteps dailySteps = null;
            try {
                Integer stepId = Integer.parseInt(stepid_edit.getText().toString());
                Integer steps = Integer.parseInt(steps_edit.getText().toString());
                dailySteps = db.dailyStepsDao().findByID(stepId);
                if (dailySteps != null) {
                    dailySteps.setSteps(steps);
                    db.dailyStepsDao().updateUsers(dailySteps);
                    return "StepID:" + dailySteps.getDailyStepId() + "UserID: " + dailySteps.getUserId() +
                            " Time:" + dailySteps.getTime() + " Steps: " + dailySteps.getSteps() + " Cal Goal: " + dailySteps.getCalorie_goal();
                }
                return "Nothing found refer to you input StepID.";
            }catch (Exception e){
                return "Your input should be number!";
            }
        }

        @Override
        protected void onPostExecute(String string) {
            edit_info.setText(string);
        }
    }

    private class PostToNetbean extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            List<Integer> idList = new ArrayList<Integer>();
            List<DailySteps> allsteps = db.dailyStepsDao().getAll();
            Date date = new Date();
            java.sql.Date date_sql = new java.sql.Date(date.getTime());
            for(DailySteps dailySteps:allsteps){
                if(!idList.contains(dailySteps.getUserId()))
                    idList.add(dailySteps.getUserId());
            }
            for(Integer id:idList){
                List<DailySteps> stepsList = db.dailyStepsDao().findByUserId(id);
                Integer totalSteps = 0;
                for(DailySteps dailySteps:stepsList){
                    totalSteps += dailySteps.getSteps();
                }
                Double calorieGoal = ((MainActivity)getActivity()).getCal_map().get(id);
                User user = new User(id);
                Double burn_steps = 0.0;
                Double burn_rest = 0.0;
                Double total_con = 0.0;
                try {
                    JSONObject burn_steps_json = new JSONObject(RestClient.CalculateCaloriesBurnedPerStep(id));
                    JSONObject burn_rest_json = new JSONObject(RestClient.totalCalBurnedRest(id));
                    JSONObject con_json = new JSONObject(RestClient.totalCalConsumed(id,date_sql));
                    burn_steps = burn_steps_json.getDouble("CalculateCaloriesBurnedPerStep") * totalSteps;
                    burn_rest = burn_rest_json.getDouble("totalCalBurnedRest");
                    total_con = con_json.getDouble("totalCalConsumed");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Double total_burn = burn_rest + burn_steps;
                Report report = new Report(calorieGoal,date,total_burn,total_con,totalSteps,user);
                RestClient.createReport(report);
            }
            db.dailyStepsDao().deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(((MainActivity)getActivity()),"Data has been stored in the NetBean!",Toast.LENGTH_SHORT).show();
        }
    }
}
