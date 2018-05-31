package com.enthalpy.controller;

import com.enthalpy.model.Enthalpy;
import com.enthalpy.model.Vector;
import com.enthalpy.model.form.TransitionForm;
import com.enthalpy.service.VectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;

@Controller
public class MainController {

    private VectorService vectorService;

    @Autowired
    public MainController(VectorService vectorService) {
        this.vectorService = vectorService;
    }

    @GetMapping("/")
    public String getIndexPage(@ModelAttribute TransitionForm form, Model model){
        vectorService.newVector();
        Vector vector = vectorService.getVector();
        model.addAttribute("form", form);
        //model.addAttribute("vector", vector);
        model.addAttribute("tempJson", vector.getTemperatureAsJsonObject());
        model.addAttribute("enthalpyJson", vector.getEnthalpyAsJsonObject());
        return "index";
    }
}
