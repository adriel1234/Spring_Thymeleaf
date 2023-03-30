package com.curso.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.curso.model.Pessoa;
import com.curso.repository.PessoaRepository;

@Controller
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@GetMapping("/cadastropessoa")
	public ModelAndView inicio() {
		
		ModelAndView modelandView = new ModelAndView("cadastro/cadastropessoa");
		modelandView.addObject("pessoaobj", new Pessoa());
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		modelandView.addObject("pessoas", pessoasIt);
		
		return modelandView;
		
	}
	
	
	@PostMapping({"*/salvarpessoa","salvarpessoa"})
	public ModelAndView salvar(Pessoa pessoa) {
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
		
		return modelandView;
	}
}
