package net.jmecn.scene;

public abstract class Scene2D {
    
    protected Result unionOp(Result a, Result b) {
        return a.sd < b.sd ? a : b;
    }

    protected Result intersectOp(Result a, Result b) {
        Result r = a.sd > b.sd ? b : a;
        r.sd = a.sd > b.sd ? a.sd : b.sd;
        return r;
    }

    protected Result subtractOp(Result a, Result b) {
        Result r = a;
        r.sd = (a.sd > -b.sd) ? a.sd : -b.sd;
        return r;
    }

    protected Result complementOp(Result a) {
        a.sd = -a.sd;
        return a;
    }
    
    public abstract Result scene(float x, float y);
}
