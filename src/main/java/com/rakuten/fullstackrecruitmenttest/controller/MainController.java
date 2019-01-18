package com.rakuten.fullstackrecruitmenttest.controller;

import com.rakuten.fullstackrecruitmenttest.database.DatabaseLoader;
import com.rakuten.fullstackrecruitmenttest.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rakuten.fullstackrecruitmenttest.storage.StorageFileNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MainController {

  @RequestMapping("/")
  @CrossOrigin(origins="*")
  public ModelAndView testMethod(Model model) {
      ModelAndView modelAndView = new ModelAndView("test");
      return modelAndView;
  }
    private final DatabaseLoader loaderService;
    private final StorageService storageService;
    private Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    public MainController(StorageService storageService, DatabaseLoader loaderService) {
        this.storageService = storageService;
        this.loaderService = loaderService;
    }

    @GetMapping("/files")
    public List<String> listUploadedFiles(Model model) throws IOException {
        return storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(MainController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList());
    }

    @PostMapping("/")
    public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws Exception {

        storageService.store(file);
        loaderService.loadFile(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");


        return new ModelAndView("test");
    }

    @PutMapping(value="/update", params={"updateColumn", "name", "updateValue"})
    public ModelAndView handleUpdate(@RequestParam("updateColumn") String updateColumn, @RequestParam("name") String name, @RequestParam("updateValue") String updateValue) {
        loaderService.updateRecord(updateColumn, name, updateValue);
        return new ModelAndView("test");
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
