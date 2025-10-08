package dev.creoii.chaos.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.KryoSerialization;

import java.nio.ByteBuffer;

public class CreoSerialization extends KryoSerialization {
    public CreoSerialization() {
        super(new Kryo());
    }

    @Override
    public synchronized void write(Connection connection, ByteBuffer buffer, Object object) {
        ByteBufferOutput out = new ByteBufferOutput(buffer.capacity(), -1);
        out.setBuffer(buffer);
        getKryo().writeClassAndObject(out, object);
        out.flush();
    }

    @Override
    public synchronized Object read(Connection connection, ByteBuffer buffer) {
        ByteBufferInput in = new ByteBufferInput(buffer);
        return getKryo().readClassAndObject(in);
    }
}
