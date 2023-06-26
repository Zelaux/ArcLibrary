package zelaux.arclib.graphics.g3d.model.obj;

import arc.graphics.Gl;
import arc.graphics.Mesh;
import arc.graphics.Texture;
import arc.graphics.VertexAttribute;
import arc.math.geom.Mat3D;
import arc.math.geom.Vec3;
import zelaux.arclib.graphics.g3d.model.Model;
import zelaux.arclib.graphics.g3d.model.obj.mtl.MTL;
import zelaux.arclib.graphics.g3d.model.obj.obj.OBJ;
import zelaux.arclib.graphics.g3d.render.Renderer3D;

public class OBJModel implements Model {
    public Texture texture;
    public Mesh mesh;
    public ObjectShader shader;

    private final OBJ obj;
    private final MTL mtl;

    public OBJModel(OBJ obj, MTL mtl, Texture texture, ObjectShader shader) {
        this.mtl = mtl;
        this.obj = obj;
        this.texture = texture;
        this.shader = shader;
        rebuildMesh();
    }

    Mat3D transformation = new Mat3D();
    Vec3 translation = new Vec3();
    float rot = 0;

    @Override
    public void render(Renderer3D renderer) {
        rot += 0.1f;
        transformation.setToRotation(Vec3.Y, rot);

        texture.bind();
        shader.bind();

        shader.apply();
        shader.setUniformMatrix4("u_proj", renderer.getProjMat().val);
        shader.setUniformMatrix4("u_transf", transformation.val);
        shader.setUniformf("u_transl", translation);

        mesh.render(shader, Gl.triangles);
    }

    public void rebuildMesh() {
        disposeMesh();
        mesh = new Mesh(true, obj.vertices.length, 0,
                VertexAttribute.position3, VertexAttribute.texCoords, VertexAttribute.normal);

        mesh.getVerticesBuffer().limit(obj.vertices.length);
        mesh.getVerticesBuffer().put(obj.vertices, 0, obj.vertices.length);
    }

    public void disposeMesh() {
        if (mesh != null)
            mesh.dispose();
    }

    public void disposeShader() {
        if (shader != null)
            shader.dispose();
    }

    public void disposeTexture() {
        if (texture != null)
            texture.dispose();
    }

    @Override
    public void dispose() {
        Model.super.dispose();
        disposeTexture();
        disposeShader();
        disposeMesh();
    }

    // TODO maybe... use reflection?
    @Override
    public OBJModel cloneModel() {
        return new OBJModel(obj, mtl, texture, shader);
    }
}