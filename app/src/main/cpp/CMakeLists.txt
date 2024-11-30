cmake_minimum_required(VERSION 3.22.1)
project("proyecto_integrador")

# Configurar OpenCV
set(OpenCV_STATIC on)
set(OpenCV_DIR /home/mateo/opencv/opencvi/OpenCV-android-sdk/sdk/native/jni)
find_package(OpenCV REQUIRED)
include_directories(/home/mateo/opencv/opencvi/OpenCV-android-sdk/sdk/native/jni/include)

# Añadir librería nativa
add_library(native-lib SHARED native-lib.cpp)

find_library(log-lib log)
find_library(android-lib android)

target_link_libraries(native-lib ${log-lib} android jnigraphics opencv_core opencv_highgui opencv_imgcodecs opencv_imgproc opencv_video opencv_videoio opencv_objdetect)

target_link_libraries(native-lib
        ${log-lib}
        ${android-lib}
        ${OpenCV_LIBS}
)
