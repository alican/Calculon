package eu.alican.calculon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.db4o.ObjectSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.alican.calculon.db.DB4OProvider;
import eu.alican.calculon.db.Db4OGenericDao;
import eu.alican.calculon.db.DbHelper;
import eu.alican.calculon.generator.Answer;
import eu.alican.calculon.generator.AnswerDao;

public class StatsActivity extends AppCompatActivity {

    List<Answer> answers = new ArrayList<>();
    DB4OProvider db4OProvider;
    TextView statsTextView;
    ArrayAdapter adapter;
    Db4OGenericDao db4OGenericDao;

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        db4OProvider = DB4OProvider.getInstance(this);
        final Context context = this;


        //answers = getIntent().getParcelableArrayListExtra("answer_list");
        AnswerDao dao = new AnswerDao(context);
        answers = dao.query(Answer.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        statsTextView = (TextView) findViewById(R.id.stats);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                answers);
        listView.setAdapter(adapter);
        setSupportActionBar(toolbar);
        calcStats();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               // dbHelper.deleteAnswerById(answers.get(position).getId());
                AnswerDao dao = new AnswerDao(context);

                dao.delete(answers.get(position));

                adapter.notifyDataSetChanged();
                calcStats();
                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    void fetchData(){
        answers = db4OProvider.findAll();
        adapter.notifyDataSetChanged();
        calcStats();
    }


    void calcStats(){
        int correctAnswers = 0;
        int wrongAnswers = 0;
        int passedAnswers = 0;
        int gesamt = answers.size();
        for (Answer answer : answers){
            if (answer.isCorrect()){
                correctAnswers++;
            }else {
                wrongAnswers++;
                if(answer.isPassed()){
                    passedAnswers++;
                }
            }
        }

        statsTextView.setText(
                String.format("Statistiken: %d/%d richtig (%.2f%%). %d Übersprungen.",
                        correctAnswers, gesamt, (correctAnswers * 1.0 / gesamt * 100), passedAnswers)
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.delete_all:

               // dbHelper.deleteAllAnswers();
                adapter.clear();
                fetchData();
                break;
        }



        return super.onOptionsItemSelected(item);
    }
}
