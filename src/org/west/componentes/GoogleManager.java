package org.west.componentes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.west.entidades.Imovel;

public class GoogleManager {

    public static String getCoordenadas(Imovel imovel) {

        String endereco = imovel.getCep().getTipo() + " " + imovel.getCep().getDescricao() + " " + String.valueOf(imovel.getNumeros().iterator().next().getNumero());

        endereco = endereco.replace(" ", "+");

        try {

            URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?address=" + endereco + "&sensor=false");
            BufferedReader leitorArquivo = new BufferedReader(new InputStreamReader(url.openStream()));
            String linhaTemp, linha = " ";

            while ((linhaTemp = leitorArquivo.readLine()) != null) {
                linha += linhaTemp;
            }

            JSONObject json = new JSONObject(linha);
            String status = json.getString("status");

            if (status.equals("OK")) {
                JSONArray results = json.getJSONArray("results");
                JSONObject location = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

                return location.getString("lat") + " - " + location.getString("lng");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
