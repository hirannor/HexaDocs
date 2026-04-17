package io.github.hirannor.hexadocs.adapter.extraction.pdf;

import jakarta.annotation.PostConstruct;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Component;

import java.nio.file.FileSystems;

@Component
public class TesseractFactory {

    private final TesseractProperties properties;

    private ITesseract tesseract;

    public TesseractFactory(final TesseractProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        final String datapath = resolveDataPath();

        if (datapath == null || datapath.isBlank()) {
            throw new IllegalStateException(
                    "Tesseract datapath not configured. Set TESSDATA_PREFIX env variable."
            );
        }

        final Tesseract t = new Tesseract();
        t.setDatapath(datapath);
        t.setLanguage(properties.getLanguage());

        t.setOcrEngineMode(properties.getOcr().getEngineMode());
        t.setPageSegMode(properties.getOcr().getPageSegMode());

        this.tesseract = t;
    }

    private String resolveDataPath() {
        final String env = System.getenv("TESSDATA_PREFIX");
        if (env != null && !env.isBlank()) {
            return normalize(env);
        }

        if (properties.getDatapath() != null && !properties.getDatapath().isBlank()) {
            return normalize(properties.getDatapath());
        }

        return null;
    }

    private String normalize(final String path) {
        return path.endsWith("/") || path.endsWith("\\")
                ? path
                : path + FileSystems.getDefault().getSeparator();
    }

    public ITesseract get() {
        return tesseract;
    }
}