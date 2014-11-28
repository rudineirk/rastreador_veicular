package sociesc.com.rastreadorveicular;

import android.app.Application;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guilherme on 10/31/14.
 */
public class RastreadorManagerApp extends Application{

    private static List<DataTracker> dbRastreadores;

    @Override
    public void onCreate() {
        super.onCreate();
        dbRastreadores = new ArrayList<DataTracker>();
    }

    public static final List<DataTracker> getRastreadores() {
        return new ArrayList<DataTracker>(dbRastreadores);
    }

    public static final void setRastreadores(List<DataTracker> rastreadores){
        dbRastreadores = new ArrayList<DataTracker>(rastreadores);
    }

    public static final DataTracker getRastreadorByPosition(int position){
        return dbRastreadores.get(position);
    }

    public static final DataTracker getRastreadorById(Long id){
        for(DataTracker rastreador : dbRastreadores){
            if(rastreador.id.equals(id)){
                return rastreador;
            }
        }

        return null;
    }
}
