package zelaux.arclib.graphics.g3d.model;

import arc.math.geom.*;
import arc.util.Disposable;
import zelaux.arclib.graphics.g3d.model.obj.*;
import zelaux.arclib.graphics.g3d.render.Renderer3D;

/** Interface for models. **/
public interface Model extends Disposable{
    /**
     * Render model.
     * @param renderer renderer that's renders this model
     **/
    void render(Renderer3D renderer);

    /** Dispose model. **/
    default void dispose(){
    }

    /** Clone this models. **/
    <T extends Model> T cloneModel();


    ObjectShader getShader();

    void setShader(ObjectShader shader);

    Mat3D getTransformation();

    void setTransformation(Mat3D transformation);

    Vec3 getTranslation();

    void setTranslation(Vec3 translation);
}
