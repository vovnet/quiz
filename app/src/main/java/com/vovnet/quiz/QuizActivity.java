package com.vovnet.quiz;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.vovnet.quiz.presenters.QuizPresenter;
import com.vovnet.quiz.tools.ImageGetter;
import com.vovnet.quiz.views.QuizView;

public class QuizActivity extends MvpAppCompatActivity implements QuizView, View.OnClickListener {

    public static final String EXTRA_TABLE_NAME = "extra_table_name";
    public static final String EXTRA_DB_NAME = "extra_db_name";

    @InjectPresenter
    QuizPresenter mPresenter;

    private TextView mQuestionTextView;
    private TextView mAnswerTextView;
    private Button mTrueBtn;
    private Button mFalseBtn;
    private Button mNextBtn;
    private LinearLayout mAnswerLayout;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        String dbName = getIntent().getStringExtra(EXTRA_DB_NAME);
        String tableName = getIntent().getStringExtra(EXTRA_TABLE_NAME);
        if (tableName != null) {
            setTitle(tableName);
            if (mPresenter.getTableName() == null) {
                mPresenter.onCreate(getApplicationContext(), dbName, tableName);
            }
        }

        mQuestionTextView = findViewById(R.id.question_text_view);
        mAnswerTextView = findViewById(R.id.answer_text_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mAnswerLayout = findViewById(R.id.answer_layout);

        mQuestionTextView.setLinksClickable(true);

        mTrueBtn = findViewById(R.id.true_button);
        mFalseBtn = findViewById(R.id.false_button);
        mNextBtn = findViewById(R.id.next_button);

        mTrueBtn.setOnClickListener(this);
        mFalseBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);

        hideElements();
    }

    @Override
    public void showQuestion(String htmlText) {
        showText(htmlText, mQuestionTextView);
        mAnswerTextView.setText("");
        mTrueBtn.setVisibility(View.VISIBLE);
        mNextBtn.setVisibility(View.GONE);
        mFalseBtn.setVisibility(View.VISIBLE);
        mAnswerLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showAnswer(String htmlText) {
        showText(htmlText, mAnswerTextView);
        mAnswerLayout.setVisibility(View.VISIBLE);
        mNextBtn.setVisibility(View.VISIBLE);
        mFalseBtn.setVisibility(View.GONE);
        mTrueBtn.setVisibility(View.GONE);
    }

    @Override
    public void showError(String msg) {
        hideElements();
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading(Boolean isLoading) {
        if (isLoading) {
            mProgressBar.setVisibility(View.VISIBLE);
            hideElements();
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            showElements();
        }
    }

    private void hideElements() {
        mQuestionTextView.setVisibility(View.INVISIBLE);
        mAnswerLayout.setVisibility(View.INVISIBLE);
        mNextBtn.setVisibility(View.INVISIBLE);
        mFalseBtn.setVisibility(View.INVISIBLE);
        mTrueBtn.setVisibility(View.INVISIBLE);
    }

    private void showElements() {
        mQuestionTextView.setVisibility(View.VISIBLE);
        mFalseBtn.setVisibility(View.VISIBLE);
        mTrueBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.true_button:
                mPresenter.onClickTrue();
                break;
            case R.id.false_button:
                mPresenter.onClickFalse();
                break;
            case R.id.next_button:
                mPresenter.onClickNext();
                break;
        }
    }

    private void showText(String htmlText, TextView view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Spanned sp = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY, new ImageGetter(1), null);
            view.setText(sp);
        } else {
            Spanned sp = Html.fromHtml(htmlText, new ImageGetter(1), null);
            view.setText(sp);
        }
    }
}
