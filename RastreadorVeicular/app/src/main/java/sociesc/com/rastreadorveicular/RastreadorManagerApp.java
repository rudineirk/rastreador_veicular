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

    //Altere para o IP de onde o servidor está rodando
    //public static final String URL_API = "http://10.8.133.220:9090/api/v1";
    //public static final String URL_API = "http://192.168.1.4:9090/api/v1";
   // public static final String URL_API = "http://192.168.43.247:9090/api/v1";
    public static final String URL_API = "http://192.168.1.5:9090/api/v1";

    @Override
    public void onCreate() {
        super.onCreate();
        dbRastreadores = new ArrayList<DataTracker>();//cria a lista de rastreadores
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
