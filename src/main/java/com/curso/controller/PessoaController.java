package com.curso.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Null;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.curso.model.Pessoa;
import com.curso.model.Telefone;
import com.curso.repository.PessoaRepository;
import com.curso.repository.TelefoneRepository;

@Controller
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@GetMapping("/cadastropessoa")
	public ModelAndView inicio() {
		
		ModelAndView modelandView = new ModelAndView("cadastro/cadastropessoa");
		modelandView.addObject("pessoaobj", new Pessoa());
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		modelandView.addObject("pessoas", pessoasIt);
		
		return modelandView;
		
	}
	
	
	@PostMapping({"*/salvarpessoa","salvarpessoa"})
	public ModelAndView salvar(@Valid Pessoa pessoa,BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
			Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
			andView.addObject("pessoas", pessoasIt);
			andView.addObject("pessoaobj", pessoa);
			
			List<String> msg = new ArrayList<>();
			for(ObjectError objectError: bindingResult.getAllErrors()) {
				msg.add(objectError.getDefaultMessage());// vem das anotações @NotEmpty			
			}
			andView.addObject("msg", msg);
			return andView;
		}
		
		pessoaRepository.save(pessoa);
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		
		andView.addObject("pessoas", pessoasIt);
		andView.addObject("pessoaobj", new Pessoa());
		
		return andView;
		
	}
	
	@GetMapping("/listapessoas")
	public ModelAndView pessoas() {
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		
		
		andView.addObject("pessoas", pessoasIt);
		andView.addObject("pessoaobj", new Pessoa());
		return andView;
	}
	
	@GetMapping("/editarpessoa/{idpessoa}")
	public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) {
		
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		
		ModelAndView modelandView = new ModelAndView("cadastro/cadastropessoa");
		modelandView.addObject("pessoaobj", pessoa.get());
		
		return modelandView;
	}
	
	@GetMapping("/removerpessoa/{idpessoa}")
	public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa) {
		pessoaRepository.deleteById(idpessoa);
		
		ModelAndView modelandView = new ModelAndView("cadastro/cadastropessoa");
		modelandView.addObject("pessoas", pessoaRepository.findAll());
		modelandView.addObject("pessoaobj", new Pessoa());
		
		return modelandView;
		
	}
	
	@PostMapping({"/pesquisapessoa","*/pesquisapessoa"})
	public ModelAndView pesquisar(@RequestParam("nomepequisa") String nomepequisa) {
		List<Pessoa> pessoas = pessoaRepository.buscarPessoaPorNome(nomepequisa.toUpperCase().trim());
		
		ModelAndView modelandView = new ModelAndView("cadastro/cadastropessoa"); 
		modelandView.addObject("pessoas", pessoas);
		modelandView.addObject("pessoaobj", new Pessoa());
		return modelandView;
		
	}
	
	@GetMapping("/telefones/{idpessoa}")
	public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) {
		
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		
		ModelAndView modelandView = new ModelAndView("cadastro/telefones");
		
		modelandView.addObject("pessoaobj", pessoa.get());
		modelandView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));
		
		return modelandView;
	}
	
	@PostMapping(value = {"/addfonePessoa/{pessoaid}","*/addfonePessoa/{pessoaid}"})
	public ModelAndView addFonePessoa(Telefone telefone,@PathVariable("pessoaid") Long pessoaid) {
		
		Pessoa pessoa = pessoaRepository.findById(pessoaid).get();
		
		if(telefone != null && telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty()){
				
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			modelAndView.addObject("pessoaobj", pessoa);
			modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
			
			List<String> msg = new ArrayList<>();
			if(telefone.getNumero().isEmpty()) {
				msg.add("Numero deve ser informado");
			}
			if(telefone.getTipo().isEmpty()) {
				msg.add("Tipo deve ser informado");
			}
			
			modelAndView.addObject("msg", msg);
			return modelAndView;
			
		}
		
		
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		
		telefone.setPessoa(pessoa);
		
		telefoneRepository.save(telefone);
		
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
			
		return modelAndView;
	}
	
	
	@GetMapping("/removertelefone/{idtelefone}")
	public ModelAndView removertelefone(@PathVariable("idtelefone") Long idtelefone) {
		Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa();
		telefoneRepository.deleteById(idtelefone);
		
		ModelAndView modelandView = new ModelAndView("cadastro/telefones");
		modelandView.addObject("pessoaobj", pessoa);
		modelandView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId()));
		return modelandView;
		
	}
	
}
