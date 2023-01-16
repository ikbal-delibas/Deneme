package com.example.deneme.ui.grupOlustur;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.deneme.GroupModel;
import com.example.deneme.R;
import com.example.deneme.Tools;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class GrupolusturFragment extends Fragment {

    EditText grupAdi,grupAciklama;
    ImageView grupResmi;
    Button grupOlusturbutton;
    RecyclerView gruplar;
    Uri filePath;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    ArrayList<GroupModel> groupModels;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grupolustur, container, false);

        grupAdi=view.findViewById(R.id.grupOlustur_grupAdi);
        grupAciklama=view.findViewById(R.id.grupOlustur_grupAciklama);
        grupResmi=view.findViewById(R.id.grupOlustur_grupResmi);
        grupOlusturbutton=view.findViewById(R.id.grupOlusturButton);
        gruplar=view.findViewById(R.id.grupOlustur_Gruplar);
        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();


        groupModels = new ArrayList<>();

        ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),result -> {
                    if(result.getResultCode()==getActivity().RESULT_OK){
                        Intent data =result.getData();
                        filePath=data.getData();
                        grupResmi.setImageURI(filePath);
                    }
                }
        );

        grupResmi.setOnClickListener(v->{
            Intent intent =new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(intent);
        });

        grupOlusturbutton.setOnClickListener(v->{
            String grupname=grupAdi.getText().toString();
            String grup_aciklama=grupAciklama.getText().toString();

            if (grupname.isEmpty()){
                Tools.showMessage("Group name cannot be empty.");
                return;
            }
            if (grup_aciklama.isEmpty()){
                Tools.showMessage("Group description cannot be empty.");
                return;

            }
            if (filePath!=null) {
                StorageReference storageReference = firebaseStorage.getReference().child("resimler/" + UUID.randomUUID().toString());
                storageReference.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        CreateGroup(grupname,grup_aciklama,downloadUrl);
                    });
                });
                return;
            }
            else{
                CreateGroup(grupname,grup_aciklama,null);
            }
                Tools.showMessage("Group created successfully.");

        });
            GruplariGetir();


        return view;
    }
    private void CreateGroup(String name,String aciklama,String imageUrl){
        String userId = firebaseAuth.getCurrentUser().getUid();

        firestore.collection("/userdata/" + userId + "/" + "groups").add(new HashMap<String, Object>(){{
            put("name", name);
            put("description", aciklama);
            put("image", imageUrl);
            put("numbers", new ArrayList<String>());
        }}).addOnSuccessListener(documentReference -> {
            Tools.showMessage("Group created successfully.");
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                GroupModel groupModel = new GroupModel( name, aciklama, imageUrl, (List<String>)documentSnapshot.get("numbers"), documentSnapshot.getId());
                groupModels.add(groupModel);
                gruplar.getAdapter().notifyItemInserted(groupModels.size() - 1);
            });
        }).addOnFailureListener(e -> {
            Tools.showMessage("Group created failed");
        });
    }
    private void GruplariGetir(){
        String userId = firebaseAuth.getCurrentUser().getUid();

        firestore.collection("/userdata/" + userId + "/" + "groups").get().addOnSuccessListener(queryDocumentSnapshots -> {
            groupModels.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                GroupModel groupModel = new GroupModel(documentSnapshot.getString("name"), documentSnapshot.getString("description"), documentSnapshot.getString("image"), (List<String>)documentSnapshot.get("numbers"),documentSnapshot.getId());
                groupModels.add(groupModel);

            }
            gruplar.setAdapter(new GroupAdapter(groupModels));
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
            gruplar.setLayoutManager(linearLayoutManager);


        });


    }
}