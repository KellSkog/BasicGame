/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
//import static com.jme3.scene.control.BillboardControl.Alignment.Camera;

/**
 *
 * @author Martina
 */
public class CameraManager {
    CameraState[] cameras;
    int currentCamera;
    Camera cam;

    CameraManager(Camera cam, CameraState[] cameras) {
        this.cam = cam;
        this.cameras = cameras;
        currentCamera = 0;
        cam.setRotation(cameras[currentCamera].getCamRotation());
        cam.setLocation(cameras[currentCamera].getCamLocation());
//            setCameraState(currentCamera);
    }

    void setCameraState(int newActiveCamera, Geometry geomRef) {
//            cameras[currentCamera].setCamera(cam.getRotation(),cam.getLocation());
        Quaternion test = cam.getRotation();
        float w = test.getW();
        float x = test.getX();
        float y = test.getY();
        float z = test.getZ();
        System.out.println(w + " " + x + " " + y + " " + z);
        Vector3f locTest = cam.getLocation();
        x = locTest.getX();
        y = locTest.getY();
        z = locTest.getZ();
        System.out.println(x + " " + y + " " + z);
        locTest = geomRef.getLocalTranslation();
        x = locTest.getX();
        y = locTest.getY();
        z = locTest.getZ();
        System.out.println("local" + x + " " + y + " " + z);
        cam.setRotation(cameras[newActiveCamera].getCamRotation());
        cam.setLocation(cameras[newActiveCamera].getCamLocation());
        currentCamera = newActiveCamera;
    }

    void checkCameraState(Node nodeRef, Geometry geomRef) {
        if (currentCamera == 1) {
            Quaternion test = nodeRef.getWorldRotation();
            cam.setLocation(geomRef.getWorldTranslation());
            test.opposite(test);
            cam.setRotation(test);
            float w = test.getW();
            float x = test.getX();
            float y = test.getY();
            float z = test.getZ();
            System.out.println(w + " " + x + " " + y + " " + z);
            Vector3f locTest = geomRef.getWorldTranslation();
            x = locTest.getX();
            y = locTest.getY();
            z = locTest.getZ();
            System.out.println(x + " " + y + " " + z);
            locTest = geomRef.getLocalTranslation();
            x = locTest.getX();
            y = locTest.getY();
            z = locTest.getZ();
            System.out.println("local" + x + " " + y + " " + z);

            cameras[currentCamera].setCamera(test, geomRef.getWorldTranslation());
        }
    }

}
