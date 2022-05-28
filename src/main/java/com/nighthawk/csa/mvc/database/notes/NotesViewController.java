package com.nighthawk.csa.mvc.database.notes;

import com.nighthawk.csa.mvc.database.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// Built using article: https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/mvc.html
// or similar: https://asbnotebook.com/2020/04/11/spring-boot-thymeleaf-form-validation-example/
@Controller
public class NotesViewController {
    // Autowired enables Control to connect HTML and POJO Object to database easily for CRUD
    @Autowired
    private ModelRepository repository;

    @RequestMapping("/notes")
    public String notes(@RequestParam("id") long id, Model model) {
        List<Notes> list = repository.listAllNotesWithId(id);
        model.addAttribute("listNotes", list);
        model.addAttribute("chapterId", id);
        return "/notes";
    }

    /*  The HTML template Forms and PersonForm attributes are bound
        @return - template for person form
        @param - Person Class
    */

    @RequestMapping(value = "/database/notescreate/{chid}", method = RequestMethod.GET)
    public String notesAdd(@PathVariable("chid") long chid, Model model) {
        model.addAttribute("chapterId", chid);
        //return "mvc/database/notescreate";
        return "/mvc/database/notescreate";
    }

//    @GetMapping("/database/not")
//    public String notesAdd(Model model) {
//        //model.addAttribute("chapterId", chid);
//        return "/mvc/database/notescreate";
//    }


    /* Gathers the attributes filled out in the form, tests for and retrieves validation error
    @param - Person object with @Valid
    @param - BindingResult object
     */
    @PostMapping("/database/notescreate")
    public String notesSave(@Valid Notes notes, BindingResult bindingResult) {
        // Validation of Decorated PersonForm attributes
        if (bindingResult.hasErrors()) {
            return "mvc/database/notescreate";
        }
        repository.saveNotes(notes);
//        repository.addRoleToPerson(person.getEmail(), "ROLE_STUDENT");
        // Redirect to next step
        return "redirect:/notes";
    }

//    @GetMapping("/database/chapterupdate/{id}")
//    public String personUpdate(@PathVariable("id") int id, Model model) {
//        model.addAttribute("person", repository.get(id));
//        return "mvc/database/personupdate";
//    }
//
//    @PostMapping("/database/personupdate")
//    public String personUpdateSave(@Valid Person person, BindingResult bindingResult) {
//        // Validation of Decorated PersonForm attributes
//        if (bindingResult.hasErrors()) {
//            return "mvc/database/personupdate";
//        }
//        repository.save(person);
//        repository.addRoleToPerson(person.getEmail(), "ROLE_STUDENT");
//
//        // Redirect to next step
//        return "redirect:/database/person";
//    }

    @GetMapping("/database/notesdelete/{id}")
    public String notesDelete(@PathVariable("id") long id) {
        repository.deleteNotes(id);
        return "redirect:/notes";
    }

//    @GetMapping("/database/person/search")
//    public String person() {
//        return "mvc/database/person_search";
//    }

}