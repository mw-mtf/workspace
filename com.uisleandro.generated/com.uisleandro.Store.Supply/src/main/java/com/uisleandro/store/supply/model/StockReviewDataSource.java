// Start of user code reserved-for:AndroidSqliteDatabaseSingle001
package com.uisleandro.store.supply.model;  

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.util.ArrayList;
import java.util.List;

import com.uisleandro.util.LoaderInterface;
import com.uisleandro.store.supply.view.StockReviewDataView;
// reserved-for:AndroidSqliteDatabaseSingle001
// End of user code

// Start of user code reserved-for:AndroidSqliteQuerySingle001
// reserved-for:AndroidSqliteQuerySingle001
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabaseSingle002
public class StockReviewDataSource implements LoaderManager.LoaderCallbacks<Cursor> {

/* Yep this class implements the loaderCallbacks 

context.getloadermanager().init(FN_id, bundle, this)
I need to know about the class Bundle, which seems cary the data

*/

	public static final String AUTHORITY = "com.uisleandro.stock_review";
	public static final String SCHEME = "content://";

	public static final int FN_STOCK_REVIEW_INSERT = 998211;
	public static final int FN_STOCK_REVIEW_UPDATE = 998212;
	public static final int FN_STOCK_REVIEW_DELETE = 998213;
	public static final int FN_STOCK_REVIEW_ALL = 998214;
	public static final int FN_STOCK_REVIEW_SOME = 998215;
	public static final int FN_STOCK_REVIEW_BY_ID = 998216;
	public static final int FN_STOCK_REVIEW_LAST_ID = 998217;

// reserved-for:AndroidSqliteDatabaseSingle002
// End of user code

// Start of user code reserved-for:AndroidSqliteQuerySingle001.1
// reserved-for:AndroidSqliteQuerySingle001.1
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabaseSingle002.1

	Context context;
	LoaderManager.LoaderCallbacks<Cursor> cursorLoader;
	public StockReviewDataSource (Context context) {
		this.context = context;
		cursorLoader = (LoaderManager.LoaderCallbacks<Cursor>) context; 
	}

	public List<StockReviewDataView> listAll () {
		List<StockReviewDataView> those = new ArrayList<>();
		Cursor cursor = context.getContentResolver().query(Uri.parse(SCHEME + AUTHORITY + "/all"), null, null, null, null);
		if (null != cursor) {
			cursor.moveToFirst();
		    while(!cursor.isAfterLast()){
		      those.add(StockReviewDataView.FromCursor(cursor));
		      cursor.moveToNext();
		    }
		    cursor.close();
		}
	    return those;
	}

	public StockReviewDataView getById (long id) {
		StockReviewDataView that = null;
		Cursor cursor = context.getContentResolver().query(Uri.parse(SCHEME + AUTHORITY + "/by_id"), null, null, new String[]{ String.valueOf(id) }, null);
		if (null != cursor) {
			cursor.moveToFirst();
		    if(!cursor.isAfterLast()){
		      that = StockReviewDataView.FromCursor(cursor);
		    }
			cursor.close();
		}
	    return that;
	}

	public List<StockReviewDataView> listSome (long page_count, long page_size) {
		List<StockReviewDataView> those = new ArrayList<>();
		Cursor cursor = context.getContentResolver().query(Uri.parse(SCHEME + AUTHORITY + "/some"), new String[]{ String.valueOf(page_count), String.valueOf(page_size) }, null, null, null);
		if (null != cursor) {
			cursor.moveToFirst();
		    while(!cursor.isAfterLast()){
		      those.add(StockReviewDataView.FromCursor(cursor));
		      cursor.moveToNext();
		    }
		    cursor.close();
		}
	    return those;
	}

	public long getLastId () {
		long result = 0;
		Cursor cursor = context.getContentResolver().query(Uri.parse(SCHEME + AUTHORITY + "/last_id"), null, null, null, null);
		if (null != cursor) {
			cursor.moveToFirst();
		    if(!cursor.isAfterLast()){
		      result = cursor.getLong(0);
		    }
		}
	    return result;	
	}

