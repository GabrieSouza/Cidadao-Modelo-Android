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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        holder.municipio.setText(String.valueOf(convenios.get(position).getProponente()));
        holder.convenio.setText(convenios.get(position).getModalidade());
        if (convenios.get(position).getObjeto_resumido().length() < 40) {
            holder.descricao.setText(convenios.get(position).getObjeto_resumido());
        } else {
            holder.descricao.setText(convenios.get(position).getObjeto_resumido().substring(0, 40) + "...");
        }
        //SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(convenios.get(position).getData_inicio_vigencia());
            holder.ini_data.setText("Inicio - " + new SimpleDateFormat("dd/MM/yyyy").format(date));
            date = new SimpleDateFormat("yyyy-MM-dd").parse(convenios.get(position).getData_fim_vigencia());
            holder.fim_data.setText("Fim - " + new SimpleDateFormat("dd/MM/yyyy").format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }

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
        private TextView municipio;
        private TextView convenio;
        private TextView descricao;
        private TextView ini_data;
        private TextView fim_data;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            municipio = (TextView) v.findViewById(R.id.text_municipio);
            convenio = (TextView) v.findViewById(R.id.type_convenio);
            descricao = (TextView) v.findViewById(R.id.title_convenio);
            ini_data = (TextView) v.findViewById(R.id.data_inicio);
            fim_data = (TextView) v.findViewById(R.id.data_fim);

        }

        @Override
        public void onClick(View v) {

            if(mRecyclerViewOnClickListenerHack != null){
                try {
                    YoYo.with(Techniques.FadeOutRight)
                            .duration(500)
                            .playOn(v.findViewById(R.id.cv));
                }catch (Exception e){
                    Log.e("Error:","Animacao");
                }
                mRecyclerViewOnClickListenerHack.onClickListener(v,getAdapterPosition());
            }

        }
    }


}
