package com.example.deneme.ui.Mesajolustur;

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.deneme.GroupModel;
import com.example.deneme.MesajModel;
import com.example.deneme.R;
import com.example.deneme.Tools;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;


public class MesajolusturFragment extends Fragment {

    EditText mesajAdi,mesaj;
    Button mesajOlusturbutton;
    RecyclerView mesajlar;


    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    ArrayList<MesajModel> mesajModelArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mesajolustur, container, false);


        mesajAdi=view.findViewById(R.id.mesajolustur_mesajAdi);
        mesaj=view.findViewById(R.id.mesajolustur_mesaj);
        mesajOlusturbutton=view.findViewById(R.id.mesajolustur_button);
        mesajlar=view.findViewById(R.id.mesajolustur_mesajlar);

        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        mesajModelArrayList=new ArrayList<>();

        mesajOlusturbutton.setOnClickListener(v -> {
            String mesajAdiS=mesajAdi.getText().toString();
            String mesajS=mesaj.getText().toString();

            if(mesajAdiS.isEmpty()||mesajS.isEmpty()){
                Tools.showMessage("Please fill in the blank fields.");
                return;
            }
            MesajOlustur(mesajAdiS,mesajS);

        });

        MesajlarıGetir();
        return view;
    }
    private void MesajOlustur(String messageName, String messageDescription) {
        String userId = firebaseAuth.getCurrentUser().getUid();

        firestore.collection("/userdata/" + userId + "/messages").add(new HashMap<String, String>(){{
                    put("name", messageName);
                    put("description", messageDescription);
                }})
                .addOnSuccessListener(documentReference -> {
                    Tools.showMessage("Message created successfully.");

                    documentReference.get().addOnSuccessListener(documentSnapshot -> {
                        MesajModel messageModel = new MesajModel(messageName, messageDescription, documentSnapshot.getId());
                        mesajModelArrayList.add(messageModel);
                        mesajlar.getAdapter().notifyItemInserted(mesajModelArrayList.size() - 1);
                    });
                })
                .addOnFailureListener(e -> {
                    Tools.showMessage("Failed to create message.");
                });
    }

    private void MesajlarıGetir(){
        String userId = firebaseAuth.getCurrentUser().getUid();
        firestore.collection("/userdata/" + userId + "/messages").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    mesajModelArrayList.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        MesajModel messageModel = new MesajModel(documentSnapshot.getString("name"), documentSnapshot.getString("description"), documentSnapshot.getId());
                        mesajModelArrayList.add(messageModel);
                    }
                    mesajlar.setAdapter(new MessageAdapter(mesajModelArrayList));
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                    mesajlar.setLayoutManager(linearLayoutManager);
                })
                .addOnFailureListener(e -> {
                    Tools.showMessage("An error occurred");
                });
    }
}