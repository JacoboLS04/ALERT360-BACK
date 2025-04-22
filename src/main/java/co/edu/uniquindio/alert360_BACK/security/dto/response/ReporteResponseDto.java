package co.edu.uniquindio.alert360_BACK.security.dto.response;

import co.edu.uniquindio.alert360_BACK.security.dto.CategoryDto;
import co.edu.uniquindio.alert360_BACK.security.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReporteResponseDto {

    private int id;
    private String title;
    private List<CategoryDto> categories;
    private String description;
    private String location;
    private List<String> images;
    private LocalDateTime date;
    private Integer userId;
    private StatusEnum status;
    private Integer importanceCount;

}
