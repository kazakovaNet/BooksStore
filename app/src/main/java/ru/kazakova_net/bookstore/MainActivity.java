package ru.kazakova_net.bookstore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ru.kazakova_net.bookstore.data.BookStoreDbHelper;

import static ru.kazakova_net.bookstore.data.BookContract.BookEntry;

public class MainActivity extends AppCompatActivity {
    
    private static final String LOG_TAG = "BOOK_LOG";
    private BookStoreDbHelper mDbHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        insertBook();
        getDataFromDb();
    }
    
    /**
     * Save new book into database.
     */
    private void insertBook() {
        // Set dummy data
        String productName = "Android Design Patterns and Best Practices, Kyle Mew";
        int price = 26;
        int quantity = 5;
        String supplierName = "Natalya Kazakova";
        String supplierPhoneNumber = "+79852173660";
        
        // Create database helper
        mDbHelper = new BookStoreDbHelper(this);
        
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        
        // Create a ContentValues object where column names are the keys,
        // and book attributes from the dummy data are the values.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_NAME, productName);
        values.put(BookEntry.COLUMN_BOOK_PRICE, price);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierName);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, supplierPhoneNumber);
        
        // Insert a new row for book in the database, returning the ID of that new row.
        long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);
        
        // Write in log depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Log.e(LOG_TAG, "Error with saving book");
        } else {
            // Otherwise, the insertion was successful and we can write a message with the row ID.
            Log.d(LOG_TAG, "Book saved with row id: " + newRowId);
        }
    }
    
    /**
     * Get data from books database
     */
    private void getDataFromDb() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookEntry.COLUMN_BOOK_SUPPLIER_PHONE};
        
        // Perform a query on the books table
        Cursor cursor = db.query(
                BookEntry.TABLE_NAME,   // The table to query
                projection,             // The columns to return
                null,          // The columns for the WHERE clause
                null,      // The values for the WHERE clause
                null,          // Don't group the rows
                null,           // Don't filter by row groups
                null);         // The sort order
        
        StringBuilder messageBuilder = new StringBuilder();
        try {
            // Create a message in the log that looks like this:
            //
            // The books table contains <number of rows in Cursor> books..
            // _id - name - price - quantity - supplier name - supplier phone number
            //
            // In the while loop below, iterate through the rows of the cursor and write
            // the information from each column in this order.
            messageBuilder.append("The pets table contains ").append(cursor.getCount()).append(" books.\n\n");
            messageBuilder.append(BookEntry._ID + " - " +
                    BookEntry.COLUMN_BOOK_NAME + " - " +
                    BookEntry.COLUMN_BOOK_PRICE + " - " +
                    BookEntry.COLUMN_BOOK_QUANTITY + " - " +
                    BookEntry.COLUMN_BOOK_SUPPLIER_NAME + " - " +
                    BookEntry.COLUMN_BOOK_SUPPLIER_PHONE + "\n");
            
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);
            
            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);
                // Write the values from each column of the current row in the cursor in the log
                messageBuilder.append("\n")
                        .append(currentID).append(" - ")
                        .append(currentName).append(" - ")
                        .append(currentPrice).append(" - ")
                        .append(currentQuantity).append(" - ")
                        .append(currentSupplierName).append(" - ")
                        .append(currentSupplierPhone);
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
        
        Log.d(LOG_TAG, messageBuilder.toString());
    }
}
