package iti.intake40.tritra.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import iti.intake40.tritra.home.HomeContract;

public class Database {
    private FirebaseDatabase dbReference;

    private Database() {
        dbReference = FirebaseDatabase.getInstance();
    }

    private static class SingletonHelper{
        private static final Database INSTANCE = new Database();
    }

    public static Database getInstance(){
        return SingletonHelper.INSTANCE;
    }

    public  void addUser(UserModle user){
        dbReference.getReference("user").child(user.getId()).setValue(user);
    }


    public void addTrip(TripModel trip,String userId){
        DatabaseReference databaseReference = dbReference.getReference("trip").child(userId);
        String id = databaseReference.push().getKey();
        trip.setId(id);
        databaseReference.child(id).setValue(trip);
        System.out.println("iiidTrip= "+id);
    }

    public void deleteTrip(String tripId,String userId){
        DatabaseReference drTrip = dbReference.getReference("trip").child(userId).child(tripId);
        DatabaseReference drNote = dbReference.getReference("note").child(tripId);
        drNote.removeValue();
        drTrip.removeValue();
        System.out.println("remove Trip = "+tripId);
    }

    public void getTripsForUser(String userId, final HomeContract.PresenterInterface homePresnter){

        dbReference.getReference("trip").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<TripModel> tripList = new ArrayList<>();
                for (DataSnapshot tripsnapshot: dataSnapshot.getChildren()){
                    TripModel trip =tripsnapshot.getValue(TripModel.class);
                    tripList.add(trip);
                }
                homePresnter.setTrips(tripList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.printf("onCancelled-DatabaseError");
                homePresnter.setTrips(new ArrayList<TripModel>());
            }
        });
        //homePresnter.setTrips(new ArrayList<TripModel>());
    }

    public void addNote(NoteModel note,String tripId){
        DatabaseReference databaseReference = dbReference.getReference("note").child(tripId);
        String id = databaseReference.push().getKey();
        note.setId(id);
        databaseReference.child(id).setValue(note);
        System.out.println("iiidnote= "+id);
    }

    public List<NoteModel>getNotesForTrip(String tripId){
        final List<NoteModel> noteList = new ArrayList<>();

        dbReference.getReference("note").child(tripId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot tripsnapshot: dataSnapshot.getChildren()){
                    NoteModel note =tripsnapshot.getValue(NoteModel.class);
                    noteList.add(note);
                    System.out.println("DaTABASE GET id = "+note.getId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.printf("onCancelled-DatabaseError NOTE");
            }
        });

        return noteList;
    }
}
