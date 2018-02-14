package com.vovnet.quiz;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.vovnet.quiz.models.Quiz;
import com.vovnet.quiz.presenters.StatisticPresenter;
import com.vovnet.quiz.tools.ImageGetter;
import com.vovnet.quiz.views.StatisticView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vladimir on 05.02.2018.
 */

public class StatisticActivity extends MvpAppCompatActivity implements StatisticView {

    public static final String EXTRA_DB_NAME = "extra_db_name";
    public static final String EXTRA_TABLE_NAME = "extra_table_name";

    @InjectPresenter
    StatisticPresenter mPresenter;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new QuizAdapter());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        String dbName = getIntent().getStringExtra(EXTRA_DB_NAME);
        String tableName = getIntent().getStringExtra(EXTRA_TABLE_NAME);
        setTitle(tableName);

        mPresenter.onCreate(getApplicationContext(), dbName, tableName);
    }

    @Override
    public void showStatistic(List<Quiz> data) {
        ((QuizAdapter) mRecyclerView.getAdapter()).setItems(data);
    }

    @Override
    public void showError(String msg) {
        mRecyclerView.setVisibility(View.GONE);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

        private List<Quiz> mQuizList = new ArrayList<>();

        @Override
        public QuizViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.quiz_item, parent, false);
            return new QuizViewHolder(view);
        }

        @Override
        public void onBindViewHolder(QuizViewHolder holder, int position) {
            holder.bind(mQuizList.get(position));
        }

        @Override
        public int getItemCount() {
            return mQuizList.size();
        }

        void setItems(List<Quiz> data) {
            mQuizList.addAll(data);
            notifyDataSetChanged();
        }

        class QuizViewHolder extends RecyclerView.ViewHolder {
            private RelativeLayout mRelativeLayout;
            private TextView mQuestion;
            private TextView mStatistics;
            private TextView mViews;

            QuizViewHolder(View itemView) {
                super(itemView);
                mRelativeLayout = itemView.findViewById(R.id.relative);
                mQuestion = itemView.findViewById(R.id.desc_text_view);
                mStatistics = itemView.findViewById(R.id.stat_text_view);
                mViews = itemView.findViewById(R.id.views_text_view);
            }

            void bind(Quiz quiz) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Spanned sp = Html.fromHtml(quiz.getQuestion(), Html.FROM_HTML_MODE_LEGACY, new ImageGetter(0.5f), null);
                    mQuestion.setText(sp);
                } else {
                    Spanned sp = Html.fromHtml(quiz.getQuestion(), new ImageGetter(0.5f), null);
                    mQuestion.setText(sp);
                }

                int views = quiz.getViews();
                mViews.setText(String.valueOf(views));
                float stat = 0;
                if (views > 0) {
                    stat = ((float) quiz.getCorrect() / quiz.getViews()) * 100;
                }
                mStatistics.setText((int)stat + " %");

                if (stat <= 100 && stat >= 80) {
                    setBackgroundColor(R.color.colorLightGreen);
                } else if (stat < 80 && stat >= 50) {
                    setBackgroundColor(R.color.colorLightYellow);
                } else if (stat < 50 && views > 0) {
                    setBackgroundColor(R.color.colorLightRed);
                } else {
                    setBackgroundColor(R.color.colorWhite);
                }
            }

            private void setBackgroundColor(int color) {
                mRelativeLayout.setBackgroundColor(getResources().getColor(color));
            }
        }
    }


}
