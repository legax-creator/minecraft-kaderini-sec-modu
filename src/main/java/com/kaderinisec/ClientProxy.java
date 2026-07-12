package com.kaderinisec;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientProxy {

    // Bu metot sadece istemci (oyuncu) bilgisayarında çalışır, sunucuyu asla kirletmez!
    public static void ekraniGoster(KaderiniSecMod mod, Ozellikler.Ozellik aAv, Ozellikler.Ozellik aDez, Ozellikler.Ozellik bAv, Ozellikler.Ozellik bDez) {
        Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().setScreen(new SecimEkrani(mod, aAv, aDez, bAv, bDez));
            }
        });
    }
}

