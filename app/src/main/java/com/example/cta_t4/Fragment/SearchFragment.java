package com.example.cta_t4.Fragment;

import androidx.fragment.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cta_t4.Activity.LoginActivity;
import com.example.cta_t4.Activity.MainActivity;
import com.example.cta_t4.R;
import com.example.cta_t4.RestClient;
import com.example.cta_t4.Validator;
import com.example.cta_t4.entity.Consumption;
import com.example.cta_t4.entity.Food;
import com.example.cta_t4.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



public class SearchFragment extends Fragment {
    View vSearch;
    List<HashMap<String, String>> catListArray;
    SimpleAdapter catListAdapter;
    SimpleAdapter fooListAdapter;
    ListView category_list;
    ListView foodname_list;
    String[] catHead = new String[] {"CATEGORY"};
    String[] fooHead = new String[] {"FOODID","FOODNAME"};
    int[] catCell = new int[] {R.id.category};
    int[] fooCell = new int[] {R.id.foodId,R.id.foodname};
    TextView food_desc;
    EditText serving_quan;
    Button addCon;
    Food con_food;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vSearch = inflater.inflate(R.layout.fragment_search, container, false);
        category_list = vSearch.findViewById(R.id.category_list);
        foodname_list = vSearch.findViewById(R.id.foodname_list);
        serving_quan = vSearch.findViewById(R.id.quantity);
        food_desc = vSearch.findViewById(R.id.food_desc);
        addCon = vSearch.findViewById(R.id.addCon);
        catListArray = new ArrayList<HashMap<String, String>>();

        final GetCategoryAsync getCategoryAsync = new GetCategoryAsync();
        getCategoryAsync.execute();
        category_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map = (HashMap<String, String>) category_list.getItemAtPosition(position);
                String category_name = map.get("CATEGORY");
                GetFoodNameAsync getFoodNameAsync = new GetFoodNameAsync();
                getFoodNameAsync.execute(category_name);
            }
        });
        foodname_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map = (HashMap<String, String>) foodname_list.getItemAtPosition(position);
                Integer foodId = Integer.parseInt(map.get("FOODID"));
                con_food = new Food(foodId);
                GetFoodDetailsAsync getFoodDetailsAsync = new GetFoodDetailsAsync();
                getFoodDetailsAsync.execute(foodId);
            }
        });
        addCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddConsumptionAsync addConsumptionAsync = new AddConsumptionAsync();
                addConsumptionAsync.execute();
            }
        });
        return vSearch;
    }

private class GetCategoryAsync extends AsyncTask<Void,Void,List<String>>{
    @Override
    protected List<String> doInBackground(Void... voids) {
        String foodString = RestClient.getAllFood();
        JSONArray foodJsonArray = new JSONArray();
        List<String> categoryList = new ArrayList<>();
        try {
            foodJsonArray = new JSONArray(foodString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0;i < foodJsonArray.length();i++) {
            JSONObject foodJson = new JSONObject();
            String category = new String();
            try {
                foodJson = foodJsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                category = foodJson.get("category").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (category != null & !categoryList.contains(category)) {
                categoryList.add(category);
            }
        }
        return categoryList;
    }

    @Override
    protected void onPostExecute(List<String> list) {
        for (String cat:list) {
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("CATEGORY",cat);
            catListArray.add(map);
        }
        catListAdapter = new SimpleAdapter(getActivity(),catListArray,R.layout.category_list,catHead,catCell);
        category_list.setAdapter(catListAdapter);
    }
}

private class GetFoodNameAsync extends AsyncTask<String,Void,List<String>>{
    @Override
    protected List<String> doInBackground(String... strings) {
        String foodString = RestClient.getFoodByCategory(strings[0]);
        JSONArray foodJsonArray = new JSONArray();
        List<String> foodList = new ArrayList<>();
        try {
            foodJsonArray = new JSONArray(foodString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0;i < foodJsonArray.length();i++) {
            JSONObject foodJson = new JSONObject();
            String foodId = new String();
            String foodName = new String();
            try {
                foodJson = foodJsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                foodId = foodJson.get("foodId").toString();
                foodName = foodJson.get("name").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            foodList.add(foodId);
            foodList.add(foodName);
            }
        return foodList;
    }

    @Override
    protected void onPostExecute(List<String> list) {
        List<HashMap<String,String>> fooListArray = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < list.size();i += 2) {
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("FOODID",list.get(i));
            map.put("FOODNAME",list.get(i+1));
            fooListArray.add(map);
        }
        fooListAdapter = new SimpleAdapter(getActivity(),fooListArray,R.layout.foodname_list,fooHead,fooCell);
        foodname_list.setAdapter(fooListAdapter);
    }
}

private class GetFoodDetailsAsync extends AsyncTask<Integer,Void,JSONObject>{

    @Override
    protected JSONObject doInBackground(Integer... integers) {
        String foodString = RestClient.GetFoodById(integers[0]);
        JSONObject foodJson = new JSONObject();
        try {
            foodJson = new JSONObject(foodString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return foodJson;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        String name = new String();
        String category = new String();
        String serving_ammount = new String();
        String serving_unit = new String();
        String calorie = new String();
        String fat = new String();
        String food = new String();
        try {
            name = jsonObject.get("name").toString();
            category = jsonObject.get("category").toString();
            serving_ammount = jsonObject.get("servingAmount").toString();
            serving_unit = jsonObject.get("servingUnit").toString();
            calorie = jsonObject.get("calorieAmount").toString();
            fat = jsonObject.get("fat").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        food = "Food name: " + name + "   |   " +
                "Category: " + category + "\n" +
                "Serving Amount: " + serving_ammount + "   |   " +
                "Serving Unit" + serving_unit + "\n" +
                "Calorie Amount: " + calorie + "   |   " +
                "Fat: " + fat;
        food_desc.setText(food);
    }
}

private class AddConsumptionAsync extends AsyncTask<Void,Void,String>{
    @Override
    protected String doInBackground(Void... voids) {
        User user = new User(((MainActivity)getActivity()).getLogin_user().getUserId());
        Consumption consumption = new Consumption();
        Date date = new Date();
        String quanString = serving_quan.getText().toString();
        if (Validator.checkDouble(quanString) && con_food != null) {
            Double quantity = Double.parseDouble(serving_quan.getText().toString());
            consumption = new Consumption(date,quantity,con_food,user);
            RestClient.createConsumption(consumption);
            return "Consumption has been added successfully!";
        }
        else if (con_food == null){
            return "Please select a food first!";
        }
        else if (!Validator.checkDouble(quanString)) {
            return "Quantity must be a number!";
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        Toast.makeText(((MainActivity)getActivity()),response,Toast.LENGTH_SHORT).show();
    }
}
}

