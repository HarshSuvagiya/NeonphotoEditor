package com.scorpion.NeonphotoEditor.Videoneoneffect.particle;

import android.content.Context;
import android.opengl.GLES20;

import com.scorpion.NeonphotoEditor.R;
import com.scorpion.NeonphotoEditor.Videoneoneffect.ShaderHelper;

import java.util.Arrays;

public class ParticleShaderProgram {
    private static final String A_PARTICLE_BIRTH_TIME = "a_BirthTime";
    private static final String A_PARTICLE_DIRECTION_VECTOR = "a_DirectionVector";
    private static final String A_PARTICLE_DURATION = "a_Duration";
    private static final String A_PARTICLE_FROM_ANGLE = "a_FromRotation";
    private static final String A_PARTICLE_FROM_COLOR = "a_FromColor";
    private static final String A_PARTICLE_FROM_SIZE = "a_FromSize";
    private static final String A_PARTICLE_POSITION = "a_BirthPosition";
    private static final String A_PARTICLE_TEXTURE_INDEX = "a_TextureIndex";
    private static final String A_PARTICLE_TO_SIZE = "a_ToSize";
    private static final String A_PARTICLE_TO_ANGLE = "a_ToRotation";
    private static final String A_PARTICLE_TO_COLOR = "a_ToColor";


    private static final String[] ATTRIBUTES = {A_PARTICLE_BIRTH_TIME, A_PARTICLE_DURATION, A_PARTICLE_FROM_SIZE,
            A_PARTICLE_TO_SIZE, A_PARTICLE_FROM_ANGLE, A_PARTICLE_TO_ANGLE, A_PARTICLE_POSITION,
            A_PARTICLE_DIRECTION_VECTOR, A_PARTICLE_FROM_COLOR, A_PARTICLE_TO_COLOR, A_PARTICLE_TEXTURE_INDEX};

    private static final int A_PARTICLE_BIRTH_TIME_LOCATION = getAttributeLocation(ATTRIBUTES, A_PARTICLE_BIRTH_TIME);
    private static final int A_PARTICLE_DIRECTION_LOCATION = getAttributeLocation(ATTRIBUTES, A_PARTICLE_DIRECTION_VECTOR);

    private static final int A_PARTICLE_DURATION_LOCATION = getAttributeLocation(ATTRIBUTES, A_PARTICLE_DURATION);

    private static final int A_PARTICLE_FROM_ANGLE_LOCATION = getAttributeLocation(ATTRIBUTES, A_PARTICLE_FROM_ANGLE);

    private static final int A_PARTICLE_FROM_COLOR_LOCATION = getAttributeLocation(ATTRIBUTES, A_PARTICLE_FROM_COLOR);

    private static final int A_PARTICLE_FROM_SIZE_LOCATION = getAttributeLocation(ATTRIBUTES, A_PARTICLE_FROM_SIZE);

    private static final int A_PARTICLE_POSITION_LOCATION = getAttributeLocation(ATTRIBUTES, A_PARTICLE_POSITION);

    private static final int A_PARTICLE_TEXTURE_INDEX_LOCATION = getAttributeLocation(ATTRIBUTES, A_PARTICLE_TEXTURE_INDEX);

    private static final int A_PARTICLE_TO_ANGLE_LOCATION = getAttributeLocation(ATTRIBUTES, A_PARTICLE_TO_ANGLE);

    private static final int A_PARTICLE_TO_COLOR_LOCATION = getAttributeLocation(ATTRIBUTES, A_PARTICLE_TO_COLOR);

    private static final int A_PARTICLE_TO_SIZE_LOCATION = getAttributeLocation(ATTRIBUTES, A_PARTICLE_TO_SIZE);

    private  int mProgram=0;

    public ParticleShaderProgram(Context context) {
        this.mProgram = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, R.raw.particle_vertex_shader), TextResourceReader.readTextFileFromResource(context, R.raw.particle_fragment_shader), ATTRIBUTES);
    }



    private static int getAttributeLocation(String[] strArr, String str) {
        return Arrays.asList(strArr).indexOf(str);
    }

    public int getParticleBirthTimeAttributeLocation() {
        return A_PARTICLE_BIRTH_TIME_LOCATION;
    }

    public int getParticleDurationAttributeLocation() {
        return A_PARTICLE_DURATION_LOCATION;
    }

    public int getParticleFromSizeAttributeLocation() {
        return A_PARTICLE_FROM_SIZE_LOCATION;
    }

    public int getParticleToSizeAttributeLocation() {
        return A_PARTICLE_TO_SIZE_LOCATION;
    }

    public int getParticleFromAngleAttributeLocation() {
        return A_PARTICLE_FROM_ANGLE_LOCATION;
    }

    public int getParticleToAngleLocation() {
        return A_PARTICLE_TO_ANGLE_LOCATION;
    }

    public int getParticlePositionAttributeLocation() {
        return A_PARTICLE_POSITION_LOCATION;
    }

    public int getParticleDirectionAttributeLocation() {
        return A_PARTICLE_DIRECTION_LOCATION;
    }

    public int getParticleFromColorAttributeLocation() {
        return A_PARTICLE_FROM_COLOR_LOCATION;
    }

    public int getParticleToColorAttributeLocation() {
        return A_PARTICLE_TO_COLOR_LOCATION;
    }

    public int getParticleTextureIndexLocation() {
        return A_PARTICLE_TEXTURE_INDEX_LOCATION;
    }
}
