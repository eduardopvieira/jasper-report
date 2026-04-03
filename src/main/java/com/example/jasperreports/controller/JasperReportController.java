package com.example.jasperreports.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jasperreports.model.Aluno;
import com.example.jasperreports.service.JasperReportService;

@RestController
@RequestMapping("/jasper-report")
public class JasperReportController {
    private final JasperReportService jasperReportService;

    public JasperReportController(JasperReportService jasperReportService) {
        this.jasperReportService = jasperReportService;
    }

    @PostMapping("/gerar-relatorio-x")
    public void gerarRelatorio(@RequestBody Aluno aluno) throws IOException {
        this.jasperReportService.gerarRelatorio(aluno);
    }
    
}
