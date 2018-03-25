package com.mad.notescanners.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.vision.text.TextRecognizer;
import com.mad.notescanners.CONSTANT;
import com.mad.notescanners.CameraPreview;
import com.mad.notescanners.R;

import java.io.File;


public class EditNoteActivity extends AppCompatActivity {

    private EditText mTitle;
    private EditText mNote;
    private String noteTitle;
    private String noteContent;
    private int position;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);

        mTitle=(EditText)findViewById(R.id.edit_note_title);
        mNote=(EditText)findViewById(R.id.edit_note_content);
        noteTitle= getIntent().getStringExtra(CONSTANT.TITLE);
        noteContent=getIntent().getStringExtra(CONSTANT.NOTE);
        position=getIntent().getIntExtra(CONSTANT.POSITION,0);
        Toast.makeText(this, "item number"+position, Toast.LENGTH_LONG).show();

        mTitle.setText(noteTitle);
        mNote.setText(noteContent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.nav_scan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // open camera and operate ocr function
                Intent intent = new Intent(view.getContext(),CameraPreview.class);
                startActivityForResult(intent,33);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String noteToAppend = "";
        if (requestCode==33){
            if (resultCode==RESULT_OK){
                Bundle bundle = data.getBundleExtra(CONSTANT.RESULT);
                noteToAppend = bundle.getString(CONSTANT.SCANNED);
            }
        }
        mNote.append("\n"+ noteToAppend);
    }

    /**
     * show the back button on toolbar
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        //return result canceled
        //pass data of the list
        Intent result = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(CONSTANT.POSITION,position);
        bundle.putString(CONSTANT.TITLE, mTitle.getText().toString());
        bundle.putString(CONSTANT.NOTE, mNote.getText().toString());
        result.putExtra(CONSTANT.RESULT,bundle);
        setResult(RESULT_CANCELED, result);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_edit_text,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_save:
                //get data from this activity and pass it back to the previous activity
                Intent result = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(CONSTANT.TITLE, mTitle.getText().toString());
                bundle.putString(CONSTANT.NOTE, mNote.getText().toString());
                bundle.putInt(CONSTANT.POSITION,position);
                result.putExtra(CONSTANT.RESULT,bundle);
                setResult(RESULT_OK,result);
                finish();
                break;
            case R.id.action_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, mNote.getText().toString());
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, getText(R.string.send_to)));
        }
        return super.onOptionsItemSelected(item);
    }
}
