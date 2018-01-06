//Start of user code reserved-for:android-sqlite-db.imports
package com.uisleandro.store.Core.model;  

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
//old
//import com.uisleandro.util.LongDateFormatter;
//import com.uisleandro.store.model.DbHelper;
//import com.uisleandro.store.sales.model.ProductOnSaleDbHelper;

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
public class ProductOnSaleProvider extends ContentProvider {


	public static final String AUTHORITY = "com.uisleandro.product_on_sale";
	public static final String SCHEME = "content://";

	public static final String PRODUCT_ON_SALE_ALL = SCHEME + AUTHORITY + "/all";
	public static final Uri URI_PRODUCT_ON_SALE_ALL = Uri.parse(PRODUCT_ON_SALE_ALL);
	public static final String PRODUCT_ON_SALE_ALL_BASE = PRODUCT_ON_SALE_ALL + "/";

	public static final String PRODUCT_ON_SALE_SOME = SCHEME + AUTHORITY + "/some";
	public static final Uri URI_PRODUCT_ON_SALE_SOME = Uri.parse(PRODUCT_ON_SALE_SOME);
	public static final String PRODUCT_ON_SALE_SOME_BASE = PRODUCT_ON_SALE_SOME + "/";

	public static final String PRODUCT_ON_SALE_BYID = SCHEME + AUTHORITY + "/byid";
	public static final Uri URI_PRODUCT_ON_SALE_BYID = Uri.parse(PRODUCT_ON_SALE_BYID);
	public static final String PRODUCT_ON_SALE_BYID_BASE = PRODUCT_ON_SALE_BYID + "/";

	public static final String PRODUCT_ON_SALE_LASTID = SCHEME + AUTHORITY + "/lastid";
	public static final Uri URI_PRODUCT_ON_SALE_LASTID = Uri.parse(PRODUCT_ON_SALE_LASTID);
	public static final String PRODUCT_ON_SALE_LASTID_BASE = PRODUCT_ON_SALE_LASTID + "/";

	private SQLiteDatabase database;
	private DbHelper db_helper;
	private static final String[] selectableColumns = new String[]{ 
		DbHelper.PRODUCT_ON_SALE_ID,
		DbHelper.PRODUCT_ON_SALE_SERVER_ID,
		DbHelper.PRODUCT_ON_SALE_DIRTY,
		DbHelper.PRODUCT_ON_SALE_LAST_UPDATE, 
		DbHelper.PRODUCT_ON_SALE_FK_SALE, 
		DbHelper.PRODUCT_ON_SALE_FK_PRODUCT
	};

	public ProductOnSaleDataSource(Context context){
		db_helper = DbHelper.getInstance(context);
		try{
			database = db_helper.getWritableDatabase();
		}catch(SQLException e){
			Log.wtf("ProductOnSaleDataSource", "Exception: "+Log.getStackTraceString(e));
		}
	}

	public void open() throws SQLException{
		database = db_helper.getWritableDatabase();
	}

	public void close(){
		db_helper.close();
	}

	public Cursor listAll(){
		Cursor cursor = database.query(DbHelper.TABLE_PRODUCT_ON_SALE,
			selectableColumns,null,null, null, null, null);
		return cursor;
	}

	public Cursor getById(long id){
		Cursor cursor = database.query(DbHelper.TABLE_PRODUCT_ON_SALE,
			selectableColumns,
			DbHelper.PRODUCT_ON_SALE_ID + " = " + id,
			null, null, null, null);
		return cursor;
	}

	public Cursor listSome(long page_count, long page_size){
		String query = "SELECT id, server_id, dirty, " +
			"last_update, " +
			"fk_sale, " +
			"fk_product" +
			" FROM " + DbHelper.TABLE_PRODUCT_ON_SALE;
		if(page_size > 0){
			query += " LIMIT " + String.valueOf(page_size) + " OFFSET " + String.valueOf(page_size * page_count);
		}
		query += ";";
		Cursor cursor = database.rawQuery(query, null);
		return cursor;
	}

	public Cursor getLastId(){
		String query = "SELECT MAX(id) FROM " + DbHelper.TABLE_PRODUCT_ON_SALE +";";
		Cursor cursor = database.rawQuery(query, null);
		return cursor;		
	}

// begin content-provider-interface

	@Override
	public boolean onCreate() {
		return false;
	}

	@Nullable
	@Override
	public String getType(@NonNull Uri uri) {
		return null;
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
		long last_id = database.insert(DbHelper.TABLE_PRODUCT_ON_SALE, null, values);
		return last_id;
	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
		int rows_affected = database.update(DbHelper.TABLE_PRODUCT_ON_SALE, values, DbHelper.PRODUCT_ON_SALE_ID + " = " + selectionArgs[0], null);
		return rows_affected;
	}

	@Override
	public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
		int rows_affected = database.delete(DbHelper.TABLE_PRODUCT_ON_SALE, DbHelper.PRODUCT_ON_SALE_ID + " = " + selectionArgs[0], null);
		return rows_affected;
	}

// end content-provider-interface 

// reserved-for:android-sqlite-db.functions
//End of user code

//Start of user code reserved-for:android-sqlite-sync.functions
//reserved-for:android-sqlite-sync.functions
//End of user code

//Start of user code reserved-for:query3.functions
//reserved-for:query3.functions
//End of user code


//Start of user code reserved-for:android-sqlite-db.begin-default-query
	// TODO: I NEED TO KNOW HOW TO MAKE VARIOUS QUERIES DEPENDING ON THE URI
	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
		Cursor result = null;
		if (URI_PRODUCT_ON_SALE_ALL.equals(uri)) {
			result = listAll();
		} else if(URI_PRODUCT_ON_SALE_SOME.equals(uri)) {
			result = listSome(Long.parseLong(selectionArgs[0]), Long.parseLong(selectionArgs[1]));
		} else if(URI_PRODUCT_ON_SALE_BYID.equals(uri)) {
			result = getById(Long.parseLong(selectionArgs[0]));
		} else if(URI_PRODUCT_ON_SALE_LASTID.equals(uri)) {
			result = getLastId();
		}
// reserved-for:android-sqlite-db.begin-default-query
//End of user code

// Start of user code reserved-for:android-sqlite-sync.default-query

// reserved-for:android-sqlite-sync.default-query
// End of user code

//Start of user code reserved-for:android-sqlite-db.end-default-query
		return result;
	}
}
// reserved-for:android-sqlite-db.end-default-query
//End of user code
