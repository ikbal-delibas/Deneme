package com.example.deneme.ui.grupOlustur;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deneme.GroupModel;
import com.example.deneme.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder>{

    List<GroupModel> groupModelList;
    public GroupAdapter(List<GroupModel> groupModelList){
        this.groupModelList=groupModelList;
    }

    @NonNull
    @Override
    public GroupAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupViewHolder groupViewHolder=new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_creategroup_group,parent,false));

        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupViewHolder holder, int position) {
        GroupModel groupModel=groupModelList.get(position);
        holder.setData(groupModel);
    }

    @Override
    public int getItemCount() {
        return groupModelList.size();
    }
    public class GroupViewHolder extends RecyclerView.ViewHolder {
        ImageView groupImage;
        TextView grupadi,grupAciklama;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            grupadi=itemView.findViewById(R.id.item_creategroup_name);
            grupAciklama=itemView.findViewById(R.id.item_creategroup_aciklama);
            groupImage=itemView.findViewById(R.id.item_creategroup_image);
        }

        public void setData(GroupModel groupModel) {
            grupadi.setText(groupModel.getName());
            grupAciklama.setText(groupModel.getDescription());
            if(groupModel.getImage()!=null){
                Picasso.get().load(groupModel.getImage()).into(groupImage);
            }
        }
    }
}
