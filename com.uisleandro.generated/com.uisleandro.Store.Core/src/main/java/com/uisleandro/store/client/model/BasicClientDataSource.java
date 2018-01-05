//Start of user code reserved-for:android-sqlite-db.imports
package com.uisleandro.store.Core.model;  

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
//old
//import com.uisleandro.util.LongDateFormatter;
//import com.uisleandro.store.model.DbHelper;
//import com.uisleandro.store.client.model.BasicClientDbHelper;

import com.uisleandro.store.DbHelper;

//TODO: I wont return any view, Id rather return the cursor instead 

// reserved-for:android-sqlite-db.imports
//End of user code

//Start of user code reserved-for:android-sqlite-sync.imports
//reserved-for:android-sqlite-sync.imports
//End of user code

//Start of user code reserved-for:query3.imports
// reserved-for:query3.imports
//End of user code

//Start of user code reserved-for:android-sqlite-db.functions
public class BasicClientDataSource {

	private SQLiteDatabase database;
	private DbHelper db_helper;
	private static final String[] selectableColumns = new String[]{ 
		DbHelper.BASIC_CLIENT_ID,
		DbHelper.BASIC_CLIENT_SERVER_ID,
		DbHelper.BASIC_CLIENT_DIRTY,
		DbHelper.BASIC_CLIENT_LAST_UPDATE, 
		DbHelper.BASIC_CLIENT_NAME, 
		DbHelper.BASIC_CLIENT_BIRTH_DATE, 
		DbHelper.BASIC_CLIENT_BIRTH_CITY, 
		DbHelper.BASIC_CLIENT_BIRTH_STATE, 
		DbHelper.BASIC_CLIENT_MOTHERS_NAME, 
		DbHelper.BASIC_CLIENT_FATHERS_NAME, 
		DbHelper.BASIC_CLIENT_PROFESSION, 
		DbHelper.BASIC_CLIENT_ZIP_CODE, 
		DbHelper.BASIC_CLIENT_ADDRESS, 
		DbHelper.BASIC_CLIENT_NEIGHBORHOOD, 
		DbHelper.BASIC_CLIENT_CITY, 
		DbHelper.BASIC_CLIENT_STATE, 
		DbHelper.BASIC_CLIENT_COMPLEMENT, 
		DbHelper.BASIC_CLIENT_FK_COUNTRY
	};

	public BasicClientDataSource(Context context){
		db_helper = DbHelper.getInstance(context);
		try{
			database = db_helper.getWritableDatabase();
		}catch(SQLException e){
			Log.wtf("BasicClientDataSource", "Exception: "+Log.getStackTraceString(e));
		}
	}

	public void open() throws SQLException{
		database = db_helper.getWritableDatabase();
	}

	public void close(){
		db_helper.close();
	}

	public long cursorToLong(Cursor cursor){
		long result = 0L;
		cursor.moveToFirst();
		if(!cursor.isAfterLast()){
			result = cursor.getLong(0);
		}
		cursor.close();
		return result;
	}

	public int cursorToInteger(Cursor cursor){
		int result = 0;
		cursor.moveToFirst();
		if(!cursor.isAfterLast()){
			result = cursor.getInt(0);
		}
		cursor.close();
		return result;
	}


	public long insert(BasicClientView that){
		ContentValues values = new ContentValues();
		//should not set the server id

		if(that.getServerId() > 0){
			values.put(DbHelper.BASIC_CLIENT_SERVER_ID, that.getServerId());
		}

		values.put(DbHelper.BASIC_CLIENT_DIRTY, that.isDirty());

		values.put(DbHelper.BASIC_CLIENT_LAST_UPDATE, that.getLastUpdate());
		values.put(DbHelper.BASIC_CLIENT_NAME, that.getName());
		values.put(DbHelper.BASIC_CLIENT_BIRTH_DATE, that.getBirthDate());
		values.put(DbHelper.BASIC_CLIENT_BIRTH_CITY, that.getBirthCity());
		values.put(DbHelper.BASIC_CLIENT_BIRTH_STATE, that.getBirthState());
		values.put(DbHelper.BASIC_CLIENT_MOTHERS_NAME, that.getMothersName());
		values.put(DbHelper.BASIC_CLIENT_FATHERS_NAME, that.getFathersName());
		values.put(DbHelper.BASIC_CLIENT_PROFESSION, that.getProfession());
		values.put(DbHelper.BASIC_CLIENT_ZIP_CODE, that.getZipCode());
		values.put(DbHelper.BASIC_CLIENT_ADDRESS, that.getAddress());
		values.put(DbHelper.BASIC_CLIENT_NEIGHBORHOOD, that.getNeighborhood());
		values.put(DbHelper.BASIC_CLIENT_CITY, that.getCity());
		values.put(DbHelper.BASIC_CLIENT_STATE, that.getState());
		values.put(DbHelper.BASIC_CLIENT_COMPLEMENT, that.getComplement());
		if(that.getFkCountry() > 0){
			values.put(DbHelper.BASIC_CLIENT_FK_COUNTRY, that.getFkCountry());
		}
		long last_id = database.insert(DbHelper.TABLE_BASIC_CLIENT, null, values);
		return last_id;
	}

