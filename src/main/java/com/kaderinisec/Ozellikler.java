package com.kaderinisec;

import java.util.ArrayList;
import java.util.List;

public class Ozellikler {

    public static class Ozellik {
        public String isim;
        public String aciklama;
        public String id;

        public Ozellik(String isim, String aciklama, String id) {
            this.isim = isim;
            this.aciklama = aciklama;
            this.id = id;
        }
    }

    public static List<Ozellik> AVANTAJLAR = new ArrayList<>();
    public static List<Ozellik> DEZAVANTAJLAR = new ArrayList<>();

    static {
        // --- 🟢 AVANTAJLAR ---
        AVANTAJLAR.add(new Ozellik("Rüzgar Ayaklı", "Kalıcı Hız II etkisi alırsın.", "hiz_2"));
        AVANTAJLAR.add(new Ozellik("Yüksek Sıçrayış", "Kalıcı Zıplama Desteği II alırsın.", "ziplama_2"));
        AVANTAJLAR.add(new Ozellik("Yunus Pratikliği", "Suda nefes alır ve hızlı yüzersin.", "yunus"));
        AVANTAJLAR.add(new Ozellik("Köstebek Gücü", "Kalıcı Acele II ile aşırı hızlı kazarsın.", "acele_2"));
        AVANTAJLAR.add(new Ozellik("Çelik Gövde", "%20 daha az hasar alırsın.", "direnc_1"));

        // --- 🔴 DEZAVANTAJLAR ---
        DEZAVANTAJLAR.add(new Ozellik("Ağır Adımlar", "Kalıcı Yavaşlık I etkisi alırsın.", "yavaslik_1"));
        DEZAVANTAJLAR.add(new Ozellik("Kırık Ayak", "Zıplama yüksekliğin yarım bloğa düşer.", "kirik_ayak"));
        DEZAVANTAJLAR.add(new Ozellik("Cam Gövde", "Maksimum canın 5 kalbe düşer.", "cam_govde"));
        DEZAVANTAJLAR.add(new Ozellik("Zayıf Kollar", "Kalıcı Zayıflık alırsın.", "zayiflik"));
        DEZAVANTAJLAR.add(new Ozellik("Karanlık Korkusu", "Işıksız yerlerde körlük alırsın.", "korluk"));
    }
}

