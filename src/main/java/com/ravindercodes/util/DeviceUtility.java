package com.ravindercodes.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.UUID;

public class DeviceUtility {

    public static String getDeviceName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "Unknown-Device";
        }
    }

    public static String generateDeviceId() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = (network != null) ? network.getHardwareAddress() : new byte[0];

            StringBuilder macAddress = new StringBuilder();
            for (byte b : mac) {
                macAddress.append(String.format("%02X", b));
            }

            String osInfo = System.getProperty("os.name") + System.getProperty("os.version");
            String uuid = UUID.randomUUID().toString();
            String rawId = macAddress + osInfo + uuid;

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(rawId.getBytes());

            StringBuilder deviceId = new StringBuilder();
            for (byte b : hashedBytes) {
                deviceId.append(String.format("%02x", b));
            }

            return deviceId.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate device ID", e);
        }
    }

}
