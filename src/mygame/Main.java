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
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.shape.Cylinder;


/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication implements PhysicsCollisionListener, ActionListener {
    BulletAppState bulletAppState;
    public enum WallProperty {VISIBLE, INVISIBLE}
    private Sphere bullet;
    private Material mat;
    private Material mat2;
    private SphereCollisionShape bulletCollisionShape;

 
    
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
        
        configureQue(mass);

        Material  ballMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        addBall(ballMat, new Sphere(30, 30, radius), mass, new Vector3f(-3, 3, -3), new Vector3f(-2,2f,0), ColorRGBA.Blue);
        addBall(ballMat, new Sphere(30, 30, radius), mass, new Vector3f(3, 3, -3), new Vector3f(2,2,0), ColorRGBA.Blue);
        addBall(ballMat, new Sphere(30, 30, radius), mass, new Vector3f(-3, -3, -3), new Vector3f(-2,-2,0), ColorRGBA.Blue);
        addBall(ballMat, new Sphere(30, 30, radius), mass, new Vector3f(3, -3, -3), new Vector3f(2,-2,0), ColorRGBA.Blue);
        addBall(ballMat, new Sphere(30, 30, radius), mass, new Vector3f(-3, 3, 3), new Vector3f(-2,2,2), ColorRGBA.Blue);
        addBall(ballMat, new Sphere(30, 30, radius), mass, new Vector3f(3, 3, 3), new Vector3f(2,2,2), ColorRGBA.Blue);
        addBall(ballMat, new Sphere(30, 30, radius), mass, new Vector3f(-3, -3, 3), new Vector3f(-2,-2,2), ColorRGBA.Blue);
        addBall(ballMat, new Sphere(30, 30, radius), mass, new Vector3f(3, -3, 3), new Vector3f(2,-2,2), ColorRGBA.Blue);
        Material  queMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        addBall(queMat, new Sphere(30, 30, radius), mass, new Vector3f(0, -2.5f, 0), new Vector3f(0,0,0), ColorRGBA.White);

        // The walls, does not move (mass=0)
//        Material wallMat = new Material(assetManager,  // Create new material and...
//            "Common/MatDefs/Light/Lighting.j3md"); // ... specify .j3md file to use (illuminated).
//        wallMat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/wood_diffuse.png")); // with Lighting.j3md
//        wallMat.setBoolean("UseMaterialColors",true);  // Only for Lighting
//        wallMat.setColor("Ambient", ColorRGBA.Green);   // Only for Lighting
//        wallMat.setColor("Diffuse", ColorRGBA.Green);   // Only for Lighting        

        Material wallMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wallMat.setColor("Color", ColorRGBA.Green);   // Color of Unshaded
        wallMat.setTexture("ColorMap", assetManager.loadTexture("felt_green.jpg"));// In project 'assets' directory
        addWall(wallMat, new Box(10.0f, 0.01f, 5.0f), new Vector3f(0, -5f, 0), WallProperty.VISIBLE); //Floor
        addWall(wallMat, new Box(10.0f, 0.01f, 5.0f), new Vector3f(0, 5f, 0), WallProperty.VISIBLE); //Ceiling
        addWall(wallMat, new Box(10.0f, 5.0f, 0.01f), new Vector3f(0, 0f, -5f), WallProperty.VISIBLE); //Long wall
        addWall(wallMat, new Box(0.01f, 5.0f, 5.0f), new Vector3f(-10.0f, 0.0f, 0.0f), WallProperty.VISIBLE); //Gable
        addWall(wallMat, new Box(0.01f, 5.0f, 5.0f), new Vector3f(10.0f, 0.0f, 0.0f), WallProperty.VISIBLE); //Gable
        
        Material matTranparent = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matTranparent.setColor("Color", new ColorRGBA(0f, 1f, 0f, 0f));   // Color of Unshaded
//        matTranparent.setTexture("ColorMap", assetManager.loadTexture("felt_green.jpg"));// In project 'assets' directory
        addWall(matTranparent, new Box(10.0f, 5.0f, 0.01f), new Vector3f(0, 0f, 5f), WallProperty.INVISIBLE); //Long wall
        
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

   public void addBall(Material material, Mesh ball, float mass, Vector3f localTranslation, Vector3f initialVelocity, ColorRGBA color) {
            Geometry geom = new Geometry("Box", ball);
            geom.setLocalTranslation(localTranslation);
            geom.addControl(new RigidBodyControl(.001f));
            material.setColor("Color", color);
            geom.setMaterial(material);
            RigidBodyControl bulletControl = new RigidBodyControl(mass);
            geom.addControl(bulletControl);
            bulletControl.setSleepingThresholds(0f,0f);
             
            bulletControl.setLinearVelocity(initialVelocity); //Set initial test velocity
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
    public void addWall(Material material, Box floorBox, Vector3f localTranslation, WallProperty visibility) {
        MeshCollisionShape meshCollisionShape = new MeshCollisionShape(floorBox);
        RigidBodyControl rigidBodyControl = new RigidBodyControl(meshCollisionShape, 0f);
        rigidBodyControl.setKinematic(false);
        rigidBodyControl.setRestitution(1);
//        floorBox.scaleTextureCoordinates(new Vector2f(3, 6));
        
        Geometry floor = new Geometry("floor", floorBox);
        if (visibility == WallProperty.INVISIBLE) {
            material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
            floor.setQueueBucket(Bucket.Transparent);
        }
        floor.setMaterial(material);
        floor.setLocalTranslation(localTranslation);
        floor.addControl(rigidBodyControl);
        rootNode.attachChild(floor);
        bulletAppState.getPhysicsSpace().add(floor);  
    }


    private void setupKeys() {
        inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("shoot2", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(this, "shoot");
        inputManager.addListener(this, "shoot2");
    }


    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code  Erling
    }
 
    private PhysicsSpace getPhysicsSpace(){
        return bulletAppState.getPhysicsSpace();
    }

   @Override
    public void collision(PhysicsCollisionEvent event) {
        if ("Box".equals(event.getNodeA().getName()) || "Box".equals(event.getNodeB().getName())) {
            if ("bullet".equals(event.getNodeA().getName()) || "bullet".equals(event.getNodeB().getName())) {
//                fpsText.setText("You hit the box!");
//                RigidBodyControl rbc = event.getNodeA().get;
            }
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
       if (name.equals("shoot") && !isPressed) {
            Geometry bulletg = new Geometry("bullet", bullet);
            bulletg.setMaterial(mat);
            bulletg.setName("bullet");
            bulletg.setLocalTranslation(cam.getLocation());
            bulletg.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
            bulletg.addControl(new RigidBodyControl(bulletCollisionShape, 1));
            bulletg.getControl(RigidBodyControl.class).setCcdMotionThreshold(0.1f);
            bulletg.getControl(RigidBodyControl.class).setLinearVelocity(cam.getDirection().mult(40));
            rootNode.attachChild(bulletg);
            getPhysicsSpace().add(bulletg);
        } else if (name.equals("shoot2") && !isPressed) {
            Geometry bulletg = new Geometry("bullet", bullet);
            bulletg.setMaterial(mat2);
            bulletg.setName("bullet");
            bulletg.setLocalTranslation(cam.getLocation());
            bulletg.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
            bulletg.addControl(new RigidBodyControl(bulletCollisionShape, 1));
            bulletg.getControl(RigidBodyControl.class).setLinearVelocity(cam.getDirection().mult(40));
            rootNode.attachChild(bulletg);
            getPhysicsSpace().add(bulletg);
        }
   }
       private void configureQue(float mass) {

        cam.setLocation(new Vector3f(10f, 0f, 40f));
        flyCam.setMoveSpeed(100);
        setupKeys();
        mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Green);

        mat2 = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.getAdditionalRenderState().setWireframe(true);
        mat2.setColor("Color", ColorRGBA.Red);
        bullet = new Sphere(32, 32, 0.4f, true, false);
        bullet.setTextureMode(Sphere.TextureMode.Projected);
        bulletCollisionShape = new SphereCollisionShape(0.1f);
        Cylinder que;
        que = new Cylinder(5,10,0.3f,20f, true);
        Geometry queGeom = new Geometry("Box", que);
           queGeom.setLocalTranslation(new Vector3f(0f,0f,-0.5f));
            queGeom.addControl(new RigidBodyControl(.001f));
            mat.setColor("Color", ColorRGBA.Green);
            queGeom.setMaterial(mat);
            RigidBodyControl bulletControl = new RigidBodyControl(mass);
            queGeom.addControl(bulletControl);
            bulletControl.setSleepingThresholds(0f,0f);
             //Set initial test velocity
            bulletControl.setLinearVelocity(new Vector3f(0,0,0));
            bulletControl.setRestitution(1);
            rootNode.attachChild(queGeom);
            // activate physics
            PhysicsSpace space = bulletAppState.getPhysicsSpace();
            space.add(bulletControl);
            bulletControl.setGravity(new Vector3f(0,0,0));
                  // add ourselves as collision listener
            space.addCollisionListener(this);

    }
}