	public Uri insert (StockReviewDataView that) {
		Uri result = context.getContentResolver().insert(Uri.parse(SCHEME + AUTHORITY + "/insert"), that.toInsertValues());
		return result;
	}

	public int update (StockReviewDataView that) {
		return context.getContentResolver().update(Uri.parse(SCHEME + AUTHORITY + "/update"), that.toUpdateValues(), null, new String[]{ String.valueOf(that.getId()) });
	}

	public int delete (StockReviewDataView that) {
		return context.getContentResolver().delete(Uri.parse(SCHEME + AUTHORITY + "/delete"), null, new String[]{ String.valueOf(that.getId()) });
	}

// reserved-for:AndroidSqliteDatabaseSingle002.1
// End of user code

// Start of user code reserved-for:AndroidSqliteQuerySingle002
// reserved-for:AndroidSqliteQuerySingle002
// End of user code


// Start of user code reserved-for:AndroidSqliteDatabaseSingle002.2
  //TODO: PLEASE REVIEW THIS CODE
  public void please_remake_it(int i, Bundle bundle) {
// reserved-for:AndroidSqliteDatabaseSingle002.2
// End of user code

// Start of user code reserved-for:AndroidSqliteQuerySingle002.1

// reserved-for:AndroidSqliteQuerySingle002.1
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabaseSingle002.3
  }
// reserved-for:AndroidSqliteDatabaseSingle002.3
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabaseSingle002.4
  @Override
  public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

    if (FN_STOCK_REVIEW_INSERT == i) {
		return new CursorLoader(this.context, Uri.parse(SCHEME + AUTHORITY + "/insert"), null, null, null, null);
    }
    else if (FN_STOCK_REVIEW_UPDATE == i) {
		return new CursorLoader(this.context, Uri.parse(SCHEME + AUTHORITY + "/update"), null, null, null, null);
    }
    else if (FN_STOCK_REVIEW_DELETE == i) {
		return new CursorLoader(this.context, Uri.parse(SCHEME + AUTHORITY + "/delete"), null, null, null, null);
    }
    else if (FN_STOCK_REVIEW_ALL == i) {
		return new CursorLoader(this.context, Uri.parse(SCHEME + AUTHORITY + "/all"), null, null, null, null);
    }
    else if (FN_STOCK_REVIEW_SOME == i) {
		return new CursorLoader(this.context, Uri.parse(SCHEME + AUTHORITY + "/some"), new String[]{ bundle.getString("page_count"), bundle.getString("page_size") }, null, null, null);
    }
    else if (FN_STOCK_REVIEW_BY_ID == i) {
		return new CursorLoader(this.context, Uri.parse(SCHEME + AUTHORITY + "/by_id"), null, null, new String[]{ bundle.getString("id") }, null);
    }
    else if (FN_STOCK_REVIEW_LAST_ID == i) {
		return new CursorLoader(this.context, Uri.parse(SCHEME + AUTHORITY + "/last_id"), null, null, null, null);
    }
// reserved-for:AndroidSqliteDatabaseSingle002.4
// End of user code

// Start of user code reserved-for:AndroidSqliteQuerySingle002.2
// reserved-for:AndroidSqliteQuerySingle002.2
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabaseSingle002.5
    return null;
  }
// reserved-for:AndroidSqliteDatabaseSingle002.5
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabaseSingle002.6
  @Override
  public void onLoaderReset(Loader<Cursor> cursorLoader) {
// reserved-for:AndroidSqliteDatabaseSingle002.6
// End of user code

// Start of user code reserved-for:AndroidSqliteQuerySingle002.3

// reserved-for:AndroidSqliteQuerySingle002.3
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabaseSingle002.7
  }
// reserved-for:AndroidSqliteDatabaseSingle002.7
// End of user code

// Start of user code reserved-for:AndroidSqliteDatabaseSingle002.8
  @Override
  public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
	((LoaderInterface)this.context).onLoadFinished(cursorLoader,cursor);
  }
}
// reserved-for:AndroidSqliteDatabaseSingle002.8
// End of user code

