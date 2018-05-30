package com.enthalpy.controller;

import com.enthalpy.model.Enthalpy;
import com.enthalpy.model.Vector;
import com.enthalpy.model.form.TransitionForm;
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

@Controller
public class MainController {

    private Vector vector;

    @Autowired
    public MainController(Vector vector) {
        this.vector = vector;
    }

    @GetMapping("/")
    public String getIndexPage(@ModelAttribute TransitionForm form, Model model){
        model.addAttribute("form", form);
        model.addAttribute("vector", vector);
        model.addAttribute("tempJson", vector.getTemperatureAsJsonObject());
        model.addAttribute("enthalpyJson", vector.getEnthalpyAsJsonObject());
        return "index";
    }

    @PostMapping("/")
    public String transition(@ModelAttribute("form") @Valid TransitionForm form, BindingResult bindingResult, Model model){

        if(form.getTempStart() > form.getTempEnd()){
            FieldError fieldError = new FieldError("form", "tempEnd", "Temperatura początkowa nie może być większa niż temperatura końcowa.");
            bindingResult.addError(fieldError);
        }

        if(bindingResult.hasErrors()){
            return "index";
        }

        Vector transitionVector = Enthalpy.transitionVector(vector, form.getTempStart(), form.getTempEnd(), form.getH(), form.getFunction());
        transitionVector.printVector();
        vector.insertVector(vector.getIndex(form.getTempStart()), vector.getIndex(form.getTempEnd()), transitionVector);
        model.addAttribute("tempJson", vector.getTemperatureAsJsonObject());
        model.addAttribute("enthalpyJson", vector.getEnthalpyAsJsonObject());
        model.addAttribute("form", form);
        model.addAttribute("vector", vector);
        System.out.println(form);
        return "index";
    }
}
