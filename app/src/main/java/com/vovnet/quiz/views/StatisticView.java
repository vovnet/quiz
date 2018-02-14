package com.vovnet.quiz.views;

import com.arellomobile.mvp.MvpView;
import com.vovnet.quiz.models.Quiz;

import java.util.List;

/**
 * Created by Vladimir on 07.02.2018.
 */

public interface StatisticView extends MvpView {
    void showStatistic(List<Quiz> data);
    void showError(String msg);
}
