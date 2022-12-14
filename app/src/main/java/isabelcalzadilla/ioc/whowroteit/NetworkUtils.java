package isabelcalzadilla.ioc.whowroteit;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//    *******************  TASK 2.4 : creación de la clase NETWORK, para construir la conexión   *******************************************
//CLASE CONEXIÓN A LA API CON QUERY
public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    // Base URL for Books API.
    private static final String BOOK_BASE_URL =  "https://www.googleapis.com/books/v1/volumes?";
    // Parameter for the search string.
    private static final String QUERY_PARAM = "q";
    // Parameter that limits search results.
    private static final String MAX_RESULTS = "maxResults";
    // Parameter to filter by print type.
    private static final String PRINT_TYPE = "printType";

    //    *******************    CHALLENGES      *******************************************
    private static final String EPUB_PARAM = "download";

    //    *******************         TASK 2.4.3 : CREACIÓN DEL MÉTODO ESTÁTICO 'GETINFO'       *******************************************
    //MÉTODO PARA OBTENER LA INFORMACIÓN DEL LIBRO A TRAVÉS DE LA API
    static String getBookInfo(String queryString, boolean epub){
        // VARIABLES DE MÉTODO PARA EJECUTAR LA CONEXIÓN
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        try{
            // CHALLENGES
            Uri builtURI;
            // condicional para verificar si devuelve solo los datos que contengan la constante 'epub' o caso contrario
            if(epub){
                // CONSTRUCCIÍN DE LA CONSULTA URI CON EL 'BUILDER'
                builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, queryString)
                        .appendQueryParameter(MAX_RESULTS, "1")
                        .appendQueryParameter(EPUB_PARAM, "epub")
                        .build();
            } else {
                // CONSTRUCCIÍN DE LA CONSULTA URI CON EL 'BUILDER'
                builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, queryString)
                        .appendQueryParameter(MAX_RESULTS, "10")
                        .appendQueryParameter(PRINT_TYPE, "books")
                        .build();
            }
            // TRNASFORMACIÓN DE LA CONSULTA URI A OBJETO URL
            URL requestURL = new URL(builtURI.toString());

            // APERTURA DE LA CONEXIÓN CON REQUEST


            //    *******************         TASK 2.5:REALIZANDO LA CONEXIÓN      *******************************************
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            // TIPO DE REQUEST
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // obteniendo y setteando la respuesta con los formatos
            InputStream inputStream = urlConnection.getInputStream();
            // DECLARACIÓN DEL BUFFERED CON EL VALOR DEL INPUTSTREAM PREVIO
            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();

            String line;
            // DECLARACIÓN Y PARSEO A FORMATO JSON lÍNEA a Linea
            while((line = reader.readLine()) != null){
                builder.append(line);
                builder.append("\n");
            }
            if(builder.length() == 0){
                return null;
            }
            bookJSONString = builder.toString();
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            //CIERRE DE LAS CONEXIONES
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // VERIFICACIÓN DESDE EL 'LOG' DE LA RESPUESTA Y FORMATO
        Log.d(LOG_TAG, bookJSONString);
        return bookJSONString;
    }
}
