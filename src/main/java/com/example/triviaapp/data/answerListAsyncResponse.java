package com.example.triviaapp.data;

import com.example.triviaapp.model.Question;

import java.util.ArrayList;

public interface answerListAsyncResponse {

    void processFinished(ArrayList<Question> questionArrayList);
}
