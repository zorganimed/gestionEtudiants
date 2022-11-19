package org.opendevup.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.opendevup.dao.EtudiantRepository;
import org.opendevup.entities.Etudiant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/Etudiant")
public class EtudiantController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(EtudiantController.class);
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
		}
		etudiantRepository.save(etudiant);
		
		File dossier = new File(imageDir);
		if(!dossier.exists()) {
			dossier.mkdir();	
		}
		if(!file.isEmpty()) {
			etudiant.setPicture(file.getOriginalFilename());
			file.transferTo(new File(imageDir+etudiant.getId()));
		}
		
		return "redirect:Index";
	}
	
	@RequestMapping(value = "/getPhoto", produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] getPhoto(Long id) throws Exception {
		File f = new File(imageDir+id);
		return IOUtils.toByteArray(new FileInputStream(f));
	}
	
	@RequestMapping(value = "/supprimer")
	public String supprimer(Long id) {
		Etudiant etudiant = etudiantRepository.getById(id);
		etudiantRepository.delete(etudiant);
		return "redirect:Index";
	}
	
	@RequestMapping(value = "/editer")
	public String editer(Long id, Model model) {
		Etudiant etudiant = etudiantRepository.getOne(id);
		model.addAttribute("etudiant", etudiant);
 		return "editEtudiant";
	}
	
	@RequestMapping(value = "/updateEtudiant",method = RequestMethod.POST)
	public String update(@Valid Etudiant etudiant, BindingResult bindingResult,
			@RequestParam("photo") MultipartFile file) throws Exception {
		System.out.println("bindingresult output "+bindingResult.hasErrors());
		if(bindingResult.hasErrors()) {
			return "editEtudiant";
		}
		if(!file.isEmpty()) {
			etudiant.setPicture(file.getOriginalFilename());
		}
		etudiantRepository.save(etudiant);
		
		File dossier = new File(imageDir);
		if(!dossier.exists()) {
			dossier.mkdir();	
		}
		byte[] bytes = file.getBytes();
		File fileupdate = new File(imageDir+etudiant.getId());
		BufferedOutputStream stream = new BufferedOutputStream(
				new FileOutputStream(fileupdate));
		stream.write(bytes);
		stream.close();

		logger.info("Server File Location="
				+ fileupdate.getAbsolutePath());

 		 /*
		if(!file.isEmpty()) {
			file.transferTo(new File(imageDir+etudiant.getId()));
		}*/
		
		return "redirect:Index";
	}

}
