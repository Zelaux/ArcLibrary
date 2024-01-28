package arclibrary.utils.graphics;

import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;

/** Stores pixmaps in different pages. **/
public class MultiPacker implements Disposable{
    public ObjectMap<String, PixmapPacker> packersMap = new ObjectMap<>();
    public Seq<PixmapPacker> packers = new Seq<>();

    /** @param page page name
     *  @return packer which this page attached
     **/
    public PixmapPacker get(String page) {
        PixmapPacker out = packersMap.get(page);
        if (out == null) {
            out = new PixmapPacker(8192, 8192, 2, false);
            packersMap.put(page, out);
            packers.add(out);
        }
        return out;
    }

    /** @param page page name
     *  @param name pixmap region name
     *  @param region pixmap region
     **/
    public void add(String page, String name, PixmapRegion region){
        add(page, name, region, null, null);
    }

    /** @param page page name
     *  @param name pixmap region name
     *  @param region pixmap region
     *  @param splits splits
     *  @param pads paddings
     **/
    public void add(String page, String name, PixmapRegion region, int[] splits, int[] pads){
        get(page).pack(name, region, splits, pads);
    }

    /** @param page page name
     *  @param name pixmap name
     *  @param pix pixmap
     **/
    public void add(String page, String name, Pixmap pix){
        add(page, name, new PixmapRegion(pix));
    }

    /**
     * Generates new atlas with loaded pages.
     * @param filter texture filter for new atlas
     * @return new texture atlas
     **/
    public TextureAtlas flush(TextureFilter filter){
        TextureAtlas atlas = new TextureAtlas();
        flush(atlas, filter);
        return atlas;
    }

    /**
     * Updates atlas with loaded pages.
     * @param atlas atlas for update
     * @param filter texture filter for new atlas
     **/
    public void flush(TextureAtlas atlas, TextureFilter filter){
        for(PixmapPacker p : packers){
            p.updateTextureAtlas(atlas, filter, filter, false, false);
        }
    }

    /** Call it after flush. **/
    @Override
    public void dispose(){
        for(PixmapPacker packer : packers){
            packer.dispose();
        }
    }
}