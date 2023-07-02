package org.jortiz.springcloud.msvc.cursos.client;

import org.jortiz.springcloud.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios", url = "http://localhost:8001")
public interface UsuarioClientRest {

    /*metodo del controlador de microservicio usuarios*/
    @GetMapping("/buscar/{id}")
    Usuario detalle(@PathVariable Long id);

    @PostMapping
    /*este usuario es la plantilla no mapeada*/
    Usuario crear(@RequestBody Usuario usuario);

    @GetMapping("/usuarios-por-curso")
    public List<Usuario> obtenerAlumnosPorCurso(@RequestParam Iterable<Long> ids);
}
