package co.edu.uniquindio.alert360_BACK.security.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MapboxResponse {
    private List<Feature> features;

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Feature {
        private List<Double> center;

        public List<Double> getCenter() {
            return center;
        }

        public void setCenter(List<Double> center) {
            this.center = center;
        }
    }
}