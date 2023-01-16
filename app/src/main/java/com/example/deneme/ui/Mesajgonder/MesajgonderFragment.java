package com.example.deneme.ui.Mesajgonder;

import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deneme.GroupModel;
import com.example.deneme.MesajModel;
import com.example.deneme.OnClickItemListener;
import com.example.deneme.R;
import com.example.deneme.ui.Grubauyeekle.GroupAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class MesajgonderFragment extends Fragment {
    RecyclerView gruplarView, mesajlarView;
    TextView secilenGrup, secilenMesaj;
    Button gonderButon;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    ArrayList<GroupModel> groupModelList;
    ArrayList<MesajModel> messageModelList;

    GroupModel secilenGrupModel;
    MesajModel secilenMesajModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mesajgonder, container, false);

        gruplarView = view.findViewById(R.id.mesajgonder_gruplar);
        mesajlarView = view.findViewById(R.id.mesajgonder_mesajlar);

        secilenGrup = view.findViewById(R.id.mesajgonder_seciliGrup);
        secilenMesaj = view.findViewById(R.id.mesajgonder_secilenmesaj);

        gonderButon = view.findViewById(R.id.mesajgonder_mesajbutton);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        groupModelList = new ArrayList<>();
        messageModelList = new ArrayList<>();

        ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGrant -> {
            if (isGrant) {
                SendSms();
            }else{
                Toast.makeText(getContext(), "Toplu sms göndermek için izin gereklidir", Toast.LENGTH_SHORT).show();
            }
        });

        gonderButon.setOnClickListener(v -> {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(android.Manifest.permission.SEND_SMS) != android.content.pm.PackageManager.PERMISSION_GRANTED){
                launcher.launch(android.Manifest.permission.SEND_SMS);
            }else{
                SendSms();
            }
        });
        GruplariGetir();
        MesajlariGetir();
        return view;

    }
    private void GruplariGetir(){
        String uid = firebaseAuth.getCurrentUser().getUid();

        firestore.collection("/userdata/" + uid + "/groups").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                groupModelList.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    GroupModel groupModel = new GroupModel(document.getString("name"), document.getString("description"), document.getString("image"), (List<String>)document.get("numbers"),document.getId());
                    groupModelList.add(groupModel);
                }

                gruplarView.setAdapter(new GroupAdapter(groupModelList, position -> {
                    secilenGrupModel = groupModelList.get(position);
                    secilenGrup.setText("Seçili Grup: " + secilenGrupModel.getName());
                }));

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                gruplarView.setLayoutManager(linearLayoutManager);
            }
        });
    }
    private void MesajlariGetir(){
        String uid = firebaseAuth.getCurrentUser().getUid();

        firestore.collection("/userdata/" + uid + "/messages").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                messageModelList.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    MesajModel messageModel = new MesajModel(document.getString("name"), document.getString("description"), document.getId());
                    messageModelList.add(messageModel);
                }

                mesajlarView.setAdapter(new MessageAdapter(messageModelList, position -> {
                    secilenMesajModel = messageModelList.get(position);
                    secilenMesaj.setText("Seçili Mesaj: " + secilenMesajModel.getMesajAdi());
                }));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                mesajlarView.setLayoutManager(linearLayoutManager);
            }
        });
    }
    private void SendSms(){
        if(secilenGrupModel == null || secilenMesajModel == null){
            Toast.makeText(getContext(), "Lütfen bir grup ve mesaj seçin", Toast.LENGTH_SHORT).show();
            return;
        }

        if(secilenGrupModel.getNumbers() != null && secilenGrupModel.getNumbers().size() > 0){
            SmsManager smsManager = SmsManager.getDefault();
            for (String number : secilenGrupModel.getNumbers()) {
                smsManager.sendTextMessage(number, null, secilenMesajModel.getMesaj(), null, null);
            }

            Toast.makeText(getContext(), "Mesajlar gönderildi", Toast.LENGTH_SHORT).show();
        }
    }


}