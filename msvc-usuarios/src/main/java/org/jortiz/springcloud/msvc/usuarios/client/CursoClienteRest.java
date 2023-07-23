package org.jortiz.springcloud.msvc.usuarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
                                            /*msvc-usuarios es el nombnre del contenedor de msvc-cursos*/
@FeignClient(name = "msvc-cursos", url = "msvc-cursos:8002") /*es bueno que coisido con el nombre del servicio cursos*/
public interface CursoClienteRest {
    @DeleteMapping("/eliminar-curso-usuario/{id}")
    void eliminarUsuarioPorId(@PathVariable Long id);
}
//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