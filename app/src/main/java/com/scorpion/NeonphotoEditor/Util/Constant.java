package com.scorpion.NeonphotoEditor.Util;

import java.util.HashMap;

public class Constant {

    public static String ANNAS_FOLDER = "Annas";

    public static String BEATING_FOLDER = "Beating";

    public static String COLORROUND_FOLDER = "ColorRound";

    public static String CALL_FOLDER = "Call";

    public static String FIREWORK_FOLDER = "Firework";

    public static String FLAMINGO_FOLDER = "Flamingo";

    public static String HALFROUND_FOLDER = "Halfround";

    public static String HEARTS_FOLDER = "Hearts";

    public static String NIMBUZZ_FOLDER = "Nimbuzz";

    public static String ROUND_FOLDER = "Round";

    public static String SQUARE_FOLDER = "Square";

    public static String TREE_FOLDER = "Tree";

    public static String MUSIC_FOLDER = "Music";
    public static int NORMAL_STICKER_SIZE = 500;


    public static String STICKER_FOLDER = "Stickers";

    public static String THUMB_FOLDER = "Thumb";

    public static String[] font = {"Amadeus", "Amaranth", "Boredom", "ChokoPlain", "Decibel", "embosst", "Gloop", "GOTHIC", " Heart", "Hilarious", "Hillock", "HoboStd", "JOKEWOOD", "Redhair", "Roboto"};
    public static String fontname;
    public static HashMap<Integer, String> hslist = new HashMap<Integer, String>() {
        {
            put(0, Constant.ANNAS_FOLDER);
            put(1, Constant.BEATING_FOLDER);
            put(2, Constant.COLORROUND_FOLDER);
            put(3, Constant.FLAMINGO_FOLDER);
            put(4, Constant.HALFROUND_FOLDER);
            put(5, Constant.HEARTS_FOLDER);
            put(6, Constant.NIMBUZZ_FOLDER);
            put(7, Constant.ROUND_FOLDER);
            put(8, Constant.SQUARE_FOLDER);
            put(9, Constant.TREE_FOLDER);
            put(10, Constant.MUSIC_FOLDER);

        }
    };

}
