#include <jni.h>
#include <android/bitmap.h>
#include <opencv2/opencv.hpp>
#include <android/log.h>

extern "C" JNIEXPORT void JNICALL
Java_ups_edu_proyecto_1integrador_MainActivity_applyCartoonEffectNative(JNIEnv *env, jobject, jobject bitmap) {
    AndroidBitmapInfo info;
    void *pixels;

    // Verificar la información del bitmap
    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) {
        __android_log_print(ANDROID_LOG_ERROR, "CartoonFilter", "Error al obtener la información del bitmap");
        return;
    }

    // Bloquear los píxeles para modificarlos
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) {
        __android_log_print(ANDROID_LOG_ERROR, "CartoonFilter", "Error al bloquear los píxeles del bitmap");
        return;
    }

    // Convertir los píxeles de Android Bitmap a cv::Mat
    cv::Mat img(info.height, info.width, CV_8UC4, pixels);

    // Convertir la imagen a formato CV_8UC3 (BGR)
    cv::Mat imgCV8UC3;
    cv::cvtColor(img, imgCV8UC3, cv::COLOR_BGRA2BGR);

    // Convertir a escala de grises
    cv::Mat gray, edges, filtered;
    cv::cvtColor(imgCV8UC3, gray, cv::COLOR_BGR2GRAY);

    // Detectar los bordes utilizando un filtro de mediana más pequeño
    cv::medianBlur(gray, edges, 5); // Cambiado de 7 a 5

    // Ajustar los parámetros del umbral adaptativo
    cv::adaptiveThreshold(edges, edges, 255, cv::ADAPTIVE_THRESH_MEAN_C, cv::THRESH_BINARY, 7, 2); // Cambiado el bloque de 9 a 7

    // Aplicar el filtro bilateral para suavizar la imagen
    cv::bilateralFilter(imgCV8UC3, filtered, 9, 75, 75);

    // Aumentar la saturación global para colores más vivos
    cv::Mat hsv;
    cv::cvtColor(filtered, hsv, cv::COLOR_BGR2HSV);
    std::vector<cv::Mat> channels;
    cv::split(hsv, channels);

    // Incrementar saturación global
    channels[1] = channels[1] * 1.5;

    // Crear una máscara para identificar tonos de piel (en HSV)
    cv::Mat skinMask;
    cv::inRange(hsv, cv::Scalar(0, 30, 60), cv::Scalar(20, 150, 255), skinMask);

    // Incrementar saturación específicamente en áreas de piel
    cv::Mat enhancedSaturation = channels[1] * 1.8; // Saturación más intensa
    enhancedSaturation.copyTo(channels[1], skinMask); // Aplicar solo en áreas de piel (mascara)

    // Combinar canales y regresar a BGR
    cv::merge(channels, hsv);
    cv::cvtColor(hsv, filtered, cv::COLOR_HSV2BGR);

    // Aplicar el efecto "cartoon"
    cv::Mat cartoon;
    cv::bitwise_and(filtered, filtered, cartoon, edges);

    // Mejorar ligeramente la nitidez
    cv::Mat sharp;
    cv::GaussianBlur(cartoon, sharp, cv::Size(0, 0), 3);
    cv::addWeighted(cartoon, 1.5, sharp, -0.5, 0, cartoon);

    // Convertir de nuevo a formato BGRA
    cv::cvtColor(cartoon, img, cv::COLOR_BGR2BGRA);

    // Desbloquear los píxeles del bitmap después de la modificación
    AndroidBitmap_unlockPixels(env, bitmap);
}
