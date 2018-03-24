package com.uisleandro.store.core.model;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.uisleandro.store.DbHelper;
import com.uisleandro.store.core.view.SystemDataView;

public class SystemOfflineHelper {

	private Context context;
	private SQLiteDatabase database;
	private DbHelper db_helper;

	public SystemOfflineHelper (Context context) {
		this.context = context;
		db_helper = DbHelper.getInstance(context);
		try{
			database = db_helper.getWritableDatabase();
		}catch(SQLException e){
			Log.wtf("SystemOfflineHelper", "Exception: "+Log.getStackTraceString(e));
		}
	}

	public void open () throws SQLException {
		database = db_helper.getWritableDatabase();
	}

	public void close () {
		db_helper.close();
	}

	public long insert(SystemDataView that){
		ContentValues values = new ContentValues();
		//should not set the server id

		if(that.getServerId() > 0){
			values.put(DbHelper.SYSTEM_SERVER_ID, that.getServerId());
		}

		values.put(DbHelper.SYSTEM_DIRTY, that.isDirty());
		values.put(DbHelper.SYSTEM_LAST_UPDATE, that.getLastUpdate());
		values.put(DbHelper.SYSTEM_NAME, that.getName());
		values.put(DbHelper.SYSTEM_ENABLED, that.getEnabled());
		if(that.getFkFkCurrency() > 0){
			values.put(DbHelper.SYSTEM_FK_CURRENCY, that.getFkFkCurrency());
		}
		values.put(DbHelper.SYSTEM_FANTASY_NAME, that.getFantasyName());
		values.put(DbHelper.SYSTEM_STORES_ADDRESS, that.getStoresAddress());
		values.put(DbHelper.SYSTEM_SRORES_ADDRESS_NUMBER, that.getSroresAddressNumber());
		values.put(DbHelper.SYSTEM_STORES_CITY, that.getStoresCity());
		values.put(DbHelper.SYSTEM_STORES_NEIGHBORHOOD, that.getStoresNeighborhood());
		values.put(DbHelper.SYSTEM_STORES_ZIP_CODE, that.getStoresZipCode());
		values.put(DbHelper.SYSTEM_STORES_STATE, that.getStoresState());
		values.put(DbHelper.SYSTEM_STORES_EMAIL, that.getStoresEmail());
		values.put(DbHelper.SYSTEM_STORES_PHONENUMBER, that.getStoresPhonenumber());
		if(that.getFkFkReseller() > 0){
			values.put(DbHelper.SYSTEM_FK_RESELLER, that.getFkFkReseller());
		}
		long last_id = database.insert(DbHelper.TABLE_SYSTEM, null, values);
		return last_id;
	}

	public int update(SystemDataView that){
		ContentValues values = new ContentValues();
		if(that.getServerId() > 0){
			values.put(DbHelper.SYSTEM_SERVER_ID, that.getServerId());
		}
		values.put(DbHelper.SYSTEM_DIRTY, that.isDirty());

		values.put(DbHelper.SYSTEM_LAST_UPDATE, that.getLastUpdate());
		values.put(DbHelper.SYSTEM_NAME, that.getName());
		values.put(DbHelper.SYSTEM_ENABLED, that.getEnabled());
		if(that.getFkFkCurrency() > 0){
			values.put(DbHelper.SYSTEM_FK_CURRENCY, that.getFkFkCurrency());
		}
		values.put(DbHelper.SYSTEM_FANTASY_NAME, that.getFantasyName());
		values.put(DbHelper.SYSTEM_STORES_ADDRESS, that.getStoresAddress());
		values.put(DbHelper.SYSTEM_SRORES_ADDRESS_NUMBER, that.getSroresAddressNumber());
		values.put(DbHelper.SYSTEM_STORES_CITY, that.getStoresCity());
		values.put(DbHelper.SYSTEM_STORES_NEIGHBORHOOD, that.getStoresNeighborhood());
		values.put(DbHelper.SYSTEM_STORES_ZIP_CODE, that.getStoresZipCode());
		values.put(DbHelper.SYSTEM_STORES_STATE, that.getStoresState());
		values.put(DbHelper.SYSTEM_STORES_EMAIL, that.getStoresEmail());
		values.put(DbHelper.SYSTEM_STORES_PHONENUMBER, that.getStoresPhonenumber());
		if(that.getFkFkReseller() > 0){
			values.put(DbHelper.SYSTEM_FK_RESELLER, that.getFkFkReseller());
		}
		int rows_affected = database.update(DbHelper.TABLE_SYSTEM, values, DbHelper.SYSTEM_ID + " = " + String.valueOf(that.getId()), null);
		return rows_affected;
	}

