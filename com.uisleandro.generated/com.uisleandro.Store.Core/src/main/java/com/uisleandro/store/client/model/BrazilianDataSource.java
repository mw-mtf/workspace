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
//import com.uisleandro.store.client.model.BrazilianDbHelper;

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
public class BrazilianDataSource {

	private SQLiteDatabase database;
	private DbHelper db_helper;
	private static final String[] selectableColumns = new String[]{ 
		DbHelper.BRAZILIAN_ID,
		DbHelper.BRAZILIAN_SERVER_ID,
		DbHelper.BRAZILIAN_DIRTY,
		DbHelper.BRAZILIAN_LAST_UPDATE, 
		DbHelper.BRAZILIAN_CPF, 
		DbHelper.BRAZILIAN_RG, 
		DbHelper.BRAZILIAN_FK_BASIC_CLIENT
	};

	public BrazilianDataSource(Context context){
		db_helper = DbHelper.getInstance(context);
		try{
			database = db_helper.getWritableDatabase();
		}catch(SQLException e){
			Log.wtf("BrazilianDataSource", "Exception: "+Log.getStackTraceString(e));
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


	public long insert(BrazilianView that){
		ContentValues values = new ContentValues();
		//should not set the server id

		if(that.getServerId() > 0){
			values.put(DbHelper.BRAZILIAN_SERVER_ID, that.getServerId());
		}

		values.put(DbHelper.BRAZILIAN_DIRTY, that.isDirty());

		values.put(DbHelper.BRAZILIAN_LAST_UPDATE, that.getLastUpdate());
		values.put(DbHelper.BRAZILIAN_CPF, that.getCpf());
		values.put(DbHelper.BRAZILIAN_RG, that.getRg());
		if(that.getFkBasicClient() > 0){
			values.put(DbHelper.BRAZILIAN_FK_BASIC_CLIENT, that.getFkBasicClient());
		}
		long last_id = database.insert(DbHelper.TABLE_BRAZILIAN, null, values);
		return last_id;
	}

	public int update(BrazilianView that){
		ContentValues values = new ContentValues();

		if(that.getServerId() > 0){
			values.put(DbHelper.BRAZILIAN_SERVER_ID, that.getServerId());
		}

		values.put(DbHelper.BRAZILIAN_DIRTY, that.isDirty());

		values.put(DbHelper.BRAZILIAN_LAST_UPDATE, that.getLastUpdate());
		values.put(DbHelper.BRAZILIAN_CPF, that.getCpf());
		values.put(DbHelper.BRAZILIAN_RG, that.getRg());
		if(that.getFkBasicClient() > 0){
			values.put(DbHelper.BRAZILIAN_FK_BASIC_CLIENT, that.getFkBasicClient());
		}

		int rows_affected = database.update(DbHelper.TABLE_BRAZILIAN, values, DbHelper.BRAZILIAN_ID + " = " + String.valueOf(that.getId()), null);
		return rows_affected;
	}

	public long delete(BrazilianView that){
		return database.delete(DbHelper.TABLE_BRAZILIAN, DbHelper.BRAZILIAN_ID + " = " + String.valueOf(that.getId()), null);
	}

	public long deleteById(long id){
		return database.delete(DbHelper.TABLE_BRAZILIAN, DbHelper.BRAZILIAN_ID + " = " + String.valueOf(id), null);
	}

	public Cursor listAll(){

		Cursor cursor = database.query(DbHelper.TABLE_BRAZILIAN,
			selectableColumns,null,null, null, null, null);
		return cursor;
	}

	public Cursor getById(long id){

		Cursor cursor = database.query(DbHelper.TABLE_BRAZILIAN,
			selectableColumns,
			DbHelper.BRAZILIAN_ID + " = " + id,
			null, null, null, null);

		return cursor;
	}

	public Cursor listSome(long page_count, long page_size){

		String query = "SELECT id, server_id, dirty, " +
			"last_update, " +
			"cpf, " +
			"rg, " +
			"fk_basic_client" +
			" FROM " + DbHelper.TABLE_BRAZILIAN;
		if(page_size > 0){
			query += " LIMIT " + String.valueOf(page_size) + " OFFSET " + String.valueOf(page_size * page_count);
		}
		query += ";";

		Cursor cursor = database.rawQuery(query, null);
		return cursor;
	}

	public long getLastId(){

		long result = 0;

		String query = "SELECT MAX(id) FROM " + DbHelper.TABLE_BRAZILIAN +";";
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

