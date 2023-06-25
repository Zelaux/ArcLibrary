package zelaux.arclib.graphics.g3d.model;

import arc.util.Disposable;
import zelaux.arclib.graphics.g3d.render.Renderer3D;

public interface Model extends Disposable {
    /** Render model.
     * @param renderer renderer that's renders this model
     **/
    void render(Renderer3D renderer) ;

    /** Dispose model. **/
    default void dispose() {
    }

    /** Clone this models. **/
    <T extends Model> T cloneModel();
}
