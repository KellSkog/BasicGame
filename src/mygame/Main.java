package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Sphere;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication implements PhysicsCollisionListener, ActionListener {

    BulletAppState bulletAppState;

    public enum WallProperty {
        VISIBLE, INVISIBLE
    }

    Que queNode;

    enum cameraId {
        queCam, globalCam
    }
    CameraManager cm;

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
        pos.add(new Vector3f(layerAt, width, r));
        pos.add(new Vector3f(layerAt, width, -r));

        pos.add(new Vector3f(layerAt, -2 * width, 2 * r));
        pos.add(new Vector3f(layerAt, - 2 * width, 0));
        pos.add(new Vector3f(layerAt, - 2 * width, -2 * r));
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
        queNode = new Que("Kön", cam, rootNode, assetManager, bulletAppState);
        queNode.configureQue(mass);

        Quaternion test = new Quaternion(0f, 0, 0f, 1).add(new Quaternion(0f, 0, 0f, 0));
        test = test.opposite(test);
        CameraState[] cameras = new CameraState[2];
        cameras[0] = new CameraState();
        cameras[0].setCamera(test, new Vector3f(10f, 0f, 40f));
        test = queNode.getWorldRotation();
        cam.setLocation(queNode.getGeom().getWorldTranslation());
        test.opposite(test);
        cameras[1] = new CameraState();
        cameras[1].setCamera(test, queNode.getGeom().getWorldTranslation());
        cm = new CameraManager(cam, cameras);
        PhysicsSpace space = bulletAppState.getPhysicsSpace();
        space.setGravity(Vector3f.ZERO);

        Geometry geom;
        Material ballMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        for (Vector3f ballstate : positions) {
            geom = addBall(ballMat, new Sphere(30, 30, radius), mass, ballstate, ColorRGBA.Blue);
            space.add(geom);
        }
        Material queMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        geom = addBall(queMat, new Sphere(30, 30, radius), mass, new Vector3f(0, 0f, 0), ColorRGBA.White);
        space.add(geom);

        Tank tank = new Tank(bulletAppState, rootNode, assetManager);
        space.addCollisionListener(tank);
//        space.addCollisionListener(queNode);
        setupKeys();
        addLight();
    }

    public void addLight() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-.5f, -.5f, -.5f).normalizeLocal());
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
        bulletControl.setSleepingThresholds(0f, 0f);
        bulletControl.setLinearVelocity(Vector3f.ZERO); //Set initial test velocity
        bulletControl.setRestitution(1);
