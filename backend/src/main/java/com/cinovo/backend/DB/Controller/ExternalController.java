package com.cinovo.backend.DB.Controller;

import com.cinovo.backend.DB.Service.ExternalService;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/external")
@JBossLog
public class ExternalController
{
    private final ExternalService externalService;

    public ExternalController(ExternalService externalService)
    {
        this.externalService = externalService;
    }
}
