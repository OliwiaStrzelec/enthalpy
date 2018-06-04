package com.enthalpy.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.enthalpy.model.Enthalpy;
import com.enthalpy.model.Vector;
import com.enthalpy.model.form.TransitionForm;
import com.enthalpy.service.VectorService;
import com.enthalpy.storage.StorageFileNotFoundException;
import com.enthalpy.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
public class FileUploadController {

    private final StorageService storageService;
    private VectorService vectorService;

    @Autowired
    public FileUploadController(StorageService storageService, VectorService vectorService) {
        this.storageService = storageService;
        this.vectorService = vectorService;
    }

    @PostMapping("/file")
    public String handleFileUpload(Model model, @RequestParam("file") MultipartFile file, @RequestParam int rowsToSkip, RedirectAttributes redirectAttributes) {
        try {
            storageService.store(file);
        }catch (RuntimeException e){
        }
        try {
            File storedFile = storageService.loadAsResource(file.getOriginalFilename()).getFile();
            Scanner scanner = new Scanner(storedFile);
            System.out.println(storedFile.getName());
            Vector vector = Vector.getTemperatureAndCp(scanner, rowsToSkip);
            addModel(model, vector);
            vectorService.setVector(vector);
            return "index";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }


    @PostMapping("/")
    public String transition(@ModelAttribute("form") @Valid TransitionForm form, BindingResult bindingResult, Model model){
        Vector vector = vectorService.getVector();
        addErrors(form, bindingResult, vector);

        if(bindingResult.hasErrors()){
            addModel(form, model, vector);
            return "index";
        }
        transition(form, vector);
        addModel(form, model, vector);
        vectorService.setVector(vector);
        return "index";
    }

    private void transition(TransitionForm form, Vector vector) {
        Vector transitionVector = Enthalpy.transitionVector(vector, form.getTempStart(), form.getTempEnd(), form.getH(), form.getFunction());
        transitionVector.printVector();
        vector.insertVector(vector.getIndex(form.getTempStart()), vector.getIndex(form.getTempEnd()), transitionVector);
    }


    private void addModel(Model model, Vector vector) {
        addModel(new TransitionForm(), model, vector);
    }


    private void addModel(TransitionForm form, Model model, Vector vector) {
        model.addAttribute("tempJson", vector.getTemperatureAsJsonObject());
        model.addAttribute("enthalpyJson", vector.getEnthalpyAsJsonObject());
        model.addAttribute("form", form);
    }

    private void addErrors(TransitionForm form, BindingResult bindingResult, Vector vector) {
        if(form.getTempStart() > form.getTempEnd()){
            FieldError fieldError = new FieldError("form", "tempEnd", "Temperatura początkowa nie może być większa niż temperatura końcowa.");
            bindingResult.addError(fieldError);
        }
        if(form.getTempStart() < Collections.min(vector.getTemperature())){
            FieldError fieldError = new FieldError("form", "tempStart", "Temperatura początkowa nie może być niższa niż najmniejsza znana temperatura.");
            bindingResult.addError(fieldError);
        }
        if(form.getTempEnd() > Collections.max(vector.getTemperature())){
            FieldError fieldError = new FieldError("form", "tempEnd", "Temperatura początkowa nie może być większa niż największa znana temperatura.");
            bindingResult.addError(fieldError);
        }
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}