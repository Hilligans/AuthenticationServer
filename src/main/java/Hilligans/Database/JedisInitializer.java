package Hilligans.Database;

import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class JedisInitializer extends Jedis {

    public JedisInitializer(String operatingSystem, String processName, String pathToServer) {
        if(operatingSystem.equals("win10")) {
            for(String string : listRunningProcesses()) {
                if(string.equals(processName)) {
                    return;
                }
            }
            if(!pathToServer.equals("")) {
                File file = new File(pathToServer);
                try {
                    Runtime.getRuntime().exec(file.getPath(), null, file.getParentFile());
                } catch (Exception ignored) {}
            }
        }
    }

    public static ArrayList<String> listRunningProcesses() {
        ArrayList<String> processes = new ArrayList<>();
        try {
            String line;
            Process p = Runtime.getRuntime().exec("tasklist.exe /fo csv /nh");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                if (!line.trim().equals("")) {
                    line = line.substring(1);
                    processes.add(line.substring(0,line.indexOf('"')));
          }
      }
      input.close();
    }
    catch (Exception err) {
      err.printStackTrace();
    }
    return processes;
  }

}
