package eu.alican.calculon.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import eu.alican.calculon.generator.Answer;

/**
 * Project: Calculon
 * Created by alican on 08.11.2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper sInstance;

    public static final String TAG = "Dbhelper";

    private static final String DATABASE_NAME = "calculon";
    private static final int DATABASE_VERSION = 6;

    private static final String TABLE_CALCOBJECTS = "calcobjects";
    private static final String TABLE_ANSWERS = "answers";

    public static final String KEY_CALCOBJECT_ID = "id";
    public static final String KEY_CALCOBJECT_REP = "rep";
    public static final String KEY_CALCOBJECT_RESULT = "result";

    public static final String KEY_ANSWER_ID = "id";
    public static final String KEY_ANSWER_CALC = "calc";
    public static final String KEY_ANSWER_ANSWER = "answr";
    public static final String KEY_ANSWER_RESULT = "result";
    public static final String KEY_ANSWER_ISCORRECT = "correct";
    public static final String KEY_ANSWER_PASSED = "passed";


    private DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static synchronized DbHelper getInstance(Context context){
        if (sInstance == null){
            sInstance = new DbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db){
        super.onConfigure(db);
        //db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        String CREATE_CALCOBJECTS_TABLE = "CREATE TABLE " + TABLE_CALCOBJECTS +
                "(" +
                    KEY_CALCOBJECT_ID + " INTEGER PRIMARY KEY," +
                    KEY_CALCOBJECT_REP + " TEXT," +
                    KEY_CALCOBJECT_RESULT + " INTEGER " +
                ")";
        String CREATE_ANSWERS_TABLE = "CREATE TABLE " + TABLE_ANSWERS +
                "(" +
                    KEY_ANSWER_ID + " INTEGER PRIMARY KEY, " +
                    KEY_ANSWER_ANSWER + " INTEGER, " +
                    KEY_ANSWER_RESULT + " INTEGER, " +
                    KEY_ANSWER_CALC + " TEXT, " +
                    KEY_ANSWER_ISCORRECT + " INTEGER, " +
                    KEY_ANSWER_PASSED + " INTEGER" +
                ")";

        db.execSQL(CREATE_CALCOBJECTS_TABLE);
        db.execSQL(CREATE_ANSWERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALCOBJECTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
            onCreate(db);
        }
    }


    public void addAnswer(Answer answer){

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_ANSWER_ANSWER, answer.getAnswer());
            values.put(KEY_ANSWER_RESULT, answer.getResult());
            values.put(KEY_ANSWER_CALC, answer.getCalc());
            values.put(KEY_ANSWER_ISCORRECT, (answer.isCorrect())? 1 : 0);
            values.put(KEY_ANSWER_PASSED, (answer.isPassed())? 1 : 0);

            db.insertOrThrow(TABLE_ANSWERS, null, values);
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.wtf(TAG, "ERROR while trying to add answer to database");
        }finally {
            db.endTransaction();
        }

    }


    public List<Answer> getAllAnswers(){
        List<Answer> answers = new ArrayList<>();


        String ANSWERS_SELECT_QUERY = "SELECT * FROM " +
                TABLE_ANSWERS;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ANSWERS_SELECT_QUERY, null);

        int idIndex = cursor.getColumnIndex(KEY_ANSWER_ID);
        int calcIndex = cursor.getColumnIndex(KEY_ANSWER_CALC);
        int answerIndex = cursor.getColumnIndex(KEY_ANSWER_ANSWER);
        int resultIndex = cursor.getColumnIndex(KEY_ANSWER_RESULT);
        int iscorrectIndex = cursor.getColumnIndex(KEY_ANSWER_ISCORRECT);
        int passedIndex = cursor.getColumnIndex(KEY_ANSWER_PASSED);

        try {
            if(cursor.moveToFirst()){
                do{
                    Answer newAnswer = new Answer();
                    newAnswer.setId(cursor.getInt(idIndex));
                    newAnswer.setAnswer(cursor.getInt(answerIndex));
                    newAnswer.setCalc(cursor.getString(calcIndex));
                    newAnswer.setResult(cursor.getInt(resultIndex));
                    newAnswer.setIsCorrect((cursor.getInt(iscorrectIndex) == 1));
                    newAnswer.setPassed((cursor.getInt(passedIndex) == 1));

                    answers.add(newAnswer);
                }while (cursor.moveToNext());
            }

        }catch (Exception e){
            Log.wtf(TAG, "Error while trying to get answers from database");
        }finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return answers;
    }


    public void deleteAllAnswers() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_ANSWERS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all answers");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteAnswerById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_ANSWERS, KEY_ANSWER_ID +  "=" + id, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete answer with id" + id);
        } finally {
            db.endTransaction();
        }
    }

}