	public int update(BasicClientView that){
		ContentValues values = new ContentValues();

		if(that.getServerId() > 0){
			values.put(DbHelper.BASIC_CLIENT_SERVER_ID, that.getServerId());
		}

		values.put(DbHelper.BASIC_CLIENT_DIRTY, that.isDirty());

		values.put(DbHelper.BASIC_CLIENT_LAST_UPDATE, that.getLastUpdate());
		values.put(DbHelper.BASIC_CLIENT_NAME, that.getName());
		values.put(DbHelper.BASIC_CLIENT_BIRTH_DATE, that.getBirthDate());
		values.put(DbHelper.BASIC_CLIENT_BIRTH_CITY, that.getBirthCity());
		values.put(DbHelper.BASIC_CLIENT_BIRTH_STATE, that.getBirthState());
		values.put(DbHelper.BASIC_CLIENT_MOTHERS_NAME, that.getMothersName());
		values.put(DbHelper.BASIC_CLIENT_FATHERS_NAME, that.getFathersName());
		values.put(DbHelper.BASIC_CLIENT_PROFESSION, that.getProfession());
		values.put(DbHelper.BASIC_CLIENT_ZIP_CODE, that.getZipCode());
		values.put(DbHelper.BASIC_CLIENT_ADDRESS, that.getAddress());
		values.put(DbHelper.BASIC_CLIENT_NEIGHBORHOOD, that.getNeighborhood());
		values.put(DbHelper.BASIC_CLIENT_CITY, that.getCity());
		values.put(DbHelper.BASIC_CLIENT_STATE, that.getState());
		values.put(DbHelper.BASIC_CLIENT_COMPLEMENT, that.getComplement());
		if(that.getFkCountry() > 0){
			values.put(DbHelper.BASIC_CLIENT_FK_COUNTRY, that.getFkCountry());
		}

		int rows_affected = database.update(DbHelper.TABLE_BASIC_CLIENT, values, DbHelper.BASIC_CLIENT_ID + " = " + String.valueOf(that.getId()), null);
		return rows_affected;
	}

	public long delete(BasicClientView that){
		return database.delete(DbHelper.TABLE_BASIC_CLIENT, DbHelper.BASIC_CLIENT_ID + " = " + String.valueOf(that.getId()), null);
	}

	public long deleteById(long id){
		return database.delete(DbHelper.TABLE_BASIC_CLIENT, DbHelper.BASIC_CLIENT_ID + " = " + String.valueOf(id), null);
	}

	public Cursor listAll(){

		Cursor cursor = database.query(DbHelper.TABLE_BASIC_CLIENT,
			selectableColumns,null,null, null, null, null);
		return cursor;
	}

	public Cursor getById(long id){

		Cursor cursor = database.query(DbHelper.TABLE_BASIC_CLIENT,
			selectableColumns,
			DbHelper.BASIC_CLIENT_ID + " = " + id,
			null, null, null, null);

		return cursor;
	}

	public Cursor listSome(long page_count, long page_size){

		String query = "SELECT id, server_id, dirty, " +
			"last_update, " +
			"name, " +
			"birth_date, " +
			"birth_city, " +
			"birth_state, " +
			"mothers_name, " +
			"fathers_name, " +
			"profession, " +
			"zip_code, " +
			"address, " +
			"neighborhood, " +
			"city, " +
			"state, " +
			"complement, " +
			"fk_country" +
			" FROM " + DbHelper.TABLE_BASIC_CLIENT;
		if(page_size > 0){
			query += " LIMIT " + String.valueOf(page_size) + " OFFSET " + String.valueOf(page_size * page_count);
		}
		query += ";";

		Cursor cursor = database.rawQuery(query, null);
		return cursor;
	}

	public long getLastId(){

		long result = 0;

		String query = "SELECT MAX(id) FROM " + DbHelper.TABLE_BASIC_CLIENT +";";
		Cursor cursor = database.rawQuery(query, null);
		
		return cursorToLong(cursor);
	}

// reserved-for:android-sqlite-db.functions
//End of user code

//Start of user code reserved-for:android-sqlite-sync.functions
//reserved-for:android-sqlite-sync.functions
//End of user code

//Start of user code reserved-for:query3.functions
//reserved-for:query3.functions
//End of user code

}

