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
package ru.kazakova_net.bookstore;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static ru.kazakova_net.bookstore.data.BookContract.BookEntry;

public class BookStoreAdapter extends CursorAdapter {
    
    /**
     * Constructs a new {@link BookStoreAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BookStoreAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }
    
    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.book_store_list_item, parent, false);
    }
    
    /**
     * This method binds the book data (in the current row pointed to by cursor) to the given
     * list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView titleTextView = view.findViewById(R.id.book_title);
        TextView priceTextView = view.findViewById(R.id.book_price);
        TextView quantityTextView = view.findViewById(R.id.book_quantity);
        
        // Find the button, by clicking on which the number of books will decrease
        ImageButton saleImageButton = view.findViewById(R.id.sale);
        
        // Find the columns of book attributes that we're interested in
        int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
        int titleColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_TITLE);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
        
        // Read the book attributes from the Cursor for the current book
        final int bookId = cursor.getInt(idColumnIndex);
        String bookTitle = cursor.getString(titleColumnIndex);
        String bookPrice = cursor.getString(priceColumnIndex);
        final String bookQuantity = cursor.getString(quantityColumnIndex);
        
        // Update the TextViews with the attributes for the current book
        titleTextView.setText(bookTitle);
        priceTextView.setText(context.getString(R.string.item_price_label, bookPrice));
        quantityTextView.setText(context.getString(R.string.item_quantity_label, bookQuantity));
        
        // Assign a click handler to the Sale button
        saleImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityInteger = Integer.parseInt(bookQuantity) - 1;
                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, bookId);
                Toast toast = Toast.makeText(context, "One book sailed!", Toast.LENGTH_SHORT);
                
                if (quantityInteger >= 0) {
                    ContentValues values = new ContentValues();
                    values.put(BookEntry.COLUMN_BOOK_QUANTITY, Integer.parseInt(String.valueOf(quantityInteger)));
                    
                    
                    context.getContentResolver().update(currentBookUri, values, null, null);
                    
                    if (toast != null) {
                        toast.cancel();
                        toast.show();
                    }
                    
                }
                
                if (quantityInteger == 0) {
                    context.getContentResolver().delete(currentBookUri, null, null);
                }
            }
        });
    }
}
