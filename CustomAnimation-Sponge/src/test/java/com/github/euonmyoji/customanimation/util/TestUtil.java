package com.github.euonmyoji.customanimation.util;

import com.flowpowered.math.vector.Vector3d;
import org.junit.Test;

/**
 * @author yinyangshi
 */
public class TestUtil {
    @Test
    public void testGetRotation() {
        System.out.println(Util.get(null, new Vector3d(0, 0, 0), new Vector3d(1, 0, 0), 1, 0, false));
        System.out.println(Util.get(null, new Vector3d(0, 0, 0), new Vector3d(0, 0, 1), 1, 0, false));
        System.out.println(Util.get(null, new Vector3d(0, 0, 0), new Vector3d(-1, 0, 0), 1, 0, false));
        System.out.println(Util.get(null, new Vector3d(0, 0, 0), new Vector3d(0, 0, -1), 1, 0, false));
        System.out.println(Util.get(null, new Vector3d(0, 0, 0), new Vector3d(3, 0, 4), 1, 0, false));
    }
}
