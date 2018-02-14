package com.vovnet.quiz.presenters;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.vovnet.quiz.R;
import com.vovnet.quiz.data.QuizDatabase;
import com.vovnet.quiz.models.Quiz;
import com.vovnet.quiz.views.StatisticView;

import java.util.List;

/**
 * Created by Vladimir on 07.02.2018.
 */

@InjectViewState
public class StatisticPresenter extends MvpPresenter<StatisticView> {
    private Context mContext;

    public void onCreate(Context context, String dbName, String tableName) {
        if (mContext == null) {
            mContext = context;
            try {
                QuizDatabase db = new QuizDatabase(context, dbName);
                List<Quiz> quizList = db.getQuiz(QuizDatabase.getTableByName(tableName));
                getViewState().showStatistic(quizList);
            } catch (Exception e) {
                getViewState().showError(mContext.getString(R.string.db_error));
            }
        }
    }
}
