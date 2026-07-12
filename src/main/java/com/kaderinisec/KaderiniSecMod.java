package com.kaderinisec;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.DistExecutor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod("kaderinisec")
public class KaderiniSecMod {

    private int tickSayaci = 0;
    private int hedefTick = 200; // Test için 10 saniye
    private Random random = new Random();

    private List<Ozellikler.Ozellik> kullanilmisAvantajlar = new ArrayList<>();
    private List<Ozellikler.Ozellik> kullanilmisDezavantajlar = new ArrayList<>();

    public static final Ozellikler.Ozellik SURPRIZ_KUTUSU = new Ozellikler.Ozellik("?? SÜRPRİZ ??", "Tıklayana kadar gizli!", "surpriz");

    public KaderiniSecMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // Zamanlayıcı sunucuda güvenle sayıyor
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide) {
            tickSayaci++;

            if (tickSayaci >= hedefTick) {
                tickSayaci = 0;
                hedefTick = random.nextInt(200) + 200; 
                secimEkraniniTetikle(event.player);
            }
        }
    }

    private void secimEkraniniTetikle(Player oyuncu) {
        if (Ozellikler.AVANTAJLAR.isEmpty() || Ozellikler.DEZAVANTAJLAR.isEmpty()) return;

        if (kullanilmisAvantajlar.size() >= Ozellikler.AVANTAJLAR.size()) {
            kullanilmisAvantajlar.clear();
        }
        if (kullanilmisDezavantajlar.size() >= Ozellikler.DEZAVANTAJLAR.size()) {
            kullanilmisDezavantajlar.clear();
        }

        Ozellikler.Ozellik aAv = benzersizRastgeleSec(Ozellikler.AVANTAJLAR, kullanilmisAvantajlar);
        Ozellikler.Ozellik aDez = benzersizRastgeleSec(Ozellikler.DEZAVANTAJLAR, kullanilmisDezavantajlar);
        Ozellikler.Ozellik bAv = benzersizRastgeleSec(Ozellikler.AVANTAJLAR, kullanilmisAvantajlar);
        Ozellikler.Ozellik bDez = benzersizRastgeleSec(Ozellikler.DEZAVANTAJLAR, kullanilmisDezavantajlar);

        while (bAv == aAv && bAv != SURPRIZ_KUTUSU) {
            bAv = benzersizRastgeleSec(Ozellikler.AVANTAJLAR, kullanilmisAvantajlar);
        }
        while (bDez == aDez && bDez != SURPRIZ_KUTUSU) {
            bDez = benzersizRastgeleSec(Ozellikler.DEZAVANTAJLAR, kullanilmisDezavantajlar);
        }

        final Ozellikler.Ozellik finalAAv = aAv;
        final Ozellikler.Ozellik finalADez = aDez;
        final Ozellikler.Ozellik finalBAv = bAv;
        final Ozellikler.Ozellik finalBDez = bDez;

        // 🛡️ İŞTE EKSİK OLAN RESMİ KÖPRÜ: 
        // Forge'a bu kodun sadece İstemci (Client) tarafında güvenle çalıştırılacağını zorla dikte ediyoruz.
        DistExecutor.unsafeRunWhenOn(net.minecraftforge.api.distmarker.Dist.CLIENT, () -> () -> {
            ClientProxy.ekraniGoster(this, finalAAv, finalADez, finalBAv, finalBDez);
        });
    }

    public void kartiKullanVeSil(Ozellikler.Ozellik avantaj, Ozellikler.Ozellik dezavantaj) {
        if (avantaj != SURPRIZ_KUTUSU && !kullanilmisAvantajlar.contains(avantaj)) {
            kullanilmisAvantajlar.add(avantaj);
        }
        if (dezavantaj != SURPRIZ_KUTUSU && !kullanilmisDezavantajlar.contains(dezavantaj)) {
            kullanilmisDezavantajlar.add(dezavantaj);
        }
    }

    private Ozellikler.Ozellik benzersizRastgeleSec(List<Ozellikler.Ozellik> anaListe, List<Ozellikler.Ozellik> kullanilmisListe) {
        int sans = random.nextInt(anaListe.size() + 1);
        if (sans == anaListe.size()) {
            return SURPRIZ_KUTUSU;
        }

        Ozellikler.Ozellik secilen = anaListe.get(sans);
        
        int denemeSayisi = 0;
        while (kullanilmisListe.contains(secilen) && denemeSayisi < 100) {
            secilen = anaListe.get(random.nextInt(anaListe.size()));
            denemeSayisi++;
        }
        
        return secilen;
    }
}
