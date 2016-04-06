package com.cidadaoandroid.entidades;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by gabri on 02/04/2016.
 */
public class Estados implements Parcelable {
    private List<String> nome;
    private List<String> sigla;

    public Estados(List<String> nome, List<String> sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public Estados(Parcel in) {
        nome = in.createStringArrayList();
        sigla = in.createStringArrayList();
    }
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

    public List<String> getNome() {
        nome.add("Acre");
        nome.add("Alagoas");
        nome.add("Amapá");
        nome.add("Amazonas");
        nome.add("Bahia");
        nome.add("Ceará");
        nome.add("Distrito Federal");
        nome.add("Espírito Santo");
        nome.add("Goiás");
        nome.add("Maranhão");
        nome.add("Mato Grosso");
        nome.add("Minas Gerais");
        nome.add("Pará");
        nome.add("Paraíba");
        nome.add("Paraná");
        nome.add("Pernambuco");
        nome.add("Piauí");
        nome.add("Rio de Janeiro");
        nome.add("Rio Grande do Norte");
        nome.add("Rio Grande do Sul");
        nome.add("Rondônia");
        nome.add("Roraima");
        nome.add("Santa Catarina");
        nome.add("São Paulo");
        nome.add("Sergipe");
        nome.add("Tocantins");
        return nome;
    }

    public void setNome(List<String> nome) {
        this.nome = nome;
    }

    public List<String> getSigla() {
        sigla.add("AC");
        sigla.add("AL");
        sigla.add("AP");
        sigla.add("AM");
        sigla.add("BA");
        sigla.add("CE");
        sigla.add("DF");
        sigla.add("ES");
        sigla.add("GO");
        sigla.add("MA");
        sigla.add("MT");
        sigla.add("MS");
        sigla.add("MG");
        sigla.add("PA");
        sigla.add("PB");
        sigla.add("PR");
        sigla.add("PE");
        sigla.add("PI");
        sigla.add("RJ");
        sigla.add("RN");
        sigla.add("RS");
        sigla.add("RO");
        sigla.add("RR");
        sigla.add("SC");
        sigla.add("SP");
        sigla.add("SE");
        sigla.add("TO");
        return sigla;
    }

    public void setSigla(List<String> sigla) {
        this.sigla = sigla;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(nome);
        dest.writeStringList(sigla);
    }
}
