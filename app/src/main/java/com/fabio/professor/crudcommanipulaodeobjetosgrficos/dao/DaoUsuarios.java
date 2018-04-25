package com.fabio.professor.crudcommanipulaodeobjetosgrficos.dao;

import android.content.Context;

import com.fabio.professor.crudcommanipulaodeobjetosgrficos.domain.Usuario;

import java.util.ArrayList;
import java.util.List;

public class DaoUsuarios {

    private DaoAdapter daoAdapter;

    public DaoUsuarios(Context context){
        daoAdapter = new DaoAdapter(context);
    }

    public boolean insert(Usuario usuario) {
        Object[] args = {
                usuario.getNome(),
                usuario.getSenha()
        };
        return daoAdapter.queryExecute(
                "INSERT INTO usuarios (nome,senha) VALUES(?,?)",
                args
        );
    }
    public boolean update(Usuario usuario) {
        Object[] args = {
                usuario.getNome(),
                usuario.getSenha(),
                usuario.getId()
        };
        return daoAdapter.queryExecute(
                "UPDATE usuarios SET nome = ?, senha = ? WHERE id = ?",
                args
        );
    }
    public boolean delete(Usuario usuario) {
        Object[] args = {
                usuario.getId()
        };
        return daoAdapter.queryExecute(
                "DELETE FROM usuarios WHERE id = ?",
                args
        );
    }
    public List<Usuario> get(){
        ObjetoBanco objetoBanco = daoAdapter.queryConsulta(
                "SELECT id,nome,senha FROM usuarios ORDER BY nome ASC",
                null
        );
        List<Usuario> usuarios = new ArrayList<>();
        for(int i = 0; i < objetoBanco.size(); i++){
            Usuario usuario = new Usuario();
            usuario.setId(objetoBanco.getLong(i, "id"));
            usuario.setNome(objetoBanco.getString(i, "nome"));
            usuario.setSenha(objetoBanco.getString(i, "senha"));
            usuarios.add(usuario);
        }
        return usuarios;
    }
    public Usuario get(Usuario usuario){
        String[] args = {
                String.valueOf(usuario.getId())
        };
        ObjetoBanco objetoBanco = daoAdapter.queryConsulta(
                "SELECT id,nome,senha FROM usuarios WHERE id = ?",
                args
        );
        Usuario retorno = new Usuario();
        if(objetoBanco.size() > 0) {
            retorno.setId(objetoBanco.getLong(0, "id"));
            retorno.setNome(objetoBanco.getString(0, "nome"));
            retorno.setSenha(objetoBanco.getString(0, "senha"));
        }
        return retorno;
    }
}