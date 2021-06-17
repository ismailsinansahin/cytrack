package com.cydeo.controller;

import com.cydeo.service.BatchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/batch")
public class BatchController {

    BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping("/batchList")
    public String batchList(Model model){
        model.addAttribute("batches", batchService.listAllBatches());
        return "batch/batch-list";
    }

}
