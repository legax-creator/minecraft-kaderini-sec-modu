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

    private int tickSayaci = 0;
    private int hedefTick = 3600; 
    private Random random = new Random();

    // Sürpriz kutusunu temsil eden hayali bir 51. özel madde
    public static final Ozellikler.Ozellik SURPRIZ_KUTUSU = new Ozellikler.Ozellik("?? SÜRPRİZ ??", "Tıklayana kadar gizli!", "surpriz");

    public KaderiniSecMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide) {
            Player oyuncu = event.player;
            tickSayaci++;

            if (tickSayaci >= hedefTick) {
                tickSayaci = 0;
                hedefTick = random.nextInt(2400) + 1200; 
                secimEkraniniTetikle(oyuncu);
            }
        }
    }

    private void secimEkraniniTetikle(Player oyuncu) {
        if (Ozellikler.AVANTAJLAR.isEmpty() || Ozellikler.DEZAVANTAJLAR.isEmpty()) return;

        // SEÇENEK A İÇİN ZAR ATIYORUZ (51 seçenek arasından)
        Ozellikler.Ozellik aAv = rastgeleSec(Ozellikler.AVANTAJLAR);
        Ozellikler.Ozellik aDez = rastgeleSec(Ozellikler.DEZAVANTAJLAR);

        // SEÇENEK B İÇİN ZAR ATIYORUZ
        Ozellikler.Ozellik bAv = rastgeleSec(Ozellikler.AVANTAJLAR);
        Ozellikler.Ozellik bDez = rastgeleSec(Ozellikler.DEZAVANTAJLAR);

        // Seçeneklerin birebir aynı gelmesini engelleme kontrolü
        while (bAv == aAv && bAv != SURPRIZ_KUTUSU) {
            bAv = rastgeleSec(Ozellikler.AVANTAJLAR);
        }
        while (bDez == aDez && bDez != SURPRIZ_KUTUSU) {
            bDez = rastgeleSec(Ozellikler.DEZAVANTAJLAR);
        }

        final Ozellikler.Ozellik finalAAv = aAv;
        final Ozellikler.Ozellik finalADez = aDez;
        final Ozellikler.Ozellik finalBAv = bAv;
        final Ozellikler.Ozellik finalBDez = bDez;

        net.minecraft.client.Minecraft.getInstance().execute(() -> {
            net.minecraft.client.Minecraft.getInstance().setScreen(
                new SecimEkrani(finalAAv, finalADez, finalBAv, finalBDez)
            );
        });
    }

    // Listeden bir özellik seçer veya 51. ihtimal olarak Sürpriz Kutusu döndürür
    private Ozellikler.Ozellik rastgeleSec(java.util.List<Ozellikler.Ozellik> liste) {
        // Toplam liste boyutu + 1 (Sürpriz kutusu için 51. slot)
        int sans = random.nextInt(liste.size() + 1);
        if (sans == liste.size()) {
            return SURPRIZ_KUTUSU; // %2 ihtimalle sürpriz kutusu denk geldi!
        }
        return liste.get(sans);
    }
}
