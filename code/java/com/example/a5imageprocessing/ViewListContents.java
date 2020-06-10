package com.example.a5imageprocessing;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewListContents extends AppCompatActivity {

    DatabaseHelper myDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcontents_layout);

        final ListView listview = (ListView)findViewById(R.id.viewcontentsListView);
        myDB = new DatabaseHelper(this);


        final ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        final ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);


        if(data.getCount()==0){
            Toast.makeText(ViewListContents.this,"Database is EMPTY :(", Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                theList.add("PLANT I.D.: "+data.getString(0)+"\n"+"ACCESSION NO.: "+data.getString(1)+"\n"+"PLOT NO.: "+data.getString(2)+"\n"+"WIDTH: "+data.getString(3)+"\n"+"HEIGHT: "+data.getString(4)+"\n"+"DATE: "+data.getString(5));
                listview.setAdapter(listAdapter);
            }
        }

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                 final int which_item = position;

                 new AlertDialog.Builder(ViewListContents.this)
                         .setTitle("ALERT")
                         .setMessage("Are you sure you want to delete this entry?")
                         .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 myDB.deleteAnEntry(which_item);
                                 //theList.remove(which_item);
                                 //((ArrayAdapter) listAdapter).notifyDataSetChanged();
                                 Toast.makeText(ViewListContents.this,"Successfully deleted an entry!", Toast.LENGTH_LONG).show();
                                 finish();
                             }
                         })

                         .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 dialog.cancel();
                             }
                         })
                            .show();

                return true;
            }
        });

    }

}