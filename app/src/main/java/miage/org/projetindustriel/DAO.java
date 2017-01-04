package miage.org.projetindustriel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Arthur on 30/11/2016.
 */

public class DAO {

    public static String post(String adress, List<String> keys, List<String> values) throws IOException {
        String result = "";
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        try {
            //encodage des paramètres de la requête
            String data="";
            for(int i=0;i<keys.size();i++){
                if (i!=0) data += "&";
                data += URLEncoder.encode(keys.get(i), "UTF-8")+"="+ URLEncoder.encode(values.get(i), "UTF-8");
            }
            //création de la connection
            URL url = new URL(adress);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            //envoi de la requête
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();

            //lecture de la réponse
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                result+=ligne;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            try{writer.close();}catch(Exception e){}
            try{reader.close();}catch(Exception e){}
        }
        return result;
    }

    public static ArrayList<HashMap<String,String>> parseResponse(String response){
        String[] listResponse = response.split(",");
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        for(String couple : listResponse){
            HashMap<String,String> hm = new HashMap<>();
            String[] coupleSplited = couple.split(":");
            hm.put(coupleSplited[0],coupleSplited[1]);
            list.add(hm);
        }
        return list;
    }
}
