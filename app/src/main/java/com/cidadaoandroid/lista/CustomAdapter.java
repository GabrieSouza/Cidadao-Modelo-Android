package com.cidadaoandroid.lista;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cidadaoandroid.R;
import com.cidadaoandroid.entidades.Convenios;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;


/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<Convenios> convenios;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    public CustomAdapter (Context context, List<Convenios> igr){
        convenios = igr;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.text_row_item,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.iv.setText(String.valueOf(convenios.get(position).getId()));
        holder.textView.setText(convenios.get(position).getModalidade());
        holder.sub.setText(convenios.get(position).getJustificativa_resumida());

    }

    @Override
    public int getItemCount() {
        return convenios.size();
    }

    public void addListItem(Convenios igreja,int position){
        convenios.add(igreja);
        notifyItemInserted(position);
    }

    public void chamaIgreja(Context context, int position){
        Toast.makeText(context,"Position:"+position, Toast.LENGTH_LONG).show();
        System.out.println("Clicou");
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        this.mRecyclerViewOnClickListenerHack = r;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textView;
        private TextView sub;
        private TextView iv;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.textView);
            sub = (TextView) v.findViewById(R.id.subtittle);
            iv = (TextView) v.findViewById(R.id.iv1);

        }

        @Override
        public void onClick(View v) {

            if(mRecyclerViewOnClickListenerHack != null){
                try {
                    YoYo.with(Techniques.StandUp)
                            .duration(500)
                            .playOn(v.findViewById(R.id.rl_item));
                }catch (Exception e){
                    Log.e("Error:","Animacao");
                }
                mRecyclerViewOnClickListenerHack.onClickListener(v,getAdapterPosition());
            }

        }
    }


}
