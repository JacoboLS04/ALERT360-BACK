package co.edu.uniquindio.alert360_BACK.security.utils;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MapboxUtil {
    private static final String MAPBOX_API_URL = "https://api.mapbox.com/geocoding/v5/mapbox.places/";
    private static final String ACCESS_TOKEN = "pk.eyJ1Ijoia3ZnYWpsIiwiYSI6ImNtOTgyN2dxMDBkdmUyd3BscDRzc3E3YjcifQ.5bW1_TXAnX8uXue-s6s0Ig";

    public static Map<String, Double> getCoordinates(String address) {
        try {
            // Codificar la dirección manualmente
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());

            // Construir la URL correctamente
            String url = UriComponentsBuilder.fromHttpUrl(MAPBOX_API_URL + encodedAddress + ".json")
                    .queryParam("access_token", ACCESS_TOKEN)
                    .queryParam("limit", 1)
                    .build()
                    .toUriString();

            // Imprimir la URL para depuración
            System.out.println("Mapbox URL: " + url);

            // Realizar la solicitud HTTP
            RestTemplate restTemplate = new RestTemplate();
            MapboxResponse response = restTemplate.getForObject(url, MapboxResponse.class);

            // Validar la respuesta
            if (response != null && response.getFeatures() != null && !response.getFeatures().isEmpty()) {
                Map<String, Double> coordinates = new HashMap<>();
                coordinates.put("latitude", response.getFeatures().get(0).getCenter().get(1));
                coordinates.put("longitude", response.getFeatures().get(0).getCenter().get(0));
                return coordinates;
            } else {
                throw new RuntimeException("No se encontraron coordenadas para la dirección: " + address);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener coordenadas de Mapbox: " + e.getMessage(), e);
        }
    }
}