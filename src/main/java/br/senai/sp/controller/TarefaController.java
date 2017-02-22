package br.senai.sp.controller;

import java.net.URI;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.dao.InterfaceDao;
import br.senai.sp.modelo.SubTarefa;
import br.senai.sp.modelo.Tarefa;

@RestController
public class TarefaController {
	@Autowired
	private InterfaceDao<Tarefa> daoTarefa;

	@RequestMapping(value = "tarefa", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Tarefa> criarTarefa(@RequestBody Tarefa tarefa) {
		try {
			for (SubTarefa subTarefa : tarefa.getSubtarefas()) {
				subTarefa.setTarefa(tarefa);
			}
			daoTarefa.inserir(tarefa);
			return ResponseEntity.created(URI.create("/tarefa/" + tarefa.getId())).body(tarefa);
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			// Problema de Validação
			return new ResponseEntity<Tarefa>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Tarefa>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@RequestMapping(value="/tarefa/{id}", produces= MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
	public Tarefa buscarTarefa(@PathVariable Long id){
		return daoTarefa.buscar(id);
		
	}
	
	@RequestMapping(value = "/tarefa", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Tarefa> listar() {
		return daoTarefa.listar();
	}
	
	@RequestMapping(value = "tarefa/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluir(@PathVariable("id") long id) {
		daoTarefa.excluir(id);
		return  ResponseEntity.noContent().build();
	}

}
