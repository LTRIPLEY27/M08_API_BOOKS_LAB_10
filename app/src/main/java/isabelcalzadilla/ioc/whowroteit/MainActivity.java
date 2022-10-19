package isabelcalzadilla.ioc.whowroteit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// TASK 4.3 : Modificación del MainActivity con LoaderManager
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    // VARIABLES PARA USER INPUT
    private EditText mBookInput;
    private TextView mTitleText;
    private TextView mAuthorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // VARIABLES INICIALIZADAS DESDE EL 'ONCREATE'

        mBookInput = (EditText)findViewById(R.id.bookInput);
        mTitleText = (TextView)findViewById(R.id.titleText);
        mAuthorText = (TextView)findViewById(R.id.authorText);

        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }

    }


    // método implementado desde el button con el 'OnClick' atributo
    public void searchBooks(View view) {
        String queryString = mBookInput.getText().toString();

        // TASK 3 : 3.1 -- 'Esconde el Teclado'
        // LLAMADO A OCULTAR EL KEYBOARD (uso del 'Context')
        InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // VERIFICACIÍON DE LA VARIABLE
        if(input != null){
            // MÉTODO QUE OCULTA EL KEYBOARD
            input.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // TASK 3 : 3.2 -- 'Manejo de la conexión'
        ConnectivityManager managerConect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //***************************** CHANGES THE 'NetworkInfo' to 'Network'
        NetworkInfo infoRed = null;

        if(managerConect != null) {
            infoRed = managerConect.getActiveNetworkInfo();
        }

        // CONDICIONALES PARA VALIDAR : CONEXIÓN, QUERY,
        if (infoRed != null && infoRed.isConnected() && queryString.length() != 0) {
            // LLAMADO AL TASK EN SEGUNDO PLANO
            //new FetchBook(mTitleText, mAuthorText).execute(queryString);

            // TASK 4.3.3 : DECLARACIÓN DEL BUNDLE PARA EL LOADER
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);

            clean(0);
        }else{
            if(queryString.length() == 0){
                clean(1);
            } else{
                clean(2);
            }
        }
    }

    // TASK 3 : 3.1 -- 'Limpia la búsqueda'
    //MÉTODO QUE LIMPIA EL TECLADO E INDICA QUE ESTÁ EN 'CARGA'
    void clean(int option){

        switch (option){
            case 0 :
                mBookInput.setText("");
                mAuthorText.setText("");
                mTitleText.setText(R.string.loading);
                break;
            case 1 :
                mBookInput.setText("");
                mAuthorText.setText("");
                mTitleText.setText(R.string.no_search_term);
                break;
            case 2 :
                mBookInput.setText("");
                mAuthorText.setText("");
                mTitleText.setText(R.string.no_network);
                break;
        }
    }

    // TASK 4.3.2 : Métodos de la Inteface 'loadermanager'
    // 'onCreateLoader' : LLAMADO PARA INSTANCIAR EL LOADER
    // TASK 4.4.1 : IMPLEMENTACIÓN DE CALLBACKS
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";

        if(args != null){
            queryString = args.getString("queryString");
        }
        return new BookLoader(this, queryString);
    }

    // 'onLoadFinished' : LLAMADO AL FINALIZAR LA TAREA EN SEGUNDO PLANO
    // TASK 4.4.2 : IMPLEMENTACIÓN DE DEL MÉTODO ONPOSTEXECUTE DE FETCHBOOK
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            // Convert the response into a JSON object.
            JSONObject jsonObject = new JSONObject(data);
            // Get the JSONArray of book items.
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            // Initialize iterator and results fields.
            int i = 0;
            String title = null;
            String authors = null;

            // Look for results in the items array, exiting when both the
            // title and author are found or when all items have been checked.
            while (i < itemsArray.length() &&
                    (authors == null && title == null)) {
                // Get the current item information.
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            // If both are found, display the result.
            if (title != null && authors != null) {
                mTitleText.setText(title);
                mAuthorText.setText(authors);
                //mBookInput.setText("");
            } else {
                // If none are found, update the UI to show failed results.
                mTitleText.setText(R.string.no_results);
                mAuthorText.setText("");
            }

        } catch (Exception e) {
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            mTitleText.setText(R.string.no_results);
            mAuthorText.setText("");
            e.printStackTrace();
        }
    }
    // 'onLoaderReset' : LLAMADO para limpiar remanentes
    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}