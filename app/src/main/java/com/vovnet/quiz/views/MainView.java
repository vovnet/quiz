package com.vovnet.quiz.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by Vladimir on 05.02.2018.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MainView extends MvpView {
    void showQuizList(String[] data);
    void showAlertDialog(int i, String title, String dbName);
    void showSelectDbDialog();
    void showError(String msg);
    void showConfirmClear(String tableName);
}
