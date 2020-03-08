package iti.intake40.tritra.notes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import iti.intake40.tritra.R;
import iti.intake40.tritra.model.Database;
import iti.intake40.tritra.model.NoteModel;

public class NoteActivity extends AppCompatActivity implements NotesContract.ViewInterface{
    NotesContract.PresenterInterface presenterInterface;
    EditText addnotetxt;
    ImageButton addnotebtn;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    String tripid;
    ArrayList<NoteModel>notes;
    NoteAdapter adapter;
    public static final String TRIP_ID_KEY="tripid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setUpViews();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notes");
        Intent receivedintent =getIntent();
        tripid=receivedintent.getStringExtra(TRIP_ID_KEY);

        presenterInterface = new NotesPresenter(this);
        presenterInterface.getAllNotes(tripid);

        addnotebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String note=addnotetxt.getText().toString().trim();
                if(TextUtils.isEmpty(note)){
                    addnotetxt.setError("fill empty field");
                }else{
                    NoteModel noteModel=new NoteModel("id",note,"TODO");
                    Database.getInstance().addNote(noteModel,tripid);
                    addnotetxt.setText("");
                    //notes.add(noteModel);

                }
            }
        });
    }


    public void setUpViews(){
        addnotetxt=findViewById(R.id.add_node_txt);
        addnotebtn=findViewById(R.id.add_node_btn);
        recyclerView=findViewById(R.id.recycleview);
    }


    @Override
    public void updateNoteList(List<NoteModel> notes) {
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new NoteAdapter(getApplicationContext(),notes,presenterInterface);
        recyclerView.setAdapter(adapter);
    }

}
