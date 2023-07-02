package org.jortiz.springcloud.msvc.cursos.controller;

import feign.FeignException;
import jakarta.validation.Valid;
import org.jortiz.springcloud.msvc.cursos.models.Usuario;
import org.jortiz.springcloud.msvc.cursos.models.entity.Curso;
import org.jortiz.springcloud.msvc.cursos.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping
public class CursoController {
    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity<?> listar() {
        return new ResponseEntity<>(service.listar(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Curso> cursoOption = service.porIdConUsuarios(id);/*service.porId(id)*/
        if (cursoOption.isPresent()) {
            return new ResponseEntity<>(cursoOption.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Curso no encontrado", HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Curso curso, BindingResult result) {
        if (result.hasErrors()) {
            return validarCampo(result);
        }
        return new ResponseEntity<>(service.guardar(curso), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> editar(@RequestBody Curso curso, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validarCampo(result);
        }
        Optional<Curso> cursOptional = service.porId(id);
        if (cursOptional.isPresent()) {
            Curso cursoDb = cursOptional.get();
            cursoDb.setNombre(curso.getNombre());
            return new ResponseEntity<>(service.guardar(cursoDb), HttpStatus.CREATED);
        }
        return new ResponseEntity<>("curso no encontrado", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Curso> cursoOptional = service.porId(id);
        if (cursoOptional.isPresent()) {
            service.eliminar(id);
            return new ResponseEntity<>("curso eliminado con exito", HttpStatus.OK);
        }
        return new ResponseEntity<>("No se encontro el curso", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> o;
        try {
            o = service.asignarUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensje", "no existe el usuario con el id o erroe en la comunicacion" + e.getMessage()));
        }
        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> o;
        try {
            o = service.crearUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensje", "no se pudo crear el usuario o erroe en la comunicacion" + e.getMessage()));
        }
        if (o.isPresent()) {
            return  ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> o;
        try {
            o = service.eliminarUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensje", "no existe el usuario con el id o erroe en la comunicacion" + e.getMessage()));
        }
        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id) {
        service.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    private static ResponseEntity<Map<String, String>> validarCampo(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
        });
        return new ResponseEntity<Map<String, String>>(errores, HttpStatus.BAD_REQUEST);
    }
}
