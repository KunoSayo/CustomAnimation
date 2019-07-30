package com.github.euonmyoji.customanimation.util;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector3d;

/**
 * @author yinyangshi
 */
public class Util {
    public static final double UNIT_RAD = Math.PI / 180;
    private static final double UNIT_ANGLE = 180 / Math.PI;

    public static Vector3d get(Vector3d start, Vector3d end, double m) {
        if (m == 1) {
            return end;
        }
        return start.add(end.sub(start).mul(m));
    }

    public static Vector2d get(Vector2d start, Vector3d location, Vector3d point, double m, double offset) {
        if (location == null) {
            return null;
        }
        Vector2d locPos = location.toVector2(true);
        Vector2d pointPos = point.toVector2(true);
        double distance = locPos.distanceSquared(pointPos);
        if (distance == 0) {
            return null;
        }
        distance = Math.sqrt(distance);

        //pitch
        //v1(xz距离, 3d y轴差值)
        Vector2d v1 = new Vector2d(distance, point.getY() - location.getY());
        Vector2d v2 = new Vector2d(distance, 0);
        //取符号 → 最后 (弧度转角 参考高中数学必修公式)
        final double pitch = Math.acos(v1.dot(v2) / (v1.length() * v2.length())) * UNIT_ANGLE * (v1.getY() >= 0 ? -1 : 1) + offset;

        //yaw
        //v1 为 看向的方向向量
        v1 = pointPos.sub(locPos);
        v2 = new Vector2d(0, v1.getY());
        double yaw = Math.acos(v1.dot(v2) / (v1.length() * v2.length())) * UNIT_ANGLE;
        if (v1.getX() < 0 && v1.getY() < 0) {
            yaw = 180 - yaw;
        } else if (v1.getX() > 0 && v1.getY() < 0) {
            yaw = 180 + yaw;
        } else if (v1.getX() > 0 && v1.getY() > 0) {
            yaw = 360 - yaw;
        }
        Vector2d result = new Vector2d(pitch, yaw);
        if (m == 1) {
            return result;
        } else {
            Vector2d d = result.sub(start);
            return start.add(d.mul(m));
        }
    }

    private static void check(Vector2d v) {

    }
}
