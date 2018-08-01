package ru.kazakova_net.bookstore;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ru.kazakova_net.bookstore.data.BookStoreDbHelper;

import static ru.kazakova_net.bookstore.data.BookContract.BookEntry;

public class MainActivity extends AppCompatActivity {
    
    private static final String LOG_TAG = "BOOK_LOG";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        insertBook();
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
        BookStoreDbHelper mDbHelper = new BookStoreDbHelper(this);
        
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
}
