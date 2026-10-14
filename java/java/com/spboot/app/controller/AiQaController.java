package com.spboot.app.controller;

import com.spboot.app.service.AiQaService;
import com.spboot.app.utils.R;
import com.spboot.app.utils.SessionFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = {"AI问答"})
@RestController
@RequestMapping("/api/ai")
public class AiQaController {

    @Autowired
    private AiQaService aiQaService;

    @ApiOperation(value = "智能问答", httpMethod = "POST")
    @PostMapping("/qa")
    public R<Object> qa(@RequestBody Map<String, Object> req) {
        String question = req.get("question") == null ? "" : req.get("question").toString();
        String scene = req.get("scene") == null ? "auction" : req.get("scene").toString();
        String username = SessionFactory.getUsername();
        String role = SessionFactory.getCx();
        return R.success(aiQaService.answer(question, scene, username, role));
    }
}

