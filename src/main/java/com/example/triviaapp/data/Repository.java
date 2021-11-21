package com.example.triviaapp.data;

import android.text.style.QuoteSpan;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.triviaapp.controller.AppController;
import com.example.triviaapp.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    ArrayList<Question> questionArrayList = new ArrayList<>();

    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions(final answerListAsyncResponse callBack){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        Question question = new Question(String.valueOf(response.getJSONArray(i).get(0)), response.getJSONArray(i).getBoolean(1));

                        questionArrayList.add(question);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }

                if(null != callBack) callBack.processFinished(questionArrayList);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);


        return questionArrayList;
    }
}
