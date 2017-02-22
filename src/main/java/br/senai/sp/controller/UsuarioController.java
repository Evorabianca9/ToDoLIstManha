package br.senai.sp.controller;

import java.net.URI;
import java.util.HashMap;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWTSigner;

import br.senai.sp.dao.UsuarioDao;
import br.senai.sp.modelo.Tarefa;
import br.senai.sp.modelo.TokenJWT;
import br.senai.sp.modelo.Usuario;

@RestController
public class UsuarioController {

	//Emite o token
	public static final String EMISSOR = "senai";
	//Chave para assinar o token e descriptografar 
	public static final String SECRET = "ToDoListSENAIInformatica";

	@Autowired
	private UsuarioDao dao;

	@RequestMapping(value = "/usuario", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Usuario> criarTarefa(@RequestBody Usuario usuario) {
		try {

			dao.inserirUsuario(usuario);
			return ResponseEntity.created(URI.create("/usuario/" + usuario.getId())).body(usuario);
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			// Problema de Validação
			return new ResponseEntity<Usuario>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Usuario>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/usuario/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
	public Usuario buscarUsuario(@PathVariable Long id) {
		return dao.buscar(id);

	}

	@RequestMapping(value = "usuario/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluir(@PathVariable("id") long id) {
		dao.excluir(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<TokenJWT> logar(@RequestBody Usuario usuario) {
		try {
			Usuario user = dao.logar(usuario);
			if (user != null) {
				// Criando a chave
				HashMap<String, Object> claims = new HashMap<String, Object>();

				// Inserindo valor nessa chave
				claims.put("iss", EMISSOR);
				claims.put("id_user", user.getId());
				claims.put("nome_user", user.getNome());

				// Buscar hora atual em segundos
				long horaAtual = System.currentTimeMillis() / 1000;

				// hora_expiração do token(hora atual + 1hora (3600 seg)
				long horaExpiracao = horaAtual + 3600;

				claims.put("iat", horaAtual);
				claims.put("exp", horaExpiracao);
				
				JWTSigner signer = new JWTSigner(SECRET);
				//Gerar o token para assinar essas chaves
				TokenJWT tokenJWT = new TokenJWT();
				//Devolve uma String, que são as claims criptografadas
				tokenJWT.setToken(signer.sign(claims));
				return ResponseEntity.ok(tokenJWT);

			} else {
				// Usuario nao autorizado
				return new ResponseEntity<TokenJWT>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<TokenJWT>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
