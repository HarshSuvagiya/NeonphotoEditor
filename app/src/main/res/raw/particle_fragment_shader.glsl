precision highp float;

varying vec4 v_Color;
varying mat2 v_Rotation;
varying float v_Progress;
varying float v_UnitTextureCoord;
varying vec2 v_CoordClamp;

uniform float u_TotalTexture;
uniform float u_Animation;//0 for false, 1 for true
uniform sampler2D u_TextureUnit;

void main()
{
    if (v_Progress > 1.0 || v_Progress < 0.0) {
        discard;
    } else {

        vec2 myvec;

        if(u_Animation == 0.0){

            myvec = v_CoordClamp;

        } else {

            float cpoint = 0.0;
            float vprog = v_Progress * 100.0;

            float modval = mod(vprog, u_TotalTexture);

            cpoint = v_UnitTextureCoord * floor(modval);
            float npoint = cpoint + v_UnitTextureCoord;

            myvec = vec2(cpoint, npoint);

            if (npoint > 1.0){

                myvec = vec2(0.0, v_UnitTextureCoord);

            }

        }

        vec2 newCoord = v_Rotation * (gl_PointCoord - vec2(0.5)) + vec2(0.5);
        newCoord.y = newCoord.y * v_UnitTextureCoord + myvec.x;
        newCoord.y = clamp(newCoord.y, myvec.x, myvec.y);

        vec4 texture = texture2D(u_TextureUnit, newCoord);
//        gl_FragColor = texture * v_Color * (1.0 - v_Progress); // apply custom colors to particle
        gl_FragColor = texture * (1.0 - v_Progress);

    }
}