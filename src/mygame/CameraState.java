/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author Martina
 */
public class CameraState {
    private Quaternion camRotation;
    private Vector3f camLocation;

    void setCamera(Quaternion rotation, Vector3f location) {
        camRotation = rotation;
        camLocation = location;
    }
//        private CameraState getCamera(){
//            return this;
//        }

    Quaternion getCamRotation() {
        return camRotation;
    }

    Vector3f getCamLocation() {
        return camLocation;
    }
}
