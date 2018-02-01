// Start of user code reserved-for:AndroidSqliteDatabase001
package com.uisleandro.store.supply.model;  

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

//old
//import com.uisleandro.util.LongDateFormatter;
//import com.uisleandro.store.model.DbHelper;
//import com.uisleandro.store.supply.model.DistributorContactDbHelper;

import com.uisleandro.store.DbHelper;
// reserved-for:AndroidSqliteDatabase001
// End of user code

// Start of user code reserved-for:AndroidSqliteSyncSingle001
// reserved-for:AndroidSqliteSyncSingle001
// End of user code

// Start of user code reserved-for:AndroidSqliteQuerySingle001// reserved-for:AndroidSqliteQuerySingle001
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabase002
public class DistributorContactProvider extends ContentProvider {


	public static final String AUTHORITY = "com.uisleandro.distributor_contact";
	public static final String SCHEME = "content://";
	private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	
	public static final int DISTRIBUTOR_CONTACT_INSERT_NUMBER = 1;
	public static final String DISTRIBUTOR_CONTACT_INSERT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/insert";
	public static final String DISTRIBUTOR_CONTACT_INSERT = SCHEME + AUTHORITY + "/insert";
	public static final Uri URI_DISTRIBUTOR_CONTACT_INSERT = Uri.parse(DISTRIBUTOR_CONTACT_INSERT);
	public static final String DISTRIBUTOR_CONTACT_INSERT_BASE = DISTRIBUTOR_CONTACT_INSERT + "/";

	public static final int DISTRIBUTOR_CONTACT_UPDATE_NUMBER = 2;
	public static final String DISTRIBUTOR_CONTACT_UPDATE_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/update";
	public static final String DISTRIBUTOR_CONTACT_UPDATE = SCHEME + AUTHORITY + "/update";
	public static final Uri URI_DISTRIBUTOR_CONTACT_UPDATE = Uri.parse(DISTRIBUTOR_CONTACT_UPDATE);
	public static final String DISTRIBUTOR_CONTACT_UPDATE_BASE = DISTRIBUTOR_CONTACT_UPDATE + "/";

	public static final int DISTRIBUTOR_CONTACT_DELETE_NUMBER = 3;
	public static final String DISTRIBUTOR_CONTACT_DELETE_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/delete";
	public static final String DISTRIBUTOR_CONTACT_DELETE = SCHEME + AUTHORITY + "/delete";
	public static final Uri URI_DISTRIBUTOR_CONTACT_DELETE = Uri.parse(DISTRIBUTOR_CONTACT_DELETE);
	public static final String DISTRIBUTOR_CONTACT_DELETE_BASE = DISTRIBUTOR_CONTACT_DELETE + "/";

	public static final int DISTRIBUTOR_CONTACT_ALL_NUMBER = 4;
	public static final String DISTRIBUTOR_CONTACT_ALL_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/all";
	public static final String DISTRIBUTOR_CONTACT_ALL = SCHEME + AUTHORITY + "/all";
	public static final Uri URI_DISTRIBUTOR_CONTACT_ALL = Uri.parse(DISTRIBUTOR_CONTACT_ALL);
	public static final String DISTRIBUTOR_CONTACT_ALL_BASE = DISTRIBUTOR_CONTACT_ALL + "/";

	public static final int DISTRIBUTOR_CONTACT_SOME_NUMBER = 5;
	public static final String DISTRIBUTOR_CONTACT_SOME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/some";
	public static final String DISTRIBUTOR_CONTACT_SOME = SCHEME + AUTHORITY + "/some";
	public static final Uri URI_DISTRIBUTOR_CONTACT_SOME = Uri.parse(DISTRIBUTOR_CONTACT_SOME);
	public static final String DISTRIBUTOR_CONTACT_SOME_BASE = DISTRIBUTOR_CONTACT_SOME + "/";

	public static final int DISTRIBUTOR_CONTACT_BY_ID_NUMBER = 6;
	public static final String DISTRIBUTOR_CONTACT_BY_ID_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/by_id";
	public static final String DISTRIBUTOR_CONTACT_BY_ID = SCHEME + AUTHORITY + "/by_id";
	public static final Uri URI_DISTRIBUTOR_CONTACT_BY_ID = Uri.parse(DISTRIBUTOR_CONTACT_BY_ID);
	public static final String DISTRIBUTOR_CONTACT_BY_ID_BASE = DISTRIBUTOR_CONTACT_BY_ID + "/";

	public static final int DISTRIBUTOR_CONTACT_LAST_ID_NUMBER = 7;
	public static final String DISTRIBUTOR_CONTACT_LAST_ID_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/last_id";
	public static final String DISTRIBUTOR_CONTACT_LAST_ID = SCHEME + AUTHORITY + "/last_id";
	public static final Uri URI_DISTRIBUTOR_CONTACT_LAST_ID = Uri.parse(DISTRIBUTOR_CONTACT_LAST_ID);
	public static final String DISTRIBUTOR_CONTACT_LAST_ID_BASE = DISTRIBUTOR_CONTACT_LAST_ID + "/";


// reserved-for:AndroidSqliteDatabase002
// End of user code

// Start of user code reserved-for:AndroidSqliteSyncSingle002
// reserved-for:AndroidSqliteSyncSingle003
// End of user code

// Start of user code reserved-for:AndroidSqliteQuerySingle002
// reserved-for:AndroidSqliteQuerySingle002
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabase003

