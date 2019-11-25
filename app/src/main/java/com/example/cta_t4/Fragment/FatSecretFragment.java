package com.example.cta_t4.Fragment;

import androidx.fragment.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cta_t4.Activity.MainActivity;
import com.example.cta_t4.FatSecretGet;
import com.example.cta_t4.GoogleSearch;
import com.example.cta_t4.R;
import com.example.cta_t4.RestClient;
import com.example.cta_t4.Validator;
import com.example.cta_t4.entity.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.RequiresApi;


public class FatSecretFragment extends Fragment {

    View vFat;
    Long food_id;
    Double ser_amount;
    String ser_unit;
    String food_name;
    Double cal_amount;
    Double fat_amount;
    Button fatSearch_btn;
    Button cate_btn;
    EditText fatSearch;
    EditText cate_input;
    ListView fatsecret_list;
    TextView food_nameId;
    TextView food_snip;
    ImageView fatSecret_image;
    SimpleAdapter fatsecrtAdapter;
    String[] fatHead = new String[] {"NAME_ID","DESC"};
    int[] fatCell = new int[] {R.id.fatsecret_nameid,R.id.description};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vFat = inflater.inflate(R.layout.fragment_fatsecret, container, false);
        fatSearch = vFat.findViewById(R.id.fat_keyword);
        fatSearch_btn = vFat.findViewById(R.id.fat_search_btn);
        fatsecret_list = vFat.findViewById(R.id.fatsecret_list);
        food_nameId = vFat.findViewById(R.id.fatsecret_nameid);
        food_snip = vFat.findViewById(R.id.fatsecret_snip);
        fatSecret_image = vFat.findViewById(R.id.fatSecret_img);
        cate_input = vFat.findViewById(R.id.category_input);
        cate_btn = vFat.findViewById(R.id.addFoo_btn);
        fatSearch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetFoodDetailAsync getFoodDetailAsync = new GetFoodDetailAsync();
                String keyword = fatSearch.getText().toString();
                getFoodDetailAsync.execute(keyword);
            }
        });
        fatsecret_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map = (HashMap<String, String>) fatsecret_list.getItemAtPosition(position);
                String name_id = map.get("NAME_ID");
                String details = map.get("DESC");
                food_name = Validator.getFoodName(name_id);
                String[] element = details.split("\\|");
                String[] cal_array = element[0].split(":");
                cal_amount = Double.parseDouble(cal_array[1]);
                String[] fat_array = element[1].split(":");
                fat_amount = Double.parseDouble(fat_array[1]);
                String[] ser_amount_array = element[2].split(":");
                ser_amount = Double.parseDouble(ser_amount_array[1]);
                String[] ser_unit_array = element[3].split(":");
                ser_unit = ser_unit_array[1];
                GetFoodSnippetAsync getFoodSnippetAsync = new GetFoodSnippetAsync();
                GetFoodImageAsync getFoodImageAsync = new GetFoodImageAsync();
                getFoodSnippetAsync.execute(food_name);
                getFoodImageAsync.execute(food_name);
            }
        });
        cate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateFoodAsync createFoodAsync = new CreateFoodAsync();
                String category = cate_input.getText().toString().toLowerCase();
                if(food_name != null) {
                    createFoodAsync.execute(category);
                }
                else {
                    cate_input.setError("Please select one food!");
                    cate_input.requestFocus();
                }
            }
        });
        return vFat;
    }

    private class GetFoodImageAsync extends AsyncTask<String,Void,Bitmap>{
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Bitmap doInBackground(String... strings) {
            String foodString = GoogleSearch.search(strings[0], new String[]{"num"}, new String[]{"1"});
            String imageLink = GoogleSearch.getImageLink(foodString);
            Bitmap image = null;
            try {
                InputStream in = new URL(imageLink).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            fatSecret_image.setImageBitmap(bitmap);
        }
    }

    private class GetFoodSnippetAsync extends AsyncTask<String,Void,String>{
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            String foodString = GoogleSearch.search(strings[0], new String[]{"num"}, new String[]{"1"});
            String snippet = GoogleSearch.getSnippet(foodString);
            return snippet;
        }

        @Override
        protected void onPostExecute(String snippet) {
            food_snip.setText(snippet);
        }
    }

    private class GetFoodDetailAsync extends AsyncTask<String,Void,List<HashMap<String,String>>>{
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected List<HashMap<String,String>> doInBackground(String... strings) {
            JSONArray foodArray = new JSONArray();
            List<HashMap<String,String>> fatsecretArray = new ArrayList<HashMap<String,String>>();
            try {
                foodArray = new JSONArray(FatSecretGet.getFoodByName(strings[0]).get("food").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0;i < foodArray.length();i++){
                HashMap<String,String> map = new HashMap<String,String>();
                JSONObject foodJson = new JSONObject();
                String food_name = new String();
                String foodId = new String();
                String food_desc = new String();
                try {
                    foodJson = foodArray.getJSONObject(i);
                    food_name = foodJson.get("food_name").toString();
                    foodId = foodJson.get("food_id").toString();
                    food_desc = foodJson.get("food_description").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String name_id = food_name + " " + foodId;
                String[] array1 = food_desc.split("-");
                Double ser_amount = Validator.getAmount(array1[0]);
                String ser_unit = Validator.getUnit(array1[0]);
                String[] array2 = array1[1].split("\\|");
                Double cal_amount = Validator.getAmount(array2[0]);
                Double fat_amount = Validator.getAmount(array2[1]);
                String details = "CALORIE: " + cal_amount.toString() + "  |  FAT: " + fat_amount.toString() + "  |  SERVING_AMOUNT: " + ser_amount.toString() + "  |  SERVING_UNIT: " + ser_unit;
                map.put("NAME_ID",name_id);
                map.put("DESC",details);
                fatsecretArray.add(map);
            }
            return fatsecretArray;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            fatsecrtAdapter = new SimpleAdapter(getActivity(),hashMaps,R.layout.fatsecret_list,fatHead,fatCell);
            fatsecret_list.setAdapter(fatsecrtAdapter);
        }
    }

    private class CreateFoodAsync extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... category) {
            Food food = new Food(cal_amount, category[0], fat_amount, food_name, ser_amount, ser_unit);
            RestClient.createFood(food);
            return "Food is added successfully!";
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(((MainActivity)getActivity()),response,Toast.LENGTH_SHORT).show();
        }
    }
}

