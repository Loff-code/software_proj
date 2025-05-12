package hellocucumber;

import app.*;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeListener;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PMAppTest {

    @Test
    void testAddObserver() {
        PM_App app = new PM_App();
        PropertyChangeListener dummyListener = evt -> {};
        app.addObserver(dummyListener);
    }




}
