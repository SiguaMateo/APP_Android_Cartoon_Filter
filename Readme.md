# Proyecto Integrador de Visión por Computador

# Aplicación Android

<video controls>
  <source src="filter_cartoon.mp4" type="video/mp4">
  Tu navegador no soporta la reproducción de videos.
</video>

## Introducción

El presente proyecto integrador pone a prueba nuestra habilidad y destreza en el procesamiento de imágenes digitales, explorando conceptos fundamentales como la manipulación de píxeles, matrices, y la implementación de operaciones morfológicas y técnicas de mejora de imágenes. Como desafío principal, desarrollamos un filtro *cartoon* (caricatura) que transforma imágenes en tiempo real capturadas desde una cámara ESP32-CAM. Este filtro utiliza técnicas avanzadas como detección de bordes, reducción de ruido y ajuste de saturación para lograr un efecto visual atractivo y estilizado.

Además, este proyecto incluye la creación de una aplicación Android diseñada para recibir el *streaming* de video en tiempo real desde la cámara ESP32-CAM. La aplicación no solo permite visualizar el video en vivo, sino que aplica el filtro *cartoon* directamente sobre el video procesado en tiempo real. Esto permite al usuario observar los resultados de manera inmediata. Este enfoque integra tecnologías de visión por computador, programación en Android y OpenCV, demostrando nuestra capacidad de implementar soluciones innovadoras que conectan hardware y software para resolver problemas prácticos de manera eficiente.

## Tecnologías Utilizadas

Para el desarrollo del proyecto, se emplearon las siguientes tecnologías:

1. **OpenCV:** Librería para el procesamiento de imágenes y video.

![OpenCV](https://upload.wikimedia.org/wikipedia/commons/thumb/3/32/OpenCV_Logo_with_text_svg_version.svg/800px-OpenCV_Logo_with_text_svg_version.svg.png)

2. **C++:** Lenguaje utilizado para implementar el procesamiento del video en tiempo real.

![C++](https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcSFQlJS-SEBXODRztLIs5VW-RL3U1IFXIH7e5Np9NrKuVYzqDRiEhUYQ2QvJz7NahmsuG4zokC-95MgmUVNmbVn0bzd4lGfrN5qOHIMeg)

3. **Android Studio:** Entorno de desarrollo para la creación de la aplicación móvil.

![Android Studio](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQuttp_isCbO4DFr4jH61d8BTt9CM1gBs6EXA&s)

4. **Arduino:** Plataforma empleada para programar la cámara ESP32-CAM.

![IDE Arduino](https://images.sftcdn.net/images/t_app-icon-m/p/222e795e-f251-4cff-8eed-2721443b569c/2420662603/arduino-ide-logo.png)

5. **Módulo ESP32-CAM:** Hardware utilizado como fuente del *streaming* de video.

![ESP32-CAM](https://circuitpython.org/assets/images/boards/large/ai-thinker-esp32-cam.jpg)

## Desarrollo

#### Despliegue de la Cámara ESP32-CAM

El desarrollo comenzó con la configuración del módulo ESP32-CAM utilizando el IDE de Arduino. Se utilizó un ejemplo proporcionado por el propio IDE que permite configurar la red Wi-Fi a la cual se conecta la cámara. Este programa genera una dirección IP que permite acceder a la transmisión de video en tiempo real. Además, la cámara proporciona un *endpoint* específico para trabajar directamente con la imagen del video:  
`http://<IP>:81/stream`.

Desde esta dirección, es posible ajustar parámetros como la resolución, calidad de imagen, brillo, contraste, entre otros, optimizando la transmisión según las necesidades del proyecto.

#### Configuración de la Aplicación Android

El desarrollo de la aplicación Android implicó varios pasos fundamentales:

1. **Diseño de la interfaz de usuario:**  
   Se diseñó una interfaz intuitiva y funcional que permite visualizar el video en tiempo real y aplicar el filtro *cartoon*. Se priorizó la simplicidad para mejorar la experiencia del usuario.

2. **Implementación del *streaming* de video:**  
   Utilizando la librería `java.net`, se realizó la conexión con la cámara ESP32-CAM mediante la IP generada y su *endpoint*. Esta etapa permitió obtener el flujo de video continuo para su procesamiento.

3. **Procesamiento del video como `Bitmap`:**  
   El video transmitido fue procesado y convertido a objetos `Bitmap`, lo que facilitó su manipulación en Android. Para manejar el flujo de datos en tiempo real, se implementaron hilos dedicados para evitar desbordes o bloqueos en la aplicación.

4. **Procesamiento de la imagen en C++:**  
   El procesamiento del video se llevó a cabo en código nativo utilizando C++ a través del NDK de Android. Cada *frame* del video fue transformado en una matriz donde se aplicaron los respectivos filtros, como detección de bordes, suavizado mediante un filtro bilateral y ajuste de saturación.

5. **Pruebas y despliegue:**  
   La aplicación final se instaló en un dispositivo móvil Android, donde se realizaron pruebas para garantizar que tanto el *streaming* como el filtro funcionaran de manera óptima.

6. **Resultados en tiempo real:**  
   La aplicación permite visualizar los resultados del filtro *cartoon* en tiempo real sobre el video transmitido por la cámara, destacando los efectos estilizados en cada *frame*.

## Conclusiones

Este proyecto integrador permitió reforzar conceptos fundamentales de visión por computador, como el manejo de píxeles, operaciones sobre matrices y técnicas de procesamiento de imágenes. Además, nos brindó la oportunidad de integrar diferentes tecnologías, desde la configuración de hardware hasta el desarrollo de aplicaciones móviles con procesamiento avanzado de video.

La implementación del filtro *cartoon* demostró cómo es posible transformar videos en tiempo real para crear efectos estilizados, lo que podría ser aplicado en otras áreas como edición de video, entretenimiento, o herramientas creativas. Finalmente, el uso de la cámara ESP32-CAM y su integración con Android muestra el potencial de estos dispositivos para crear soluciones innovadoras, accesibles y eficientes.