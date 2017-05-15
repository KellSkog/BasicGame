/*
 * 2 Balls
*/

package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication implements PhysicsCollisionListener {
    PhysicsSpace space;
    Geometry geom;
    Material mat;
    BulletAppState bulletAppState;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
//        bulletAppState = new BulletAppState();
//        stateManager.attach(bulletAppState);
//        bulletAppState.setDebugEnabled(true);
//        bullet = new Sphere(32, 32, 0.4f, true, false);
//        bullet.setTextureMode(Sphere.TextureMode.Projected);
//        bulletCollisionShape = new SphereCollisionShape(0.4f);
//
//        PhysicsTestHelper.createPhysicsTestWorld(rootNode, assetManager, bulletAppState.getPhysicsSpace());
//        PhysicsTestHelper.createBallShooter(this, rootNode, bulletAppState.getPhysicsSpace());
    

        List<Sphere> balls = new ArrayList<>();
        //assetManager provided by JMonkey somehow..
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        float radius = 1;
 
        balls.add(new Sphere(30, 30, radius));
        balls.add(new Sphere(30, 30, radius));
           
        float mass = 10;
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
// Ball  0
            geom = new Geometry("Box", balls.get(0));
            geom.setLocalTranslation(-6, 2, -3);
            geom.addControl(new RigidBodyControl(.001f));
            mat.setColor("Color", ColorRGBA.Green);
            geom.setMaterial(mat);
            RigidBodyControl bulletControl = new RigidBodyControl(mass);
            geom.addControl(bulletControl);
            //Set initial test velocity
            bulletControl.setLinearVelocity(new Vector3f(1,-0.2f,0));
             bulletControl.setRestitution(1);
 
            rootNode.attachChild(geom);
            // activate physics
            space = bulletAppState.getPhysicsSpace();
           space.add(bulletControl);
            bulletControl.setGravity(new Vector3f(0,0,0));
              bulletControl.setSleepingThresholds(0f,0f);
                  // add ourselves as collision listener
            space.addCollisionListener(this);
            
            // Ball 1
            geom = new Geometry("Box", balls.get(1));
            geom.setLocalTranslation(1*5-2, 2, -3);
            geom.addControl(new RigidBodyControl(.001f));
            mat.setColor("Color", ColorRGBA.Green);
            geom.setMaterial(mat);
            bulletControl = new RigidBodyControl(mass);
            geom.addControl(bulletControl);
            bulletControl.setSleepingThresholds(0f,0f);
             //Set initial test velocity
            bulletControl.setLinearVelocity(new Vector3f(-1,0,0));
            bulletControl.setRestitution(1);
             
            rootNode.attachChild(geom);
            // activate physics
            space = bulletAppState.getPhysicsSpace();
            space.add(bulletControl);
            bulletControl.setGravity(new Vector3f(0,0,0));
                  // add ourselves as collision listener
            space.addCollisionListener(this);

            
        // The floor, does not move (mass=0)
        initFloor();
        initWall1();
    }

    public void initFloor() {
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Yellow);
        material.getAdditionalRenderState().setWireframe(true);
        Box floorBox = new Box(22f, 0.1f, 11f);
        MeshCollisionShape meshCollisionShape = new MeshCollisionShape(floorBox);
        RigidBodyControl rigidBodyControl = new RigidBodyControl(meshCollisionShape, 0f);
//        rigidBodyControl.setKinematic(false);
//        rigidBodyControl.setRestitution(1);
//        floorBox.scaleTextureCoordinates(new Vector2f(3, 6));
        
        Geometry floor = new Geometry("floor", floorBox);
        floor.setMaterial(material);
        floor.setLocalTranslation(0, -5.5f, 0);
        floor.addControl(rigidBodyControl);
        rootNode.attachChild(floor);
        bulletAppState.getPhysicsSpace().add(floor);  
    }
    
     public void initWall1() {
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Yellow);
        material.getAdditionalRenderState().setWireframe(true);
        Box floorBox = new Box(22f, 11f, 0.1f);
        MeshCollisionShape meshCollisionShape = new MeshCollisionShape(floorBox);
        RigidBodyControl rigidBodyControl = new RigidBodyControl(meshCollisionShape, 0f);
//        rigidBodyControl.setKinematic(false);
//        rigidBodyControl.setRestitution(1);
//        floorBox.scaleTextureCoordinates(new Vector2f(3, 6));
        
        Geometry floor = new Geometry("floor", floorBox);
        floor.setMaterial(material);
        floor.setLocalTranslation(0, -5.5f, 5.5f);
        floor.addControl(rigidBodyControl);
        rootNode.attachChild(floor);
        bulletAppState.getPhysicsSpace().add(floor);  
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

   @Override
    public void collision(PhysicsCollisionEvent event) {
        if ("Box".equals(event.getNodeA().getName()) || "Box".equals(event.getNodeB().getName())) {
            if ("bullet".equals(event.getNodeA().getName()) || "bullet".equals(event.getNodeB().getName())) {
//                fpsText.setText("You hit the box!");
//                RigidBodyControl rbc = event.getNodeA().get;
            }
        }
    }
}