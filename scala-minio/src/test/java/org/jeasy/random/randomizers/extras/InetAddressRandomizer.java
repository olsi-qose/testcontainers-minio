package org.jeasy.random.randomizers.extras;

import java.net.Inet4Address;
import java.net.InetAddress;

import org.jeasy.random.randomizers.AbstractRandomizer;

import com.google.common.net.InetAddresses;

@SuppressWarnings("UnstableApiUsage")
public class InetAddressRandomizer extends AbstractRandomizer<InetAddress> {

    @Override
    public Inet4Address getRandomValue() {
        return InetAddresses.fromInteger(random.nextInt());
    }
}
