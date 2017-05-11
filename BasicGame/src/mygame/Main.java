/*
 * 2 Balls
*/

package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication implements PhysicsCollisionListener {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
     //   bulletAppState = new BulletAppState();
//        stateManager.attach(bulletAppState);
//        bulletAppState.setDebugEnabled(true);
//        bullet = new Sphere(32, 32, 0.4f, true, false);
//        bullet.setTextureMode(Sphere.TextureMode.Projected);
//        bulletCollisionShape = new SphereCollisionShape(0.4f);
//
//        PhysicsTestHelper.createPhysicsTestWorld(rootNode, assetManager, bulletAppState.getPhysicsSpace());
//        PhysicsTestHelper.createBallShooter(this, rootNode, bulletAppState.getPhysicsSpace());
    

        List<Sphere> balls = new ArrayList<>();
        List<SphereCollisionShape> bullets = new ArrayList<>();
        Geometry geom;
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        float radius = 1;
 
        balls.add(new Sphere(30, 30, radius));
        bullets.add(new SphereCollisionShape(radius));
        balls.add(new Sphere(30, 30, radius));
        bullets.add(new SphereCollisionShape(radius));
           
        
//        Iterator<Sphere> iter = balls.iterator();
        float mass = 10;
        BulletAppState bulletAppState = new BulletAppState();

        for(int i = 0; i < 2; i++) {
            geom = new Geometry("Box", balls.get(i));
 //           geom = new Geometry("Box", iter.next());
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
            stateManager.attach(bulletAppState);
            bulletAppState.getPhysicsSpace().add(bulletControl);
            bulletControl.setGravity(new Vector3f(0,0,0));
                  // add ourselves as collision listener
            bulletAppState.getPhysicsSpace().addCollisionListener(this);

            
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code  Erling
    }
 
//    private PhysicsSpace getPhysicsSpace(){
//        return bulletAppState.getPhysicsSpace();
//    }

   
    public void collision(PhysicsCollisionEvent event) {
        if ("Box".equals(event.getNodeA().getName()) || "Box".equals(event.getNodeB().getName())) {
            if ("bullet".equals(event.getNodeA().getName()) || "bullet".equals(event.getNodeB().getName())) {
                fpsText.setText("You hit the box!");
            }
        }
    }
}