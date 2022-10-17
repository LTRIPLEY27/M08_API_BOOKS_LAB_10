package isabelcalzadilla.ioc.whowroteit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;

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

        // LLAMADO A OCULTAR EL KEYBOARD (uso del 'Context')
        InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // VERIFICACIÍON DE LA VARIABLE
        if(input != null){
            // MÉTODO QUE OCULTA EL KEYBOARD
            input.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        // LLAMADO AL TASK EN SEGUNDO PLANO
        new FetchBook(mTitleText, mAuthorText).execute(queryString);
        clean();
    }

    //MÉTODO QUE LIMPIA EL TECLADO E INDICA QUE ESTÁ EN 'CARGA'
    void clean(){
        mBookInput.setText("");
        mAuthorText.setText("");
        mTitleText.setText(R.string.loading);
    }
}