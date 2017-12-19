package net.jmecn.scene;

import net.jmecn.math.Color;

public class Result {
    public float sd;
    public Color emissive;
    public float reflectivity;
    public float eta;
    public Color absorption;

    public Result(float sd) {
        this(sd, Color.BLACK);
    }
    
    public Result(float sd, Color emissive) {
        this(sd, emissive, 0f);
    }
    
    public Result(float sd, Color emissive, float reflectivity) {
        this(sd, emissive, reflectivity, 0f);
    }
    
    public Result(float sd, Color emissive, float reflectivity, float eta) {
        this(sd, emissive, reflectivity, eta, Color.WHITE);
    }
    
    public Result(float sd, Color emissive, float reflectivity, float eta, Color absorption) {
        this.sd = sd;
        this.emissive = emissive;
        this.reflectivity = reflectivity;
        this.eta = eta;
        this.absorption = absorption;
    }
}