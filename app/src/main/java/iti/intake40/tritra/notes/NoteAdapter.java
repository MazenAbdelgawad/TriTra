package iti.intake40.tritra.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import iti.intake40.tritra.R;
import iti.intake40.tritra.model.NoteModel;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.HolderNode> {
    Context _context;
    List<NoteModel>notes;
    NotesContract.PresenterInterface changeInterface;

    public NoteAdapter(Context _context, List<NoteModel> notes, NotesContract.PresenterInterface notePresnter) {
        this._context = _context;
        this.notes = notes;
        this.changeInterface = notePresnter;
    }

    @NonNull
    @Override
    public HolderNode onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.custom_note_row,parent,false);
        HolderNode holderNode=new HolderNode(view);
        return holderNode;
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderNode holder, final int position) {
     holder.notetxt.setText(notes.get(position).getNote());
     holder.noteimbtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             notes.get(position).getId();
             changeInterface.notifyDelete(notes.get(position),position);


         }
     });

     if(notes.get(position).getStatus().equals(NoteModel.STATUS.TODO))
        holder.notecheckbox.setChecked(false);
     else
         holder.notecheckbox.setChecked(true);

     holder.notecheckbox.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             if(holder.notecheckbox.isChecked()){
                 holder.notecheckbox.setChecked(false);
                 NoteModel noteModel = notes.get(position);
                 notes.get(position).setStatus(NoteModel.STATUS.DONE);
                 changeInterface.notifyUpdate(notes.get(position),position);
             }else{
                 holder.notecheckbox.setChecked(true);
                 NoteModel noteModel = notes.get(position);
                 notes.get(position).setStatus(NoteModel.STATUS.TODO);
                 changeInterface.notifyUpdate(notes.get(position),position);

             }
         }
     });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class HolderNode extends RecyclerView.ViewHolder {
        private EditText notetxt;
        private ImageButton noteimbtn;
        private CheckBox notecheckbox;

        public HolderNode(@NonNull View itemView) {
            super(itemView);
            notetxt=itemView.findViewById(R.id.notetxt);
            noteimbtn=itemView.findViewById(R.id.deletebtn);
            notecheckbox=itemView.findViewById(R.id.notecheckbox);
        }
    }

}
