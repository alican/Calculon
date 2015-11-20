package eu.alican.calculon.generator;

import eu.alican.calculon.db.Db4OGenericDao;
import android.content.Context;

/**
 * Project: Calculon
 * Created by alican on 19.11.2015.
 */


public class AnswerDao extends Db4OGenericDao<Answer> {

    /**
     * @param context
     */
    public AnswerDao(Context context) {
        super(context);
    }

}
