package com.senaigo.retrofit.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityUser extends AppCompatActivity {

    UserInterface apiUserInterface;

    EditText txtId;
    EditText txtUserName;
    EditText txtData;
    TextView txtId2;
    ImageButton imgBtnEdit;
    ListView listViewUser;
    List<User> listUser;
    List<Map<String,String>> colecao = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        apiUserInterface = APIClient.getClient().create(UserInterface.class);

        Call<List<User>> get = apiUserInterface.get();
        get.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                listViewUser = findViewById(R.id.listViewUser);
                listUser = response.body();

                for(User u : listUser){
                    //Criar dados para adapter
                    Map<String, String> mapUser = new HashMap<>();
                    mapUser.put("userId", u.getUserId() + "");
                    mapUser.put("title", u.getTitle());

                    colecao.add(mapUser);
                }

                String[] from = {"userId","title"};
                int[] to = {R.id.txtId2,R.id.txtUserName};

                SimpleAdapter simpleAdapter =
                        new SimpleAdapter(
                                getApplicationContext(),
                                colecao,
                                R.layout.user,
                                from,
                                to);

                listViewUser.setAdapter(simpleAdapter);
                listViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        teste();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addUser(View view) {
    }

    public void teste() {
//        String username,data;
//        Integer id;
//        String pos = listViewUser.getPositionForView((View)view.getParent()) + "";
//        txtId2 = findViewById(R.id.txtId2);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Erro!");
        alertDialog.setMessage("Aki");
        alertDialog.show();
//        id = Integer.parseInt(txtId.getText().toString());
//        username = txtUserName.getText().toString();
    }
}
