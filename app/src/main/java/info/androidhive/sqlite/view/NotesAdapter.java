package info.androidhive.sqlite.view;

/**
 * Created by ravi on 20/02/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.DatabaseHelper;
import info.androidhive.sqlite.database.model.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private Context context;
    private List<Note> notesList;
    private DatabaseHelper db;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public TextView dot;
        public TextView timestamp;
        public CheckBox  chkIos;
        public RelativeLayout task;
        String status;

        public MyViewHolder(View view) {
            super(view);
            note = view.findViewById(R.id.note);
            dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);
            chkIos= (CheckBox)view.findViewById(R.id.finished);
            task= (RelativeLayout) view.findViewById(R.id.task);
            db = new DatabaseHelper(context);
        }
    }


    public NotesAdapter(Context context, List<Note> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Note note = notesList.get(position);

        holder.note.setText(note.getNote());
        if(note.getStatus()!=null)
        if(note.getStatus().equals("Finished")) {
            holder.task.setBackgroundResource(R.drawable.finished);
            holder.chkIos.setChecked(true);

        }

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(note.getTimestamp()));
        holder.chkIos.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        //holder.chkIos.setChecked(objIncome.isSelected());

        holder.chkIos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
               if(holder.chkIos.isChecked()) {
                   Toast.makeText(context, "Task Finished", Toast.LENGTH_SHORT).show();
                   updateNoteStatus(holder.getAdapterPosition(),true);
                   holder.task.setBackgroundResource(R.drawable.finished);
               }else {
                   Toast.makeText(context, "Task forwarded to redo", Toast.LENGTH_SHORT).show();
                   updateNoteStatus(holder.getAdapterPosition(),false);
                   holder.task.setBackgroundResource(R.color.white);
               }

            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }

    private void updateNoteStatus( int position,boolean flag) {
        Note n = notesList.get(position);

        // updating note text


        // updating note in db
        db.updateNoteStatus(n,flag);

        // refreshing the list


    }
}
