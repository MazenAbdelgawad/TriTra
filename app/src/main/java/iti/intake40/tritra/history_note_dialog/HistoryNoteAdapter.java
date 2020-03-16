package iti.intake40.tritra.history_note_dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import iti.intake40.tritra.R;
import iti.intake40.tritra.model.NoteModel;

public class HistoryNoteAdapter extends RecyclerView.Adapter<HistoryNoteAdapter.HolderNode> {
    Context _context;
    List<NoteModel>notes;

    public HistoryNoteAdapter(Context _context, List<NoteModel> notes) {
        this._context = _context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public HolderNode onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.card_note_history,parent,false);
        HolderNode holderNode=new HolderNode(view);
        return holderNode;
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderNode holder, final int position) {
     holder.notetxt.setText(notes.get(position).getNote());
        System.out.println("SOUT= " + notes.get(position).getNote());
     if(notes.get(position).getStatus().equals(NoteModel.STATUS.TODO))
        holder.notecheckbox.setChecked(false);
     else
         holder.notecheckbox.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class HolderNode extends RecyclerView.ViewHolder {
        private MaterialTextView notetxt;
        private CheckBox notecheckbox;

        public HolderNode(@NonNull View itemView) {
            super(itemView);
            notetxt=itemView.findViewById(R.id.notetxt);
            notecheckbox=itemView.findViewById(R.id.notecheckbox);
        }
    }


}
