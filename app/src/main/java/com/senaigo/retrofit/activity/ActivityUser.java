package com.senaigo.retrofit.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.senaigo.retrofit.R;
import com.senaigo.retrofit.bootstrap.APIClient;
import com.senaigo.retrofit.interfaces.UserInterface;
import com.senaigo.retrofit.model.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityUser extends AppCompatActivity {

    UserInterface apiUserInterface;

    EditText txtUserId;
    EditText txtTitle;
    EditText txtBody;
    TextView textView;
    TextView txtId2;
    ImageButton imgBtnEdit;
    ListView listViewUser;
    List<User> listUser;
    List<Map<String, Object>> colecao = new LinkedList<>();
    Map<String, Object> mapSelecionado;
    private Integer poss;
    Button deletar;
    Button editar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        listViewUser = findViewById(R.id.listViewUser);

        deletar = findViewById(R.id.btn2);
        editar = findViewById(R.id.btn1);

        txtUserId = findViewById(R.id.txtUserId);
        txtTitle = findViewById(R.id.txtTitle);
        txtBody = findViewById(R.id.txtBody);
        textView = findViewById(R.id.textView);

        apiUserInterface = APIClient.getClient().create(UserInterface.class);

        Call<List<User>> get = apiUserInterface.get();
        get.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                listUser = response.body();

                for (User u : listUser) {
                    //Criar dados para adapter
                    colecao.add(u.getMap());
                }

                setarAdapter();
                listViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mapSelecionado = colecao.get(position);
                        String textoBtn = getString(R.string.btn_deletar) + " " + mapSelecionado.get("id");
                        String textoBtnEditar = getString(R.string.btn_editar) + " " + mapSelecionado.get("id");
                        deletar.setText(textoBtn);
                        editar.setText(textoBtnEditar);
                        setPoss(new Integer(position));
                        deletar.setEnabled(Boolean.TRUE);
                        editar.setEnabled(Boolean.TRUE);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setarAdapter() {
        String[] from = {"id", "title"};
        int[] to = {R.id.txtId2, R.id.txtTitle};

        SimpleAdapter simpleAdapter =
                new SimpleAdapter(
                        getApplicationContext(),
                        colecao,
                        R.layout.user,
                        from,
                        to);

        listViewUser.setAdapter(simpleAdapter);
    }

    @SuppressLint("SetTextI18n")
    public void editar(View view) {
        textView.setText(mapSelecionado.get("id") + "");
        txtUserId.setText(mapSelecionado.get("userId") + "");
        txtTitle.setText(mapSelecionado.get("title") + "");
        txtBody.setText(mapSelecionado.get("body") + "");
        editar.setEnabled(Boolean.FALSE);
        deletar.setEnabled(Boolean.FALSE);
    }

    public void deletar(View view) {
        Call<Void> delete = apiUserInterface.delete((Integer) mapSelecionado.get("id"));
        delete.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                colecao.remove((int)getPoss());
                limparCampos();
                setarAdapter();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                alertDialog.setTitle("Erro!");
                alertDialog.setMessage(t.getMessage());
                alertDialog.show();
            }
        });
    }

    public void adicionar(View view) {
        try {
            User user = new User();
            user.setId(verificarObjeto(textView.getText()) ? Integer.valueOf(textView.getText().toString()) : null);
            user.setUserId(tratarEditTextInteger(txtUserId));
            user.setBody(tratarEditTextString(txtBody));
            user.setTitle(tratarEditTextString(txtTitle));
            if (user.getId() == null) {
                Call<User> post = apiUserInterface.post(user);
                post.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User user = response.body();
                        colecao.add(user.getMap());
                        limparCampos();
                        setarAdapter();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                        alertDialog.setTitle("Erro!");
                        alertDialog.setMessage(t.getMessage());
                        alertDialog.show();
                    }
                });
            } else {
                Call<User> put = apiUserInterface.put(user, user.getId());
                put.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User user = response.body();
                        colecao.set(getPoss(), user.getMap());
                        limparCampos();
                        setarAdapter();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                        alertDialog.setTitle("Erro!");
                        alertDialog.setMessage(t.getMessage());
                        alertDialog.show();
                    }
                });
            }
        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Erro!");
            alertDialog.setMessage(e.getMessage());
            alertDialog.show();
        }
    }

    private String tratarEditTextString(EditText editText) throws Exception {
        if (verificarObjeto(editText.getText())) {
            return editText.getText().toString();
        }
        throw new Exception(String.format("Campo \"%s\" Não Pode ser Vazio", editText.getHint()));
    }

    private Integer tratarEditTextInteger(EditText editText) throws Exception {
        if (verificarObjeto(editText.getText())) {
            return Integer.valueOf(editText.getText().toString());
        }
        throw new Exception(String.format("Campo \"%s\" Não Pode ser Vazio", editText.getHint()));
    }

    private Boolean verificarObjeto(Object objeto) {
        return objeto != null && !objeto.toString().trim().equals("");
    }

    private void limparCampos() {
        textView.setText("");
        txtUserId.setText("");
        txtTitle.setText("");
        txtBody.setText("");
        editar.setEnabled(Boolean.FALSE);
        deletar.setEnabled(Boolean.FALSE);
        poss = 0;
        mapSelecionado = null;
    }

    public Integer getPoss() {
        return poss;
    }

    public void setPoss(Integer poss) {
        this.poss = poss;
    }
}
