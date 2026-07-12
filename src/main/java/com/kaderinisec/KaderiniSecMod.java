package com.kaderinisec;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.world.entity.player.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod("kaderinisec")
public class KaderiniSecMod {

    private int tickSayaci = 0;
    private int hedefTick = 3600; 
    private Random random = new Random();

    // 🧠 Daha önce seçilmiş olan özellikleri aklında tutan hafıza listeleri
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

        // 🧠 Eğer tüm kartlar kullanıldıysa hafızayı sıfırla (Döngü başa sarsın)
        if (kullanilmisAvantajlar.size() >= Ozellikler.AVANTAJLAR.size()) {
            kullanilmisAvantajlar.clear();
            oyuncu.sendSystemMessage(net.minecraft.network.chat.Component.literal("§6[Kaderini Seç] §bTüm avantajlar tükendi! Havuz yenileniyor..."));
        }
        if (kullanilmisDezavantajlar.size() >= Ozellikler.DEZAVANTAJLAR.size()) {
            kullanilmisDezavantajlar.clear();
            oyuncu.sendSystemMessage(net.minecraft.network.chat.Component.literal("§6[Kaderini Seç] §bTüm dezavantajlar tükendi! Havuz yenileniyor..."));
        }

        // SEÇENEK A İÇİN DAHA ÖNCE ÇIKMAMIŞ KARTLARI SEÇİYORUZ
        Ozellikler.Ozellik aAv = benzersizRastgeleSec(Ozellikler.AVANTAJLAR, kullanilmisAvantajlar);
        Ozellikler.Ozellik aDez = benzersizRastgeleSec(Ozellikler.DEZAVANTAJLAR, kullanilmisDezavantajlar);

        // SEÇENEK B İÇİN DAHA ÖNCE ÇIKMAMIŞ KARTLARI SEÇİYORUZ
        Ozellikler.Ozellik bAv = benzersizRastgeleSec(Ozellikler.AVANTAJLAR, kullanilmisAvantajlar);
        Ozellikler.Ozellik bDez = benzersizRastgeleSec(Ozellikler.DEZAVANTAJLAR, kullanilmisDezavantajlar);

        // Aynı ekranda iki butonun da birebir aynı gelmesini engelleme kontrolü
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

        // Hata veren kısım düzeltildi: 'this' (yani bu sınıf) ilk parametre olarak eklendi!
        net.minecraft.client.Minecraft.getInstance().execute(() -> {
            net.minecraft.client.Minecraft.getInstance().setScreen(
                new SecimEkrani(this, finalAAv, finalADez, finalBAv, finalBDez)
            );
        });
    }

    // 🔍 SecimEkrani'ndan çağrılan ve eksik olan o meşhur metot eklendi!
    public void kartiKullanVeSil(Ozellikler.Ozellik avantaj, Ozellikler.Ozellik dezavantaj) {
        if (avantaj != SURPRIZ_KUTUSU && !kullanilmisAvantajlar.contains(avantaj)) {
            kullanilmisAvantajlar.add(avantaj);
        }
        if (dezavantaj != SURPRIZ_KUTUSU && !kullanilmisDezavantajlar.contains(dezavantaj)) {
            kullanilmisDezavantajlar.add(dezavantaj);
        }
    }

    // 🔍 Sadece daha önce seçilmemiş kartları bulan akıllı zar motoru
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
