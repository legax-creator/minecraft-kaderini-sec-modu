package com.kaderinisec;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod("kaderinisec")
@Mod.EventBusSubscriber(modid = "kaderinisec", value = Dist.CLIENT)
public class KaderiniSecMod {

    private static int tickSayaci = 0;
    private static int hedeftick = 200; // İlk test için 10 saniye (200 tick)
    private static final Random random = new Random();

    private static final List<Ozellikler.Ozellik> kullanilmisAvantajlar = new ArrayList<>();
    private static final List<Ozellikler.Ozellik> kullanilmisDezavantajlar = new ArrayList<>();
    public static final Ozellikler.Ozellik SURPRIZ_KUTUSU = new Ozellikler.Ozellik("?? SÜRPRİZ ??", "Tıklayana kadar gizli!", "surpriz");

    private static KaderiniSecMod instance;

    public KaderiniSecMod() {
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            
            // Oyuncu dünyadaysa sayacı çalıştır
            if (mc.player != null && mc.level != null) {
                tickSayaci++;

                // 🟡 TEST 1: Her 1 saniyede bir chata durum yazdır
                if (tickSayaci % 20 == 0) {
                    int kalanSaniye = (hedeftick - tickSayaci) / 20;
                    mc.player.sendSystemMessage(Component.literal("§e[Mod Test] Sayaç çalışıyor... Kalan süre: " + kalanSaniye + " sn."));
                }

                // Süre dolduğunda
                if (tickSayaci >= hedeftick) {
                    tickSayaci = 0;
                    hedeftick = random.nextInt(200) + 200; // Sonraki süre 10-20 sn arası
                    
                    // 🟢 TEST 2: Süre dolduğu an chata uyarı geç
                    mc.player.sendSystemMessage(Component.literal("§c§l[Mod Test] SÜRE DOLDU! TEST EKRANI AÇILIYOR..."));
                    
                    secimEkraniniTetikle();
                }
            }
        }
    }

    private static void secimEkraniniTetikle() {
        // 🟫 TEST 3: Minecraft'ın kendi hatasız toprak ekranını zorla açıyoruz!
        Minecraft.getInstance().execute(() -> {
            Minecraft.getInstance().setScreen(
                new net.minecraft.client.gui.screens.GenericDirtMessageScreen(
                    Component.literal("KADERİNİ SEÇ TETİKLENDİ!")
                )
            );
        });
    }

    public void kartiKullanVeSil(Ozellikler.Ozellik avantaj, Ozellikler.Ozellik dezavantaj) {
        if (avantaj != SURPRIZ_KUTUSU && !kullanilmisAvantajlar.contains(avantaj)) kullanilmisAvantajlar.add(avantaj);
        if (dezavantaj != SURPRIZ_KUTUSU && !kullanilmisDezavantajlar.contains(dezavantaj)) kullanilmisDezavantajlar.add(dezavantaj);
    }

    private static Ozellikler.Ozellik benzersizRastgeleSec(List<Ozellikler.Ozellik> anaListe, List<Ozellikler.Ozellik> kullanilmisListe) {
        int sans = random.nextInt(anaListe.size() + 1);
        if (sans == anaListe.size()) return SURPRIZ_KUTUSU;
        Ozellikler.Ozellik secilen = anaListe.get(sans);
        int denemeSayisi = 0;
        while (kullanilmisListe.contains(secilen) && denemeSayisi < 100) {
            secilen = anaListe.get(random.nextInt(anaListe.size()));
            denemeSayisi++;
        }
        return secilen;
    }
}
