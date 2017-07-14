/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.scene.Node;

/**
 *
 * @author Martina
 */
public class Tank implements PhysicsCollisionListener {
    Node rootNode;
    BulletAppState bulletAppState;
    
    public Tank(BulletAppState bulletAppState, Node rootNode, AssetManager assetManager) {
        this.rootNode = rootNode;
        this.bulletAppState = bulletAppState;
        
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
        wallMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Front);
        addWall(wallMat, new Box(10.0f, 0.01f, 5.0f), new Vector3f(0, -5f, 0), new Vector3f(0, -1f, 0), Spatial.CullHint.Never); //Floor
        addWall(wallMat, new Box(10.0f, 0.01f, 5.0f), new Vector3f(0, 5f, 0), new Vector3f(0, 1f, 0), Spatial.CullHint.Never); //Ceiling
        addWall(wallMat, new Box(10.0f, 5.0f, 0.01f), new Vector3f(0, 0f, -5f), new Vector3f(0, 0f, -1f), Spatial.CullHint.Never); //Long wall
        addWall(wallMat, new Box(0.01f, 5.0f, 5.0f), new Vector3f(-10.0f, 0.0f, 0.0f), new Vector3f(-1f, 0f, 0), Spatial.CullHint.Never); //Gable
        addWall(wallMat, new Box(0.01f, 5.0f, 5.0f), new Vector3f(10.0f, 0.0f, 0.0f), new Vector3f(1f, 0f, 0), Spatial.CullHint.Never); //Gable
        addWall(wallMat, new Box(10.0f, 5.0f, 0.01f), new Vector3f(0, 0f, 5f), new Vector3f(0, 0f, 1f), Spatial.CullHint.Always); //Long wall
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
    
    public void addWall(Material material, Box floorBox, Vector3f localTranslation, Vector3f normal, Spatial.CullHint visible) {
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
}
