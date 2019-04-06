package com.senaigo.retrofit.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.senaigo.retrofit.R;
import com.senaigo.retrofit.adapter.UserAdapter;
import com.senaigo.retrofit.bootstrap.APIClient;
import com.senaigo.retrofit.interfaces.UserInterface;
import com.senaigo.retrofit.model.Address;
import com.senaigo.retrofit.model.Company;
import com.senaigo.retrofit.model.Geo;
import com.senaigo.retrofit.model.User;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityUser extends AppCompatActivity {

    UserInterface apiUserInterface;

    EditText txtName;
    EditText txtUsername;
    EditText txtEmail;
    EditText txtPhone;
    EditText txtWebsite;
    EditText txtStreet;
    EditText txtSuite;
    EditText txtCity;
    EditText txtZipcode;
    EditText txtLat;
    EditText txtLng;
    EditText txtNameCompany;
    EditText txtCatchPhrase;
    EditText txtBs;
    TextView textView;
    ListView listViewUser;
    List<User> listUser = new LinkedList<>();
    User objSelecionado;
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

        capturarEditTexts();

        apiUserInterface = APIClient.getClient().create(UserInterface.class);

        Call<List<User>> get = apiUserInterface.get();
        get.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                listUser = response.body();

                setarAdapter();
                listViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        objSelecionado = listUser.get(position);
                        String textoBtn = getString(R.string.btn_deletar) + " " + objSelecionado.getId();
                        String textoBtnEditar = getString(R.string.btn_editar) + " " + objSelecionado.getId();
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
        UserAdapter pessoaAdapter = new UserAdapter(this, listUser);
        listViewUser.setAdapter(pessoaAdapter);
    }

    @SuppressLint("SetTextI18n")
    public void editar(View view) {
        setarCampos(objSelecionado);
        textView.setText(objSelecionado.getId() + "");
        editar.setEnabled(Boolean.FALSE);
        deletar.setEnabled(Boolean.FALSE);
    }

    public void deletar(View view) {
        Call<Void> delete = apiUserInterface.delete(objSelecionado.getId());
        delete.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                listUser.remove((int)getPoss());
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
            User user = getValorCampos();
            user.setId(verificarObjeto(textView.getText()) ? Integer.valueOf(textView.getText().toString()) : null);
            if (user.getId() == null) {
                Call<User> post = apiUserInterface.post(user);
                post.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User user = response.body();
                        listUser.add(user);
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
                        listUser.set(getPoss(), user);
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

    private Double tratarEditTextDouble(EditText editText) throws Exception {
        if (verificarObjeto(editText.getText())) {
            return Double.valueOf(editText.getText().toString());
        }
        throw new Exception(String.format("Campo \"%s\" Não Pode ser Vazio", editText.getHint()));
    }

    private Boolean verificarObjeto(Object objeto) {
        return objeto != null && !objeto.toString().trim().equals("");
    }

    private void limparCampos() {
        setarCamposVazio();
        editar.setEnabled(Boolean.FALSE);
        deletar.setEnabled(Boolean.FALSE);
        poss = 0;
        objSelecionado = null;
    }

    public Integer getPoss() {
        return poss;
    }

    public void setPoss(Integer poss) {
        this.poss = poss;
    }

    private void capturarEditTexts() {
        textView = findViewById(R.id.txtViewId);
        txtName = findViewById(R.id.txtName);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtWebsite = findViewById(R.id.txtWebsite);
        txtStreet = findViewById(R.id.txtStreet);
        txtSuite = findViewById(R.id.txtSuite);
        txtCity = findViewById(R.id.txtCity);
        txtZipcode = findViewById(R.id.txtZipcode);
        txtLat = findViewById(R.id.txtLat);
        txtLng = findViewById(R.id.txtLng);
        txtNameCompany = findViewById(R.id.txtNameCompany);
        txtCatchPhrase = findViewById(R.id.txtCatchPhrase);
        txtBs = findViewById(R.id.txtBs);
    }

    private void setarCamposVazio(){
        setarCampos("", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

    private void setarCampos(User user){
        setarCampos(user.getId() + "" , user.getName(), user.getUsername(), user.getEmail(), user.getPhone(),
                user.getWebsite(), user.getAddress().getStreet(), user.getAddress().getSuite(), user.getAddress().getCity(),
                user.getAddress().getZipcode(), user.getAddress().getGeo().getLat() + "", user.getAddress().getGeo().getLng() + "",
                user.getCompany().getName(), user.getCompany().getCatchPhrase(), user.getCompany().getBs());
    }

    private void setarCampos(String id, String name, String username, String email,
                             String phone, String website, String street, String suite, String city, String zipcode,
                             String lat, String lng, String companyName, String catchPhrase, String bs) {
        textView.setText(id);
        txtName.setText(name);
        txtUsername.setText(username);
        txtEmail.setText(email);
        txtPhone.setText(phone);
        txtWebsite.setText(website);
        txtStreet.setText(street);
        txtSuite.setText(suite);
        txtCity.setText(city);
        txtZipcode.setText(zipcode);
        txtLat.setText(lat);
        txtLng.setText(lng);
        txtNameCompany.setText(companyName);
        txtCatchPhrase.setText(catchPhrase);
        txtBs.setText(bs);
    }

    private User getValorCampos() throws Exception {
        User user = new User();
        user.setAddress(new Address());
        user.setCompany(new Company());
        user.getAddress().setGeo(new Geo());
        user.setName(tratarEditTextString(txtName));
        user.setUsername(tratarEditTextString(txtUsername));
        user.setEmail(tratarEditTextString(txtEmail));
        user.setPhone(tratarEditTextString(txtPhone));
        user.setWebsite(tratarEditTextString(txtWebsite));
        user.getAddress().setStreet(tratarEditTextString(txtStreet));
        user.getAddress().setSuite(tratarEditTextString(txtSuite));
        user.getAddress().setCity(tratarEditTextString(txtCity));
        user.getAddress().setZipcode(tratarEditTextString(txtZipcode));
        user.getAddress().getGeo().setLat(tratarEditTextDouble(txtLat));
        user.getAddress().getGeo().setLng(tratarEditTextDouble(txtLng));
        user.getCompany().setName(tratarEditTextString(txtNameCompany));
        user.getCompany().setCatchPhrase(tratarEditTextString(txtCatchPhrase));
        user.getCompany().setBs(tratarEditTextString(txtBs));
        return user;
    }
}
