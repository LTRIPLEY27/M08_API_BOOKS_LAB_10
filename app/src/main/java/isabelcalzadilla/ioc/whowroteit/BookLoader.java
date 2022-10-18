package isabelcalzadilla.ioc.whowroteit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;


// TASK 4 : 4.1 --> Crear clase AsycTaskLoader
public class BookLoader extends AsyncTaskLoader<String> {
    public BookLoader(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return null;
    }
}
