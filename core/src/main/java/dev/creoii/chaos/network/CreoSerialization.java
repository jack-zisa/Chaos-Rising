package dev.creoii.chaos.network;

import com.badlogic.gdx.utils.Pool;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.KryoSerialization;

import java.nio.ByteBuffer;

public class CreoSerialization extends KryoSerialization {
    public final Pool<ByteBufferOutput> OUT_BYTE_BUFFERS = new Pool<>(128) {
        @Override
        protected ByteBufferOutput newObject() {
            return new ByteBufferOutput();
        }
    };
    public final Pool<ByteBufferInput> IN_BYTE_BUFFERS = new Pool<>(128) {
        @Override
        protected ByteBufferInput newObject() {
            return new ByteBufferInput();
        }
    };

    public CreoSerialization() {
        super(new Kryo());
    }

    @Override
    public synchronized void write(Connection connection, ByteBuffer buffer, Object object) {
        try (ByteBufferOutput out = OUT_BYTE_BUFFERS.obtain()) {
            out.setBuffer(buffer);
            getKryo().writeClassAndObject(out, object);
            out.flush();
        }

    }

    @Override
    public synchronized Object read(Connection connection, ByteBuffer buffer) {
        ByteBufferInput in = IN_BYTE_BUFFERS.obtain();
        in.setBuffer(buffer);
        return getKryo().readClassAndObject(in);
    }
}
