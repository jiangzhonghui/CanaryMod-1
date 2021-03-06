import java.util.Random;

public class OBlockPortal extends OBlockBreakable {

    public OBlockPortal(int i) {
        super(i, "portal", OMaterial.D, false);
        this.b(true);
    }

    public void a(OWorld oworld, int i, int j, int k, Random random) {
        super.a(oworld, i, j, k, random);
        if (oworld.t.d() && random.nextInt(2000) < oworld.r) {
            int l;

            for (l = j; !oworld.w(i, l, k) && l > 0; --l) {
                ;
            }

            if (l > 0 && !oworld.u(i, l + 1, k)) {
                OEntity oentity = OItemMonsterPlacer.a(oworld, 57, (double) i + 0.5D, (double) l + 1.1D, (double) k + 0.5D);

                if (oentity != null) {
                    oentity.ao = oentity.ac();
                }
            }
        }
    }

    public OAxisAlignedBB b(OWorld oworld, int i, int j, int k) {
        return null;
    }

    public void a(OIBlockAccess oiblockaccess, int i, int j, int k) {
        float f;
        float f1;

        if (oiblockaccess.a(i - 1, j, k) != this.cF && oiblockaccess.a(i + 1, j, k) != this.cF) {
            f = 0.125F;
            f1 = 0.5F;
            this.a(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
        } else {
            f = 0.5F;
            f1 = 0.125F;
            this.a(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
        }
    }

    public boolean c() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public boolean o_(OWorld oworld, int i, int j, int k) {
        byte b0 = 0;
        byte b1 = 0;

        if (oworld.a(i - 1, j, k) == OBlock.au.cF || oworld.a(i + 1, j, k) == OBlock.au.cF) {
            b0 = 1;
        }

        if (oworld.a(i, j, k - 1) == OBlock.au.cF || oworld.a(i, j, k + 1) == OBlock.au.cF) {
            b1 = 1;
        }

        if (b0 == b1) {
            return false;
        } else {
            if (oworld.a(i - b0, j, k - b1) == 0) {
                i -= b0;
                k -= b1;
            }

            int l;
            int i1;

            for (l = -1; l <= 2; ++l) {
                for (i1 = -1; i1 <= 3; ++i1) {
                    boolean flag = l == -1 || l == 2 || i1 == -1 || i1 == 3;

                    if (l != -1 && l != 2 || i1 != -1 && i1 != 3) {
                        int j1 = oworld.a(i + b0 * l, j + i1, k + b1 * l);

                        if (flag) {
                            if (j1 != OBlock.au.cF) {
                                return false;
                            }
                        } else if (j1 != 0 && j1 != OBlock.aw.cF) {
                            return false;
                        }
                    }
                }
            }

            // CanaryMod hook onPortalCreate
            Block[][] portalBlocks = new Block[3][2];

            for (i1 = 0; i1 < 3; ++i1) {
                for (l = 0; l < 2; ++l) {
                    portalBlocks[i1][l] = new Block(oworld.world, Block.Type.Portal.getType(), i + b0 * l, j + 2 - i1, k + b1 * l);
                }
            }
            if (!(Boolean) etc.getLoader().callHook(PluginLoader.Hook.PORTAL_CREATE, (Object) portalBlocks)) { // Cast to make single argument
                for (l = 0; l < 2; ++l) {
                    for (i1 = 0; i1 < 3; ++i1) {
                        oworld.f(i + b0 * l, j + i1, k + b1 * l, OBlock.bj.cF, 0, 2);
                    }
                }

                return true;
            }
            return false;
        }
    }

    public void a(OWorld oworld, int i, int j, int k, int l) {
        byte b0 = 0;
        byte b1 = 1;

        if (oworld.a(i - 1, j, k) == this.cF || oworld.a(i + 1, j, k) == this.cF) {
            b0 = 1;
            b1 = 0;
        }

        int i1;

        for (i1 = j; oworld.a(i, i1 - 1, k) == this.cF; --i1) {
            ;
        }

        if (oworld.a(i, i1 - 1, k) != OBlock.au.cF) {
            oworld.i(i, j, k);
        } else {
            int j1;

            for (j1 = 1; j1 < 4 && oworld.a(i, i1 + j1, k) == this.cF; ++j1) {
                ;
            }

            if (j1 == 3 && oworld.a(i, i1 + j1, k) == OBlock.au.cF) {
                boolean flag = oworld.a(i - 1, j, k) == this.cF || oworld.a(i + 1, j, k) == this.cF;
                boolean flag1 = oworld.a(i, j, k - 1) == this.cF || oworld.a(i, j, k + 1) == this.cF;

                if (flag && flag1) {
                    oworld.i(i, j, k);
                } else {
                    if ((oworld.a(i + b0, j, k + b1) != OBlock.au.cF || oworld.a(i - b0, j, k - b1) != this.cF) && (oworld.a(i - b0, j, k - b1) != OBlock.au.cF || oworld.a(i + b0, j, k + b1) != this.cF)) {
                        oworld.i(i, j, k);
                    }
                }
            } else {
                oworld.i(i, j, k);
            }
        }
    }

    public int a(Random random) {
        return 0;
    }

    public void a(OWorld oworld, int i, int j, int k, OEntity oentity) {
        if (oentity.o == null && oentity.n == null) {
            oentity.ab();
        }
    }
}
