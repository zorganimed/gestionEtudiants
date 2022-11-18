package org.opendevup.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.opendevup.dao.EtudiantRepository;
import org.opendevup.entities.Etudiant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/Etudiant")
public class EtudiantController {
	
	@Autowired
	private EtudiantRepository etudiantRepository;
	@Value("${dir.images}")
	private String imageDir;
		
	@RequestMapping(value = "/Index")
	public String Index(Model model,
			@RequestParam(name = "page", defaultValue = "0") int p,
			@RequestParam(name = "motCle", defaultValue = "") String mc) {
	
		Page<Etudiant> pageEtudiants = etudiantRepository.chercherEtudiants("%"+mc+"%",
		   PageRequest.of(p, 5));
		
		int pageCount = pageEtudiants.getTotalPages();
		
		int[] pages = new int[pageCount];
		for(int i = 0; i<pageCount;i++) {
			pages[i]=i;
		}
		model.addAttribute("pages", pages);
		model.addAttribute("pageEtudiants", pageEtudiants);
		model.addAttribute("pageCourante", p);
		model.addAttribute("motCle", mc);
		return "etudiants";
	}
	
	@RequestMapping(value = "/form",method = RequestMethod.GET)
	public String formEtudiant(Model model) {
		model.addAttribute("etudiant", new Etudiant());
		return "formEtudiant";
	}
	
	@RequestMapping(value = "/saveEtudiant",method = RequestMethod.POST)
	public String save(@Valid Etudiant etudiant, BindingResult bindingResult,
			@RequestParam("photo") MultipartFile file) throws Exception {
		System.out.println("bindingresult output "+bindingResult.hasErrors());
		if(bindingResult.hasErrors()) {
			return "formEtudiant";
		}
		if(!file.isEmpty()) {
			etudiant.setPicture(file.getOriginalFilename());
			System.out.println(file.getOriginalFilename());
			System.out.println("System.getProperty(user.home) "+System.getProperty("user.home"));
			file.transferTo(new File(imageDir+file.getOriginalFilename()));
		}
		etudiantRepository.save(etudiant);
		return "redirect:Index";
	}

}
