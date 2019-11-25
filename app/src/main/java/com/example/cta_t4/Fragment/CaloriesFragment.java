package com.example.cta_t4.Fragment;

import androidx.fragment.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cta_t4.Activity.MainActivity;
import com.example.cta_t4.R;
import com.example.cta_t4.RestClient;
import com.example.cta_t4.SQLite.DailySteps;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CaloriesFragment extends Fragment {
    View vCal;

    TextView cal_info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vCal = inflater.inflate(R.layout.fragment_calorie,container,false);
        cal_info = vCal.findViewById(R.id.cal_full_info);
        DisplayInfo displayInfo = new DisplayInfo();
        displayInfo.execute();
        return vCal;
    }


    private class DisplayInfo extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {
            Integer userId = ((MainActivity)getActivity()).getLogin_user().getUserId();
            Double cal_goal = ((MainActivity)getActivity()).getCal_map().get(userId);
            List<DailySteps> stepsList = new ArrayList<>();
            Date date = new Date();
            java.sql.Date date_sql = new java.sql.Date(date.getTime());
            Integer totalSteps = 0;
            for(DailySteps dailySteps:stepsList){
                totalSteps += dailySteps.getSteps();
            }
            Double burn_steps = 0.0;
            Double burn_rest = 0.0;
            Double total_con = 0.0;
            try {
                JSONObject burn_steps_json = new JSONObject(RestClient.CalculateCaloriesBurnedPerStep(userId));
                JSONObject burn_rest_json = new JSONObject(RestClient.totalCalBurnedRest(userId));
                JSONObject con_json = new JSONObject(RestClient.totalCalConsumed(userId,date_sql));
                burn_steps = burn_steps_json.getDouble("CalculateCaloriesBurnedPerStep") * totalSteps;
                burn_rest = burn_rest_json.getDouble("totalCalBurnedRest");
                total_con = con_json.getDouble("totalCalConsumed");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Double total_burn = burn_rest + burn_steps;
            return "Calorie Goal: " + cal_goal + "\n" +
                    "Total Steps Taken: " + totalSteps + "\n" +
                    "Total Calorie Consumed: " + total_con + "\n" +
                    "Total Calorie Burned: " + total_burn;
        }

        @Override
        protected void onPostExecute(String string) {
            cal_info.setText(string);
        }
    }

}
