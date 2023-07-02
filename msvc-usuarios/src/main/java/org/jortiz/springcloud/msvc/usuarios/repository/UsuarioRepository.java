package org.jortiz.springcloud.msvc.usuarios.repository;

import org.jortiz.springcloud.msvc.usuarios.models.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

//    Optional<Usuario>findByEmail(String email);

    @Query( "select u from Usuario u where u.email = ?1")
    Optional<Usuario> findByEmail(String email);

    /*crear  metodo en el UsuarioService */
    /*boolean existePoremail(String email);*/
    boolean existsByEmail(String email);
}
