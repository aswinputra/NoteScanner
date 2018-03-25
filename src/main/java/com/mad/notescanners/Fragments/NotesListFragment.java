package com.mad.notescanners.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mad.notescanners.adapter.AdapterNote;
import com.mad.notescanners.CONSTANT;
import com.mad.notescanners.activity.EditNoteActivity;
import com.mad.notescanners.Model.Note;
import com.mad.notescanners.R;
import com.mad.notescanners.db.NoteDatabaseHelper;

import java.util.ArrayList;

/**
 * Created by aswinhartono on 2/6/17.
 */

public class NotesListFragment extends Fragment {

    private Note notes;
    private ArrayList<Note> mNoteList= new ArrayList<>();
    private AdapterNote mAdapter;
    private NoteDatabaseHelper mDb;

    @SuppressLint("ValidFragment")
    public NotesListFragment(ArrayList<Note> mNoteList, NoteDatabaseHelper db){
        this.mNoteList = mNoteList;
        this.mDb = db;
    }

    public NotesListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list,container,false);

        RecyclerView NoteContainer = (RecyclerView) view.findViewById(R.id.notes_container);
        mAdapter = new AdapterNote(getContext(),mNoteList);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        NoteContainer.setLayoutManager(mLayoutManager);
        NoteContainer.setItemAnimator(new DefaultItemAnimator());
        NoteContainer.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add new note to database
                mDb.insertNote("","");
                //add new note to ArrayList
                notes = new Note("","");
                mNoteList.add(notes);
                int position = mNoteList.size();
                //get title and content and pass it to the next activity
                Intent newNote = new Intent(getContext(), EditNoteActivity.class);
                newNote.putExtra(CONSTANT.TITLE,notes.getmTitle());
                newNote.putExtra(CONSTANT.TITLE,notes.getmNote());
                newNote.putExtra(CONSTANT.POSITION,position-1);
                startActivityForResult(newNote,1);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1) {
            int position;
            if(resultCode == EditNoteActivity.RESULT_OK){
                //update sqlite database after save
                Bundle bundle = data.getBundleExtra(CONSTANT.RESULT);
                String titleToChange = bundle.getString(CONSTANT.TITLE);
                String contentToChange = bundle.getString(CONSTANT.NOTE);
                position = bundle.getInt(CONSTANT.POSITION);
                mNoteList.get(position).setmTitle(titleToChange);
                mNoteList.get(position).setmNote(contentToChange);
                String positiontoUpdate = Integer.toString(position +1);
                mDb.updateNote(positiontoUpdate, titleToChange, contentToChange);
            }
            if (resultCode ==EditNoteActivity.RESULT_CANCELED){

                Bundle cancel = data.getBundleExtra(CONSTANT.RESULT);
                position = cancel.getInt(CONSTANT.POSITION);
                String title = cancel.getString(CONSTANT.TITLE);
                String note = cancel.getString(CONSTANT.NOTE);
                mNoteList.get(position).setmTitle(title);
                mNoteList.get(position).setmNote(note);
                String positiontoUpdate = Integer.toString(position +1);
                //check if both text are empty then delete
                if (title.equals("")&& note.equals("")) {
                    String posRemove = Integer.toString(position +1);
                    mNoteList.remove(position);
                    mDb.deleteNote(posRemove);
                }
                // update if content not empty
                else {
                    mDb.updateNote(positiontoUpdate, title, note);
                }
            }
        }
        //update adapter after all is checked
        mAdapter.notifyDataSetChanged();
    }


}
