/*
 * 2 Balls
*/

package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;


/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication implements PhysicsCollisionListener {
    BulletAppState bulletAppState;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
//        PhysicsTestHelper.createPhysicsTestWorld(rootNode, assetManager, bulletAppState.getPhysicsSpace());
//        PhysicsTestHelper.createBallShooter(this, rootNode, bulletAppState.getPhysicsSpace());

        //assetManager provided by JMonkey somehow..
        
        
        float radius = 1;
        float mass = 10;
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        Material  ballMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        addBall(ballMat, new Sphere(30, 30, radius), mass, new Vector3f(-3, 3, -3), new Vector3f(-1,1f,0));
        addBall(ballMat, new Sphere(30, 30, radius), mass, new Vector3f(3, 3, -3), new Vector3f(1,1,0));
        addBall(ballMat, new Sphere(30, 30, radius), mass, new Vector3f(-3, -3, -3), new Vector3f(-1,-1,0));
        addBall(ballMat, new Sphere(30, 30, radius), mass, new Vector3f(3, -3, -3), new Vector3f(1,-1,0));
        addBall(ballMat, new Sphere(30, 30, radius), mass, new Vector3f(-3, 3, 3), new Vector3f(-1,1f,1));
        addBall(ballMat, new Sphere(30, 30, radius), mass, new Vector3f(3, 3, 3), new Vector3f(1,1,1));
        addBall(ballMat, new Sphere(30, 30, radius), mass, new Vector3f(-3, -3, 3), new Vector3f(-1,-1,1));
        addBall(ballMat, new Sphere(30, 30, radius), mass, new Vector3f(3, -3, 3), new Vector3f(1,-1,1));

        // The walls, does not move (mass=0)
        Material wallMat = new Material(assetManager,  // Create new material and...
            "Common/MatDefs/Light/Lighting.j3md"); // ... specify .j3md file to use (illuminated).
       
//        Material wallMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        wallMat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/wood_diffuse.png")); // with Lighting.j3md
        addWall(wallMat, new Box(10.0f, 0.01f, 5.0f), new Vector3f(0, -5f, 0)); //Floor
        addWall(wallMat, new Box(10.0f, 0.01f, 5.0f), new Vector3f(0, 5f, 0)); //Ceiling
        addWall(wallMat, new Box(10.0f, 5.0f, 0.01f), new Vector3f(0, 0f, 5f)); //Long wall
        addWall(wallMat, new Box(10.0f, 5.0f, 0.01f), new Vector3f(0, 0f, -5f)); //Long wall
        addWall(wallMat, new Box(0.01f, 5.0f, 5.0f), new Vector3f(-10.0f, 0.0f, 0.0f)); //Gable
        addWall(wallMat, new Box(0.01f, 5.0f, 5.0f), new Vector3f(10.0f, 0.0f, 0.0f)); //Gable
        
        addLight();
    }
    public void addLight() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
        sun.setColor(ColorRGBA.White);
//        PointLight lamp_light = new PointLight();
//        lamp_light.setColor(ColorRGBA.White);
//        lamp_light.setRadius(4f);
//        lamp_light.setPosition(new Vector3f(-5f, -5f, 5f));
//        lamp_light.setPosition(new Vector3f(lamp_geo.getLocalTranslation()));
        rootNode.addLight(sun);
    }
    public void addBall(Material material, Mesh ball, float mass, Vector3f localTranslation, Vector3f initialVelocity) {
            Geometry geom = new Geometry("Box", ball);
            geom.setLocalTranslation(localTranslation);
            geom.addControl(new RigidBodyControl(.001f));
            material.setColor("Color", ColorRGBA.Green);
            geom.setMaterial(material);
            RigidBodyControl bulletControl = new RigidBodyControl(mass);
            geom.addControl(bulletControl);
            bulletControl.setSleepingThresholds(0f,0f);
             //Set initial test velocity
            bulletControl.setLinearVelocity(initialVelocity);
            bulletControl.setRestitution(1);
//            bulletControl.setFriction(50f); //Has no effect!
            rootNode.attachChild(geom);
            // activate physics
            PhysicsSpace space = bulletAppState.getPhysicsSpace();
            space.add(bulletControl);
            bulletControl.setGravity(new Vector3f(0,0,0));
                  // add ourselves as collision listener
            space.addCollisionListener(this);
    }
    public void addWall(Material material, Box floorBox, Vector3f localTranslation) {
        material.setBoolean("UseMaterialColors",true);  // Set some parameters, e.g. blue.
        material.setColor("Ambient", ColorRGBA.Green);   // ... color of this object
        material.setColor("Diffuse", ColorRGBA.Green);   // ... color of light being reflected
        
//        material.setColor("Color", ColorRGBA.Yellow);
//        material.getAdditionalRenderState().setWireframe(true);
        MeshCollisionShape meshCollisionShape = new MeshCollisionShape(floorBox);
        RigidBodyControl rigidBodyControl = new RigidBodyControl(meshCollisionShape, 0f);
        rigidBodyControl.setKinematic(false);
        rigidBodyControl.setRestitution(1);
//        floorBox.scaleTextureCoordinates(new Vector2f(3, 6));
        
        Geometry floor = new Geometry("floor", floorBox);
        floor.setMaterial(material);
        floor.setLocalTranslation(localTranslation);
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