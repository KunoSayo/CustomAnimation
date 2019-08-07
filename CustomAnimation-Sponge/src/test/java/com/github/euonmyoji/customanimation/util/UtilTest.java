package com.github.euonmyoji.customanimation.util;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector3d;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;

/**
 * @author yinyangshi
 */
public class UtilTest {
    /**
     * Test for the pitch & yaw
     */
    @Test(timeout = 50)
    public void testGetRotation() {
        assertEquals("The yaw should be - 90",
                Objects.requireNonNull(Util.get(null, new Vector3d(0, 0, 0), new Vector3d(1, 1, 0), 1, 0)).getY(),
                -90, 0.0);
        assertEquals("the yaw should be 0",
                Objects.requireNonNull(Util.get(null, new Vector3d(0, 0, 0), new Vector3d(0, 1, 1), 1, 0)).getY(),
                0, 0);
        assertEquals("the yaw should be 90",
                Objects.requireNonNull(Util.get(null, new Vector3d(0, 0, 0), new Vector3d(-1, 1, 0), 1, 0)).getY(),
                90, 0);
        assertEquals("the yaw should be 180",
                Math.abs(Objects.requireNonNull(Util.get(null, new Vector3d(0, 0, 0), new Vector3d(0, 1, -1), 1, 0)).getY()),
                180, 0);

        assertEquals("the yaw should be -45",
                Objects.requireNonNull(Util.get(null, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), 1, 0)).getY()
                , -45, 1);
        assertEquals("the yaw should be -135",
                Objects.requireNonNull(Util.get(null, new Vector3d(0, 0, 0), new Vector3d(1, 1, -1), 1, 0)).getY(),
                -135, 1);
        assertEquals("the yaw should be 45",
                Objects.requireNonNull(Util.get(null, new Vector3d(0, 0, 0), new Vector3d(-1, 1, 1), 1, 0)).getY(),
                45, 1);
        assertEquals("the yaw should be 135",
                Objects.requireNonNull(Util.get(null, new Vector3d(0, 0, 0), new Vector3d(-1, 1, -1), 1, 0)).getY(),
                135, 1);

        //test for the pitch
        assertEquals("the pitch should be -90",
                Objects.requireNonNull(Util.get(new Vector2d(0, 0), new Vector3d(0, 0, 0), new Vector3d(0, 1, 0), 1, 0)).getX(),
                -90, 0);
        assertEquals("the pitch should be 0",
                Objects.requireNonNull(Util.get(null, new Vector3d(0, 0, 0), new Vector3d(1, 0, 1), 1, 0)).getX(),
                0, 0);
        assertEquals("the pitch should be 90",
                Objects.requireNonNull(Util.get(new Vector2d(0, 0), new Vector3d(0, 0, 0), new Vector3d(0, -1, 0), 1, 0)).getX(),
                90, 0);

        //test for the time
        for (int i = 0; i < 50000; i++) {
            Util.get(null, new Vector3d(50, 100, 50), new Vector3d(11234, 200, 1564), 1, 0);
        }
    }
}
