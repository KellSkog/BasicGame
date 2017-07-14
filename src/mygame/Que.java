/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author Martina
 */
public class Que extends Node implements PhysicsCollisionListener {
    Camera cam;
    Cylinder cyl;
    Node rootNode;
    AssetManager assetManager;
    BulletAppState bulletAppState;
    Geometry queGeom;
    Sphere bullet;
    SphereCollisionShape bulletCollisionShape;
    Material mat;
    Material mat2;
    public Que(String name, Camera cam, Node rootNode, AssetManager assetManager, BulletAppState bulletAppState) {
        super(name);
        this.cam = cam;
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.bulletAppState = bulletAppState;
    }
    public Material getMat() {return mat; }
    public Material getMat2() {return mat2; }
    public Geometry getGeom() {return queGeom; }
    public Sphere getBullet() {return bullet; }
    public SphereCollisionShape getCollisionShape() {return bulletCollisionShape; }
    public void configureQue(float mass) {

//        cam.setLocation(new Vector3f(10f, 0f, 40f));
//        flyCam.setMoveSpeed(25);
        cam.setViewPort(0.0f, 1.0f, 0.0f, 1.0f);
        Quaternion test = getWorldRotation();
//        setupKeys();
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Green);

        mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.getAdditionalRenderState().setWireframe(true);
        mat2.setColor("Color", ColorRGBA.Red);
        bullet = new Sphere(32, 32, 0.4f, true, false);
        bullet.setTextureMode(Sphere.TextureMode.Projected);
        bulletCollisionShape = new SphereCollisionShape(0.1f);
//        Cylinder que;
        cyl = new Cylinder(5, 10, 0.3f, 20f, true);
        queGeom = new Geometry("Box", cyl);
        queGeom.setLocalTranslation(new Vector3f(0f, 0f, 20f));
        queGeom.addControl(new RigidBodyControl(.001f));
        mat.setColor("Color", ColorRGBA.Green);
        queGeom.setMaterial(mat);
//            RigidBodyControl bulletControl = new RigidBodyControl(mass);
//            queGeom.addControl(bulletControl);
//            bulletControl.setSleepingThresholds(0f,0f);
        //Set initial test velocity
//            bulletControl.setLinearVelocity(new Vector3f(0,0,0));
//            bulletControl.setRestitution(1);
        attachChild(queGeom);
        rootNode.attachChild(this);
        // activate physics
        PhysicsSpace space = bulletAppState.getPhysicsSpace();
//            cam2 = cam.clone();
//        ViewPort view_2 = renderManager.createMainView("View of camera #n", cam2);
//        view_2.setEnabled(true);
//        view_2.attachScene(rootNode);
//        view_2.setBackgroundColor(ColorRGBA.Black);
//        cam2.setLocation(queGeom.getWorldTranslation());
//        test.opposite(test);
//        cam2.setRotation(test);
//        cam2.setViewPort( 0.5f , 1.0f   ,   0.0f , 0.5f );

//            space.add(bulletControl);
//            bulletControl.setGravity(new Vector3f(0,0,0));
        // add ourselves as collision listener
//        space.addCollisionListener(this);

    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        System.out.println("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
