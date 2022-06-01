package com.nighthawk.csa.mvc.database.notes;

import com.nighthawk.csa.mvc.database.ModelRepository;
import com.nighthawk.csa.mvc.uploads.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

// Built using article: https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/mvc.html
// or similar: https://asbnotebook.com/2020/04/11/spring-boot-thymeleaf-form-validation-example/
@Controller
public class NotesViewController {
    // Autowired enables Control to connect HTML and POJO Object to database easily for CRUD
    @Autowired
    private ModelRepository repository;

    @GetMapping("/notes")
    public String notes(@RequestParam(name = "id", defaultValue = "-1") long id, Model model) {
        if (id == -1) {
			return "/notes_none";
		}
		List<Notes> list = repository.listAllNotesWithId(id);
        model.addAttribute("listNotes", list);
        model.addAttribute("chapterId", id);
        return "/notes";
    }

    @GetMapping("/addnote")
    public String addNoteForm(@RequestParam("chid") long chid, Model model){
        model.addAttribute("newPlaceholderNote", new Notes("", "", chid));
        return "/addnote";
    }

    @PostMapping("/addnote")
    public String addNoteSubmit(@ModelAttribute Notes note, @RequestParam("file") MultipartFile formFile, @RequestParam("type") String type, RedirectAttributes redirectAttributes){		
		if (type.equals("file")) {
			String filePath = "uploads/";       // thus, uploads defined outside of static
			String webPath = "/" + filePath;    // webPath

			// try/catch is in place, but error handling is not implemented (returns without alerts)
			try {
				// Creating the directory to store file
				File dir = new File( filePath );
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				byte[] bytes = formFile.getBytes();

				// File write alternatives (going with Stream for now as in theory it would be non-blocking)
				if (false) {
					Path path = Paths.get(filePath + formFile.getOriginalFilename());
					Files.write(path, bytes);
				} else {
					String path = filePath + formFile.getOriginalFilename();
					File serverFile = new File( path );
					BufferedOutputStream stream = new BufferedOutputStream(
							new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
				}

			} catch (IOException e) {
				e.printStackTrace();        // app stays alive, errors go to run console, /var/log/syslog
			}

			note.setLink(filePath + formFile.getOriginalFilename());
		}
		
		redirectAttributes.addAttribute("id", note.getChapterId());

		
        repository.saveNotes(note);
        return "redirect:/notes";
    }

    /*  The HTML template Forms and PersonForm attributes are bound
        @return - template for person form
        @param - Person Class
    */

//    @RequestMapping(value = "/database/notescreate/{chid}", method = RequestMethod.GET)
//    public String notesAdd(@PathVariable("chid") long chid, Model model) {
//        model.addAttribute("chapterId", chid);
//        //return "mvc/database/notescreate";
//        return "/mvc/database/notescreate";
//    }


    /* Gathers the attributes filled out in the form, tests for and retrieves validation error
    @param - Person object with @Valid
    @param - BindingResult object
     */
//    @PostMapping("/database/notescreate")
//    public String notesSave(@Valid Notes notes, BindingResult bindingResult) {
//        // Validation of Decorated PersonForm attributes
//        if (bindingResult.hasErrors()) {
//            return "mvc/database/notescreate";
//        }
//        repository.saveNotes(notes);
////        repository.addRoleToPerson(person.getEmail(), "ROLE_STUDENT");
//        // Redirect to next step
//        return "redirect:/notes";
//    }

//    @GetMapping("/database/chapterupdate/{id}")
//    public String personUpdate(@PathVariable("id") int id, Model model) {
//        model.addAttribute("person", repository.get(id));
//        return "mvc/database/personupdate";
//    }
//

//
//    @GetMapping("/database/notesdelete/{id}")
//    public String notesDelete(@PathVariable("id") long id) {
//        repository.deleteNotes(id);
//        return "redirect:/notes";
//    }



}