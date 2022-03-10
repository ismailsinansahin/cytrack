package com.cydeo.controller;

import com.cydeo.dto.BatchDTO;
import com.cydeo.enums.BatchStatus;
import com.cydeo.service.BatchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
@RequestMapping("/batches")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping("/batchList")
    public String goBatchList(Model model){
        model.addAttribute("batches", batchService.listAllBatches());
        return "batch/batch-list";
    }

    @GetMapping("/batchCreate")
    public String goBatchCreate(Model model){
        model.addAttribute("newBatch", new BatchDTO());
        model.addAttribute("batchStatuses", Arrays.asList(BatchStatus.values()));
        return "batch/batch-create";
    }

    @PostMapping("/batchCreate")
    public String createBatch(BatchDTO batchDto){
        batchService.create(batchDto);
        return "redirect:/batches/batchList";
    }

    @GetMapping("/batchEdit/{batchId}")
    public String goBatchEdit(@PathVariable("batchId") Long batchId, Model model){
        model.addAttribute("batch", batchService.getByBatchId(batchId));
        model.addAttribute("batchStatuses", Arrays.asList(BatchStatus.values()));
        return "batch/batch-edit";
    }

    @PostMapping("/batchUpdate/{batchId}")
    public String updateBatch(@PathVariable("batchId") Long batchId, BatchDTO batchDTO){
        batchService.save(batchDTO, batchId);
        return "redirect:/batches/batchList";
    }

    @PostMapping(value = "/allActions/{batchId}", params = {"action=showGroups"})
    public String goGroupsPage(@PathVariable("batchId") Long batchId){
        return "redirect:/groups/groupList/" + batchId;
    }

    @PostMapping(value = "/allActions/{batchId}", params = {"action=showTasks"})
    public String goTasksPage(@PathVariable("batchId") Long batchId){
        return "redirect:/tasks/taskList/" + batchId;
    }

    @PostMapping(value = "/allActions/{batchId}", params = {"action=edit"})
    public String goEditPage(@PathVariable("batchId") Long batchId){
        return "redirect:/batches/batchEdit/" + batchId;
    }

    @PostMapping(value = "/allActions/{batchId}", params = {"action=delete"})
    public String deleteBatch(@PathVariable("batchId") Long batchId){
        batchService.delete(batchId);
        return "redirect:/batches/batchList";
    }

    @PostMapping(value = "/allActions/{batchId}", params = {"action=start"})
    public String startBatch(@PathVariable("batchId") Long batchId){
        batchService.start(batchId);
        return "redirect:/batches/batchList";
    }

    @PostMapping(value = "/allActions/{batchId}", params = {"action=complete"})
    public String completeBatch(@PathVariable("batchId") Long batchId){
        batchService.complete(batchId);
        return "redirect:/batches/batchList";
    }

}