//        bulletControl.setFriction(50f); //Has no effect!
        rootNode.attachChild(geom);

        return geom;
    }

    private void setupKeys() {
        inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("shoot2", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(this, "shoot");
        inputManager.addListener(this, "shoot2");

        inputManager.addMapping("queRight", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("queLeft", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("queUp", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("queDown", new KeyTrigger(KeyInput.KEY_N));
        inputManager.addMapping("camGlobal", new KeyTrigger(KeyInput.KEY_B));
        inputManager.addMapping("camQue", new KeyTrigger(KeyInput.KEY_M));
        inputManager.addListener(this, "queRight");
        inputManager.addListener(this, "queLeft");
        inputManager.addListener(this, "queUp");
        inputManager.addListener(this, "queDown");
        inputManager.addListener(this, "camGlobal");
        inputManager.addListener(this, "camQue");
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

    private PhysicsSpace getPhysicsSpace() {
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
        boolean camMoved = false;
        if (name.equals("shoot") && !isPressed) {
            perform(new Geometry("bullet", queNode.getBullet()), queNode.getMat());
        } else if (name.equals("shoot2") && isPressed) {
            perform(new Geometry("bullet", queNode.getBullet()), queNode.getMat2());
        } else if (name.equals("queRight") && isPressed) {
            queNode.rotate(queNode.getWorldRotation().fromAngleAxis(FastMath.DEG_TO_RAD * 20, new Vector3f(0, 1, 0)));
            cm.checkCameraState(queNode, queNode.getGeom());

        } else if (name.equals("queLeft") && isPressed) {

            queNode.rotate(queNode.getWorldRotation().fromAngleAxis(-FastMath.DEG_TO_RAD * 20, new Vector3f(0, 1, 0)));
            cm.checkCameraState(queNode, queNode.getGeom());//queGeom);

        } else if (name.equals("queUp") && isPressed) {
            queNode.rotate(queNode.getWorldRotation().fromAngleAxis(-FastMath.DEG_TO_RAD * 20, new Vector3f(1, 0, 0)));
            cm.checkCameraState(queNode, queNode.getGeom());//queGeom);
            List<Spatial> children = rootNode.getChildren();
//            for (Spatial child : children) {
//                if (child.getName().equals("floor")) {
//                    if (child.getCullHint() == CullHint.Never) {
//                        child.setCullHint(CullHint.Always);
//                    } else {
//                        child.setCullHint(CullHint.Never);
//                    }
//                }
//            }

        } else if (name.equals("queDown") && isPressed) {
            queNode.rotate(queNode.getWorldRotation().fromAngleAxis(FastMath.DEG_TO_RAD * 20, new Vector3f(1, 0, 0)));
            cm.checkCameraState(queNode, queNode.getGeom());//queGeom);queGeom);

        } else if (name.equals("camGlobal") && isPressed) {

            cm.setCameraState(0, queNode.getGeom());//queGeom);queGeom);
//        cam.setLocation(new Vector3f(10f, 0f, 40f));
//        Quaternion test = new Quaternion(0f,0,0f,1).add(new Quaternion(0f,0,0f,0));
//        test.opposite(test);
//        cam.setRotation(test);
//        float w = test.getW();
//        float x = test.getX();
//        float y = test.getY();
//        float z = test.getZ();
//        
//        System.out.println(w + " " + x + " " + y + " " + z);
        } else if (name.equals("camRight") && isPressed) {
            camMoved = true;
        } else if (name.equals("camLeft") && isPressed) {
            camMoved = true;
        } else if (name.equals("camUp") && isPressed) {
            camMoved = true;
        } else if (name.equals("camDown") && isPressed) {
            camMoved = true;

        } else if (name.equals("camQue") && isPressed) {
            cm.setCameraState(1, queNode.getGeom());//queGeom);queGeom);
            camMoved = true;

//           Quaternion test = queNode.getWorldRotation();
//            cam.setLocation(queGeom.getWorldTranslation());
//            test.opposite(test);
//            cam.setRotation(test);
        }
        if (camMoved) {
            setWallVisibility();
        }
    }

    private void setVisibility(Spatial s) {
        Vector3f normal = ((Vector3f) (s.getUserData("Normal")));
        Vector3f cameraPos = cam.getLocation();
        Vector3f sum = normal.subtract(cameraPos);
        if (sum.lengthSquared() > cameraPos.lengthSquared()) {
            s.setCullHint(CullHint.Never);
        } else {
            s.setCullHint(CullHint.Always);
        }
    }

    private void setWallVisibility() {
//        rootNode.getChildren().stream()
//                .filter(child -> child.getName().equals("floor"));
        List<Spatial> children = rootNode.getChildren();
        System.out.println(children);
        for (Spatial child : children) {
            if (child.getName().equals("floor")) {
                setVisibility(child);
            }
        }
    }

    private void perform(Geometry geom, Material material) {
        Vector3f camLoc = cam.getLocation();
        Vector3f camDir = cam.getDirection();
        geom.setMaterial(material);
        geom.setName("bullet");
        geom.setLocalTranslation(camLoc);
        geom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        geom.addControl(new RigidBodyControl(queNode.getCollisionShape(), 10));
        geom.getControl(RigidBodyControl.class).setLinearVelocity(camDir.mult(40));
        rootNode.attachChild(geom);
        getPhysicsSpace().add(geom);
    }
}