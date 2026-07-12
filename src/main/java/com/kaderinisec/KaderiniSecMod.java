package com.kaderinisec;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.world.entity.player.Player;
import java.util.Random;

@Mod("kaderinisec")
public class KaderiniSecMod {

    // 3 dakika = 180 saniye. Minecraft saniyede 20 tick çalışır. 180 * 20 = 3600 tick.
    private int tickSayaci = 0;
    private int hedefTick = 3600; 
    private Random random = new Random();

    public KaderiniSecMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Mod baslangic ayarlari
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // Sadece sunucu tarafinda ve oyuncunun tick sonunda kontrol et
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide) {
            Player oyuncu = event.player;
            tickSayaci++;

            // Rastgele belirlenen sureye ulasildiginda
            if (tickSayaci >= hedefTick) {
                tickSayaci = 0;
                // Bir sonraki secim icin 1 ile 3 dakika arasinda rastgele yeni sure (1200 ile 3600 tick arasi)
                hedefTick = random.nextInt(2400) + 1200; 

                // Secim ekranini tetikle
                secimEkraniniTetikle(oyuncu);
            }
        }
    }

    private void secimEkraniniTetikle(Player oyuncu) {
        // Havuzda ozellik yoksa durdur
        if (Ozellikler.AVANTAJLAR.isEmpty() || Ozellikler.DEZAVANTAJLAR.isEmpty()) return;

        // Secenek A icin rastgele avantaj ve dezavantaj
        Ozellikler.Ozellik aAv = Ozellikler.AVANTAJLAR.get(random.nextInt(Ozellikler.AVANTAJLAR.size()));
        Ozellikler.Ozellik aDez = Ozellikler.DEZAVANTAJLAR.get(random.nextInt(Ozellikler.DEZAVANTAJLAR.size()));

        // Secenek B icin rastgele avantaj ve dezavantaj
        Ozellikler.Ozellik bAv = Ozellikler.AVANTAJLAR.get(random.nextInt(Ozellikler.AVANTAJLAR.size()));
        Ozellikler.Ozellik bDez = Ozellikler.DEZAVANTAJLAR.get(random.nextInt(Ozellikler.DEZAVANTAJLAR.size()));

        // Ayni ozelliklerin gelmesini engelle
        while (bAv == aAv) {
            bAv = Ozellikler.AVANTAJLAR.get(random.nextInt(Ozellikler.AVANTAJLAR.size()));
        }
        while (bDez == aDez) {
            bDez = Ozellikler.DEZAVANTAJLAR.get(random.nextInt(Ozellikler.DEZAVANTAJLAR.size()));
        }

        // Ekran istemci (Client) tarafinda acilmak zorunda oldugu icin guvenli tetikleme
        final Ozellikler.Ozellik finalAAv = aAv;
        final Ozellikler.Ozellik finalADez = aDez;
        final Ozellikler.Ozellik finalBAv = bAv;
        final Ozellikler.Ozellik finalBDez = bDez;

        distractionFreeScreenOpen(finalAAv, finalADez, finalBAv, finalBDez);
    }

    private void distractionFreeScreenOpen(Ozellikler.Ozellik aAv, Ozellikler.Ozellik aDez, Ozellikler.Ozellik bAv, Ozellikler.Ozellik bDez) {
        // Minecraft istemci ekranini acan yardimci metot
        net.minecraft.client.Minecraft.getInstance().execute(() -> {
            net.minecraft.client.Minecraft.getInstance().setScreen(
                new SecimEkrani(aAv, aDez, bAv, bDez)
            );
        });
    }
}

