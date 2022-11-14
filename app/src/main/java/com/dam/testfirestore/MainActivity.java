package com.dam.testfirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    /** cles de la base */
    private static final String KEY_TITRE = "titre";
    private static final String KEY_NOTE = "note";

    private EditText etNoteTitle, etNoteText;
    private TextView tvShowNote1, tvShowNote2;
    private Button btnSave, btnShow;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // 3 méthodes d'acces possible voir le cours pour les deux autres
    private DocumentReference noteRef = db.document("listeDeNote/Note1");



    private void initUI(){
        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteText = findViewById(R.id.etNoteText);
        tvShowNote1 = findViewById(R.id.tvShowNote1);
        tvShowNote2 = findViewById(R.id.tvShowNote2);
        btnSave = findViewById(R.id.btnSave);
        btnShow = findViewById(R.id.btnShow);
    }

    // méthode pour ajouter un listener sur btnSend
    private void sendMessage(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titre = etNoteTitle.getText().toString();
                String note = etNoteText.getText().toString();

                Map<String, Object> noteNode = new HashMap<>();
                noteNode.put(KEY_TITRE, titre);
                noteNode.put(KEY_NOTE, note);

                // envoi des données
                noteRef.set(noteNode)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Note enregistrée", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Erreur lors de l'envoi", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailure: " + e.toString());
                            }
                        });
            }

        });
    }

    private void showNote1(){
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                String titre = documentSnapshot.getString(KEY_TITRE);
                                String note = documentSnapshot.getString(KEY_NOTE);
                                // ou encore
                                // Map<String, Object> noteNode = documentSnapshot.getData();
                                // titre = noteNode.get().KEY_TITRE);
                                tvShowNote1.setText("Titre de la note : " + titre + "\n" + "Note : " + note);
                                Toast.makeText(MainActivity.this, "Document lu par la méthode 1", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Le document n'existe pas", Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Erreur lors de la lecture !", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: "+ e.toString());
                        }
                    });
            }
        });
    }

    protected void onStart() {
        super.onStart();
        noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(MainActivity.this, "Erreur lors de la lecture !", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: "+ error.toString());
                    return;
                }
                if (value.exists()) {
                    Map<String, Object> noteNode = value.getData();
                    String titre = noteNode.get(KEY_TITRE).toString();
                    String note = noteNode.get(KEY_NOTE).toString();
                    tvShowNote2.setText("Titre de la note : " + titre + "\n" + "Note : " + note);
                } else {
                    tvShowNote2.setText("");
                }

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        sendMessage();
        showNote1();
    }
}