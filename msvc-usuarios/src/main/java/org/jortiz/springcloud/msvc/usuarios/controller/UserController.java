package org.jortiz.springcloud.msvc.usuarios.controller;


import jakarta.validation.Valid;
import org.jortiz.springcloud.msvc.usuarios.models.entity.Usuario;
import org.jortiz.springcloud.msvc.usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/*ctrl + tab --> escoger archivos abierto*/
@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public Map<String,List<Usuario>> listar() {
        return Collections.singletonMap("usuarios", service.listar());
    }

    @GetMapping("/buscar/{id}")/*el @PathVariable permite que valor de la ruta url se pasa por el argumento del metodo detalle*/
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        Optional<Usuario> usuarioOptiotan = service.porId(id);

        if (usuarioOptiotan.isPresent()) {
            return new ResponseEntity<>(usuarioOptiotan.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("usuario no encontrado", HttpStatus.NOT_FOUND);
    }


    @PostMapping/*@RequestBody indica que se enviara un usuario en el cuerpo de la peticion y el jsonse convierte en un objeto usuario*/
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {
        /*devuelve un valor booleano, si es true, quiere decir que hay errores*/
        if (result.hasErrors()) {/*hasErrors boolean*/
            return getMapResponseEntity(result);
        }
        Optional<Usuario> usuarioOptional = service.porEmail(usuario.getEmail());
        if (!usuario.getEmail().isEmpty() && usuarioOptional.isPresent()) {
            /*metodo que valida por el email y si el usuaio existe*/
            return getMapResponseEntity(usuario);
        }
//        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
        Usuario uNuevo = service.guardar(usuario);
        return new ResponseEntity<>(uNuevo, HttpStatus.CREATED);
    }




    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return getMapResponseEntity(result);
        }
        Optional<Usuario> usuarioOptional = service.porEmail(usuario.getEmail());
        if (!usuario.getEmail().isEmpty() && usuarioOptional.isPresent()) {
            return getMapResponseEntity(usuario);
        }

        Optional<Usuario> o = service.porId(id);
        /*o es el optional que viene de la base de datos*/
        if (o.isPresent()) {
            Usuario usuarioDb = o.get();/*usuario optional se convierte a un objetopara poder actualzar*/
            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(usuario.getPassword());
            return new ResponseEntity<>(service.guardar(usuarioDb), HttpStatus.CREATED);
        }
        return new ResponseEntity<>("No se encontrol el Usuario ", HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Usuario> o = service.porId(id);
        if (o.isPresent()) {
            service.eliminar(id);
            return new ResponseEntity<>("Usuario ".concat(String.valueOf(o.get().getNombre())).concat(" eliminado"), HttpStatus.OK);
        }
        return new ResponseEntity<>("No se encontro", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/usuarios-por-curso")         /*@RequestParam es para mandar parametros cuando es get*/
    public ResponseEntity<?>obtenerAlumnosPorCurso(@RequestParam List<Long>ids){
        return ResponseEntity.ok(service.listarPorIds(ids));
    }

  /*  @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) {
        Optional<Usuario> usuarioEncontrado = service.porEmail(email);
        if (usuarioEncontrado.isPresent()) {
            return new ResponseEntity<>(usuarioEncontrado.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>("El usuario no existe en la base de datos ", HttpStatus.NOT_FOUND);

    }*/

    /*METODO ARA VALIDAR CAMPOS*/
    private static ResponseEntity<Map<String, String>> getMapResponseEntity(BindingResult result) {
        Map<String, String> errores = new HashMap<>();/*Map convierte los mensajes de error en json*/
        result.getFieldErrors().forEach(err -> {/*getFielfErros contiene una la lista de los campos erroneos*/
            errores.put(err.getField(), "El Campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }

    private static ResponseEntity<Map<String, String>> getMapResponseEntity(Usuario usuario) {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje ", "email electronico ".concat(usuario.getEmail()).concat(" ya existe en la base de datos"));
//        return new ResponseEntity<>(Collections.singletonMap("mensaje ", "ya"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
