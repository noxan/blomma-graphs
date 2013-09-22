package com.github.noxan.blommagraphs.scheduling.serializer.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.github.noxan.blommagraphs.scheduling.ScheduledTaskList;


public class ObjectScheduledTaskListSerializer {
    public void serialize(String pathname, ScheduledTaskList scheduledTaskList) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(pathname));
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(scheduledTaskList);

        oos.close();
        fos.close();
    }

    public ScheduledTaskList deserialize(String pathname) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = new FileInputStream(new File(pathname));
        ObjectInputStream ois = new ObjectInputStream(fis);

        ScheduledTaskList scheduledTaskList = (ScheduledTaskList) ois.readObject();

        ois.close();
        fis.close();

        return scheduledTaskList;
    }
}
