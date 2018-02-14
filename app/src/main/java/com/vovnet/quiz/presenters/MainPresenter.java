package com.vovnet.quiz.presenters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.vovnet.quiz.R;
import com.vovnet.quiz.data.QuizDatabase;
import com.vovnet.quiz.views.MainView;

import java.util.List;

/**
 * Created by Vladimir on 05.02.2018.
 */

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    private static final String LAST_DB = "last_db";

    private SharedPreferences mSharedPreferences;

    private List<String> mTables;
    private Context mContext;
    private String mDbName;

    public void onCreate(Context context, SharedPreferences sharedPreferences) {
        mContext = context;
        mSharedPreferences = sharedPreferences;
        if (sharedPreferences.contains(LAST_DB)) {
            try {
                showTables(mSharedPreferences.getString(LAST_DB, ""));
            } catch (Exception e) {
                getViewState().showError(mContext.getString(R.string.db_error));
            }
            mDbName = mSharedPreferences.getString(LAST_DB, "");
        }
    }

    public void onSelectedDb(String dbName) {
        try {
            showTables(dbName);
            mDbName = dbName;
            mSharedPreferences
                    .edit()
                    .putString(LAST_DB, mDbName)
                    .apply();
        } catch (Exception e) {
            getViewState().showError(mContext.getString(R.string.db_error));
        }
    }

    public void onClickTableItem(int i) {
        getViewState().showAlertDialog(i, mTables.get(i), mDbName);
    }

    public void onClickClearStatistic(String tableName) {
        getViewState().showConfirmClear(tableName);
    }

    @SuppressLint("StaticFieldLeak")
    public void clearStatistics(final String tableName) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    QuizDatabase db = new QuizDatabase(mContext, mDbName);
                    db.clearStatistic(QuizDatabase.getTableByName(tableName));
                }  catch (Exception e) {
                    Log.w("Error", "doInBackground: error", e);
                }

                return null;
            }
        }.execute();
    }

    public void onClickAddDb() {
        getViewState().showSelectDbDialog();
    }

    private void showTables(String dbName) {
        mTables = new QuizDatabase(mContext, dbName).getQuizTables();
        getViewState().showQuizList(mTables.toArray(new String[0]));
    }
}
