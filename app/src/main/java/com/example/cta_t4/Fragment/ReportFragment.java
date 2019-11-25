package com.example.cta_t4.Fragment;

import android.app.DatePickerDialog;
import androidx.fragment.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import androidx.annotation.RequiresApi;

public class ReportFragment extends Fragment {
    View vReport;

    Button report_btn;
    TextView start_tv;
    TextView end_tv;
    long noOfDaysBetween;
    List<BarEntry> yVals1 = new ArrayList<BarEntry>();
    List<BarEntry> yVals2 = new ArrayList<BarEntry>();
    List<String> xLabel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vReport = inflater.inflate(R.layout.fragment_report, container, false);
        report_btn = vReport.findViewById(R.id.report_btn);
        start_tv = vReport.findViewById(R.id.start_date);
        end_tv = vReport.findViewById(R.id.end_date);
        noOfDaysBetween = 0;
        start_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int m, int dayOfMonth) {
                        start_tv.setText(Integer.toString(y) + "-" + Integer.toString(m + 1) + "-" + Integer.toString(dayOfMonth));
                    }
                }, year, month, day);
                dpd.show();
            }
        });
        end_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int m, int dayOfMonth) {
                        end_tv.setText(Integer.toString(y) + "-" + Integer.toString(m + 1) + "-" + Integer.toString(dayOfMonth));
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BarChart barChart = vReport.findViewById(R.id.barChart);
                barChart.setDrawBarShadow(false);
                barChart.setDrawValueAboveBar(true);
                Description description = new Description();
                description.setText("");
                barChart.setDescription(description);
                barChart.setMaxVisibleValueCount(50);
                barChart.setPinchZoom(false);
                barChart.setDrawGridBackground(false);

                XAxis xl = barChart.getXAxis();
                xl.setGranularity(1f);
                xl.setCenterAxisLabels(true);

                YAxis leftAxis = barChart.getAxisLeft();
                leftAxis.setDrawGridLines(false);
                leftAxis.setSpaceTop(30f);
                barChart.getAxisRight().setEnabled(false);

                //data
                float groupSpace = 0.04f;
                float barSpace = 0.02f;
                float barWidth = 0.46f;

                String start_date = start_tv.getText().toString();
                String end_date = end_tv.getText().toString();
                //Parsing the date
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-dd");
                LocalDate dateBefore = LocalDate.parse(start_date, formatter);
                LocalDate dateAfter = LocalDate.parse(end_date, formatter);

                //calculating number of days in between
                noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
                xLabel = new ArrayList<>();
                String incre_date = start_date;
                for (int i = 0; i < noOfDaysBetween; i++) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    try {
                        c.setTime(sdf.parse(incre_date));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //Incrementing the date by 1 day
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    incre_date = sdf.format(c.getTime());
                    xLabel.add(incre_date);
                }
                xl.setValueFormatter(new IndexAxisValueFormatter(xLabel));
                GetBarChartInfo getBarChartInfo = new GetBarChartInfo();
                List<Double> infoList = new ArrayList<>();
                try {
                    infoList = getBarChartInfo.execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int j = 0;
                for (int i = 0; i < xLabel.size(); i++) {
                    yVals1.add(new BarEntry(i, Float.valueOf(infoList.get(j).toString())));
                    yVals2.add(new BarEntry(i, Float.valueOf(infoList.get(j + 1).toString())));
                    j += 2;
                }
                BarDataSet set1, set2;

                if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
                    set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
                    set2 = (BarDataSet) barChart.getData().getDataSetByIndex(1);
                    set1.setValues(yVals1);
                    set2.setValues(yVals2);
                    barChart.getData().notifyDataChanged();
                    barChart.notifyDataSetChanged();
                } else {
                    set1 = new BarDataSet(yVals1, "totalCalConsumed");
                    set1.setColor(Color.rgb(104, 241, 175));
                    set2 = new BarDataSet(yVals2, "totalCalBurned");
                    set2.setColor(Color.rgb(164, 228, 251));

                    ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                    dataSets.add(set1);
                    dataSets.add(set2);

                    BarData data = new BarData(dataSets);
                    barChart.setData(data);
                }

                barChart.getBarData().setBarWidth(barWidth);
                barChart.groupBars(0,groupSpace, barSpace);

                barChart.invalidate();
            }
        });
        return vReport;
    }

    private class GetBarChartInfo extends AsyncTask<Void, Void, List<Double>> {
        @Override
        protected List<Double> doInBackground(Void... voids) {
            Integer userId = ((MainActivity) getActivity()).getLogin_user().getUserId();
            List<Double> infoList = new ArrayList<>();
            for (String date : xLabel) {
                try {
                    JSONArray infoArray = new JSONArray(RestClient.totalConBurnedRemainCal(userId, date));
                    JSONObject conJson = infoArray.getJSONObject(0);
                    JSONObject burnJson = infoArray.getJSONObject(1);
                    Double total_con = conJson.getDouble("totalCalConsumed");
                    Double total_burn = burnJson.getDouble("totalCalBurned");
                    infoList.add(total_con);
                    infoList.add(total_burn);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return infoList;
        }
    }
}
