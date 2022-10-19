package isabelcalzadilla.ioc.whowroteit;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import org.jetbrains.annotations.Nullable;

// *********************        TASK 4 : 4.1 --> Crear clase AsycTaskLoader     ****************************************
public class BookLoader extends AsyncTaskLoader<String> {

    //4.2.3 VARIABLES DE CLASE
    private String mQueryString;

    // CHALLENGE
    private boolean epub;

    // CONSTRUCTOR
    public BookLoader(Context context, String query, boolean epub) {
        super(context);
        this.mQueryString = query;
        this.epub = epub;
    }

    //4.2.4 LLAMADO AL 'NetworkUtils'
    @Nullable
    @Override
    public String loadInBackground() {
        // RECIBE LA QUERYSTRING DEFINIDA EN LA ACTIVITYMAIN
        return NetworkUtils.getBookInfo(mQueryString, epub);
    }

    // TASK 4 : 4.2 --> mÉTODOS NECESARIOS
    @Override
    protected void onStartLoading() {
        forceLoad();
        super.onStartLoading();
    }
}
