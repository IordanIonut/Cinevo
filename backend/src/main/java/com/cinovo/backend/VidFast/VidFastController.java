package com.cinovo.backend.VidFast;

import com.cinovo.backend.VidFast.Model.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cinovo.backend.VidFast.Model.Result;

@RestController
@RequestMapping("/api/vidfast")
public class VidFastController
{
    private final VidFastService vidFastService;

    public VidFastController(VidFastService vidFastService)
    {
        this.vidFastService = vidFastService;
    }

    @PostMapping("/check-url")
    public ResponseEntity<Result> checkUrl(@RequestBody Request req)
    {
        if(req == null || req.getUrl() == null || req.getUrl().isBlank())
        {
            return ResponseEntity.badRequest().build();
        }
        Result result = vidFastService.check(req.getUrl());
        return ResponseEntity.ok(result);
    }
}