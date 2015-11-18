package eu.alican.calculon.generator;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;

/**
 * Project: Calculon
 * Created by alican on 21.10.2015.
 */
public class Generator {

    Random randomGenerator;
    int countOfNumbers;

    Deque<Integer> numStack = new ArrayDeque<>();
    Deque<String> operatorStack = new ArrayDeque<>();

    ArrayList<String> operators = new ArrayList<>();

    public Generator(int countOfNumbers) {
        randomGenerator = new Random();
        this.countOfNumbers = countOfNumbers;


        operators.add("+");
        operators.add("-");
        operators.add("*");
        operators.add("=");
    }

    public CalcObject gen(){

        numStack.clear();
        operatorStack.clear();

        for (int i = 0; i < countOfNumbers; i++){
            numStack.add(randomGenerator.nextInt(100));
        }
        for (int i = 1; i < (countOfNumbers-1); i++){
            operatorStack.add(operators.get(randomGenerator.nextInt(2)));
        }
        operatorStack.add(operators.get(3));

        return new CalcObject(numStack, operatorStack);

    }


}