	static {
		MATCHER.addURI(AUTHORITY,"insert", DISTRIBUTOR_CONTACT_INSERT_NUMBER);
		MATCHER.addURI(AUTHORITY,"update", DISTRIBUTOR_CONTACT_UPDATE_NUMBER);
		MATCHER.addURI(AUTHORITY,"delete", DISTRIBUTOR_CONTACT_DELETE_NUMBER);
		MATCHER.addURI(AUTHORITY,"all", DISTRIBUTOR_CONTACT_ALL_NUMBER);
		MATCHER.addURI(AUTHORITY,"some", DISTRIBUTOR_CONTACT_SOME_NUMBER);
		MATCHER.addURI(AUTHORITY,"by_id", DISTRIBUTOR_CONTACT_BY_ID_NUMBER);
		MATCHER.addURI(AUTHORITY,"last_id", DISTRIBUTOR_CONTACT_LAST_ID_NUMBER);
// reserved-for:AndroidSqliteDatabase003
// End of user code

// Start of user code reserved-for:AndroidSqliteQuerySingle002.1
// reserved-for:AndroidSqliteQuerySingle002.1
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabase003.1
	}

	private SQLiteDatabase database;
	private DbHelper db_helper;
	private static final String[] selectableColumns = new String[] { 
		DbHelper.DISTRIBUTOR_CONTACT_ID,
		DbHelper.DISTRIBUTOR_CONTACT_SERVER_ID,
		DbHelper.DISTRIBUTOR_CONTACT_DIRTY,
		DbHelper.DISTRIBUTOR_CONTACT_LAST_UPDATE, 
		DbHelper.DISTRIBUTOR_CONTACT_NAME, 
		DbHelper.DISTRIBUTOR_CONTACT_EMAIL1, 
		DbHelper.DISTRIBUTOR_CONTACT_EMAIL2, 
		DbHelper.DISTRIBUTOR_CONTACT_PHONE_NUMBER1, 
		DbHelper.DISTRIBUTOR_CONTACT_PHONE_NUMBER2, 
		DbHelper.DISTRIBUTOR_CONTACT_PHONE_NUMBER3, 
		DbHelper.DISTRIBUTOR_CONTACT_PHONE_NUMBER4, 
		DbHelper.DISTRIBUTOR_CONTACT_FK_BRAND
	};

	public DistributorContactProvider (Context context) {
		db_helper = DbHelper.getInstance(context);
		try{
			database = db_helper.getWritableDatabase();
		}catch(SQLException e){
			Log.wtf("DistributorContactDataSource", "Exception: "+Log.getStackTraceString(e));
		}
	}

	public void open () throws SQLException {
		database = db_helper.getWritableDatabase();
	}

	public void close () {
		db_helper.close();
	}

	public Cursor listAll () {
		Cursor cursor = database.query(DbHelper.TABLE_DISTRIBUTOR_CONTACT,
			selectableColumns,null,null, null, null, null);
		return cursor;
	}

	public Cursor getById (long id) {
		Cursor cursor = database.query(DbHelper.TABLE_DISTRIBUTOR_CONTACT,
			selectableColumns,
			DbHelper.DISTRIBUTOR_CONTACT_ID + " = " + id,
			null, null, null, null);
		return cursor;
	}

	public Cursor listSome (long page_count, long page_size) {
		String query = "SELECT id, server_id, dirty, " +
			"last_update, " +
			"name, " +
			"email1, " +
			"email2, " +
			"phone_number1, " +
			"phone_number2, " +
			"phone_number3, " +
			"phone_number4, " +
			"fk_brand" +
			" FROM " + DbHelper.TABLE_DISTRIBUTOR_CONTACT;
		if(page_size > 0){
			query += " LIMIT " + String.valueOf(page_size) + " OFFSET " + String.valueOf(page_size * page_count);
		}
		query += ";";
		Cursor cursor = database.rawQuery(query, null);
		return cursor;
	}

	public Cursor getLastId () {
		String query = "SELECT MAX(id) FROM " + DbHelper.TABLE_DISTRIBUTOR_CONTACT +";";
		Cursor cursor = database.rawQuery(query, null);
		return cursor;		
	}

// begin content-provider-interface

	@Override
	public boolean onCreate () {
		return false;
	}

