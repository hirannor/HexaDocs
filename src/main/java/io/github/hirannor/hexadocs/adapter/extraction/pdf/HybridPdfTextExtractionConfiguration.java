package io.github.hirannor.hexadocs.adapter.extraction.pdf;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.extraction",
        havingValue = "hybrid-pdf-text-extractor"
)
@EnableConfigurationProperties({TesseractProperties.class})
public class HybridPdfTextExtractionConfiguration {

    private final TesseractProperties config;

    public HybridPdfTextExtractionConfiguration(final TesseractProperties config) {
        this.config = config;
    }

    @Bean
    public ITesseract createTesseract() {

        final String dataPath = resolveDataPath();

        final Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(dataPath);
        tesseract.setLanguage(config.getLanguage());

        tesseract.setOcrEngineMode(config.getOcr().getEngineMode());
        tesseract.setPageSegMode(config.getOcr().getPageSegMode());

        return tesseract;
    }

    private String resolveDataPath() {
        final String env = System.getenv("TESSDATA_PREFIX");

        if (env != null && !env.isBlank()) {
            return normalize(env);
        }

        if (config.getDatapath() != null && !config.getDatapath().isBlank()) {
            return normalize(config.getDatapath());
        }

        throw new IllegalStateException("Tesseract datapath not configured");
    }

    private String normalize(final String path) {
        return path.endsWith("/") || path.endsWith("\\")
                ? path
                : path + "/";
    }
}
