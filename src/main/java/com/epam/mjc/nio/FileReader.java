package com.epam.mjc.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;


public class FileReader {

    public Profile getDataFromFile(File file) {

        String profileString = getProfileString(file);

        if (profileString == null){
            return null;
        }

        String[] lines = profileString.split("\n");

        HashMap<String,String> profileMap = new HashMap<>();

        for (String line:  lines) {
            String[] words = line.split(":");

            if (words.length != 2){
                continue;
            }
            profileMap.put(words[0],words[1].trim());
        }

        Profile profile = new Profile();

        profile.setName(profileMap.get("Name"));
        profile.setAge(Integer.parseInt(profileMap.get("Age")));
        profile.setEmail(profileMap.get("Email"));
        profile.setPhone(Long.parseLong(profileMap.get("Phone")));

        return profile;
    }

    private String getProfileString(File file){

        try(RandomAccessFile aFile = new RandomAccessFile(file, "r");
            FileChannel inChannel = aFile.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            StringBuilder content = new StringBuilder();

            while (inChannel.read(buffer) > 0) {
                buffer.flip();
                for (int i = 0; i < buffer.limit(); i++) {
                    content.append((char) buffer.get());
                }
                buffer.clear(); // do something with the data and clear/compact it.
            }

            return content.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
