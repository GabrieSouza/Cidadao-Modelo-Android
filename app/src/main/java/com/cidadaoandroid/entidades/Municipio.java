package com.cidadaoandroid.entidades;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by gabri on 05/04/2016.
 */
public class Municipio implements Parcelable{

    public static final Creator<Municipio> CREATOR = new Creator<Municipio>() {
        @Override
        public Municipio createFromParcel(Parcel in) {
            return new Municipio(in);
        }

        @Override
        public Municipio[] newArray(int size) {
            return new Municipio[size];
        }
    };
    private int id;
    private String nome;
    private List<String> id_proponentes;

    public Municipio() {
    }

    protected Municipio(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        id_proponentes = in.createStringArrayList();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getId_proponentes() {
        return id_proponentes;
    }

    public void setId_proponentes(List<String> id_proponentes) {
        this.id_proponentes = id_proponentes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nome);
        dest.writeStringList(id_proponentes);
    }
}