	public List<SystemDataView> listForInsertOnServer(long page_count, long page_size){

		String query = "SELECT t0.id, t0.server_id, t0.dirty, "+
		"t0.last_update, " +
		"t0.name, " +
		"t0.enabled, " +
		"t4.server_id as fk_currency, " +
		"t0.fantasy_name, " +
		"t0.stores_address, " +
		"t0.srores_address_number, " +
		"t0.stores_city, " +
		"t0.stores_neighborhood, " +
		"t0.stores_zip_code, " +
		"t0.stores_state, " +
		"t0.stores_email, " +
		"t0.stores_phonenumber, " +
		"t14.server_id as fk_reseller" +
		" FROM "+DbHelper.TABLE_SYSTEM+" t0" +
		" INNER JOIN "+DbHelper.TABLE_CURRENCY+" t1 ON t0.fk_currency = t1.id" +
		" INNER JOIN "+DbHelper.TABLE_RESELLER+" t2 ON t0.fk_reseller = t2.id";		query += " WHERE t0.server_id IS NULL";
		if(page_size > 0){
			query += " LIMIT " + String.valueOf(page_size) + " OFFSET " + String.valueOf(page_size * page_count);
		}
		query += ";";
		Log.wtf("rest-api", query);
		List<SystemDataView> those = new ArrayList<>();
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	      those.add(SystemDataView.FromCursor(cursor));
	      cursor.moveToNext();
	    }
	    cursor.close();
		return those;
	}

	//list for update on server
	//translates the foreign keys
	public List<SystemDataView> listForUpdateOnServer(long page_count, long page_size){
		//Log.wtf("rest-api", "listSomeDirty");
		// Estou com um erro pois nao gero as tabelas que nao fazem parte deste modulo
		String query = "SELECT t0.id, t0.server_id, t0.dirty, "+
		"t0.last_update, " +
		"t0.name, " +
		"t0.enabled, " +
		"t0.fantasy_name, " +
		"t0.stores_address, " +
		"t0.srores_address_number, " +
		"t0.stores_city, " +
		"t0.stores_neighborhood, " +
		"t0.stores_zip_code, " +
		"t0.stores_state, " +
		"t0.stores_email, " +
		"t0.stores_phonenumber" +
		" FROM "+DbHelper.TABLE_SYSTEM+" t0" +
		" INNER JOIN "+DbHelper.TABLE_CURRENCY+" t1 ON t0.fk_currency = t1.id" +
		" INNER JOIN "+DbHelper.TABLE_RESELLER+" t2 ON t0.fk_reseller = t2.id";		query += " WHERE t0." + DbHelper.SYSTEM_DIRTY + " = 1";
		if(page_size > 0) {
			query += " LIMIT " + String.valueOf(page_size) + " OFFSET " + String.valueOf(page_size * page_count);
		}
		query += ";";
		//Log.wtf("rest-api", query);
		List<SystemDataView> those = new ArrayList<>();
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
	    while(!cursor.isAfterLast()){
	      those.add(SystemDataView.FromCursor(cursor));
	      cursor.moveToNext();
	    }
	    cursor.close();
	}

	public int fixAfterServerInsertAndUpdate(long local_id, long remote_id, long last_update_time){
		ContentValues values = new ContentValues();
		values.put(DbHelper.SYSTEM_SERVER_ID, remote_id);
		values.put(DbHelper.SYSTEM_LAST_UPDATE_TIME, last_update_time);
		values.put(DbHelper.SYSTEM_DIRTY, 0);
		int rows_affected = database.update(
			DbHelper.TABLE_SYSTEM,
			values,
			DbHelper.SYSTEM_ID + " = " + String.valueOf(local_id),
		null);
		return rows_affected;
	}

	// given the last id i have on client i can
	// on the client side
	public long getLastServerId(){
		long result = 0;
		String query = "SELECT MAX(server_id) FROM " + DbHelper.TABLE_SYSTEM +";";
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
		String query = "SELECT last_update_time FROM " + DbHelper.TABLE_UPDATE_HISTORY +" WHERE table_name = '"+DbHelper.TABLE_SYSTEM+"';";
		Cursor cursor = database.rawQuery(query, null);
		long res = cursor.getLong(0);
		cursor.close();
		return res;
	} 

	//get the last_update_time, from this table, if if null
	public void before_client_updating(){
		String query = "UPDATE " + DbHelper.TABLE_UPDATE_HISTORY + " SET last_update_time = ( SELECT MAX(last_update_time) FROM " +
			DbHelper.TABLE_SYSTEM + " ) WHERE table_name = '" + DbHelper.TABLE_SYSTEM + "' AND last_update_time IS NULL;";
		database.rawQuery(query, null);
	}

	//set the last_update_time, from this table, to null
	public void after_client_updating(){
		String query = "UPDATE " + DbHelper.TABLE_UPDATE_HISTORY + " SET last_update_time = NULL WHERE table_name = '" + DbHelper.TABLE_SYSTEM + "';";
		database.rawQuery(query, null);
	}


	//after i will update then client
	//and after updating the client i need to fix the foreign keys
	public int fixClientForeignKeys(){
		String query = "UPDATE " + DbHelper.TABLE_SYSTEM + " SET "+
		"fk_currency = ( SELECT id FROM " + DbHelper.TABLE_CURRENCY + " WHERE " + DbHelper.TABLE_CURRENCY + ".server_id = " + DbHelper.TABLE_SYSTEM + ".fk_currency ), " +
		"fk_reseller = ( SELECT id FROM " + DbHelper.TABLE_RESELLER + " WHERE " + DbHelper.TABLE_RESELLER + ".server_id = " + DbHelper.TABLE_SYSTEM + ".fk_reseller );";		int result = 0;
		Cursor cursor = database.rawQuery(query, null);
		int res = cursor.getInt(0);
		cursor.close();
		return res;
	}



}
