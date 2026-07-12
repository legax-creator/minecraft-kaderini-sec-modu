package com.kaderinisec;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import java.util.Random;

public class SecimEkrani extends Screen {

    private KaderiniSecMod modAnaBeyin;
    private Ozellikler.Ozellik secenekA_Avantaj;
    private Ozellikler.Ozellik secenekA_Dezavantaj;
    private Ozellikler.Ozellik secenekB_Avantaj;
    private Ozellikler.Ozellik secenekB_Dezavantaj;
    private Random random = new Random();

    public SecimEkrani(KaderiniSecMod beyin, Ozellikler.Ozellik aAv, Ozellikler.Ozellik aDez, Ozellikler.Ozellik bAv, Ozellikler.Ozellik bDez) {
        super(Component.literal("Kaderini Seç!"));
        this.modAnaBeyin = beyin;
        this.secenekA_Avantaj = aAv;
        this.secenekA_Dezavantaj = aDez;
        this.secenekB_Avantaj = bAv;
        this.secenekB_Dezavantaj = bDez;
    }

    @Override
    protected void init() {
        super.init();

        int butonGenislik = 160;
        int butonYukseklik = 60;
        int solX = this.width / 2 - butonGenislik - 10;
        int Y = this.height / 2 - 20;

        // SEÇENEK A BUTONU
        this.addRenderableWidget(Button.builder(
            Component.literal(secenekA_Avantaj == KaderiniSecMod.SURPRIZ_KUTUSU ? "ŞANSINI DENE (A)" : "SEÇENEK A KABUL"), 
            button -> {
                Player oyuncu = Minecraft.getInstance().player;
                if (oyuncu != null) {
                    kutuyuAcVeVer(oyuncu, secenekA_Avantaj, secenekA_Dezavantaj);
                }
                this.onClose();
            })
            .bounds(solX, Y, butonGenislik, butonYukseklik)
            .build()
        );

        // SEÇENEK B BUTONU
        int sagX = this.width / 2 + 10;
        this.addRenderableWidget(Button.builder(
            Component.literal(secenekB_Avantaj == KaderiniSecMod.SURPRIZ_KUTUSU ? "ŞANSINI DENE (B)" : "SEÇENEK B KABUL"), 
            button -> {
                Player oyuncu = Minecraft.getInstance().player;
                if (oyuncu != null) {
                    kutuyuAcVeVer(oyuncu, secenekB_Avantaj, secenekB_Dezavantaj);
                }
                this.onClose();
            })
            .bounds(sagX, Y, butonGenislik, butonYukseklik)
            .build()
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(this.font, "§l§6KADERİNİ SEÇ!", this.width / 2, this.height / 2 - 80, 0xFFFFFF);

        int solX = this.width / 2 - 90;
        if (secenekA_Avantaj == KaderiniSecMod.SURPRIZ_KUTUSU) {
            guiGraphics.drawCenteredString(this.font, "§k§a?? SÜRPRİZ ??", solX, this.height / 2 - 50, 0xFFFFFF);
            guiGraphics.drawCenteredString(this.font, "§k§c?? SÜRPRİZ ??", solX, this.height / 2 + 50, 0xFFFFFF);
        } else {
            guiGraphics.drawCenteredString(this.font, "§a🟢 " + secenekA_Avantaj.isim, solX, this.height / 2 - 50, 0xFFFFFF);
            guiGraphics.drawCenteredString(this.font, "§c🔴 " + secenekA_Dezavantaj.isim, solX, this.height / 2 + 50, 0xFFFFFF);
        }

        int sagX = this.width / 2 + 90;
        if (secenekB_Avantaj == KaderiniSecMod.SURPRIZ_KUTUSU) {
            guiGraphics.drawCenteredString(this.font, "§k§a?? SÜRPRİZ ??", sagX, this.height / 2 - 50, 0xFFFFFF);
            guiGraphics.drawCenteredString(this.font, "§k§c?? SÜRPRİZ ??", sagX, this.height / 2 + 50, 0xFFFFFF);
        } else {
            guiGraphics.drawCenteredString(this.font, "§a🟢 " + secenekB_Avantaj.isim, sagX, this.height / 2 - 50, 0xFFFFFF);
            guiGraphics.drawCenteredString(this.font, "§c🔴 " + secenekB_Dezavantaj.isim, sagX, this.height / 2 + 50, 0xFFFFFF);
        }
    }

    private void kutuyuAcVeVer(Player oyuncu, Ozellikler.Ozellik avantaj, Ozellikler.Ozellik dezavantaj) {
        Ozellikler.Ozellik sonAv = avantaj;
        Ozellikler.Ozellik sonDez = dezavantaj;

        // Sürpriz kutusu açıldıysa o anlık ana listeden rastgele bir şey çek
        if (avantaj == KaderiniSecMod.SURPRIZ_KUTUSU) {
            sonAv = Ozellikler.AVANTAJLAR.get(random.nextInt(Ozellikler.AVANTAJLAR.size()));
        }
        if (dezavantaj == KaderiniSecMod.SURPRIZ_KUTUSU) {
            sonDez = Ozellikler.DEZAVANTAJLAR.get(random.nextInt(Ozellikler.DEZAVANTAJLAR.size()));
        }

        // Seçilen kartları ana beyindeki "kalanlar" listesinden sildiriyoruz!
        modAnaBeyin.kartiKullanVeSil(sonAv, sonDez);

        oyuncu.removeAllEffects();

        // 🟢 AVANTAJLAR
        switch (sonAv.id) {
            case "hiz_2": oyuncu.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 99999, 1)); break;
            case "ziplama_2": oyuncu.addEffect(new MobEffectInstance(MobEffects.JUMP, 99999, 1)); break;
            case "yunus": 
                oyuncu.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 99999, 0)); 
                oyuncu.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 99999, 0)); 
                break;
            case "acele_2": oyuncu.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 99999, 1)); break;
            case "direnc_1": oyuncu.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 99999, 0)); break;
        }

        // 🔴 DEZAVANTAJLAR
        switch (sonDez.id) {
            case "yavaslik_1": oyuncu.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 99999, 0)); break;
            case "zayiflik": 
                oyuncu.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 99999, 0)); 
                oyuncu.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 99999, 0)); 
                break;
            case "korluk": oyuncu.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 99999, 0)); break;
            case "cam_govde": oyuncu.addEffect(new MobEffectInstance(MobEffects.HUNGER, 99999, 1)); break;
            case "kirik_ayak": oyuncu.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 99999, 2)); break;
        }

        oyuncu.sendSystemMessage(Component.literal("§6[Kaderini Seç] §fKader belirlendi: §a+" + sonAv.isim + " §ffakat §c-" + sonDez.isim));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
