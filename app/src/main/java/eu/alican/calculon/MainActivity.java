package eu.alican.calculon;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import eu.alican.calculon.db.DB4OProvider;
import eu.alican.calculon.db.DbHelper;
import eu.alican.calculon.generator.Answer;
import eu.alican.calculon.generator.AnswerDao;
import eu.alican.calculon.generator.CalcObject;
import eu.alican.calculon.generator.Generator;

public class MainActivity extends AppCompatActivity {

    Generator generator;
    TextView calcLabel;
    TextView result;
    CalcObject calcObject;

    EditText answerBox;
    DB4OProvider db4OProvider;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getBackground().setAlpha(0);

        context = this;

        db4OProvider = DB4OProvider.getInstance(context);


        calcLabel = (TextView) findViewById(R.id.textView);
        result = (TextView) findViewById(R.id.result);
        answerBox = (EditText) findViewById(R.id.answer_box);

        generate();

        Button enterBttn = (Button) findViewById(R.id.enter);

        enterBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterAnswer();
            }
        });


        calcLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Answer answer = new Answer(calcObject.toString(), 0, calcObject.getResult(), false, true );
                new SaveObjectOnDb4o(context).execute(answer, null, null);
                generate();
            }
        });


    }


    void generate(){

        generator = new Generator(4);
        calcObject = generator.gen();
        calcLabel.setText(calcObject.toString());
        result.setText(String.format("%d", calcObject.getResult()));
        answerBox.setText("");
    }

    void enterAnswer(){

        if (!answerBox.getText().toString().matches("")){
            int answerInt = Integer.valueOf(answerBox.getText().toString());
            boolean isCorrect;

            isCorrect = (answerInt == calcObject.getResult());

            Answer answer = new Answer(calcObject.toString(), answerInt, calcObject.getResult(), isCorrect, false );
            //answers.add(answer);
            new SaveObjectOnDb4o(context).execute(answer, null, null);
            generate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.action_stats:
                Intent intent = new Intent(this, StatsActivity.class);
                //intent.putParcelableArrayListExtra("answer_list", answers);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public class SaveObjectOnDb4o extends AsyncTask<Answer, Void, Void> {

        public SaveObjectOnDb4o(Context mContext) {

        }

        @Override
        protected Void doInBackground(Answer... answers) {
            try {
                AnswerDao dao = new AnswerDao(context);
                dao.store(answers[0]);

                dao.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            // mListAdapter.notifyDataSetChanged();
        }
    }
}
