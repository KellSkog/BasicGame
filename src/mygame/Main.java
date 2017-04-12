/*
 * 2 Balls
*/

package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Collection<Sphere> balls = new ArrayList<>();
//        Collection<PhysicsSphere> balls = new ArrayList<>();
        Geometry geom;
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        
        for(int i = 0; i < 2; i++) {
            balls.add(new Sphere(30, 30, 1));
            
        }
        Iterator<Sphere> iter = balls.iterator();
        float mass = 10;
        for(int i = 0; i < 2; i++) {
            geom = new Geometry("Box", iter.next());
            geom.setLocalTranslation(i*4-2, 2, -3);
            geom.addControl(new RigidBodyControl(.001f));
            geom.getControl(RigidBodyControl.class).setRestitution(1);
            mat.setColor("Color", ColorRGBA.Green);
            geom.setMaterial(mat);
            RigidBodyControl bulletControl = new RigidBodyControl(mass);
            geom.addControl(bulletControl);
            bulletControl.setLinearVelocity(new Vector3f(1-i*2,0,0));
            
            rootNode.attachChild(geom);
            // activate physics
            BulletAppState bulletAppState = new BulletAppState();
            stateManager.attach(bulletAppState);
            bulletAppState.getPhysicsSpace().add(bulletControl);
            bulletControl.setGravity(new Vector3f(0,0,0));
           
            
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
