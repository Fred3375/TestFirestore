package com.dam.testfirestore;

import com.google.firebase.firestore.Exclude;

public class ModelNote {
    private String documentId;
    private String titre;
    private String note;

    public ModelNote(String titre, String note) {
        this.titre = titre;
        this.note = note;
    }

    public ModelNote() {
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
