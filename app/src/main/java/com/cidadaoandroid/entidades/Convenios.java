package com.cidadaoandroid.entidades;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gabri on 22/03/2016.
 */
public class Convenios implements Parcelable{
    private int id;
    private String modalidade;
    private String orgao_concedente;
    private  String justificativa_resumida;
    private  String objeto_resumido;
    private  String data_inicio_vigencia;
    private  String data_fim_vigencia;
    private  String valor_global;
    private  String valor_repasse;
    private  String valor_contra_partida;
    private  String data_assinatura;
    private  String data_publicacao;
    private  String situacao;
    private  String proponente;

    public Convenios(){

    }
    public Convenios(Parcel in) {
        id = in.readInt();
        modalidade = in.readString();
        orgao_concedente = in.readString();
        justificativa_resumida = in.readString();
        objeto_resumido = in.readString();
        data_inicio_vigencia = in.readString();
        data_fim_vigencia = in.readString();
        valor_global = in.readString();
        valor_repasse = in.readString();
        valor_contra_partida = in.readString();
        data_assinatura = in.readString();
        data_publicacao = in.readString();
        situacao = in.readString();
        proponente = in.readString();
    }

    public static final Creator<Convenios> CREATOR = new Creator<Convenios>() {
        @Override
        public Convenios createFromParcel(Parcel in) {
            return new Convenios(in);
        }

        @Override
        public Convenios[] newArray(int size) {
            return new Convenios[size];
        }
    };



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getOrgao_concedente() {
        return orgao_concedente;
    }

    public void setOrgao_concedente(String orgao_concedente) {
        this.orgao_concedente = orgao_concedente;
    }

    public String getJustificativa_resumida() {
        return justificativa_resumida;
    }

    public void setJustificativa_resumida(String justificativa_resumida) {
        this.justificativa_resumida = justificativa_resumida;
    }

    public String getObjeto_resumido() {
        return objeto_resumido;
    }

    public void setObjeto_resumido(String objeto_resumido) {
        this.objeto_resumido = objeto_resumido;
    }

    public String getData_inicio_vigencia() {
        return data_inicio_vigencia;
    }

    public void setData_inicio_vigencia(String data_inicio_vigencia) {
        this.data_inicio_vigencia = data_inicio_vigencia;
    }

    public String getData_fim_vigencia() {
        return data_fim_vigencia;
    }

    public void setData_fim_vigencia(String data_fim_vigencia) {
        this.data_fim_vigencia = data_fim_vigencia;
    }

    public String getValor_global() {
        return valor_global;
    }

    public void setValor_global(String valor_global) {
        this.valor_global = valor_global;
    }

    public String getValor_repasse() {
        return valor_repasse;
    }

    public void setValor_repasse(String valor_repasse) {
        this.valor_repasse = valor_repasse;
    }

    public String getValor_contra_partida() {
        return valor_contra_partida;
    }

    public void setValor_contra_partida(String valor_contra_partida) {
        this.valor_contra_partida = valor_contra_partida;
    }

    public String getData_assinatura() {
        return data_assinatura;
    }

    public void setData_assinatura(String data_assinatura) {
        this.data_assinatura = data_assinatura;
    }

    public String getData_publicacao() {
        return data_publicacao;
    }

    public void setData_publicacao(String data_publicacao) {
        this.data_publicacao = data_publicacao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getProponente() {
        return proponente;
    }

    public void setProponente(String proponente) {
        this.proponente = proponente;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(modalidade);
        dest.writeString(orgao_concedente);
        dest.writeString(justificativa_resumida);
        dest.writeString(objeto_resumido);
        dest.writeString(data_inicio_vigencia);
        dest.writeString(data_fim_vigencia);
        dest.writeString(valor_global);
        dest.writeString(valor_repasse);
        dest.writeString(valor_contra_partida);
        dest.writeString(data_assinatura);
        dest.writeString(data_publicacao);
        dest.writeString(situacao);
        dest.writeString(proponente);
    }
}
