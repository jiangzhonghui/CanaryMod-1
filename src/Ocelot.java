
public class Ocelot extends TamableEntity {

    /**
     * Basic ocelot constructor.
     *
     * @param entity An instance of OEntityOcelot to wrap around.
     */
    public Ocelot(OEntityOcelot entity) {
        super(entity);
    }

    /**
     * Set the skin of the ocelot.
     *
     * @param skin ID of the skin.
     * 0 - Normal,
     * 1 - Black,
     * 2 - Orange,
     * 3 - Siamese,
     * Any other value defaults to normal.
     */
    public void setSkin(int skin) {
        // SRG getEntity().func_70912_b(skin);
        getEntity().p(skin);
    }

    /**
     * Get the id of this ocelots skin.
     *
     * @return 0, 1, 2 or 3. See {@link Ocelot#setSkin(int)} for skin ids.
     */
    public int getSkin() {
        // SRG return getEntity().func_70913_u();
        return getEntity().ca();
    }

    @Override
    public OEntityOcelot getEntity() {
        return (OEntityOcelot) entity;
    }

}
