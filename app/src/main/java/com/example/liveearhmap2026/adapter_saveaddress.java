package com.example.liveearhmap2026;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.liveearhmap2026.modules.SaveAddressActivity;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R;

import java.util.List;

public class adapter_saveaddress extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<String> list_title, list_detail,list_latitude,list_longitude,list_ids;
    Context contex;
    public adapter_saveaddress(SaveAddressActivity saveaddress_module, List<String> list_title, List<String> list_detail, List<String> list_latitude, List<String> list_longitude, List<String> list_ids) {
        contex = saveaddress_module;
        this.list_title = list_title;
        this.list_detail = list_detail;
        this.list_latitude = list_latitude;
        this.list_longitude = list_longitude;
        this.list_ids = list_ids;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlelayout_all_savedaddress_recycler, parent, false);
        viewHolder = new ViewHolderClass(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {
        try {
            ViewHolderClass vaultItemHolder = (ViewHolderClass) holder;
            vaultItemHolder.title.setText(list_title.get(position));
            vaultItemHolder.detail.setText(list_detail.get(position));

            vaultItemHolder.layout_alladdress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (vaultItemHolder.button__delete.getVisibility() == View.VISIBLE) {
                            vaultItemHolder.layout_alladdress.setBackgroundResource(R.drawable.round10_shape);
                            vaultItemHolder.button__delete.setVisibility(View.GONE);
                        } else {
                            vaultItemHolder.layout_alladdress.setBackgroundResource(R.drawable.round10_shape_selected);
                            vaultItemHolder.button__delete.setVisibility(View.VISIBLE);
                        }
                        SaveAddressActivity.itemclick_check = true;
                        map_buttons.LATITUDE_SAVEADDRESS=list_latitude.get(position);
                        map_buttons.LONGITUDE_SAVEADDRESS=list_longitude.get(position);
                        map_buttons.ADDRESSLINE_SAVEADDRESS=list_detail.get(position);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            vaultItemHolder.button__delete.setOnClickListener(view -> {

                try {
                    SQLite_Class.delete_item((Activity) contex, list_ids.get(position));
                    list_title.remove(position);
                    list_detail.remove(position);
                    list_latitude.remove(position);
                    list_longitude.remove(position);
                    list_ids.remove(position);
                    notifyDataSetChanged();
//                    f(position,list_title.size());
                    SaveAddressActivity.toast_error_itemdelete((Activity) contex,"Address deleted");

                } catch (Exception ignore) {
                }
            });

        } catch (Exception ignore) {
        }
    }

    @Override
    public int getItemCount() {
        return list_title.size();
    }

    public static class ViewHolderClass extends RecyclerView.ViewHolder {
        ConstraintLayout layout_alladdress;
        TextView title, detail;
        AppCompatImageButton button__delete;

        public ViewHolderClass(View itemView) {
            super(itemView);
            layout_alladdress = itemView.findViewById(R.id.single_savedaddress_layout);
            title = itemView.findViewById(R.id.textview_savedaddress_title);
            detail = itemView.findViewById(R.id.textview_savedaddress_detail);
            button__delete = itemView.findViewById(R.id.button_savedaddress_delete);
        }
    }
}




