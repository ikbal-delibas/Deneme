package com.example.deneme.ui.Mesajgonder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deneme.MesajModel;
import com.example.deneme.OnClickItemListener;
import com.example.deneme.R;

import java.util.List;
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    List<MesajModel> messageModelList;
    OnClickItemListener onClickItemEventListener;

    public MessageAdapter(List<MesajModel> messageModelList, OnClickItemListener onClickItemEventListener) {
        this.messageModelList = messageModelList;
        this.onClickItemEventListener = onClickItemEventListener;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mesajgonder_mesajlar, parent, false), onClickItemEventListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MesajModel messageModel = messageModelList.get(position);
        holder.setData(messageModel);
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView, descriptionTextView;
        OnClickItemListener onClickItemEventListener;
        public MessageViewHolder( View itemView, OnClickItemListener onClickItemEventListener) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.item_mesajgonder_mesajadi);
            descriptionTextView = itemView.findViewById(R.id.item_mesajgonder_aciklama);
            this.onClickItemEventListener = onClickItemEventListener;

            itemView.setOnClickListener(this);
        }

        public void setData(MesajModel messageModel){
            nameTextView.setText(messageModel.getMesajAdi());
            descriptionTextView.setText(messageModel.getMesaj());
        }

        @Override
        public void onClick(View view) {
            onClickItemEventListener.OnclickItemEvent(getAdapterPosition());
        }
    }
}
