package eu.alican.calculon.generator;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Project: Calculon
 * Created by alican on 21.10.2015.
 */
public class CalcObject {
    Deque<Integer> numStack = new ArrayDeque<>();
    Deque<String> operatorStack = new ArrayDeque<>();

    String rep;
    int result = 0;


    public CalcObject(Deque<Integer> numStack, Deque<String> operatorStack) {
        this.numStack = numStack;
        this.operatorStack = operatorStack;


//        Deque<Integer> copyNumStack = new ArrayDeque<>(numStack);
//        Deque<String> copyOperatorStack = new ArrayDeque<>(operatorStack);


        StringBuilder stringBuilder = new StringBuilder(20);

        result = numStack.pollFirst();
        stringBuilder.append(result);

        for (int i = 0; i < numStack.size(); i++){

            int num = numStack.pollFirst();
            String op = operatorStack.pollFirst();

            stringBuilder.append(" ");
            stringBuilder.append(op);
            stringBuilder.append(" ");
            stringBuilder.append(num);


            switch (op) {

                case "+": result = result + num; break;
                case "-": result = result - num; break;
                case "/": result = result / num; break;
                case "*": result = result * num; break;
            }

        }
        rep = stringBuilder.toString();
    }

    @Override
    public String toString() {
        return rep;
    }

    public int getResult(){

        return result;
    }

}
