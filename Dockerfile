# ===== BUILD STAGE =====
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


# ===== RUNTIME =====
FROM eclipse-temurin:21-jdk-jammy

RUN apt-get update && apt-get install -y \
    tesseract-ocr \
    tesseract-ocr-eng \
    tesseract-ocr-hun \
    libtesseract-dev \
    libleptonica-dev \
    pkg-config \
    && rm -rf /var/lib/apt/lists/*

# IMPORTANT: force consistent native linking
RUN ln -sf /usr/lib/x86_64-linux-gnu/liblept.so.5 /usr/lib/liblept.so.5 || true

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Tessdata path (correct one for Ubuntu tesseract 4.x)
ENV TESSDATA_PREFIX=/usr/share/tesseract-ocr/4.00/tessdata

# IMPORTANT: avoid JVM native caching issues
ENV LD_LIBRARY_PATH=/usr/lib/x86_64-linux-gnu:/usr/lib

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]