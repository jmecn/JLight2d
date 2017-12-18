package net.jmecn.scene;

public class Result {
    public float sd;
    public float emissive;
    public float reflectivity;
    public float eta;
    public float absorption;

    public Result(float sd) {
        this(sd, 0f, 0f, 0f, 0f);
    }
    
    public Result(float sd, float emissive) {
        this(sd, emissive, 0f, 0f, 0f);
    }
    
    public Result(float sd, float emissive, float reflectivity) {
        this(sd, emissive, reflectivity, 0f, 0f);
    }
    
    public Result(float sd, float emissive, float reflectivity, float eta) {
        this(sd, emissive, reflectivity, eta, 0f);
    }
    
    public Result(float sd, float emissive, float reflectivity, float eta, float absorption) {
        this.sd = sd;
        this.emissive = emissive;
        this.reflectivity = reflectivity;
        this.eta = eta;
        this.absorption = absorption;
    }
}