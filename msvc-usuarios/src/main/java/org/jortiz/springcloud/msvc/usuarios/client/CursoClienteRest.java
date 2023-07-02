package org.jortiz.springcloud.msvc.usuarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "eliminar-usuario", url = "http://host.docker.internat:8002")
public interface CursoClienteRest {
    @DeleteMapping("/eliminar-curso-usuario/{id}")
    void eliminarUsuarioPorId(@PathVariable Long id);
}
