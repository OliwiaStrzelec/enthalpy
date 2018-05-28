package com.enthalpy.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.enthalpy.model.Vector;
import com.enthalpy.storage.StorageFileNotFoundException;
import com.enthalpy.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/file")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam int rowsToSkip, RedirectAttributes redirectAttributes) {
        storageService.store(file);
        System.out.println(rowsToSkip);
        try {
            File storedFile = storageService.loadAsResource(file.getOriginalFilename()).getFile();
            Scanner scanner = new Scanner(storedFile);
            System.out.println(storedFile.getName());
            Vector vector = Vector.getTemperatureAndCp(scanner, 5);
            vector.printVector();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}