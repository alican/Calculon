package eu.alican.calculon.generator;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Project: Calculon
 * Created by alican on 22.10.2015.
 */
public class Answer implements Parcelable {
    int id;
    String calc;
    int answer;
    int result;
    boolean isCorrect;
    boolean passed;

    public boolean isPassed() {
        return passed;
    }

    public Answer(){

    }


    protected Answer(Parcel in) {
        calc = in.readString();
        answer = in.readInt();
        result = in.readInt();
        isCorrect = in.readByte() != 0;
        passed = in.readByte() != 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(calc);
        dest.writeInt(answer);
        dest.writeInt(result);
        dest.writeByte((byte) (isCorrect ? 1 : 0));
        dest.writeByte((byte) (passed ? 1 : 0));

    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    @Override
    public String toString() {
        if (passed){
            return String.format("%s = %d - Aufgabe übersprungen", calc,result );
        }else {
            return String.format("%s = %d - Deine Eingabe: %d", calc,result, answer );
        }
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    public String getCalc() {
        return calc;
    }

    public void setCalc(String calc) {
        this.calc = calc;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Answer(String calc, int answer, int result, boolean isCorrect, boolean passed) {
        this.calc = calc;
        this.answer = answer;
        this.result = result;
        this.isCorrect = isCorrect;
        this.passed = passed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
