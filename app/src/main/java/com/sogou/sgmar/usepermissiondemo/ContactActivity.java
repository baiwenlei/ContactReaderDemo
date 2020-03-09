package com.sogou.sgmar.usepermissiondemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {
    private ArrayList<String> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        readContacts();

        RecyclerView listView = (RecyclerView)findViewById(R.id.contact_view);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(new ContactAdapter());
    }

    void readContacts() {
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null,
                null);

        if (cursor == null) {
            return;
        }

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contacts.add(name + " " + number);
            Log.e("baiwenlei", "get contact: " + name + " " + number);
        }

        cursor.close();
    }

    class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Holder> {
        @NonNull
        @Override
        public ContactAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactAdapter.Holder holder, int position) {
            if ((position & 1) == 1) {
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
            String contact = contacts.get(position);
            holder.textView.setText(contact);
        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView textView;
            public Holder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.contact_item_text_view);
            }
        }
    }
}
