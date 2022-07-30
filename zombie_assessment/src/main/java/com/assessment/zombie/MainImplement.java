package com.assessment.zombie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;


public class MainImplement {
    List<String> arrayStr = new ArrayList<>();
    static JSONArray movesOfZombie = new JSONArray();

    static JSONArray zombieJsonArr = new JSONArray();
    static JSONObject zombieJsonObj = new JSONObject();

    static JSONArray creatureJsonArr = new JSONArray();
    static JSONObject creatureJsonObj = new JSONObject();
    static int dimension = 0;
    public static void main(String[] args) throws JSONException {
        MainImplement mainImplement = new MainImplement();
        mainImplement.readFile();

        // dimension of Grid
        dimension = Integer.parseInt(mainImplement.arrayStr.get(0));
        //Get initial position of Zombie from input sile
        String zombieText = mainImplement.arrayStr.get(1).replaceAll("\\(", "").replaceAll("\\)", "");
        mainImplement.arrayStr.set(1, zombieText);
        zombieJsonObj.put("x", Integer.parseInt(mainImplement.arrayStr.get(1).split(",")[0]));
        zombieJsonObj.put("y", Integer.parseInt(mainImplement.arrayStr.get(1).split(",")[1]));
        zombieJsonArr.put(zombieJsonObj);

        // Get  Creatures's initial positions from input sile
         creatureJsonArr = new JSONArray();
         creatureJsonObj = new JSONObject();
        String[] creatures = mainImplement.arrayStr.get(2).split("\\)\\(");
        for (String creature: creatures) {
            String tmpCreature = creature.replaceAll("\\)", "").replaceAll("\\(", "");
            creatureJsonObj = new JSONObject();
            creatureJsonObj.put("x", Integer.parseInt(tmpCreature.split(",")[0]));
            creatureJsonObj.put("y", Integer.parseInt(tmpCreature.split(",")[1]));
            creatureJsonArr.put(creatureJsonObj);
        }
        // Get Zombie's movement from input file
        String strMoves = mainImplement.arrayStr.get(3);
        movesOfZombie = MainImplement.convertMoves(strMoves);
        int maxNumberOfZombies = zombieJsonArr.length() + creatureJsonArr.length();

        //loop for quantity of Zombies
        for (int i = 0; i < maxNumberOfZombies; i++) {
            if (i + 1 > zombieJsonArr.length()) {
                break;
            }
            zombieMoves(zombieJsonArr.getJSONObject(i));
        }

        // List out of Zombie's positions or Creature's postions
        System.out.println("Zombie's positions: " );
        for (int i = 0; i < zombieJsonArr.length(); i++) {
            System.out.print("(" + zombieJsonArr.getJSONObject(i).get("x") + "," + zombieJsonArr.getJSONObject(i).get("y") + ")");
        }
        System.out.println();
        System.out.print("Creature's positions: " );
        if (creatureJsonArr.length() == 0) {
            System.out.println("None");
        } else {
            for (int i = 0; i < creatureJsonArr.length(); i++) {
                System.out.print("(" + creatureJsonArr.getJSONObject(i).get("x") + "," + creatureJsonArr.getJSONObject(i).get("y") + ")");
            }
        }
    }
    public static void zombieMoves(JSONObject zombie) throws JSONException {
        for (int i = 0; i < movesOfZombie.length(); i++) {
            zombie.put("x", zombie.getInt("x") + movesOfZombie.getJSONObject(i).getInt("x"));
            zombie.put("y", zombie.getInt("y") + movesOfZombie.getJSONObject(i).getInt("y"));

            // if zombie moves over (greater or less than dimension) the grid, that it can be at begin or end  of grid position
            if (zombie.getInt("x") >= 4) {
                zombie.put("x", zombie.getInt("x") - dimension);
            } else if (zombie.getInt("x") < 0) {
                zombie.put("x", zombie.getInt("x") + dimension);
            }
            if (zombie.getInt("y") >= 8) {
                zombie.put("x", zombie.getInt("x") - dimension);
            } else if (zombie.getInt("x") < 0) {
                zombie.put("y", zombie.getInt("y") + dimension);
            }
            infect(zombie);
        }

    }
    public static void infect(JSONObject zombie) throws JSONException {
        for (int i = 0; i < creatureJsonArr.length(); i++) {
            if (zombie.getInt("x") == creatureJsonArr.getJSONObject(i).getInt("x") &&
                    zombie.getInt("y") == creatureJsonArr.getJSONObject(i).getInt("y")) {
                zombieJsonArr.put(creatureJsonArr.get(i));
                creatureJsonArr.remove(i);
            }
        }
    }
    public void readFile() {
        try {

            File myObj = new File(".\\src\\input");
            Scanner myReader = new Scanner(myObj);
            System.out.println("Input data: ");
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
                arrayStr.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static JSONArray convertMoves(String moves) {
        JSONObject  json = new JSONObject();
        JSONArray array = new JSONArray();
        Map<Integer, Integer> map = new HashMap<>();

        for(int i = 0; i < moves.length(); i++) {
            char c = moves.charAt(i);
            if (c == 'U') {
                json = new JSONObject();
                json.put("x", 0);
                json.put("y", -1);
                array.put(i, json);
            } else  if (c == 'D') {
                json = new JSONObject();
                json.put("x", 0);
                json.put("y", 1);
                array.put(i, json);
            } else if (c == 'L') {
                json = new JSONObject();
                json.put("x", -1);
                json.put("y", 0);
                array.put(i, json);
            }
            else if (c == 'R') {
                json = new JSONObject();
                json.put("x", 1);
                json.put("y", 0);
                array.put(i, json);
            }
        }

        return array;
    }
}