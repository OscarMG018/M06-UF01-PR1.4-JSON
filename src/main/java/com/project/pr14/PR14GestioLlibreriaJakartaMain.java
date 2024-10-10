package com.project.pr14;

import jakarta.json.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;

import com.project.objectes.Llibre;

/**
 * Classe principal que gestiona la lectura i el processament de fitxers JSON per obtenir dades de llibres.
 */
public class PR14GestioLlibreriaJakartaMain {

    private final File dataFile;

    /**
     * Constructor de la classe PR14GestioLlibreriaJSONPMain.
     *
     * @param dataFile Fitxer on es troben els llibres.
     */
    public PR14GestioLlibreriaJakartaMain(File dataFile) {
        this.dataFile = dataFile;
    }

    public static void main(String[] args) {
        File dataFile = new File(System.getProperty("user.dir"), "data/pr14" + File.separator + "llibres_input.json");
        PR14GestioLlibreriaJakartaMain app = new PR14GestioLlibreriaJakartaMain(dataFile);
        app.processarFitxer();
    }

    /**
     * Processa el fitxer JSON per carregar, modificar, afegir, esborrar i guardar les dades dels llibres.
     */
    public void processarFitxer() {
        List<Llibre> llibres = carregarLlibres();
        if (llibres != null) {
            modificarAnyPublicacio(llibres, 1, 1995);
            afegirNouLlibre(llibres, new Llibre(4, "Històries de la ciutat", "Miquel Soler", 2022));
            esborrarLlibre(llibres, 2);
            guardarLlibres(llibres);
        }
    }

    /**
     * Carrega els llibres des del fitxer JSON.
     *
     * @return Llista de llibres o null si hi ha hagut un error en la lectura.
     */
    public List<Llibre> carregarLlibres() {
        FileReader reader = null;
        JsonReader jsonReader = null;
        try {
            reader = new FileReader(dataFile);
            jsonReader = Json.createReader(reader);
            JsonArray jsonArray = jsonReader.readArray();
            List<Llibre> llibres = new ArrayList<>();
            for (JsonValue jsonValue : jsonArray) {
                JsonObject jsonObject = (JsonObject) jsonValue;
                int id = jsonObject.getInt("id");
                String titol = jsonObject.getString("titol");
                String autor = jsonObject.getString("autor");
                int any = jsonObject.getInt("any");
                llibres.add(new Llibre(id, titol, autor, any));
            }
            return llibres;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (reader != null)
                    reader.close();
                if (jsonReader != null)
                    jsonReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Modifica l'any de publicació d'un llibre amb un id específic.
     *
     * @param llibres Llista de llibres.
     * @param id Identificador del llibre a modificar.
     * @param nouAny Nou any de publicació.
     */
    public void modificarAnyPublicacio(List<Llibre> llibres, int id, int nouAny) {
        for (Llibre llibre : llibres) {
            if (llibre.getId() == id) {
                llibre.setAny(nouAny);
                break;
            }
        }
    }

    /**
     * Afegeix un nou llibre a la llista de llibres.
     *
     * @param llibres Llista de llibres.
     * @param nouLlibre Nou llibre a afegir.
     */
    public void afegirNouLlibre(List<Llibre> llibres, Llibre nouLlibre) {
        llibres.add(nouLlibre);
    }

    /**
     * Esborra un llibre amb un id específic de la llista de llibres.
     *
     * @param llibres Llista de llibres.
     * @param id Identificador del llibre a esborrar.
     */
    public void esborrarLlibre(List<Llibre> llibres, int id) {
        for (Llibre llibre : llibres) {
            if (llibre.getId() == id) {
                llibres.remove(llibre);
                break;
            }
        }
    }

    /**
     * Guarda la llista de llibres en un fitxer nou.
     *
     * @param llibres Llista de llibres a guardar.
     */
    public void guardarLlibres(List<Llibre> llibres) {
        File outputFile = new File(dataFile.getParent(),"llibres_output_jakarta.json");
        FileWriter writer = null;
        JsonWriter jsonWriter = null;
        try {
            writer = new FileWriter(outputFile);
            jsonWriter = Json.createWriter(writer);
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for (Llibre llibre : llibres) {
                jsonArrayBuilder.add(LlibreToJsonObject(llibre));
            }
            JsonArray jsonArray = jsonArrayBuilder.build();
            jsonWriter.writeArray(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (writer != null)
                    writer.close();
                if (jsonWriter != null)
                    jsonWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public JsonObject LlibreToJsonObject(Llibre llibre) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("id", llibre.getId());
        jsonObjectBuilder.add("titol", llibre.getTitol());
        jsonObjectBuilder.add("autor", llibre.getAutor());
        jsonObjectBuilder.add("any", llibre.getAny());
        return jsonObjectBuilder.build();
    }
}