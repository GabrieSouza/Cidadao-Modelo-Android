package com.cidadaoandroid.entidades;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gabri on 02/04/2016.
 */
public class Estados implements Parcelable {
    public static final Creator<Estados> CREATOR = new Creator<Estados>() {
        @Override
        public Estados createFromParcel(Parcel in) {
            return new Estados(in);
        }

        @Override
        public Estados[] newArray(int size) {
            return new Estados[size];
        }
    };
    private String[] nomes = {"Acre", "Alagoas", "Amapá", "Amazonas", "Bahia", "Ceará", "Distrito Federal", "Espírito Santo", "Goiás", "Maranhão", "Mato Grosso",
            "Mato Grosso do Sul", "Minas Gerais", "Pará", "Paraíba", "Paraná", "Pernambuco", "Piauí", "Rio de Janeiro", "Rio Grande do Norte", "Rio Grande do Sul",
            "Rondônia", "Roraima", "Santa Catarina", "São Paulo", "Sergipe", "Tocantins"};
    private String[] siglas = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO",
            "RR", "SC", "SP", "SE", "TO"};

    public Estados() {
    }

    protected Estados(Parcel in) {
        nomes = in.createStringArray();
        siglas = in.createStringArray();
    }

    public String[] getNomes() {
        return nomes;
    }

    public void setNomes(String[] nomes) {
        this.nomes = nomes;
    }

    public String[] getSiglas() {
        return siglas;
    }

    public void setSiglas(String[] siglas) {
        this.siglas = siglas;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(nomes);
        dest.writeStringArray(siglas);
    }
}
