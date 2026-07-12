package com.kaderinisec;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SecimEkrani extends Screen {

    private Ozellikler.Ozellik secenekA_Avantaj;
    private Ozellikler.Ozellik secenekA_Dezavantaj;
    private Ozellikler.Ozellik secenekB_Avantaj;
    private Ozellikler.Ozellik secenekB_Dezavantaj;

    public SecimEkrani(Ozellikler.Ozellik aAv, Ozellikler.Ozellik aDez, Ozellikler.Ozellik bAv, Ozellikler.Ozellik bDez) {
        super(Component.literal("Kaderini Seç!"));
        this.secenekA_Avantaj = aAv;
        this.secenekA_Dezavantaj = aDez;
        this.secenekB_Avantaj = bAv;
        this.secenekB_Dezavantaj = bDez;
    }

    @Override
    protected void init() {
        super.init();

        // Sol Buton (Seçenek A) - Ekranın ortasının biraz soluna koyuyoruz
        int butonGenislik = 160;
        int butonYukseklik = 60;
        int solX = this.width / 2 - butonGenislik - 10;
        int Y = this.height / 2 - 20;

        this.addRenderableWidget(Button.builder(
            Component.literal("SEÇENEK A"), 
            button -> {
                // Seçenek A seçildiğinde tetiklenecek kodlar
                oyuncuyaOzellikleriVer(secenekA_Avantaj, secenekA_Dezavantaj);
                this.onClose(); // Ekranı kapat
            })
            .bounds(solX, Y, butonGenislik, butonYukseklik)
            .build()
        );

        // Sağ Buton (Seçenek B) - Ekranın ortasının biraz sağına koyuyoruz
        int sagX = this.width / 2 + 10;

        this.addRenderableWidget(Button.builder(
            Component.literal("SEÇENEK B"), 
            button -> {
                // Seçenek B seçildiğinde tetiklenecek kodlar
                oyuncuyaOzellikleriVer(secenekB_Avantaj, secenekB_Dezavantaj);
                this.onClose(); // Ekranı kapat
            })
            .bounds(sagX, Y, butonGenislik, butonYukseklik)
            .build()
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Arka planı hafif karart
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // Ana Başlık
        guiGraphics.drawCenteredString(this.font, "§l§6KADERİNİ SEÇ!", this.width / 2, this.height / 2 - 80, 0xFFFFFF);

        // Seçenek A Detay Yazıları (Sol Butonun Üzeri/Altı)
        int solX = this.width / 2 - 90;
        guiGraphics.drawCenteredString(this.font, "§a🟢 " + secenekA_Avantaj.isim, solX, this.height / 2 - 50, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, "§c🔴 " + secenekA_Dezavantaj.isim, solX, this.height / 2 + 50, 0xFFFFFF);

        // Seçenek B Detay Yazıları (Sağ Butonun Üzeri/Altı)
        int sagX = this.width / 2 + 90;
        guiGraphics.drawCenteredString(this.font, "§a🟢 " + secenekB_Avantaj.isim, sagX, this.height / 2 - 50, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, "§c🔴 " + secenekB_Dezavantaj.isim, sagX, this.height / 2 + 50, 0xFFFFFF);
    }

    private void oyuncuyaOzellikleriVer(Ozellikler.Ozellik avantaj, Ozellikler.Ozellik dezavantaj) {
        // Bu kısım oyuncuya Minecraft efektlerini (Hız, Güç vb.) veya özel mekanikleri tanımlayacak
    }

    @Override
    public boolean shouldCloseOnEsc() {
        // Oyuncu ESC tuşuna basarak bu ekrandan kaçamasın, illa seçim yapsın!
        return false;
    }
}
