package org.jortiz.springcloud.msvc.usuarios.service;

import org.jortiz.springcloud.msvc.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    /*los servis son para la logica de negocio, conlos objetos repositorio*/
    List<Usuario> listar();

    /*es una clase que envuelve el objeto para saber si eta en la consulta y evitar el nullponterexception*/
    Optional<Usuario> porId(Long id);
    Usuario guardar(Usuario usuario);
    void eliminar(Long id);

    List<Usuario> listarPorIds(Iterable<Long> ids);

    /*buscar usuario por corre*/
    Optional<Usuario> porEmail(String email);

    boolean existePorEmail(String email);
}
