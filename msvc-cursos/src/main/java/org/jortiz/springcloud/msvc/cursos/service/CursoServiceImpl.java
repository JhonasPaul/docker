package org.jortiz.springcloud.msvc.cursos.service;

import org.jortiz.springcloud.msvc.cursos.client.UsuarioClientRest;
import org.jortiz.springcloud.msvc.cursos.models.Usuario;
import org.jortiz.springcloud.msvc.cursos.models.entity.Curso;
import org.jortiz.springcloud.msvc.cursos.models.entity.CursoUsuario;
import org.jortiz.springcloud.msvc.cursos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService {
    @Autowired
    private CursoRepository repository;

    @Autowired
    private UsuarioClientRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id) {
        Optional<Curso> o = repository.findById(id);
        if (o.isPresent()) {
            Curso curso = o.get();
            if (!curso.getCursoUsuarios().isEmpty()) {
                List<Long> ids = curso.getCursoUsuarios().stream().map(cu -> cu.getUsuarioId())
                        .collect(Collectors.toList());

                List<Usuario> usuarios = client.obtenerAlumnosPorCurso(ids);
                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarCursoUsuarioPorId(Long id) {
        repository.eliminarCursoUsuarioPorID(id);
    }


    /*gestion de usuarios a cursos*/
    /*asignar usuaria un curso en especifico*/
    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        /*busca el curso por id en la basde de datos*/
        Optional<Curso> o = repository.findById(cursoId);
        if (o.isPresent()) {                           /*este usuario es la plantilla no mapada*/
            Usuario usuarioMsvc = client.detalle(usuario.getId());

            /*obtenemos el curso que existe en la base de datos*/
            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    /*seria como el json que obtenemso en el request body*/
    @Override
    @Transactional                                             /*recibimos un usuari con todos sus datos*/
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        /*busca el curso por id en la basde de datos*/
        Optional<Curso> o = repository.findById(cursoId);
        if (o.isPresent()) {                           /*este usuario es la plantilla no mapada*/
            Usuario usuarioNuevoMsvc = client.crear(usuario);

            /*obtenemos el curso que existe en la base de datos*/
            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioNuevoMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        /*busca el curso por id en la basde de datos*/
        Optional<Curso> o = repository.findById(cursoId);
        /*si el curso esta presente*/
        if (o.isPresent()) {
            /*buscamos el usuario qeu queremos eliminar exista en el microservicio usuario emdiabte el detalle*/
            Usuario usuarioMsvc = client.detalle(usuario.getId());/*este usuario es la plantilla no mapada*/

            /*obtenemos el curso que existe en la base de datos*/
            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.removeCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }
}
