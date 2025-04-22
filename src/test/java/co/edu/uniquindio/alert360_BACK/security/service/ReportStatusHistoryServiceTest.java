package co.edu.uniquindio.alert360_BACK.security.service;

import co.edu.uniquindio.alert360_BACK.security.entity.ReportStatusHistory;
import co.edu.uniquindio.alert360_BACK.security.enums.StatusEnum;
import co.edu.uniquindio.alert360_BACK.security.repository.ReportStatusHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReportStatusHistoryServiceTest {

    @Autowired
    private ReportStatusHistoryService reportStatusHistoryService;

    @Autowired
    private ReportStatusHistoryRepository reportStatusHistoryRepository;

    @BeforeEach
    public void setUp() {
        // Limpia el repositorio antes de cada test
        reportStatusHistoryRepository.deleteAll();
    }

    @Test
    public void saveReportStatusHistoryTest() {
        // Configura los datos de prueba
        ReportStatusHistory history = new ReportStatusHistory(
                1,
                StatusEnum.PENDING,
                StatusEnum.RESOLVED,
                LocalDateTime.now()
        );

        // Llama al método que deseas probar
        ReportStatusHistory savedHistory = reportStatusHistoryService.save(history);

        // Verifica los resultados
        assertNotNull(savedHistory);
        assertEquals(1, savedHistory.getReportId());
        assertEquals(StatusEnum.PENDING, savedHistory.getPreviousStatus());
        assertEquals(StatusEnum.RESOLVED, savedHistory.getNewStatus());
    }

    @Test
    public void getStatusHistoryByReportIdTest() {
        // Configura los datos de prueba
        ReportStatusHistory history1 = new ReportStatusHistory(
                2,
                StatusEnum.PENDING,
                StatusEnum.RESOLVED,
                LocalDateTime.now()
        );
        ReportStatusHistory history2 = new ReportStatusHistory(
                2,
                StatusEnum.PENDING,
                StatusEnum.RESOLVED,
                LocalDateTime.now()
        );
        reportStatusHistoryRepository.saveAll(List.of(history1, history2));

        // Llama al método que deseas probar
        List<ReportStatusHistory> historyList = reportStatusHistoryService.getStatusHistoryByReportId(2);

        // Verifica los resultados
        assertNotNull(historyList);
        assertEquals(2, historyList.size());
        assertEquals(StatusEnum.PENDING, historyList.get(0).getPreviousStatus());
        assertEquals(StatusEnum.RESOLVED, historyList.get(1).getNewStatus());
    }
}