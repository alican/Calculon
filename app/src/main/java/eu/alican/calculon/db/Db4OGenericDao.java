package eu.alican.calculon.db;

/**
 * Project: Calculon
 * Created by alican on 19.11.2015.
 */


import android.content.Context;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import com.db4o.query.QueryComparator;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public abstract class Db4OGenericDao<T> {

    private static ObjectContainer oc = null;
    private Context context;

    private String DB_NAME = "db_";

    /**
     * @param context
     */
    public Db4OGenericDao(Context context) {
        this.context = context;
    }


    protected ObjectContainer db() {

        if (oc == null || oc.ext().isClosed()) {
            synchronized (this) {
                try {
                    if (oc == null || oc.ext().isClosed()) {
                        oc = Db4oEmbedded.openFile(dbConfig(), db4oDBFullPath(context));
                    }

                    return oc;

                } catch (Exception ie) {
                    Log.e(Db4OGenericDao.class.getName(), ie.toString());
                    throw new IllegalStateException("error while init the db4o database", ie);
                }
            }
        } else
            return oc;
    }



    private EmbeddedConfiguration dbConfig() throws IOException {
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();

        return configuration;
    }


    private String db4oDBFullPath(Context ctx) {
        return ctx.getDir("data", 0) + "/" + DB_NAME + ".DB4O";
    }


    public void store(T object) throws DatabaseClosedException, DatabaseReadOnlyException {
        ObjectContainer objectContainer = db();
        objectContainer.store(object);
        objectContainer.commit();

    }

    public void close() {
        if (oc != null)
            oc.close();

    }

    public <TargetType> ObjectSet<TargetType> query(Predicate<TargetType> targetTypePredicate) throws Db4oIOException, DatabaseClosedException {
        ObjectContainer oc = db();
        return oc.query(targetTypePredicate);
    }

    public <T> ObjectSet<T> queryByExample(T o) throws Db4oIOException, DatabaseClosedException {
        ObjectContainer oc = db();
        return oc.queryByExample(o);
    }


    public <TargetType> List<TargetType> query(Class<TargetType> targetTypeClass) throws Db4oIOException, DatabaseClosedException {
        ObjectContainer oc = db();
        return oc.query(targetTypeClass);
    }

    public Query query() throws DatabaseClosedException {
        ObjectContainer oc = db();
        return oc.query();
    }

    public void delete(T object) throws Db4oIOException, DatabaseClosedException, DatabaseReadOnlyException {
        ObjectContainer oc = db();
        ObjectSet result = oc.queryByExample(object);
        T found = (T) result.next();
        oc.delete(found);
        oc.commit();
    }

    public <TargetType> ObjectSet<TargetType> query(
            Predicate<TargetType> targetTypePredicate,
            Comparator<TargetType> targetTypeComparator)
                throws Db4oIOException, DatabaseClosedException {

        ObjectContainer oc = db();
        return oc.query(targetTypePredicate, targetTypeComparator);
    }


    public <TargetType> ObjectSet<TargetType> query(Predicate<TargetType> targetTypePredicate, QueryComparator<TargetType> targetTypeQueryComparator) throws Db4oIOException, DatabaseClosedException {
        ObjectContainer oc = db();
        return oc.query(targetTypePredicate, targetTypeQueryComparator);
    }
}