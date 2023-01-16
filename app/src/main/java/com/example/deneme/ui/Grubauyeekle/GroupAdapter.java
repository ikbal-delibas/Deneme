package com.example.deneme.ui.Grubauyeekle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deneme.GroupModel;
import com.example.deneme.OnClickItemListener;
import com.example.deneme.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    List<GroupModel> groupModelList;
    OnClickItemListener onClickItemEventListener;
    public GroupAdapter(List<GroupModel> groupModelList, OnClickItemListener onClickItemEventListener) {
        this.groupModelList = groupModelList;
        this.onClickItemEventListener = onClickItemEventListener;
    }


    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupViewHolder groupViewHolder = new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grubaekle_gruplar, parent, false), onClickItemEventListener);
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GroupModel groupModel = groupModelList.get(position);
        holder.setData(groupModel);
    }

    @Override
    public int getItemCount() {
        return groupModelList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView groupImageView;
        TextView groupNameTextView, groupDescriptionTextView;

        OnClickItemListener onClickItemEventListener;
        public GroupViewHolder( View itemView, OnClickItemListener onClickItemEventListener) {
            super(itemView);

            groupImageView = itemView.findViewById(R.id.item_grubaekle_grupresmi);
            groupNameTextView = itemView.findViewById(R.id.item_grubaekle_grupadi);
            groupDescriptionTextView = itemView.findViewById(R.id.item_grubaekle_grupaciklama);

            this.onClickItemEventListener = onClickItemEventListener;
            itemView.setOnClickListener(this);
        }

        public void setData(GroupModel groupModel) {
            groupNameTextView.setText(groupModel.getName());
            groupDescriptionTextView.setText(groupModel.getDescription());

            if(groupModel.getImage() != null){
                Picasso.get().load(groupModel.getImage()).into(groupImageView);
            }
        }

        @Override
        public void onClick(View view) {
            onClickItemEventListener.OnclickItemEvent(getAdapterPosition());
        }
    }
}