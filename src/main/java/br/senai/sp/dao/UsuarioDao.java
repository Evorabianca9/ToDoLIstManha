package br.senai.sp.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.senai.sp.modelo.Tarefa;
import br.senai.sp.modelo.Usuario;

@Repository
public class UsuarioDao {
	@PersistenceContext
	private EntityManager manager;

	
	@Transactional
	public void inserirUsuario(Usuario usuario){
		
		manager.persist(usuario);
	}
	
	
	public Usuario buscar(Long id) {
		return manager.find(Usuario.class, id);
	}
	
	
	@Transactional
	public void excluir(Long id) {
		Usuario usuario = buscar(id);
		manager.remove(usuario);
	}
	
	
	
	public Usuario logar(Usuario usuario){
		TypedQuery<Usuario> query = manager.createQuery("select u from Usuario u where u.email = :email and u.senha = :senha", Usuario.class);
		query.setParameter("email", usuario.getEmail());
		query.setParameter("senha", usuario.getSenha());
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	} 


}
