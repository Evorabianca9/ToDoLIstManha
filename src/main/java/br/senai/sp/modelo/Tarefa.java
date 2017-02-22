package br.senai.sp.modelo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Tarefa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titulo;
	//Hibernate cria uma tabela associativa
	//Mas como nao precisamos vamos modificar 
	//fazendo a anotação @ManyToOne na SubTarefa
	
	//CascateType.ALL quando cria uma tarefa ele automaticamente cria a subTarefas
	//orphanRemoval = true quando exclui a Tarefa as Subtarefas são excluidas juntos.
	@OneToMany(mappedBy = "tarefa", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SubTarefa> subtarefas;
	
	
	//Atributo virtual 
	@JsonProperty("feita")
	public boolean isRealizada(){
		for(SubTarefa subtarefa : subtarefas){
			if (!subtarefa.isFeita()) {
				return false;
			}
		}
		return true;
	}
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public List<SubTarefa> getSubtarefas() {
		return subtarefas;
	}
	public void setSubtarefas(List<SubTarefa> subtarefas) {
		this.subtarefas = subtarefas;
	}
}
