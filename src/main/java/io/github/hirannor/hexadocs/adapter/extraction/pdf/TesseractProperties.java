package io.github.hirannor.hexadocs.adapter.extraction.pdf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tesseract")
public class TesseractProperties {

    private String datapath;
    private String language = "eng+hun";

    private Ocr ocr = new Ocr();

    public static class Ocr {
        private int dpi = 300;
        private int pageSegMode = 1;
        private int engineMode = 1;

        public int getDpi() { return dpi; }

        public void setDpi(final int dpi) { this.dpi = dpi; }

        public int getPageSegMode() { return pageSegMode; }

        public void setPageSegMode(final int pageSegMode) { this.pageSegMode = pageSegMode; }

        public int getEngineMode() { return engineMode; }

        public void setEngineMode(final int engineMode) { this.engineMode = engineMode; }
    }

    public String getDatapath() { return datapath; }

    public void setDatapath(final String datapath) { this.datapath = datapath; }

    public String getLanguage() { return language; }

    public void setLanguage(final String language) { this.language = language; }

    public Ocr getOcr() { return ocr; }

    public void setOcr(final Ocr ocr) { this.ocr = ocr; }
}