package com.scorpion.NeonphotoEditor.Videoneoneffect.Particle;

public class Geometry {

    public static class Point {
        public final float x;
        public final float y;
        public final float z;

        public Point(float f, float f2, float f3) {
            x = f;
            y = f2;
            z = f3;
        }

    }




    public static class Vector {
        public float x;
        public float y;
        public float z;

        public Vector(float f, float f2, float f3) {
            x = f;
            y = f2;
            z = f3;
        }

        public float length() {
            return (float) Math.sqrt((double) ((x * x) + (y * y) + (z * z)));
        }

    }





}
