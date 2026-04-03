package com.example.jasperreports.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.example.jasperreports.model.Aluno;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class JasperReportService {

    public static final String CERTIFICADOS = "classpath:jasper/certificados/";
    public static final String IMAGEBG = "classpath:jasper/img/certificado-teste.png";
    public static final String ARQUIVOJRXML = "certificado.jrxml";
    public static final Logger LOGGER = LoggerFactory.getLogger(JasperReportService.class);
    public static final String DESTINOPDF = "C:\\jasper-report\\";

    public void gerarRelatorio(Aluno aluno) throws IOException {

        byte[] imagebg = this.loadimage(IMAGEBG);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("nome", aluno.getNome());
        parameters.put("curso", aluno.getCurso());
        parameters.put("cargaHoraria", aluno.getCargaHoraria());
        parameters.put("dataInicio", aluno.getDataInicio());
        parameters.put("dataFim", aluno.getDataFim());
        parameters.put("imageJasper", imagebg);

        String pathAbsolute = getAbsolutePath();

        try {
            String folderDiretorio = getDiretorioSave("certificados-salvos");
            JasperReport report = JasperCompileManager.compileReport(pathAbsolute);
            LOGGER.info("report compilado: {} ", pathAbsolute);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
            LOGGER.info("jasper print");
            JasperExportManager.exportReportToPdfFile(print, folderDiretorio);
            LOGGER.info("PDF EXPORTADO PARA: {}", folderDiretorio);

        } catch (Exception e) {
            LOGGER.error("Erro ao compilar o relatório: " + e.getMessage());
        }
    }

    private byte[] loadimage(String imagebg) throws IOException {
        String image = ResourceUtils.getFile(imagebg).getAbsolutePath();
        File file = new File(image);
        try (InputStream inputStream = new FileInputStream(file)) {
            return IOUtils.toByteArray(inputStream);
        }
    }

    private String getDiretorioSave(String folderName) {
        this.createDiretorio(folderName);
        return DESTINOPDF + folderName.concat(".pdf");
    }

    private void createDiretorio(String folderName) {
        File diretorio = new File(DESTINOPDF);
        if (!diretorio.exists()) {
            diretorio.mkdir();
        }
    }

    public String getAbsolutePath() throws FileNotFoundException {
        return ResourceUtils.getFile(CERTIFICADOS + ARQUIVOJRXML).getAbsolutePath();
    }

}
