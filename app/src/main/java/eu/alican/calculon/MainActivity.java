package eu.alican.calculon;

import android.content.Intent;
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

import eu.alican.calculon.db.DbHelper;
import eu.alican.calculon.generator.Answer;
import eu.alican.calculon.generator.CalcObject;
import eu.alican.calculon.generator.Generator;

public class MainActivity extends AppCompatActivity {

    Generator generator;
    TextView calcLabel;
    TextView result;
    CalcObject calcObject;

    EditText answerBox;
    DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getBackground().setAlpha(0);


        dbHelper = DbHelper.getInstance(this);


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
                dbHelper.addAnswer(new Answer(calcObject.toString(), 0, calcObject.getResult(), false, true ));
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
            dbHelper.addAnswer(answer);
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
}
