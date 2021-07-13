package sample.ProductObserver;

import org.junit.Test;

import java.util.Calendar;
import java.util.Timer;

import static org.junit.Assert.*;

public class TriggerTuskTest {

    @Test
    public void triggerTuskTest(){
        Timer timer = new Timer();
        timer.schedule(
                new TriggerTusk(),
                Calendar.getInstance().getTime(),
                3600000);
    }
}