package com.example.deneme.ui.Mesajolustur;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deneme.MesajModel;
import com.example.deneme.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    List<MesajModel> messageModelList;

    public MessageAdapter(List<MesajModel> messageModelList) {
        this.messageModelList = messageModelList;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mesajolustur_mesaj, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        MesajModel messageModel = messageModelList.get(position);
        holder.setData(messageModel);
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView mesajAdi, mesaj;
        public MessageViewHolder( View itemView) {
            super(itemView);

            mesajAdi = itemView.findViewById(R.id.item_mesajolustur_mesaj_mesajAdi);
            mesaj = itemView.findViewById(R.id.item_mesajolustur_mesaj_mesaj);
        }

        public void setData(MesajModel groupModel) {
            mesajAdi.setText(groupModel.getMesajAdi());
            mesaj.setText(groupModel.getMesaj());
        }
    }
}