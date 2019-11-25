package com.example.cta_t4.Fragment;


import android.app.DatePickerDialog;
import androidx.fragment.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.cta_t4.Activity.MainActivity;
import com.example.cta_t4.R;
import com.example.cta_t4.RestClient;
import com.example.cta_t4.Validator;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PieFragment extends Fragment {
    View rView;
    DatePickerDialog picker;
    TextView pieDate;
    PieChart pieChart;
    Button pie_gen_btn;
    public static final int[] PIE_COLORS = {
            Color.rgb(181, 194, 202), Color.rgb(129, 216, 200), Color.rgb(241, 214, 145),
            Color.rgb(108, 176, 223)};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rView = inflater.inflate(R.layout.fragment_pie, container, false);
        pieDate = rView.findViewById(R.id.select_date_tv);
        pieChart = rView.findViewById(R.id.pie_chart);
        pie_gen_btn = rView.findViewById(R.id.search_pie_btn);
        pieDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                picker = new DatePickerDialog((MainActivity) getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                pieDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();

            };
        });

        pie_gen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Double> dataList = new ArrayList<Double>();
                String datePicked = pieDate.getText().toString();
                FindReportByDateAsynTask findReportByDateAsynTask = new FindReportByDateAsynTask();
                try {
                    dataList =  findReportByDateAsynTask.execute(datePicked).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (dataList.size() == 4) {
                    List<PieEntry> yVals = new ArrayList<>();
                    yVals.add(new PieEntry(Validator.toFloat(dataList.get(0)*100), "Total Consumed"));
                    if(dataList.get(1) < 0 ) {
                        yVals.add(new PieEntry(Validator.toFloat(Math.abs(dataList.get(1) * 100)), "Calorie Deficit"));
                    }else
                        yVals.add(new PieEntry(Validator.toFloat(Math.abs(dataList.get(1) * 100)), "Remaining Goal"));
                    yVals.add(new PieEntry(Validator.toFloat(dataList.get(2)*100), "Burn At Rest"));
                    yVals.add(new PieEntry(Validator.toFloat(dataList.get(3)*100), "Burn By Steps"));
                    PieDataSet pieDataSet = new PieDataSet(yVals, "");
                    pieDataSet.setColors(PIE_COLORS);
                    pieDataSet.setValueFormatter(new PercentFormatter());
                    PieData pieData = new PieData(pieDataSet);
                    showChart(pieChart,pieData);
                    pieDate.setText("Your pie chart generated as bellow");
                }else{
                    pieDate.setText("That day doesn't has report, choose another date pls.");
                }
            }
        });

        return rView;

    }

    private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleRadius(60f);
        pieChart.setTransparentCircleRadius(64f);
        pieChart.setDrawCenterText(true);
        Description description = new Description();
        description.setText("This is your daily report of pie chart");
        pieChart.setDescription(description);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setRotationAngle(90);
        pieChart.setRotationEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Daily Calorie");
        pieData.setValueTextSize(15);
        pieChart.setData(pieData);
        Legend mLegend = pieChart.getLegend();
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);
        pieChart.animateXY(1000, 1000);
    }

    private class FindReportByDateAsynTask extends AsyncTask<String, Void, List<Double>> {

        @Override
        protected List<Double> doInBackground(String... strings) {
            List<Double> resultList = new ArrayList<Double>();
            Integer userId = ((MainActivity) getActivity()).getLogin_user().getUserId();
            String date = strings[0];
            String infoString = RestClient.totalConBurnedRemainCal(userId, date);
            String repString = RestClient.findReportById(userId);
            JSONArray infoArray = null;
            JSONArray repArray = null;
            try {
                infoArray = new JSONArray(infoString);
                repArray = new JSONArray(repString);
                JSONObject repJson = repArray.getJSONObject(0);
                Double total_step = repJson.getDouble("totalStepTaken");
                Double total_con = infoArray.getJSONObject(0).getDouble("totalCalConsumed");
                Double remaining = infoArray.getJSONObject(2).getDouble("remainCalories");
                String bmrStrng = RestClient.BMR(userId);
                JSONObject bmrJson = new JSONObject(bmrStrng);
                Double BMR = bmrJson.getDouble("BMR");
                String perString = RestClient.CalculateCaloriesBurnedPerStep(userId);
                JSONObject perJson = new JSONObject(perString);
                Double perStep = perJson.getDouble("CalculateCaloriesBurnedPerStep");
                Double burnSteps = perStep * total_step;
                Double total = total_con + remaining + BMR + burnSteps;
                DecimalFormat df = new DecimalFormat("0.000 ");
                resultList.add(Double.parseDouble(df.format(total_con/total)));
                resultList.add(Double.parseDouble(df.format(remaining/total)));
                resultList.add(Double.parseDouble(df.format(BMR/total)));
                resultList.add(Double.parseDouble(df.format(burnSteps/total)));
            } catch (JSONException e) {
                e.printStackTrace();
                return new ArrayList<Double>();
            }
            return resultList;
        }

    }

}

