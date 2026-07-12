package com.kaderinisec;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod("kaderinisec")
// 🛡️ Forge'a bu sınıfın içindeki etkinlikleri otomatik ve zorunlu olarak kaydetmesini söylüyoruz (Sadece İstemcide)
@Mod.EventBusSubscriber(modid = "kaderinisec", value = Dist.CLIENT)
public class KaderiniSecMod {

    // Forge'un otomatik okuyabilmesi için zamanlayıcı bileşenlerini statik yapıyoruz
    private static int tickSayaci = 0;
    private static int hedeftick = 200; // İlk ekran 10 saniye
    private static final Random random = new Random();

    private static final List<Ozellikler.Ozellik> kullanilmisAvantajlar = new ArrayList<>();
    private static final List<Ozellikler.Ozellik> kullanilmisDezavantajlar = new ArrayList<>();

    public static final Ozellikler.Ozellik SURPRIZ_KUTUSU = new Ozellikler.Ozellik("?? SÜRPRİZ ??", "Tıklayana kadar gizli!", "surpriz");

    // Tek bir statik örnek (SecimEkrani'na göndermek için)
    private static KaderiniSecMod instance;

    public KaderiniSecMod() {
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    // 🖥️ Statik hale getirilmiş istemci zamanlayıcısı - Forge bunu asla görmezden gelemez
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            
            // Oyuncu dünyadaysa ve ekranda başka menü açık değilse say
            if (mc.player != null && mc.level != null && mc.screen == null) {
                tickSayaci++;

                if (tickSayaci >= hedeftick) {
                    tickSayaci = 0;
                    hedeftick = random.nextInt(200) + 200; // Sonraki ekranlar 10-20 sn arası
                    
                    secimEkraniniTetikle();
                }
            }
        }
    }

    private static void secimEkraniniTetikle() {
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

        // Doğrudan ve güvenle ekranı aç
        Minecraft.getInstance().setScreen(
            new SecimEkrani(instance, finalAAv, finalADez, finalBAv, finalBDez)
        );
    }

    public void kartiKullanVeSil(Ozellikler.Ozellik avantaj, Ozellikler.Ozellik dezavantaj) {
        if (avantaj != SURPRIZ_KUTUSU && !kullanilmisAvantajlar.contains(avantaj)) {
            kullanilmisAvantajlar.add(avantaj);
        }
        if (dezavantaj != SURPRIZ_KUTUSU && !kullanilmisDezavantajlar.contains(dezavantaj)) {
            kullanilmisDezavantajlar.add(dezavantaj);
        }
    }

    private static Ozellikler.Ozellik benzersizRastgeleSec(List<Ozellikler.Ozellik> anaListe, List<Ozellikler.Ozellik> kullanilmisListe) {
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
