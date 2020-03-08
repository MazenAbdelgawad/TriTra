package iti.intake40.tritra.notes;

import java.util.List;

import iti.intake40.tritra.model.NoteModel;

public interface NotesContract {

    interface PresenterInterface {
        void getAllNotes(String tripId);
        void setAllNotes(List<NoteModel> notes);
        void notifyUpdate(NoteModel note, int pos);
        void notifyDelete(NoteModel note, int pos);

    }

    interface ViewInterface {
        void updateNoteList(List<NoteModel> notes);
    }
}
