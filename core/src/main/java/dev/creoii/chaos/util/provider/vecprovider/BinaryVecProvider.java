package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.util.provider.Operation;

public class BinaryVecProvider implements VecProvider {
    private final VecProvider a, b;
    private final Operation op;

    public BinaryVecProvider(VecProvider a, VecProvider b, Operation op) {
        this.a = a;
        this.b = b;
        this.op = op;
    }

    @Override
    public Vector2 get(Context context) {
        Vector2 av = a.get(context), bv = b.get(context);
        return switch (op) {
            case ADD -> av.add(bv);
            case SUB -> av.sub(bv);
            case MUL -> av.scl(bv);
            case DIV -> {
                float x = av.x / bv.x;
                float y = av.y / bv.y;
                yield new Vector2(x, y);
            }
        };
    }

    @Override
    public VecProvider copy() {
        return new BinaryVecProvider(a.copy(), b.copy(), op);
    }
}
