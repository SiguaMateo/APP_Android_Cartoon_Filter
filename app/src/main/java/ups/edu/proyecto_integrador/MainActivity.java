package ups.edu.proyecto_integrador;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String STREAM_URL = "http://192.168.18.149:81/stream";
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    static {
        // Cargar la librería nativa que contiene el filtro cartoon
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar SurfaceView y SurfaceHolder
        surfaceView = findViewById(R.id.surfaceview);
        surfaceHolder = surfaceView.getHolder();

        // Obtener y mostrar el fotograma en un hilo separado
        new Thread(() -> {
            while (true) {
                Bitmap frame = getBitmapFromMjpegStream(STREAM_URL);
                if (frame != null) {
                    // Aplicar el filtro de cartoon sobre el fotograma utilizando C++
                    applyCartoonEffectNative(frame);

                    // Dibujar el fotograma en el SurfaceView
                    if (surfaceHolder.getSurface().isValid()) {
                        Canvas canvas = surfaceHolder.lockCanvas();
                        if (canvas != null) {
                            // Obtener el tamaño del SurfaceView
                            int surfaceWidth = surfaceView.getWidth();
                            int surfaceHeight = surfaceView.getHeight();

                            // Escalar el Bitmap al tamaño del SurfaceView
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(frame, surfaceWidth, surfaceHeight, true);

                            // Dibujar el Bitmap escalado en el SurfaceView
                            canvas.drawBitmap(scaledBitmap, 0, 0, null);
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                } else {
                    Log.e(TAG, "No se pudo obtener un fotograma del stream.");
                }
            }
        }).start();
    }

    private Bitmap getBitmapFromMjpegStream(String urlString) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            Log.d(TAG, "Conectando al stream...");
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000); // Tiempo de espera para conexión
            connection.setReadTimeout(5000);    // Tiempo de espera para lectura
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            Log.d(TAG, "Código de respuesta: " + responseCode);
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "Respuesta no válida del servidor.");
                return null;
            }

            // Procesar MJPEG
            inputStream = connection.getInputStream();
            ByteArrayOutputStream jpegData = new ByteArrayOutputStream();
            boolean startFrame = false;

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    if (startFrame) {
                        jpegData.write(buffer[i]);
                    }
                    if (buffer[i] == (byte) 0xFF && buffer[(i + 1) % bytesRead] == (byte) 0xD8) { // SOI (Inicio de la immagen)
                        startFrame = true;
                        jpegData.reset();
                        jpegData.write(buffer[i]);
                    } else if (buffer[i] == (byte) 0xFF && buffer[(i + 1) % bytesRead] == (byte) 0xD9) { // EOI (Final de la imagen)
                        jpegData.write(buffer[i]);
                        startFrame = false;

                        // Decodificar el fotograma JPEG
                        byte[] jpegFrame = jpegData.toByteArray();
                        return BitmapFactory.decodeByteArray(jpegFrame, 0, jpegFrame.length);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al procesar el stream MJPEG: ", e);
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException ignored) {}
            if (connection != null) connection.disconnect();
        }
        return null;
    }

    // Método nativo para aplicar el filtro cartoon
    public native void applyCartoonEffectNative(Bitmap bitmap);
}
