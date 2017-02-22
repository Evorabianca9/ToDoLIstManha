package br.senai.sp.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.senai.sp.modelo.SubTarefa;
import br.senai.sp.modelo.Tarefa;


@Repository
public class SubTarefaDao {
	@PersistenceContext
	private EntityManager manager;
	
	@Transactional
	public void criarSubTarefa(Long idTarefa, SubTarefa subtarefa){
		//Tras uma referencia de tarefa, para depois persistir a subTarefa
		//Objeto gerenciado pelo EntityManager
		subtarefa.setTarefa(manager.find(Tarefa.class, idTarefa));
		manager.persist(subtarefa);
	}
	
	
	public SubTarefa buscarSubTarefa(Long id){
		return manager.find(SubTarefa.class, id);
	}
	
	
	@Transactional
	public void marcarFeita(Long idSubtarefa, boolean valor){
		/*Merge e Persist atualiza dados no banco
		 *Merge -> atualiza o objeto offline(a qualquer momento) 
		 *Persist -> atualiza somente online com o hibernate*/
		SubTarefa subtarefa = buscarSubTarefa(idSubtarefa);
		subtarefa.setFeita(valor);
		manager.merge(subtarefa);
	}
	
	@Transactional
	public void excluir(Long idSubtarefa){
		SubTarefa subtarefa = buscarSubTarefa(idSubtarefa);
		Tarefa tarefa = subtarefa.getTarefa();
		tarefa.getSubtarefas().remove(subtarefa);
		manager.merge(tarefa);
	}

}
