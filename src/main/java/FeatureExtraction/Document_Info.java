package FeatureExtraction;

import java.util.HashMap;

public class Document_Info {
    public HashMap<String, Double> getTf() {
        return tf;
    }

    private HashMap<String, Double> tf = new HashMap<>();

    public Document_Info(String label, HashMap<String, Double> tf ) {
        this.tf = tf;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    private String label;
}
