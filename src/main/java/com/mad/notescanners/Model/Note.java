package com.mad.notescanners.Model;

/**
 * Created by aswinhartono on 2/6/17.
 */

public class Note{

    private  String mTitle;
    private  String mNote;

    public Note(String title, String note){
        setmTitle(title);
        setmNote(note);
    }

    public String getmNote() {
        return mNote;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmNote(String mNote) {
        this.mNote = mNote;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
