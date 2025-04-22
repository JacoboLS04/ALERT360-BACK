package co.edu.uniquindio.alert360_BACK.security.utils;

import co.edu.uniquindio.alert360_BACK.security.dto.response.ReporteResponseDto;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.tokens.DocumentEndToken;

import java.io.FileNotFoundException;
import java.util.List;

@Component
public class PdfGenerator {

    public void generateReportPdf(List<ReporteResponseDto>reports, String filePath) throws FileNotFoundException {

            PdfWriter pdfWriter = new PdfWriter(filePath);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            Document document = new Document(pdfDocument);

            document.add(new Paragraph("Alert report").setBold().setFontSize(18));

            for (ReporteResponseDto report : reports) {
                document.add(new Paragraph("ID: " + report.getId()));
                document.add(new Paragraph("Título: " + report.getTitle()));
                document.add(new Paragraph("Descripción: " + report.getDescription()));
                document.add(new Paragraph("Ubicación: " + report.getLocation()));
                document.add(new Paragraph("Fecha: " + report.getDate().toString()));
                document.add(new Paragraph("Estado: " + report.getStatus().toString()));
                document.add(new Paragraph("Usuario ID: " + report.getUserId()));
                document.add(new Paragraph("--------------------------------------------------"));
            }

            document.close();

    }



}
