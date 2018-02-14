package com.vovnet.quiz.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by Vladimir on 05.02.2018.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface QuizView extends MvpView {
    void showQuestion(String htmlText);
    void showAnswer(String htmlText);
    void showError(String msg);
    void showLoading(Boolean isLoading);
}
