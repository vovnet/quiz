package com.vovnet.quiz.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vovnet.quiz.models.Quiz;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vladimir on 03.02.2018.
 */

public class QuizDatabase extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "quiz.sqlite";
    private final static int DATABASE_VERSION = 1;

    public QuizDatabase(Context context, String dbName) {
        super(context, dbName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //createTable("test_quiz", db);
        //mock("test_quiz", db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public List<String> getQuizTables() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> tables = new ArrayList<>();

        Cursor c = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table'",
                null
        );

        String tableName;
        String resultTableName;

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                tableName = c.getString(0);
                if (tableName.contains("quiz")) {
                    String[] split = tableName.split("_");
                    resultTableName = "";
                    for (int i = 0; i < split.length-1; i++) {
                        resultTableName += split[i] + " ";
                    }
                    tables.add(resultTableName.trim());
                }
                c.moveToNext();
            }
        }
        c.close();
        db.close();

        return tables;
    }

    public List<Quiz> getQuiz(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Quiz> result = new ArrayList<>();

        String[] columns = {
                QuizSchema.FIELD_ID,
                QuizSchema.FIELD_QUESTION,
                QuizSchema.FIELD_ANSWER,
                QuizSchema.FIELD_VIEWS,
                QuizSchema.FIELD_CORRECT,
                QuizSchema.FIELD_INCORRECT,
                QuizSchema.FIELD_RATING
        };
        Cursor c  = db.query(tableName, columns, null, null, null,
                null, null);

        if (c.moveToFirst()){
            do {
                result.add(new Quiz(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getInt(3),
                        c.getInt(4),
                        c.getInt(5),
                        c.getInt(6)
                ));
            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return result;
    }

    public void saveQuiz(String tableName, Quiz quiz) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QuizSchema.FIELD_VIEWS, quiz.getViews());
        values.put(QuizSchema.FIELD_CORRECT, quiz.getCorrect());
        values.put(QuizSchema.FIELD_INCORRECT, quiz.getIncorrect());
        values.put(QuizSchema.FIELD_RATING, quiz.getRating());

        db.update(tableName, values, "_id = ?",
                new String[] { String.valueOf(quiz.getId()) });

        db.close();
    }

    public void clearStatistic(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QuizSchema.FIELD_VIEWS, 0);
        values.put(QuizSchema.FIELD_CORRECT, 0);
        values.put(QuizSchema.FIELD_INCORRECT, 0);
        values.put(QuizSchema.FIELD_RATING, 1);

        db.update(tableName, values, QuizSchema.FIELD_VIEWS + " >= ? ", new String[] {"0"});
        db.close();
    }

    public static String getTableByName(String name) {
        String[] arr = name.split(" ");
        String result = "";
        for (String s : arr) {
            result += s + "_";
        }
        return result + QuizSchema.PREFIX_QUIZ;
    }



    public void mock(String tableName, SQLiteDatabase db) {
        for (int i = 1; i < 20; i++) {
            String question = "Long long long string......... long long long long";
            if (i % 2 == 0) {
                question = "<p><b>Вопрос № " + i + "</b></p><p>Текст вопроса...</p>" +
                        "<img src='/mnt/sdcard/Base/Flags/akrotiri.png' />";
            }
            String answer = "<p>Форматированный <b>ответ</b> на вопрос " + i + "</p>" +
                    "<img src='/mnt/sdcard/Base/Flags/french-southern-and-antarctic-lands.png' />";
            ContentValues values = new ContentValues();
            values.put(QuizSchema.FIELD_QUESTION, question);
            values.put(QuizSchema.FIELD_ANSWER, answer);
            db.insert(tableName, null, values);
        }
    }

    private void createTable(String tableName, SQLiteDatabase db) {
        db.execSQL("create table " + tableName + "(" +
                QuizSchema.FIELD_ID + " integer primary key autoincrement, " +
                QuizSchema.FIELD_QUESTION + " text not null, " +
                QuizSchema.FIELD_ANSWER + "  text not null, " +
                QuizSchema.FIELD_VIEWS + " integer default 0, " +
                QuizSchema.FIELD_CORRECT + " integer default 0, " +
                QuizSchema.FIELD_INCORRECT + " integer default 0, " +
                QuizSchema.FIELD_RATING + " integer default 1)"
        );
    }
}
