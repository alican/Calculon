package eu.alican.calculon.db;

/**
 * Project: Calculon
 * Created by alican on 18.11.2015.
 */
import java.util.List;
import android.content.Context;
import eu.alican.calculon.generator.Answer;

public class DB4OProvider extends Db4oHelper {

    private static DB4OProvider provider = null;

    private DB4OProvider(Context ctx) {
        super(ctx);
    }

    public static DB4OProvider getInstance(Context ctx) {
        if (provider == null)
            provider = new DB4OProvider(ctx);
        return provider;
    }

    public void store(Answer answer) {
        db().store(answer);
    }

    public void delete(Answer answer) {
        db().delete(answer);
    }

    public List<Answer> findAll() {
        return db().query(Answer.class);
    }

    public List<Answer> getRecord(Answer answer) {
        return db().queryByExample(answer);
    }

}