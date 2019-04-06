package com.senaigo.retrofit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.senaigo.retrofit.R;
import com.senaigo.retrofit.model.User;

import java.util.List;

public class UserAdapter extends BaseAdapter {


    Context context;
    List<User> colecao;
    LayoutInflater inflter;

    public UserAdapter(final Context applicationContext,
                       final List<User> colecao) {
        this.context = applicationContext;
        this.colecao = colecao;

    }

    @Override
    public int getCount() {
        return this.colecao != null ? this.colecao.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return this.colecao.get(i);
    }

    private User parsetItem(int i){
        return this.colecao.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // inflate the layout for each list row
        //'Infla' o layout(pega a referencia) para ser trabalhada
        //no método
        if (view == null) {
            view = LayoutInflater.from(context).
                    inflate(R.layout.user,
                            viewGroup, false);
        }

        // pega o objeto corrente da lista
        User user = parsetItem(i);

        //Neste ponto vc ira popular os dados do seu layout,
        //utilizando JAVA.

        TextView campoid, campoTexto1, campoTexto2;

        //CASO não queria declarar um campo
        //((TextView)view.findViewById(R.id.txtItemNome)).setText(pessoa.getNome());

        campoid = view.findViewById(R.id.txtId);
        campoTexto1 = view.findViewById(R.id.txt1);
        campoTexto2 = view.findViewById(R.id.txt2);

        campoid.setText(user.getId() + "");
        campoTexto1.setText(user.getName());
        campoTexto2.setText(user.getUsername());

        return view;
    }
}