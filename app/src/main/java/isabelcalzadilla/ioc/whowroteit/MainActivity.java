package isabelcalzadilla.ioc.whowroteit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

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
            new FetchBook(mTitleText, mAuthorText).execute(queryString);
            // TASK 3 : 3.1 -- 'Limpia la búsqueda'
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
}