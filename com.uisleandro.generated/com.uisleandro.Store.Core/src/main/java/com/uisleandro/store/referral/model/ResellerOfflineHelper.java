package com.uisleandro.store.referral.model;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.uisleandro.store.DbHelper;
import com.uisleandro.store.referral.view.ResellerDataView;

public class ResellerOfflineHelper {

	private Context context;
	private SQLiteDatabase database;
	private DbHelper db_helper;

	public ResellerOfflineHelper (Context context) {
		this.context = context;
		db_helper = DbHelper.getInstance(context);
		try{
			database = db_helper.getWritableDatabase();
		}catch(SQLException e){
			Log.wtf("ResellerOfflineHelper", "Exception: "+Log.getStackTraceString(e));
		}
	}

	public void open () throws SQLException {
		database = db_helper.getWritableDatabase();
	}

	public void close () {
		db_helper.close();
	}

	public long insert(ResellerDataView that){
		ContentValues values = new ContentValues();
		//should not set the server id

		if(that.getServerId() > 0){
			values.put(DbHelper.RESELLER_SERVER_ID, that.getServerId());
		}

		values.put(DbHelper.RESELLER_DIRTY, that.isDirty());
		values.put(DbHelper.RESELLER_LAST_UPDATE, that.getLastUpdate());
		values.put(DbHelper.RESELLER_SYSTEM_AMOUNT, that.getSystemAmount());
		values.put(DbHelper.RESELLER_NAME, that.getName());
		values.put(DbHelper.RESELLER_ADDRESS, that.getAddress());
		values.put(DbHelper.RESELLER_NEIGHBORHOOD, that.getNeighborhood());
		values.put(DbHelper.RESELLER_CITY, that.getCity());
		values.put(DbHelper.RESELLER_STATE, that.getState());
		values.put(DbHelper.RESELLER_ZIP_CODE, that.getZipCode());
		long last_id = database.insert(DbHelper.TABLE_RESELLER, null, values);
		return last_id;
	}

	public int update(ResellerDataView that){
		ContentValues values = new ContentValues();
		if(that.getServerId() > 0){
			values.put(DbHelper.RESELLER_SERVER_ID, that.getServerId());
		}
		values.put(DbHelper.RESELLER_DIRTY, that.isDirty());

		values.put(DbHelper.RESELLER_LAST_UPDATE, that.getLastUpdate());
		values.put(DbHelper.RESELLER_SYSTEM_AMOUNT, that.getSystemAmount());
		values.put(DbHelper.RESELLER_NAME, that.getName());
		values.put(DbHelper.RESELLER_ADDRESS, that.getAddress());
		values.put(DbHelper.RESELLER_NEIGHBORHOOD, that.getNeighborhood());
		values.put(DbHelper.RESELLER_CITY, that.getCity());
		values.put(DbHelper.RESELLER_STATE, that.getState());
		values.put(DbHelper.RESELLER_ZIP_CODE, that.getZipCode());
		int rows_affected = database.update(DbHelper.TABLE_RESELLER, values, DbHelper.RESELLER_ID + " = " + String.valueOf(that.getId()), null);
		return rows_affected;
	}

	public List<ResellerDataView> listForInsertOnServer(long page_count, long page_size){

		String query = "SELECT t0.id, t0.server_id, t0.dirty, "+
		"t0.last_update, " +
		"t0.system_amount, " +
		"t0.name, " +
		"t0.address, " +
		"t0.neighborhood, " +
		"t0.city, " +
		"t0.state, " +
		"t0.zip_code" +
		" FROM "+DbHelper.TABLE_RESELLER+" t0";
		query += " WHERE t0.server_id IS NULL";
		if(page_size > 0){
			query += " LIMIT " + String.valueOf(page_size) + " OFFSET " + String.valueOf(page_size * page_count);
		}
		query += ";";
		Log.wtf("rest-api", query);
		List<ResellerDataView> those = new ArrayList<>();
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	      those.add(ResellerDataView.FromCursor(cursor));
	      cursor.moveToNext();
	    }
	    cursor.close();
		return those;
	}

	//list for update on server
	//translates the foreign keys
	public List<ResellerDataView> listForUpdateOnServer(long page_count, long page_size){
		//Log.wtf("rest-api", "listSomeDirty");
		// Estou com um erro pois nao gero as tabelas que nao fazem parte deste modulo
		String query = "SELECT t0.id, t0.server_id, t0.dirty, "+
		"t0.last_update, " +
		"t0.system_amount, " +
		"t0.name, " +
		"t0.address, " +
		"t0.neighborhood, " +
		"t0.city, " +
		"t0.state, " +
		"t0.zip_code" +
		" FROM "+DbHelper.TABLE_RESELLER+" t0";
		query += " WHERE t0." + DbHelper.RESELLER_DIRTY + " = 1";
		if(page_size > 0) {
			query += " LIMIT " + String.valueOf(page_size) + " OFFSET " + String.valueOf(page_size * page_count);
		}
		query += ";";
		//Log.wtf("rest-api", query);
		List<ResellerDataView> those = new ArrayList<>();
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	      those.add(ResellerDataView.FromCursor(cursor));
	      cursor.moveToNext();
	    }
	    cursor.close();
	}

	public int fixAfterServerInsertAndUpdate(long local_id, long remote_id, long last_update_time){
		ContentValues values = new ContentValues();
		values.put(DbHelper.RESELLER_SERVER_ID, remote_id);
		values.put(DbHelper.RESELLER_LAST_UPDATE, last_update_time);
		values.put(DbHelper.RESELLER_DIRTY, 0);
		int rows_affected = database.update(
			DbHelper.TABLE_RESELLER,
			values,
			DbHelper.RESELLER_ID + " = " + String.valueOf(local_id),
		null);
		return rows_affected;
	}

	// given the last id i have on client i can
	// on the client side
	public long getLastServerId(){
		long result = 0;
		String query = "SELECT MAX(server_id) FROM " + DbHelper.TABLE_RESELLER +";";
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
		String query = "SELECT last_update_time FROM " + DbHelper.TABLE_UPDATE_HISTORY +" WHERE table_name = '"+DbHelper.TABLE_RESELLER+"';";
		Cursor cursor = database.rawQuery(query, null);
		long res = cursor.getLong(0);
		cursor.close();
		return res;
	} 

	//get the last_update_time, from this table, if if null
	public void before_client_updating(){
		String query = "UPDATE " + DbHelper.TABLE_UPDATE_HISTORY + " SET last_update_time = ( SELECT MAX(last_update_time) FROM " +
			DbHelper.TABLE_RESELLER + " ) WHERE table_name = '" + DbHelper.TABLE_RESELLER + "' AND last_update_time IS NULL;";
		database.rawQuery(query, null);
	}

	//set the last_update_time, from this table, to null
	public void after_client_updating(){
		String query = "UPDATE " + DbHelper.TABLE_UPDATE_HISTORY + " SET last_update_time = NULL WHERE table_name = '" + DbHelper.TABLE_RESELLER + "';";
		database.rawQuery(query, null);
	}


	//after i will update then client
	// this class don't need to fix client foreign keys


}
