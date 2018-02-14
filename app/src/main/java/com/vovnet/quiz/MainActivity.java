package com.vovnet.quiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.vovnet.quiz.presenters.MainPresenter;
import com.vovnet.quiz.views.MainView;


public class MainActivity extends MvpAppCompatActivity implements MainView {

    @InjectPresenter
    MainPresenter mPresenter;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        mListView = findViewById(R.id.list_view);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        mPresenter.onCreate(getApplicationContext(), getSharedPreferences("settings", Context.MODE_PRIVATE));
    }

    @Override
    public void showQuizList(String[] data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, data);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPresenter.onClickTableItem(i);
            }
        });
    }

    @Override
    public void showAlertDialog(final int index, final String title, final String dbName) {
        String[] items = {
                getResources().getString(R.string.start),
                getResources().getString(R.string.statistic),
                getResources().getString(R.string.remove)
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        Intent quizIntent = new Intent(MainActivity.this, QuizActivity.class);
                        quizIntent.putExtra(QuizActivity.EXTRA_DB_NAME, dbName);
                        quizIntent.putExtra(QuizActivity.EXTRA_TABLE_NAME, title);
                        startActivity(quizIntent);
                        break;
                    case 1:
                        Intent intent = new Intent(MainActivity.this, StatisticActivity.class);
                        intent.putExtra(QuizActivity.EXTRA_DB_NAME, dbName);
                        intent.putExtra(StatisticActivity.EXTRA_TABLE_NAME, title);
                        startActivity(intent);
                        break;
                    case 2:
                        mPresenter.onClickClearStatistic(title);
                        break;
                }
            }
        });
        builder.create().show();

    }

    @Override
    public void showSelectDbDialog() {
        Intent intent = new Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), 1);
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showConfirmClear(final String tableName) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.remove));
        builder.setMessage(R.string.sure);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.clearStatistics(tableName);
            }
        });

        builder.setCancelable(true);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            mPresenter.onSelectedDb(data.getData().getEncodedPath());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPresenter.onClickAddDb();
        return true;
    }
}
