package com.keax.infrastructure.controllers;

import com.keax.application.services.InstitutionService;
import com.keax.domain.models.Institution;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institution")
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @PostMapping
    public ResponseEntity<Institution> create(@RequestBody Institution institution){
        Institution create = institutionService.createInstitution(institution);
        return new ResponseEntity<>(create, HttpStatus.CREATED);
    }

    @PutMapping("/{institution_id}")
    public ResponseEntity<Institution> update(@PathVariable Long institution_id,@RequestBody Institution institution){
        return institutionService.updateInstitution(institution_id, institution)
                .map(task -> new ResponseEntity<>(task, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{institution_id}")
    private ResponseEntity<Boolean> delete(@PathVariable Long institution_id){
        if (institutionService.deleteInstitution(institution_id)){
            return  new ResponseEntity<>(true, HttpStatus.OK);
        }else{
            return  new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping
    public ResponseEntity<List<Institution>> listInstitution(){
        List<Institution> institutions = institutionService.getListInstitution();
        return new ResponseEntity<>(institutions, HttpStatus.OK);
    }

}
