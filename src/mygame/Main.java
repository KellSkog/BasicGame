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
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.RenderState;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Cylinder;
import java.util.ArrayList;
import java.util.List;


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

    private void buildPyramid(float r, float pointOffset, List<Vector3f> pos) {
        float sqrt3 = (float) Math.sqrt(3);
        double height = r * Math.sqrt(8.0 / 3);//Height between layers are r * sqrt(8/3)
        float width = r / sqrt3;
        float layerAt = pointOffset;
        //TipBall
        pos.add(new Vector3f(layerAt, 0, 0));
        
        //Tripple
        layerAt += height;
        pos.add(new Vector3f(layerAt, 2 * width, 0)); //Top
        pos.add(new Vector3f(layerAt, -width, r));
        pos.add(new Vector3f(layerAt, -width, -r));
        //Six
        layerAt += height;
        pos.add(new Vector3f(layerAt, 4 * width, 0)); //Top
        pos.add(new Vector3f(layerAt,  width, r));
        pos.add(new Vector3f(layerAt,  width, -r));
        
        pos.add(new Vector3f(layerAt,  -2 * width, 2 * r));
        pos.add(new Vector3f(layerAt,  - 2 *width, 0));
        pos.add(new Vector3f(layerAt,  - 2 * width, -2 * r));
    }
    
    

    @Override
    public void simpleInitApp() {
//        PhysicsTestHelper.createPhysicsTestWorld(rootNode, assetManager, bulletAppState.getPhysicsSpace());
//        PhysicsTestHelper.createBallShooter(this, rootNode, bulletAppState.getPhysicsSpace());

        //assetManager provided by JMonkey somehow..
        
        
        float radius = 0.5f;
        float mass = 10;
        float pointAt = 6.0f;

        List<Vector3f> positions = new ArrayList<>();
        buildPyramid(radius, pointAt, positions);
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        configureQue(mass);
        PhysicsSpace space = bulletAppState.getPhysicsSpace();
        space.setGravity(Vector3f.ZERO);

        Geometry geom;
        Material  ballMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        for (Vector3f ballstate : positions) {
            geom = addBall(ballMat, new Sphere(30, 30, radius), mass, ballstate, ColorRGBA.Blue);
            space.add(geom);
        }
        Material  queMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        geom = addBall(queMat, new Sphere(30, 30, radius), mass, new Vector3f(0, 0f, 0), ColorRGBA.White);
        space.add(geom);

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
        wallMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Back);
        addWall(wallMat, new Box(10.0f, 0.01f, 5.0f), new Vector3f(0, -5f, 0), new Vector3f(0, -1f, 0), CullHint.Never); //Floor
        addWall(wallMat, new Box(10.0f, 0.01f, 5.0f), new Vector3f(0, 5f, 0), new Vector3f(0, 1f, 0), CullHint.Never); //Ceiling
        addWall(wallMat, new Box(10.0f, 5.0f, 0.01f), new Vector3f(0, 0f, -5f), new Vector3f(0, 0f, -1f), CullHint.Never); //Long wall
        addWall(wallMat, new Box(0.01f, 5.0f, 5.0f), new Vector3f(-10.0f, 0.0f, 0.0f), new Vector3f(-1f, 0f, 0), CullHint.Never); //Gable
        addWall(wallMat, new Box(0.01f, 5.0f, 5.0f), new Vector3f(10.0f, 0.0f, 0.0f), new Vector3f(1f, 0f, 0), CullHint.Never); //Gable
        

        addWall(wallMat, new Box(10.0f, 5.0f, 0.01f), new Vector3f(0, 0f, 5f), new Vector3f(0, 0f, 1f), CullHint.Always); //Long wall
        
        space.addCollisionListener(this);
        
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
    public Geometry addBall(Material material, Mesh ball, float mass, Vector3f localTranslation, ColorRGBA color) {
        Geometry geom = new Geometry("Box", ball);
        geom.setLocalTranslation(localTranslation);
        material.setColor("Color", color);
        geom.setMaterial(material);
        
        RigidBodyControl bulletControl = new RigidBodyControl(mass);
        geom.addControl(bulletControl);
        bulletControl.setSleepingThresholds(0f,0f);
        bulletControl.setLinearVelocity(Vector3f.ZERO); //Set initial test velocity
        bulletControl.setRestitution(1);
//        bulletControl.setFriction(50f); //Has no effect!
        rootNode.attachChild(geom);

        return geom;
    }
    public void addWall(Material material, Box floorBox, Vector3f localTranslation, Vector3f normal, CullHint visible) {
        MeshCollisionShape meshCollisionShape = new MeshCollisionShape(floorBox);
        RigidBodyControl rigidBodyControl = new RigidBodyControl(meshCollisionShape, 0f);
        rigidBodyControl.setKinematic(false);
        rigidBodyControl.setRestitution(1);

//        material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        Geometry floor = new Geometry("floor", floorBox);
//        if (visibility == WallProperty.INVISIBLE) {
//            
//            floor.setQueueBucket(Bucket.Transparent);
//        }
        floor.setMaterial(material);
        floor.setLocalTranslation(localTranslation);
        floor.addControl(rigidBodyControl);
        floor.setCullHint(visible);
        floor.setUserData("Normal", normal);
        rootNode.attachChild(floor);
        bulletAppState.getPhysicsSpace().add(floor);  
    }

    private void setupKeys() {
        inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("shoot2", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(this, "shoot");
        inputManager.addListener(this, "shoot2");
        
        inputManager.addMapping("camRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("camLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("camUp", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("camDown", new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addListener(this, "camRight");
        inputManager.addListener(this, "camLeft");
        inputManager.addListener(this, "camUp");
        inputManager.addListener(this, "camDown");
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
           perform(new Geometry("bullet", bullet), mat);
        } else if (name.equals("shoot2") && isPressed) {
           perform(new Geometry("bullet", bullet), mat2);
        } else if (name.equals("camRight") && isPressed) {

                    
        } else if (name.equals("camLeft") && isPressed) {

                    
        } else if (name.equals("camUp") && isPressed) {
            List<Spatial> children = rootNode.getChildren();
            for (Spatial child : children) {
                if (child.getName().equals("floor")) {
                    if (child.getCullHint() == CullHint.Never) {
                        child.setCullHint(CullHint.Always);
                    } else {
                        child.setCullHint(CullHint.Never);
                    }
                }
            }
                    
        } else if (name.equals("camDown") && isPressed) {

                    
        }
   }
    private void print(String s) {
        System.out.println(s);
    }
    private void perform(Geometry geom, Material material) {
        Vector3f camLoc = cam.getLocation();
        Vector3f camDir = cam.getDirection();
        geom.setMaterial(material);
        geom.setName("bullet");
        geom.setLocalTranslation(camLoc);
        geom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        geom.addControl(new RigidBodyControl(bulletCollisionShape, 10));
        geom.getControl(RigidBodyControl.class).setLinearVelocity(camDir.mult(40));
        rootNode.attachChild(geom);
        getPhysicsSpace().add(geom);
    }
    private void configureQue(float mass) {

        cam.setLocation(new Vector3f(10f, 0f, 40f));
        flyCam.setMoveSpeed(25);
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
           queGeom.setLocalTranslation(new Vector3f(0f,0f,20f));
            queGeom.addControl(new RigidBodyControl(.001f));
            mat.setColor("Color", ColorRGBA.Green);
            queGeom.setMaterial(mat);
//            RigidBodyControl bulletControl = new RigidBodyControl(mass);
//            queGeom.addControl(bulletControl);
//            bulletControl.setSleepingThresholds(0f,0f);
             //Set initial test velocity
//            bulletControl.setLinearVelocity(new Vector3f(0,0,0));
//            bulletControl.setRestitution(1);
            rootNode.attachChild(queGeom);
            // activate physics
            PhysicsSpace space = bulletAppState.getPhysicsSpace();
//            space.add(bulletControl);
//            bulletControl.setGravity(new Vector3f(0,0,0));
                  // add ourselves as collision listener
            space.addCollisionListener(this);

    }
}
