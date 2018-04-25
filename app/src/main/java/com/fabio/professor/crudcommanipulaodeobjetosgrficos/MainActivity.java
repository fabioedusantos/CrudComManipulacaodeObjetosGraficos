package com.fabio.professor.crudcommanipulaodeobjetosgrficos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fabio.professor.crudcommanipulaodeobjetosgrficos.dao.DaoAdapter;
import com.fabio.professor.crudcommanipulaodeobjetosgrficos.dao.DaoUsuarios;
import com.fabio.professor.crudcommanipulaodeobjetosgrficos.domain.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private TextView lblTitulo;
    private EditText txtNome;
    private EditText txtSenha;
    private EditText txtConfirme;
    private Usuario usuario;
    private DaoUsuarios daoUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        lblTitulo = findViewById(R.id.lblTitulo);
        txtNome = findViewById(R.id.txtNome);
        txtSenha = findViewById(R.id.txtSenha);
        txtConfirme = findViewById(R.id.txtConfirme);

        usuario = new Usuario();

        DaoAdapter daoAdapter = new DaoAdapter(this);
        daoAdapter.onCreate(daoAdapter.getWritableDatabase());
        daoUsuarios = new DaoUsuarios(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<String> lista = new ArrayList<>();
        final List<Usuario> usuarios = daoUsuarios.get();

        if(usuarios.size() > 0) setTitle("Usuários Cadastrados");
        else                    setTitle("Não há usuários cadastrados");

        for(Usuario u : usuarios) lista.add(u.getNome());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                lista);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                usuario = usuarios.get(position);
                txtNome.setText(usuario.getNome());
                txtSenha.setText(usuario.getSenha());
                txtConfirme.setText(usuario.getSenha());
                lblTitulo.setText("Alterar Usuário");
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                usuario = usuarios.get(position);
                if(daoUsuarios.delete(usuario)) {
                    Toast.makeText(
                            MainActivity.this,
                            "Sucesso!", Toast.LENGTH_SHORT).show();
                    limpar();
                    onResume();
                } else{
                    Toast.makeText(
                            MainActivity.this,
                            "Erro!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    public void salvar(View v){
        if(txtNome.getText().toString().length() < 3){
            txtNome.setError("Nome deve ter ao menos 3 caracteres.");
            return;
        }
        if(txtSenha.getText().toString().length() < 6){
            txtSenha.setError("Senha deve ter ao menos 6 caracteres.");
            return;
        }
        if(!txtSenha.getText().toString().equals(txtConfirme.getText().toString())){
            txtConfirme.setError("As senhas devem ser iguais.");
            return;
        }

        usuario.setNome(txtNome.getText().toString());
        usuario.setSenha(txtSenha.getText().toString());

        boolean result;
        if(usuario.getId() == 0) result = daoUsuarios.insert(usuario);
        else                     result = daoUsuarios.update(usuario);

        if(result) {
            Toast.makeText(this, "Sucesso!", Toast.LENGTH_SHORT).show();
            limpar();
            onResume();
        } else{
            Toast.makeText(this, "Erro!", Toast.LENGTH_SHORT).show();
        }
    }

    public void limpar(View v){
        limpar();
    }

    private void limpar(){
        usuario = new Usuario();
        txtNome.setText("");
        txtSenha.setText("");
        txtConfirme.setText("");
        lblTitulo.setText("Novo Usuário");
    }
}