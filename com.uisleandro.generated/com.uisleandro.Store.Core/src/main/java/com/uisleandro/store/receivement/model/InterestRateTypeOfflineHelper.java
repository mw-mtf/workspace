package com.uisleandro.store.receivement.model;

import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.util.Log;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.uisleandro.store.receivement.view.InterestRateTypeDataView

public class InterestRateTypeOfflineHelper {

	private Context context;
	private SQLiteDatabase database;
	private DbHelper db_helper;

	public InterestRateTypeOfflineHelper (Context context) {
		this.context = context;
	}

	public InterestRateTypeOfflineHelper (Context context) {
		db_helper = DbHelper.getInstance(context);
		try{
			database = db_helper.getWritableDatabase();
		}catch(SQLException e){
			Log.wtf("InterestRateTypeDataSource", "Exception: "+Log.getStackTraceString(e));
		}
	}

	public void open () throws SQLException {
		database = db_helper.getWritableDatabase();
	}

	public void close () {
		db_helper.close();
	}

	public List<InterestRateTypeDataView> listForInsertOnServer(long page_count, long page_size){

		String query = "SELECT t0.id, t0.server_id, t0.dirty, "+
		"t0.last_update, " +
		"t0.name" +
		" FROM "+DbHelper.TABLE_INTEREST_RATE_TYPE+" t0";

		query += " WHERE t0.server_id IS NULL";

		if(page_size > 0){
			query += " LIMIT " + String.valueOf(page_size) + " OFFSET " + String.valueOf(page_size * page_count);
		}

		query += ";";

		Log.wtf("rest-api", query);

		List<InterestRateTypeView> those = new ArrayList<>();
		Cursor cursor = database.rawQuery(query, null);

		cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	      those.add(InterestRateTypeView.FromCursor(cursor));
	      cursor.moveToNext();
	    }
	    cursor.close();

		return those;
	}

	//list for update on server
	//translates the foreign keys
	public List<InterestRateTypeDataView> listForUpdateOnServer(long page_count, long page_size){

		//Log.wtf("rest-api", "listSomeDirty");
		// Estou com um erro pois nao gero as tabelas que nao fazem parte deste modulo

		String query = "SELECT t0.id, t0.server_id, t0.dirty, "+
		"t0.last_update, " +
		"t0.name" +
		" FROM "+DbHelper.TABLE_INTEREST_RATE_TYPE+" t0";

		query += " WHERE t0." + DbHelper.INTEREST_RATE_TYPE_DIRTY + " = 1";

		if(page_size > 0) {
			query += " LIMIT " + String.valueOf(page_size) + " OFFSET " + String.valueOf(page_size * page_count);
		}

		query += ";";

		//Log.wtf("rest-api", query);

		List<InterestRateTypeView> those = new ArrayList<>();
		Cursor cursor = database.rawQuery(query, null);

		cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	      those.add(InterestRateTypeView.FromCursor(cursor));
	      cursor.moveToNext();
	    }
	    cursor.close();

	}

	public int fixAfterServerInsertAndUpdate(long local_id, long remote_id, long last_update_time){
		ContentValues values = new ContentValues();

		values.put(DbHelper.INTEREST_RATE_TYPE_SERVER_ID, remote_id);
		values.put(DbHelper.INTEREST_RATE_TYPE_LAST_UPDATE_TIME, last_update_time);
		values.put(DbHelper.INTEREST_RATE_TYPE_DIRTY, 0);


		int rows_affected = database.update(
			DbHelper.TABLE_INTEREST_RATE_TYPE,
			values,
			DbHelper.INTEREST_RATE_TYPE_ID + " = " + String.valueOf(local_id),
		null);
		return rows_affected;
	}

	// given the last id i have on client i can
	// on the client side
	public long getLastServerId(){

		long result = 0;


		String query = "SELECT MAX(server_id) FROM " + DbHelper.TABLE_INTEREST_RATE_TYPE +";";
		Cursor cursor = database.rawQuery(query, null);

		return cursorToLong(cursor);
	}

	//the idea is that i don't want to iterate over all the data,
	//bringing all the data to the server is expensive, even if the return value is null sometimes
	//just bring from the server what is newer than my newer data, for updating
	//the implementation is also easier :D
	public long getLastUpdateTime(){

		long result = 0;
		String query = "SELECT last_update_time FROM " + DbHelper.TABLE_UPDATE_HISTORY +" WHERE table_name = '"+DbHelper.TABLE_INTEREST_RATE_TYPE+"';";
		Cursor cursor = database.rawQuery(query, null);

		return cursorToLong(cursor);

	} 

	//get the last_update_time, from this table, if if null
	public void before_client_updating(){

		String query = "UPDATE " + DbHelper.TABLE_UPDATE_HISTORY + " SET last_update_time = ( SELECT MAX(last_update_time) FROM " +
			DbHelper.TABLE_INTEREST_RATE_TYPE + " ) WHERE table_name = '" + DbHelper.TABLE_INTEREST_RATE_TYPE + "' AND last_update_time IS NULL;";
		database.rawQuery(query, null);

	}

	//set the last_update_time, from this table, to null
	public void after_client_updating(){

		String query = "UPDATE " + DbHelper.TABLE_UPDATE_HISTORY + " SET last_update_time = NULL WHERE table_name = '" + DbHelper.TABLE_INTEREST_RATE_TYPE + "';";
		database.rawQuery(query, null);

	}


	//after i will update then client
	// this class doesn't need to fix client foreign keys




}
