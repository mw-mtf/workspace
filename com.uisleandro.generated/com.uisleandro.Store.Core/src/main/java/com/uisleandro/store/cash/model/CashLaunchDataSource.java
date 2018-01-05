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
//import com.uisleandro.store.cash.model.CashLaunchDbHelper;

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
public class CashLaunchDataSource {

	private SQLiteDatabase database;
	private DbHelper db_helper;
	private static final String[] selectableColumns = new String[]{ 
		DbHelper.CASH_LAUNCH_ID,
		DbHelper.CASH_LAUNCH_SERVER_ID,
		DbHelper.CASH_LAUNCH_DIRTY,
		DbHelper.CASH_LAUNCH_LAST_UPDATE, 
		DbHelper.CASH_LAUNCH_FK_CASH_REGISTER, 
		DbHelper.CASH_LAUNCH_JUSTIFICATION, 
		DbHelper.CASH_LAUNCH_AMOUNT_SPENT, 
		DbHelper.CASH_LAUNCH_FK_CURRENCY
	};

	public CashLaunchDataSource(Context context){
		db_helper = DbHelper.getInstance(context);
		try{
			database = db_helper.getWritableDatabase();
		}catch(SQLException e){
			Log.wtf("CashLaunchDataSource", "Exception: "+Log.getStackTraceString(e));
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


	public long insert(CashLaunchView that){
		ContentValues values = new ContentValues();
		//should not set the server id

		if(that.getServerId() > 0){
			values.put(DbHelper.CASH_LAUNCH_SERVER_ID, that.getServerId());
		}

		values.put(DbHelper.CASH_LAUNCH_DIRTY, that.isDirty());

		values.put(DbHelper.CASH_LAUNCH_LAST_UPDATE, that.getLastUpdate());
		if(that.getFkCashRegister() > 0){
			values.put(DbHelper.CASH_LAUNCH_FK_CASH_REGISTER, that.getFkCashRegister());
		}
		values.put(DbHelper.CASH_LAUNCH_JUSTIFICATION, that.getJustification());
		values.put(DbHelper.CASH_LAUNCH_AMOUNT_SPENT, that.getAmountSpent());
		if(that.getFkCurrency() > 0){
			values.put(DbHelper.CASH_LAUNCH_FK_CURRENCY, that.getFkCurrency());
		}
		long last_id = database.insert(DbHelper.TABLE_CASH_LAUNCH, null, values);
		return last_id;
	}

	public int update(CashLaunchView that){
		ContentValues values = new ContentValues();

		if(that.getServerId() > 0){
			values.put(DbHelper.CASH_LAUNCH_SERVER_ID, that.getServerId());
		}

		values.put(DbHelper.CASH_LAUNCH_DIRTY, that.isDirty());

		values.put(DbHelper.CASH_LAUNCH_LAST_UPDATE, that.getLastUpdate());
		if(that.getFkCashRegister() > 0){
			values.put(DbHelper.CASH_LAUNCH_FK_CASH_REGISTER, that.getFkCashRegister());
		}
		values.put(DbHelper.CASH_LAUNCH_JUSTIFICATION, that.getJustification());
		values.put(DbHelper.CASH_LAUNCH_AMOUNT_SPENT, that.getAmountSpent());
		if(that.getFkCurrency() > 0){
			values.put(DbHelper.CASH_LAUNCH_FK_CURRENCY, that.getFkCurrency());
		}

		int rows_affected = database.update(DbHelper.TABLE_CASH_LAUNCH, values, DbHelper.CASH_LAUNCH_ID + " = " + String.valueOf(that.getId()), null);
		return rows_affected;
	}

	public long delete(CashLaunchView that){
		return database.delete(DbHelper.TABLE_CASH_LAUNCH, DbHelper.CASH_LAUNCH_ID + " = " + String.valueOf(that.getId()), null);
	}

	public long deleteById(long id){
		return database.delete(DbHelper.TABLE_CASH_LAUNCH, DbHelper.CASH_LAUNCH_ID + " = " + String.valueOf(id), null);
	}

	public Cursor listAll(){

		Cursor cursor = database.query(DbHelper.TABLE_CASH_LAUNCH,
			selectableColumns,null,null, null, null, null);
		return cursor;
	}

	public Cursor getById(long id){

		Cursor cursor = database.query(DbHelper.TABLE_CASH_LAUNCH,
			selectableColumns,
			DbHelper.CASH_LAUNCH_ID + " = " + id,
			null, null, null, null);

		return cursor;
	}

	public Cursor listSome(long page_count, long page_size){

		String query = "SELECT id, server_id, dirty, " +
			"last_update, " +
			"fk_cash_register, " +
			"justification, " +
			"amount_spent, " +
			"fk_currency" +
			" FROM " + DbHelper.TABLE_CASH_LAUNCH;
		if(page_size > 0){
			query += " LIMIT " + String.valueOf(page_size) + " OFFSET " + String.valueOf(page_size * page_count);
		}
		query += ";";

		Cursor cursor = database.rawQuery(query, null);
		return cursor;
	}

	public long getLastId(){

		long result = 0;

		String query = "SELECT MAX(id) FROM " + DbHelper.TABLE_CASH_LAUNCH +";";
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

