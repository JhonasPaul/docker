package org.jortiz.springcloud.msvc.cursos.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;
import org.jortiz.springcloud.msvc.cursos.models.Usuario;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    /*tabla intermedia*/
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    /*crea la lalve foranea lamada curso_id en al tabla cursoUsuario*/
    @JoinColumn(name = "curso_id")
    private List<CursoUsuario> cursoUsuarios;

    /*indica que nos e mapeara a la persistencia, a las tablas*/
    @Transient
    private List<Usuario> usuarios;

    /*crer la intancia ArraysList de cursoUsuarios*/
    public Curso() {
        cursoUsuarios = new ArrayList<>();
        usuarios = new ArrayList<>();
    }

    public List<CursoUsuario> getCursoUsuarios() {
        return cursoUsuarios;
    }

    public void setCursoUsuarios(List<CursoUsuario> cursoUsuarios) {
        this.cursoUsuarios = cursoUsuarios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /*agrega de a uno un nuevo usuario*/
    public void addCursoUsuario(CursoUsuario cursoUsuario) {
        /*es la ArraysList de la relacion*/
        cursoUsuarios.add(cursoUsuario);
    }

    /*elimina de a uno un nuevo usuario*/
    public void removeCursoUsuario(CursoUsuario cursoUsuario) {
        cursoUsuarios.remove(cursoUsuario);
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
