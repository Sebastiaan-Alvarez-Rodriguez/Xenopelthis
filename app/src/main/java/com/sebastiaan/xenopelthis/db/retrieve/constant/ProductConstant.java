package com.sebastiaan.xenopelthis.db.retrieve.constant;

import android.content.Context;
import android.os.AsyncTask;

import com.sebastiaan.xenopelthis.db.Database;
import com.sebastiaan.xenopelthis.db.dao.DAOProduct;

public class ProductConstant {
    private DAOProduct dbInterface;

    public ProductConstant(Context context) {
        dbInterface = Database.getDatabase(context).getDAOProduct();
    }

    public void isUnique(String name, ConstantResultListener<Boolean> listener) {
        IsUniqueTask s = new IsUniqueTask(dbInterface, listener);
        s.execute(name);
    }

    private static class IsUniqueTask extends AsyncTask<String, Void, Boolean> {
        private DAOProduct dbInterface;
        private ConstantResultListener<Boolean> listener;
        IsUniqueTask(DAOProduct dbInterface, ConstantResultListener<Boolean> listener) {
            this.dbInterface = dbInterface;
            this.listener = listener;
        }
        @Override
        protected Boolean doInBackground(String... strings) {
            return dbInterface.findExact(strings[0]) == null;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            listener.onResult(result);
        }
    }
}
