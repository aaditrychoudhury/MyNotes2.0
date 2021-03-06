package com.example.mynotes20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
RecyclerView recyclerView;
FloatingActionButton fab;
Adapter adapter;
DatabaseClass databaseClass;



List<Model> notesList;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
         fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(MainActivity.this,Notes.class);
                 startActivity(intent);
             }
         });



        notesList = new ArrayList<>();

        databaseClass = new DatabaseClass(this);
        fetchAllNotesFromDatabase();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,MainActivity.this,notesList);
        recyclerView.setAdapter(adapter);
    }
    void fetchAllNotesFromDatabase(){
      Cursor cursor = databaseClass.readAllData();
      if (cursor.getCount()==0){
          Toast.makeText(this, "NO Data to show ", Toast.LENGTH_SHORT).show();
      }else{
          while (cursor.moveToNext()){
              notesList.add(new Model(cursor.getString(0),cursor.getString(1),cursor.getString(2)));

          }
      }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu, menu );
        MenuItem searchItem = menu.findItem(R.id.searchOption);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Your Note ");
        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);

                return true;
            }
        };
        searchView.setOnQueryTextListener(listener);




        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.allDelete)
        {
            deleteAllNotes();

        }

        return super.onOptionsItemSelected(item);
    }
    private void deleteAllNotes(){
    DatabaseClass db = new DatabaseClass(MainActivity.this);
    db.deleteAllNotes();
    recreate();


    }
}