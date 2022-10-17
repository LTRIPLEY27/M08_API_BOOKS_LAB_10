package isabelcalzadilla.ioc.whowroteit;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

// CLASE QUE IMPLEMENTARÁ LA SUBCLASE ASYNC PARA EL HILO DE LA CONEXIÓN
public class FetchBook extends AsyncTask <String, Void, String> {

    // ENLACES DE ACCESO A LAS TEXVIEWS A MOSTRAR, CON 'WEAKREFERENCES' PARA OPTIMIZAR LA MEMORIA
    private WeakReference<TextView> mTitleText;
    private WeakReference<TextView> mAuthorText;

    // constructor
    public FetchBook(TextView titleText, TextView authorText) {
        this.mTitleText = new WeakReference<>(titleText);
        this.mAuthorText = new WeakReference<>(authorText);
    }

    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getBookInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        // ADICIÓN DEL BLOQUE TRY/CATCH
        try{
            // PARSEA A JSON LA STRING RECIBIDA
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            // VARIABLES QUE USARÁ EL PARSING LOOP

            int i = 0;
            String title = null;
            String authors = null;

            //******* CONDICIONAL DEL WHILE PARA QUE VAYA IMPRIMIENDO LOS RESULTADOS
            while (i < itemsArray.length() && (authors == null && title == null)) {

                // DECLARACIÓN DE NUEVAS ENIDADES JSONOBJECT
                // 'book' ---> RECOGE UNA ARRAY DE 'items' PARA IMPRIMIR CADA RESPUESTA
                JSONObject book = itemsArray.getJSONObject(i);
                // 'volumnInfo' --< JSONOBJECT que recoge a su vez cada 'volumeInfo' DEL ANTERIOR JSONOBJECT 'book' PAA IR RECORRIDENDO ENTIDAS Y SUS ATRIBUTOS UNO A UNO
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                // OBTENCIÓN DE CADA RESULTADO 'volumeInfo' DEL 'title' y 'authors' DENTRO DEL TRY PARA LLAMAR A EXCEPCIÓN EN CASO DE NO POSEER
                try {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                i++;
            }

            // CONDICIONAL PARA LLAMAR A RESULTADO EN CASO DE NO TENER 'title u 'author'
            // SETTEA EL VALOR DE <WeakReference>
            if (title == null && authors == null) {
                mTitleText.get().setText(R.string.no_results);
                mAuthorText.get().setText("");
            }

            mTitleText.get().setText(title);
            mAuthorText.get().setText(authors);

    } catch (JSONException e) {
            mTitleText.get().setText(R.string.no_results);
            mAuthorText.get().setText("");
            e.printStackTrace();
        }
    }
}