	@Nullable
	@Override
	public String getType (@NonNull Uri uri) {

		switch (MATCHER.match(uri)){
			case DISTRIBUTOR_CONTACT_INSERT_NUMBER:
				return DISTRIBUTOR_CONTACT_INSERT_TYPE;
			case DISTRIBUTOR_CONTACT_UPDATE_NUMBER:
				return DISTRIBUTOR_CONTACT_UPDATE_TYPE;
			case DISTRIBUTOR_CONTACT_DELETE_NUMBER:
				return DISTRIBUTOR_CONTACT_DELETE_TYPE;
			case DISTRIBUTOR_CONTACT_ALL_NUMBER:
				return DISTRIBUTOR_CONTACT_ALL_TYPE;
			case DISTRIBUTOR_CONTACT_SOME_NUMBER:
				return DISTRIBUTOR_CONTACT_SOME_TYPE;
			case DISTRIBUTOR_CONTACT_BY_ID_NUMBER:
				return DISTRIBUTOR_CONTACT_BY_ID_TYPE;
			case DISTRIBUTOR_CONTACT_LAST_ID_NUMBER:
				return DISTRIBUTOR_CONTACT_LAST_ID_TYPE;
// reserved-for:AndroidSqliteDatabase003.1
// End of user code

// Start of user code reserved-for:AndroidSqliteQuerySingle002.2
// reserved-for:AndroidSqliteQuerySingle002.2
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabase003.2
		}
		return null;
	}
// reserved-for:AndroidSqliteDatabase003.2
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabase004
	@Nullable
	@Override
	public Uri insert (@NonNull Uri uri, @Nullable ContentValues values) {
		Cursor result = null;
		if (URI_DISTRIBUTOR_CONTACT_INSERT.equals(uri)) {
			result = database.insert(DbHelper.TABLE_DISTRIBUTOR_CONTACT, null, values);
		}
// reserved-for:AndroidSqliteDatabase004
// End of user code

// Start of user code reserved-for:AndroidSqliteQuerySingle003
/* @Insert */
// reserved-for:AndroidSqliteQuerySingle003
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabase005
		return null;
	}
// reserved-for:AndroidSqliteDatabase005
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabase006
	@Override
	public int update (@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
		int result = 0;
		if (URI_DISTRIBUTOR_CONTACT_UPDATE.equals(uri)) {
			result = database.update(DbHelper.TABLE_DISTRIBUTOR_CONTACT, values, DbHelper.DISTRIBUTOR_CONTACT_ID + " = " + selectionArgs[0], null);
		}
// reserved-for:AndroidSqliteDatabase006
// End of user code

// Start of user code reserved-for:AndroidSqliteQuerySingle004
/* @UpdateWhere */
// reserved-for:AndroidSqliteQuerySingle004
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabase007
		return result;
	}
// reserved-for:AndroidSqliteDatabase007
// End of user code


// Start of user code reserved-for:AndroidSqliteDatabase008
	@Override
	public int delete (@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
		int result = 0;
		if (URI_DISTRIBUTOR_CONTACT_DELETE.equals(uri)) {
			result = database.delete(DbHelper.TABLE_DISTRIBUTOR_CONTACT, DbHelper.DISTRIBUTOR_CONTACT_ID + " = " + selectionArgs[0], null);
		}
// reserved-for:AndroidSqliteDatabase008
// End of user code

// Start of user code reserved-for:AndroidSqliteQuerySingle005
/* @DeleteWhere */
// reserved-for:AndroidSqliteQuerySingle005
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabase009
		return result;
	}
// reserved-for:AndroidSqliteDatabase009
// End of user code

// Start of user code reserved-for:AndroidSqliteSyncSingle003
// reserved-for:AndroidSqliteSyncSingle003
// End of user code

// Start of user code reserved-for:AndroidSqliteQuerySingle006
// reserved-for:AndroidSqliteQuerySingle006
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabase010
	// TODO: I NEED TO KNOW HOW TO MAKE VARIOUS QUERIES DEPENDING ON THE URI
	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
		Cursor result = null;
		if (URI_DISTRIBUTOR_CONTACT_ALL.equals(uri)) {
			result = listAll();
		}
		else if (URI_DISTRIBUTOR_CONTACT_SOME.equals(uri)) {
			result = listSome(Long.parseLong(selectionArgs[0]), Long.parseLong(selectionArgs[1]));
		}
		else if (URI_DISTRIBUTOR_CONTACT_BY_ID.equals(uri)) {
			result = getById(Long.parseLong(selectionArgs[0]));
		}
		else if (URI_DISTRIBUTOR_CONTACT_LAST_ID.equals(uri)) {
			result = getLastId();
		}
// reserved-for:AndroidSqliteDatabase010
// End of user code

// Start of user code reserved-for:AndroidSqliteSyncSingle004
// reserved-for:AndroidSqliteSyncSingle004
// End of user code

// Start of user code reserved-for:AndroidSqliteQuerySingle007
/* @ExistsWhere||@SelectValueWhere||@SelectOneWhere||@SelectListWhere */
// Start of user code reserved-for:AndroidSqliteQuerySingle007

// Start of user code reserved-for:AndroidSqliteDatabase011
		return result;
	}
}
// reserved-for:AndroidSqliteDatabase011
// End of user code
