package com.mad.notescanners.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mad.notescanners.CONSTANT;
import com.mad.notescanners.activity.EditNoteActivity;
import com.mad.notescanners.Model.Note;
import com.mad.notescanners.R;

import java.util.ArrayList;

/**
 * Created by aswinhartono on 2/6/17.
 */

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.ViewHolder> {
    private final LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<Note> mNotes;
    private Fragment mFilledFragment;
    private FragmentManager mFragmentManager;

    public AdapterNote(Context context, ArrayList<Note> notes) {
        this.mContext = context;
        this.mNotes = notes;
        mInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public AdapterNote.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_notes,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AdapterNote.ViewHolder holder, final int position) {
        final Note notes = mNotes.get(position);
        //check if title is empty
        if (notes.getmTitle().equals("")|| notes.getmTitle()==null){
            //change the Content size into bigger
            holder.mTitle.setText(notes.getmTitle());
            holder.mNote.setText(notes.getmNote());
            holder.mNote.setTextSize(CONSTANT.NOTESIZE);
            holder.mTitle.setVisibility(View.GONE);
        }
        else{
            //set the title and note as normal
            holder.mTitle.setVisibility(View.VISIBLE);
            holder.mTitle.setText(notes.getmTitle());
            holder.mNote.setText(notes.getmNote());
            holder.mNote.setTextSize(CONSTANT.NORMALSIZE);
        }
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pass data to editTextActivity for the item choosen
                Intent intent = new Intent(mContext,EditNoteActivity.class);
                intent.putExtra(CONSTANT.TITLE, notes.getmTitle());
                intent.putExtra(CONSTANT.NOTE, notes.getmNote());
                intent.putExtra(CONSTANT.POSITION,position);
                //Change activity
                ((Activity) mContext).startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mNote;
        private RelativeLayout mLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.adapter_note_title);
            mNote = (TextView) itemView.findViewById(R.id.adapter_note_content);
            mLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout_adapter);
        }
    }
}
