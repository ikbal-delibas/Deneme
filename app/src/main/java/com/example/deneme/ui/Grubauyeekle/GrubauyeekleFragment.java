package com.example.deneme.ui.Grubauyeekle;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deneme.ContactModel;
import com.example.deneme.GroupModel;
import com.example.deneme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GrubauyeekleFragment extends Fragment {

    FirebaseAuth firebase;
    FirebaseFirestore firestore;

    RecyclerView gruplarView, rehberView;
    TextView secilengrupAdi;

    GroupModel groupModel;

    ArrayList<GroupModel> groupModelList;
    ArrayList<ContactModel> contactModelList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_grubauyeekle, container, false);

        firestore=FirebaseFirestore.getInstance();
        firebase=FirebaseAuth.getInstance();

        gruplarView=view.findViewById(R.id.grubaekle_gruplar);
        rehberView=view.findViewById(R.id.grubaekle_rehber);
        secilengrupAdi=view.findViewById(R.id.grubaekle_seciliGrup);
        contactModelList = new ArrayList<>();
        groupModelList=new ArrayList<>();

        ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGrant -> {
                    if (isGrant) {
                        RehberBaglantisi();
                    }else{
                        Toast.makeText(getContext(), "Uygulamanın düzgün çalışması için rehber okuma izni gerekli", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            launcher.launch(Manifest.permission.READ_CONTACTS);
        }else{
            RehberBaglantisi();
        }



        GruplarıGetir();


        return view;
    }

    private void RehberBaglantisi() {
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        contactModelList.clear();
        while (cursor.moveToNext()){
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            ContactModel contactModel = new ContactModel(name, number, image);
            contactModelList.add(contactModel);
        }

        rehberView.setAdapter(new ContactAdapter(contactModelList, position -> {
            ContactModel contactModel = contactModelList.get(position);

            if(groupModel != null){
                new AlertDialog.Builder(getContext())
                        .setTitle("Kişi Ekle")
                        .setMessage(contactModel.getName() + " kişisini " + groupModel.getName() + " grubuna eklemek istiyor musunuz?")
                        .setPositiveButton("Evet", (dialog, which) -> {
                            firestore.collection("/userdata/" + firebase.getCurrentUser().getUid() + "/groups").document(groupModel.getUid()).update( new HashMap<String, Object>(){{
                                put("numbers", FieldValue.arrayUnion(contactModel.getNumber()));
                            }}).addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Kişi eklendi", Toast.LENGTH_SHORT).show();
                            });
                        })
                        .setNegativeButton("Hayır", null)
                        .show();
            }
        }));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rehberView.setLayoutManager(linearLayoutManager);

    }


    private void GruplarıGetir(){
        String uid = firebase.getCurrentUser().getUid();

        firestore.collection("/userdata/" + uid + "/groups").get().addOnSuccessListener(queryDocumentSnapshots -> {
            groupModelList.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                GroupModel groupModel = new GroupModel(documentSnapshot.getString("name"), documentSnapshot.getString("description"), documentSnapshot.getString("image"), (List<String>)documentSnapshot.get("numbers"),documentSnapshot.getId());
                groupModelList.add(groupModel);
            }

            gruplarView.setAdapter(new GroupAdapter(groupModelList, position -> {
                groupModel = groupModelList.get(position);
                secilengrupAdi.setText("Seçili grup: " + groupModel.getName());
            }));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            gruplarView.setLayoutManager(linearLayoutManager);
        });
    }
}