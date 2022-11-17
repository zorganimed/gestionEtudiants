package org.opendevup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.opendevup.dao.EtudiantRepository;
import org.opendevup.entities.Etudiant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootApplication
public class TpSpringMvcApplication {

	public static void main(String[] args) throws ParseException {
	ApplicationContext ctx = SpringApplication.run(TpSpringMvcApplication.class, args);
    EtudiantRepository etudiantRepository = ctx.getBean(EtudiantRepository.class);
    DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
    etudiantRepository.save(new Etudiant("Mohamed", dt.parse("1990-05-20"), "mohamed@gmail.com","mohamed.png"));
    etudiantRepository.save(new Etudiant("Sihem", dt.parse("1990-10-20"), "sihem@gmail.com","sihem.png"));
    etudiantRepository.save(new Etudiant("Jouri", dt.parse("2021-03-01"), "jouri@gmail.com","jouri.png"));
    
    Page<Etudiant> etds= etudiantRepository.findAll(PageRequest.of(0, 5));
    
    etds.forEach(e-> System.out.println(e.getNom()));
    
    Page<Etudiant> et= etudiantRepository.chercherEtudiants("%r%",PageRequest.of(0, 2));
    et.forEach(e-> System.out.println(e.getNom()));
    
    
    
	}
	
	

}
