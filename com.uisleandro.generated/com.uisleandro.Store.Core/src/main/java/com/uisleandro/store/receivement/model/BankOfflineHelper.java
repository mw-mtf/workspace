package com.uisleandro.store.receivement.model;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.uisleandro.store.DbHelper;
import com.uisleandro.store.receivement.view.BankDataView;

public class BankOfflineHelper {

	private Context context;
	private SQLiteDatabase database;
	private DbHelper db_helper;

	public BankOfflineHelper (Context context) {
		this.context = context;
		db_helper = DbHelper.getInstance(context);
		try{
			database = db_helper.getWritableDatabase();
		}catch(SQLException e){
			Log.wtf("BankOfflineHelper", "Exception: "+Log.getStackTraceString(e));
		}
	}

	public void open () throws SQLException {
		database = db_helper.getWritableDatabase();
	}

	public void close () {
		db_helper.close();
	}

	public long insert(BankDataView that){
		ContentValues values = new ContentValues();
		//should not set the server id

		if(that.getServerId() > 0){
			values.put(DbHelper.BANK_SERVER_ID, that.getServerId());
		}

		values.put(DbHelper.BANK_DIRTY, that.isDirty());
		values.put(DbHelper.BANK_LAST_UPDATE, that.getLastUpdate());
		values.put(DbHelper.BANK_CODE, that.getCode());
		values.put(DbHelper.BANK_NAME, that.getName());
		long last_id = database.insert(DbHelper.TABLE_BANK, null, values);
		return last_id;
	}

	public int update(BankDataView that){
		ContentValues values = new ContentValues();
		if(that.getServerId() > 0){
			values.put(DbHelper.BANK_SERVER_ID, that.getServerId());
		}
		values.put(DbHelper.BANK_DIRTY, that.isDirty());

		values.put(DbHelper.BANK_LAST_UPDATE, that.getLastUpdate());
		values.put(DbHelper.BANK_CODE, that.getCode());
		values.put(DbHelper.BANK_NAME, that.getName());
		int rows_affected = database.update(DbHelper.TABLE_BANK, values, DbHelper.BANK_ID + " = " + String.valueOf(that.getId()), null);
		return rows_affected;
	}

	public List<BankDataView> listForInsertOnServer(long page_count, long page_size){

		String query = "SELECT t0.id, t0.server_id, t0.dirty, "+
		"t0.last_update, " +
		"t0.code, " +
		"t0.name" +
		" FROM "+DbHelper.TABLE_BANK+" t0";
		query += " WHERE t0.server_id IS NULL";
		if(page_size > 0){
			query += " LIMIT " + String.valueOf(page_size) + " OFFSET " + String.valueOf(page_size * page_count);
		}
		query += ";";
		Log.wtf("rest-api", query);
		List<BankDataView> those = new ArrayList<>();
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	      those.add(BankDataView.FromCursor(cursor));
	      cursor.moveToNext();
	    }
	    cursor.close();
		return those;
	}

	//list for update on server
	//translates the foreign keys
	public List<BankDataView> listForUpdateOnServer(long page_count, long page_size){
		//Log.wtf("rest-api", "listSomeDirty");
		// Estou com um erro pois nao gero as tabelas que nao fazem parte deste modulo
		String query = "SELECT t0.id, t0.server_id, t0.dirty, "+
		"t0.last_update, " +
		"t0.code, " +
		"t0.name" +
		" FROM "+DbHelper.TABLE_BANK+" t0";
		query += " WHERE t0." + DbHelper.BANK_DIRTY + " = 1";
		if(page_size > 0) {
			query += " LIMIT " + String.valueOf(page_size) + " OFFSET " + String.valueOf(page_size * page_count);
		}
		query += ";";
		//Log.wtf("rest-api", query);
		List<BankDataView> those = new ArrayList<>();
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	      those.add(BankDataView.FromCursor(cursor));
	      cursor.moveToNext();
	    }
	    cursor.close();
	}

	public int fixAfterServerInsertAndUpdate(long local_id, long remote_id, long last_update_time){
		ContentValues values = new ContentValues();
		values.put(DbHelper.BANK_SERVER_ID, remote_id);
		values.put(DbHelper.BANK_LAST_UPDATE, last_update_time);
		values.put(DbHelper.BANK_DIRTY, 0);
		int rows_affected = database.update(
			DbHelper.TABLE_BANK,
			values,
			DbHelper.BANK_ID + " = " + String.valueOf(local_id),
		null);
		return rows_affected;
	}

	// given the last id i have on client i can
	// on the client side
	public long getLastServerId(){
		long result = 0;
		String query = "SELECT MAX(server_id) FROM " + DbHelper.TABLE_BANK +";";
		Cursor cursor = database.rawQuery(query, null);
		long res = cursor.getLong(0);
		cursor.close();
		return res;
	}

	//the idea is that i don't want to iterate over all the data,
	//bringing all the data to the server is expensive, even if the return value is null sometimes
	//just bring from the server what is newer than my newer data, for updating
	//the implementation is also easier :D
	public long getLastUpdateTime(){
		long result = 0;
		String query = "SELECT last_update_time FROM " + DbHelper.TABLE_UPDATE_HISTORY +" WHERE table_name = '"+DbHelper.TABLE_BANK+"';";
		Cursor cursor = database.rawQuery(query, null);
		long res = cursor.getLong(0);
		cursor.close();
		return res;
	} 

	//get the last_update_time, from this table, if if null
	public void before_client_updating(){
		String query = "UPDATE " + DbHelper.TABLE_UPDATE_HISTORY + " SET last_update_time = ( SELECT MAX(last_update_time) FROM " +
			DbHelper.TABLE_BANK + " ) WHERE table_name = '" + DbHelper.TABLE_BANK + "' AND last_update_time IS NULL;";
		database.rawQuery(query, null);
	}

	//set the last_update_time, from this table, to null
	public void after_client_updating(){
		String query = "UPDATE " + DbHelper.TABLE_UPDATE_HISTORY + " SET last_update_time = NULL WHERE table_name = '" + DbHelper.TABLE_BANK + "';";
		database.rawQuery(query, null);
	}


	//after i will update then client
	// this class don't need to fix client foreign keys


}
