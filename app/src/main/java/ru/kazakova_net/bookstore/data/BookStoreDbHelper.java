/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.kazakova_net.bookstore.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static ru.kazakova_net.bookstore.data.BookContract.BookEntry;

/**
 * Database helper for BookStore app. Manages database creation and version management.
 */
public class BookStoreDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = BookStoreDbHelper.class.getSimpleName();
    
    /**
     * Database version.
     */
    private static final int DATABASE_VERSION = 2;
    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "bookstore.db";
    
    /**
     * Constructs a new instance of {@link BookStoreDbHelper}.
     *
     * @param context of the app
     */
    public BookStoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a String that contains the SQL statement to create the books table
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME +
                " (" + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BookEntry.COLUMN_BOOK_NAME + " TEXT, "
                + BookEntry.COLUMN_BOOK_PRICE + " INTEGER, "
                + BookEntry.COLUMN_BOOK_QUANTITY + " INTEGER, "
                + BookEntry.COLUMN_BOOK_SUPPLIER_NAME + " TEXT, "
                + BookEntry.COLUMN_BOOK_SUPPLIER_PHONE + " TEXT);";
        
        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_BOOKS_TABLE);
    }
    
    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Delete table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BookEntry.TABLE_NAME + ";");
        
        // Create new version of table
        onCreate(sqLiteDatabase);
    }
}
