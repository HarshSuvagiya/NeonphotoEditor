package com.scorpion.NeonphotoEditor.util;

import java.util.ArrayList;
import java.util.HashMap;

public class Constant {
    public static final int BALLOON = 6;
    public static String BALLOON_FOLDER = "Balloon";
    public static final int BELL = 7;
    public static String BELL_FOLDER = "Bell";
    public static final int BOTTLE_OPEN = 8;
    public static String BOTTLE_OPEN_FOLDER = "Bottle Open";
    public static final int CALL = 24;
    public static String CALL_FOLDER = "Call";
    public static final int FIREWORK = 25;
    public static String FIREWORK_FOLDER = "Firework";
    public static final int FLYING_HEART = 9;
    public static String FLYING_HEART_FOLDER = "Flying Heart";
    public static final int GIFT = 10;
    public static String GIFT_FOLDER = "Gift";
    public static final int GUITAR = 11;
    public static String GUITAR_FOLDER = "Guitar";
    public static final int HEART = 12;
    public static String HEART_FOLDER = "Heart";
    public static final int HOLIDAY = 13;
    public static String HOLIDAY_FOLDER = "Holiday";
    public static final int MAGIC_HAT = 14;
    public static String MAGIC_HAT_FOLDER = "Magic Hat";
    public static final int MEGA_STAR = 15;
    public static String MEGA_STAR_FOLDER = "Mega Star";
    public static final int MUSIC = 16;
    public static String MUSIC_FOLDER = "Music";
    public static int NORMAL_STICKER_SIZE = 300;
    public static final int PARTY = 17;
    public static String PARTY_FOLDER = "Party";
    public static final int PIZZA = 18;
    public static String PIZZA_FOLDER = "Pizza";
    public static final int ROCKET = 19;
    public static String ROCKET_FOLDER = "Rocket";
    public static final int SANTA = 20;
    public static String SANTA_FOLDER = "Santa";
    public static String STICKER_FOLDER = "Stickers";
    public static final int SWIRL1 = 0;
    public static String SWIRL1_FOLDER = "Swirl1";
    public static final int SWIRL2 = 1;
    public static String SWIRL2_FOLDER = "Swirl2";
    public static final int SWIRL3 = 2;
    public static String SWIRL3_FOLDER = "Swirl3";
    public static final int SWIRL4 = 3;
    public static String SWIRL4_FOLDER = "Swirl4";
    public static final int SWIRL5 = 4;
    public static String SWIRL5_FOLDER = "Swirl5";
    public static final int SWIRL6 = 5;
    public static String SWIRL6_FOLDER = "Swirl6";
    public static int SWIRL_STICKER_SIZE = 450;
    public static final int TEACUP = 26;
    public static String TEACUP_FOLDER = "Teacup";
    public static String THUMB_FOLDER = "Thumb";
    public static final int TOY = 21;
    public static String TOY_FOLDER = "Toy";
    public static final int WITCH_HAT = 22;
    public static String WITCH_HAT_FOLDER = "Witch Hat";
    public static final int XMAS = 23;
    public static String XMAS_FOLDER = "Xmas";
    public static String[] font = {"Amadeus", "Amaranth", "Boredom", "ChokoPlain", "Decibel", "embosst", "Gloop", "GOTHIC", " Heart", "Hilarious", "Hillock", "HoboStd", "JOKEWOOD", "Redhair", "Roboto"};
    public static String fontname;
    public static HashMap<Integer, String> hslist = new HashMap<Integer, String>() {
        {
            put(6, Constant.BALLOON_FOLDER);
            put(7, Constant.BELL_FOLDER);
            put(8, Constant.BOTTLE_OPEN_FOLDER);
            put(9, Constant.FLYING_HEART_FOLDER);
            put(10, Constant.GIFT_FOLDER);
            put(11, Constant.GUITAR_FOLDER);
            put(12, Constant.HEART_FOLDER);
            put(13, Constant.HOLIDAY_FOLDER);
            put(14, Constant.MAGIC_HAT_FOLDER);
            put(15, Constant.MEGA_STAR_FOLDER);
            put(16, Constant.MUSIC_FOLDER);
            put(17, Constant.PARTY_FOLDER);
            put(18, Constant.PIZZA_FOLDER);
            put(19, Constant.ROCKET_FOLDER);
            put(20, Constant.SANTA_FOLDER);
            put(0, Constant.SWIRL1_FOLDER);
            put(1, Constant.SWIRL2_FOLDER);
            put(21, Constant.TOY_FOLDER);
            put(22, Constant.WITCH_HAT_FOLDER);
            put(23, Constant.XMAS_FOLDER);
            put(26, Constant.TEACUP_FOLDER);
            put(25, Constant.FIREWORK_FOLDER);
            put(24, Constant.CALL_FOLDER);
            put(2, Constant.SWIRL3_FOLDER);
            put(3, Constant.SWIRL4_FOLDER);
            put(4, Constant.SWIRL5_FOLDER);
            put(5, Constant.SWIRL6_FOLDER);
        }
    };
    public static ArrayList<String> slist = new ArrayList<String>() {
        {
            add(Constant.BALLOON_FOLDER);
            add(Constant.BELL_FOLDER);
            add(Constant.BOTTLE_OPEN_FOLDER);
            add(Constant.FLYING_HEART_FOLDER);
            add(Constant.GIFT_FOLDER);
            add(Constant.GUITAR_FOLDER);
            add(Constant.HEART_FOLDER);
            add(Constant.HOLIDAY_FOLDER);
            add(Constant.MAGIC_HAT_FOLDER);
            add(Constant.MEGA_STAR_FOLDER);
            add(Constant.MUSIC_FOLDER);
            add(Constant.PARTY_FOLDER);
            add(Constant.PIZZA_FOLDER);
            add(Constant.ROCKET_FOLDER);
            add(Constant.SANTA_FOLDER);
            add(Constant.SWIRL1_FOLDER);
            add(Constant.SWIRL2_FOLDER);
            add(Constant.TOY_FOLDER);
            add(Constant.WITCH_HAT_FOLDER);
            add(Constant.XMAS_FOLDER);
        }
    };
}