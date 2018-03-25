package com.mad.notescanners.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mad.notescanners.Fragments.NotesListFragment;
import com.mad.notescanners.Model.Note;
import com.mad.notescanners.R;
import com.mad.notescanners.db.NoteDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static Fragment mFragment;
    private static FragmentManager mFManager;
    private ArrayList<Note> mNoteList=new ArrayList<>();
    private Note note;
    private NoteDatabaseHelper mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        mDatabase = new NoteDatabaseHelper(this);
        //addData();
        setData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFragment = new NotesListFragment(mNoteList,mDatabase);
        mFManager = getSupportFragmentManager();
        mFManager.beginTransaction().replace(R.id.content_notes,mFragment).commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setData() {
        Cursor res = mDatabase.getAllNote();
        //check if empty
        if (res.getCount() ==0){
            //show message empty
            return;
        }
        while (res.moveToNext()){
            note = new Note(res.getString(1),res.getString(2));
            mNoteList.add(note);
        }
    }

    public void addData(){
        boolean isInserted = mDatabase.insertNote("Yes", "It works");
        if (isInserted){
            Toast.makeText(NotesActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(NotesActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment uploadType = getSupportFragmentManager().findFragmentById(R.id.content_notes);

        if (uploadType != null) {
            uploadType.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_list:
                mFragment = new NotesListFragment(mNoteList,mDatabase);
                break;
            default:
                mFragment = new NotesListFragment(mNoteList,mDatabase);
                break;
        }

        mFManager.beginTransaction().replace(R.id.content_notes,mFragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
