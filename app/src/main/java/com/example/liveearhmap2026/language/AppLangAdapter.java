package com.example.liveearhmap2026.language;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;


import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R;

import java.util.List;

public class AppLangAdapter extends RecyclerView.Adapter<AppLangAdapter.MyHolder> {
    Context context;
    List<Lang_Model2> list;
    LayoutInflater inflater;
   public boolean isReady=false;
   int colorWhite, colorBlack;
    langActivity actvity;
    public AppLangAdapter(Context context, List<Lang_Model2> list){
        this.context=context;
        this.list=list;

        inflater=LayoutInflater.from(context);
        selectedLang=list.get(0);
        actvity= (langActivity) context;
        colorBlack =context.getResources().getColor(R.color.black);
        colorWhite=context.getResources().getColor(R.color.white);
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(inflater.inflate(R.layout.app_lang_item, parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Lang_Model2 model2=list.get(position);


        holder.tv_name.setText(model2.langName);
        if(model2.checked){
            holder.checkBox.setVisibility(View.VISIBLE);
        }else{
            holder.checkBox.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    public  class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_name;
        AppCompatImageView checkBox;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            checkBox=itemView.findViewById(R.id.checkbox);
           checkBox.setOnClickListener(this);
           itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int p=getLayoutPosition();
            selectedLang= list.get(p);
            selectedLang.checked=true;
            checkBox.setVisibility(View.INVISIBLE);
            uncheckAll(selectedLang);
            notifyDataSetChanged();
        }
    }

    private Lang_Model2 selectedLang;

    public Lang_Model2 getSelectedLang() {
        return selectedLang;
    }

    void uncheckAll(Lang_Model2 langModel2){

        for (Lang_Model2 model2:list){

           if(model2!=langModel2){
               model2.checked=false;
           }
        }
    }

}
