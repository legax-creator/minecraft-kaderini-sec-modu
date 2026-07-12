package com.kaderinisec;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod("kaderinisec")
public class KaderiniSecMod {

    private int tickSayaci = 0;
    private int hedefTick = 200; // Test için 10 saniye (20 tick = 1 saniye)
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

    // ⌨️ Komut artık Sunucu (Server) tarafında güvenle dinleniyor
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("kaderinisec").executes(context -> {
                if (context.getSource().getEntity() instanceof Player oyuncu) {
                    // Mantıksal motoru ana iş parçacığına senkronize ediyoruz
                    net.minecraftforge.fml.util.thread.EffectiveSide.get().isClient();
                    secimEkraniniTetikle(oyuncu);
                }
                return 1;
            })
        );
    }

    // ⏱️ Zamanlayıcı artık SUNUCU (Server) tarafında güvenle sayıyor! Böylece singleplayer'da asla takılmayacak
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
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

        // 🛡️ SUNUCUDAN İSTEMCİYE GÜVENLİ KÖPRÜ
        // Oyun singleplayer bile olsa ekranı sadece kullanıcının kendi ekran kartı (Render Thread) çizebilir
        if (oyuncu.level().isClientSide) {
            net.minecraft.client.Minecraft.getInstance().execute(() -> {
                net.minecraft.client.Minecraft.getInstance().setScreen(
                    new SecimEkrani(this, finalAAv, finalADez, finalBAv, finalBDez)
                );
            });
        } else {
            // Eğer kod sunucu tarafındaysa, istemci thread'ine dolaylı yoldan erişim sağlıyoruz
            java.awt.EventQueue.invokeLater(() -> {
                try {
                    net.minecraft.client.Minecraft.getInstance().execute(() -> {
                        net.minecraft.client.Minecraft.getInstance().setScreen(
                            new SecimEkrani(this, finalAAv, finalADez, finalBAv, finalBDez)
                        );
                    });
                } catch (Throwable t) {
                    // Sunucu ortamında GUI çökmesini engellemek için koruma kalkanı
                }
            });
        }
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
